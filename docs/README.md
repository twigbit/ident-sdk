# ![twigbit IdentSDK](images/header.png)

The twigbit Ident SDK is a lightweight convenience layer on top of the [AusweisApp2 SDK](https://www.ausweisapp.bund.de/fuer-diensteanbieter/software-development-kit-sdk/) written in Kotlin.
We are aiming to extract and eliminate the recurring code and configuration that every developer faces integrating AusweisIdent functionality in their apps. 
Moreover, we are providing convenience tooling for the [AusweisIdent mobile identification service](https://www.ausweisident.de/) provided by Bundesdruckerei GmbH and Governikus KG. 

![twigbit technologies GmbH, Governikus GmbH & Co. KG, AusweisIDent](images/logos.png)

## Features

- Simplify the tedious [AusweisApp2 SDK](https://www.ausweisapp.bund.de/sdk/) configuration
- Replace the JSON based messaging system by convenient wrapper methods, giving developers to must-have convenience such as code completion
- Lightweight — besides the [AusweisApp2 SDK](https://www.ausweisapp.bund.de/sdk/), the only other dependency is [Google GSON](https://github.com/google/gson) for JSON parsing
- Drop-in UI — Provide a simple, customizable drop in UI as a quick integration with identification processes

**Looking for some backend examples?** See our AusweisIDent backend examples repository at [twigbit/ausweisident-backend-examples](https://github.com/twigbit/ausweisident-backend-examples).

# Documentation 

All code is provided in Kotlin. The integration works in Java analogously, all examples are interchangable. 

## Adding the dependency

First, add the jitpack maven repository to the **root** `build.gradle` file.

```gradle 
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Then, add the drop in ui as a dependency to your **projects** `build.gradle` file. 
Also make sure, that your projects `minSdkVersion` is set to at least version 21. 

```gradle
android {
    defaultConfig {
        minSdkVersion 21
    }
}
dependencies {
  implementation 'com.github.twigbit:ident-sdk:0.1.4'
}
```

## Quick start 
Using the drop in UI, you can implement a fully functional AusweisIDent identification cycle within minutes. 

To start the identification process, use the `AusweisIdentBuilder` to create the TCTokenURL with your credentials and the permitted/required scopes and pass it to the `DropInRequest`. 
Optionally, you can give the process an identifier to match the result using the `.state(...)` argument.

```kotlin
val REQUEST_CODE_IDENTIFICATION = 0;
private fun startDropInIdentification(){
        val tcTokenUrl = AusweisIdentBuilder()
            .ref()
            .clientId("your-client-id")
            .redirectUrl("your-redirect-url")
            .state("your-persistant-id")
            .scope(AusweisIdentScopes.FAMILY_NAMES)
            .scope(AusweisIdentScopes.GIVEN_NAMES)
            .scope(AusweisIdentScopes.DATE_OF_BIRTH)
            .build()

        val dropInRequest = DropInRequest(tcTokenUrl)
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE_IDENTIFICATION)
    }
```

To get the resulting delivery URL, listen to the activity result. 

```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_IDENTIFICATION) {
            if (resultCode == Activity.RESULT_OK) {
                // Success. Update the UI to reflect the successful identification
                // and fetch the user data from the server where they were delivered.
                
                val resultUrl = data!!.getStringExtra(IdentificationManager.EXTRA_DROPIN_RESULT)
                Log.d(Tags.TAG_IDENT_DEBUG, resultUrl);
                // to deliver the data to the server, call this URL and follow the redirect chain
                                
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the identification
            } else {
                // An error occured during the identification
            }
        }
    }
```

You will get the `resultUrl` as described above. To evalutate the resultUrl and deliver the result to you backend, read the section on [Handling the result URL](#handling-the-result-url)


### Styling the Drop-In UI
To change the appearance of the drop in UI, please read the [styleguide](STYLEGUIDE.md).

## Implement your own UI 

To implement your own identification UI, you can use a custom activity and react to the IdentificationManagers callbacks.

### Implementing the callback

First, create the `IdentificationManager.Callback` in your activity to listen and react to identification events.  

```kotlin

val identificationCallback = object: IdentificationManager.Callback{
        override fun onCompleted(resultUrl: String) {
            // The identification was complete, display a success message to the user and fetch the identification result from the server using the resultUrl
            TODO("not implemented") 
        }

        override fun onRequestAccessRights(accessRights: List<String>) {
            // A list of the fields that the sdk is trying to access has arrived. Display them to the user and await his confirmation.
            TODO("not implemented") 
        }
        
        override fun onRequestCertificate(certificateInfo: CertificateInfo, certificateValidity: CertificateValidity) {
            // The certificate info has beed requested and is delivered here
             TODO("not implemented") 
        }
        
        override fun onCardRecognized(card: Card?) {
            // A card was attached to the NFC reader
            TODO("not implemented") 
        }

        override fun onRequestPin() {
            // The id cards PIN was requested. Display a PIN dialog to the user.
            // To continue the identification process, call identificationManager.setPin(pin: String)
            TODO("not implemented") 
        }

        override fun onRequestPuk() {
            // The id cards PUK was requested. Display a PUK diaphlog to the user.
            // To continue the identification process, call identificationManager.setPuk(puk: String)
            TODO("not implemented") 
        }

        override fun onRequestCan() {
            // The id cards CAN was requested. Display a CAN dialog to the user.
            // To continue the identification process, call identificationManager.setCan(can: String)
            TODO("not implemented") 
        }

        override fun onError(error: String) {
            // An error occured. Display an error/issue dialog to the user.
            TODO("not implemented") 
        }
      
    }
```

## Binding the IdentificationManager to your activities lifecycle

Then, initialize an `IdentificationFragment` in your activites `onCreate` method to bind to the activity livecycle. 
For concenience, we make it available within the activity with a getter. 

To access the `identificationManager` to communicate with the SDK, implement the `IsIdentificationUI` interface. 

TODO: Remove overhead on the interface

```kotlin
class IndependentIdentificationActivity : AppCompatActivity(), IsIdentificationUI {

    var identificationFragment: IdentificationFragment? = null
    // convenience getter
    override val identificationManager: IdentificationManager?
        get() {
            return identificationFragment?.identificationManager
        }
   
    override fun onCreate(savedInstanceState: Bundle?) {
        // ... 
       
        identificationFragment = IdentificationFragment.newInstance(this)
        identificationFragment!!.identificationManager.addCallback(identificationCallback)
        
    }
}
```


### Dispatching NFC Tags to the manager

In order to read the id cards data from the NFC chip, you need to dispatch NFC tags from your activity to the `IdentificationManager`.

Add this code to your activity to receive and pass the NFC intents: 

```kotlin
 /*
    To receive and dispatch NFC tags to the SDK, we need to initalize a forground dispatcher and attach it to the lifecycle.
    Then, we can pass Tag Intents from the `onNewIntent` to the `Identificationmanager`
     */

    var mDispatcher: ForegroundDispatcher? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
       
        // ... 
       
        // Initialize the NFC Tag foreground dispatcher
        mDispatcher = ForegroundDispatcher(this)
    }
    public override fun onResume() {
        super.onResume()
        mDispatcher!!.enable()
    }

    public override fun onPause() {
        super.onPause()
        mDispatcher!!.disable()
    }

    override fun onNewIntent(intent: Intent?) {
        // dispatch NFC tag intents to the manager
        super.onNewIntent(intent)
        val tag = intent!!.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        if (tag != null) {
            identificationManager?.dispatchNfcTag(tag)
        }
    }
```

To start the identification process call the `identificationManagers.startIdent` method with your `tcTokenUrl`. 


```kotlin
val tcTokenUrl = AusweisIdentBuilder()
            .ref()
            .clientId("your-client-id")
            .redirectUrl("your-redirect-url")
            .state("your-persistant-id")
            .scope(AusweisIdentScopes.FAMILY_NAMES)
            .scope(AusweisIdentScopes.GIVEN_NAMES)
            .scope(AusweisIdentScopes.DATE_OF_BIRTH)
            .build()
identificationFragment?.identificationManager?.startIdent(tcTokenUrl);
```

For a fully working example, see [IndependentIdentificationActivity](../example/src/main/java/com/twigbit/identsdk/example/IndependentIdentificationActivity.kt).

### Displaying certificate information

To display information about the service providers certificate, you can call the method `identificationManager.getCertificate()` to request the certificate information. 
The certificate info will be delivered asynchronously in the callback via the `onRequestCertificate(certificateInfo: CertificateInfo, certificateValidity: CertificateValidity)` method. 
The certificate informations are mirrored from the AusweisApp2 SDK messanges and are wrapped by the classes [CertificateInfo](../identsdk/src/main/java/com/twigbit/identsdk/core/IdentificationUtil.kt) and [CertificateValidity](../identsdk/src/main/java/com/twigbit/identsdk/core/IdentificationUtil.kt) 

### Handling the result URL 

TODO: Cleanup/ refactor 

Calling this url will result in several redirects with the last redirect pointing to your redirectUrl with the `code` query parameter after a successful identification or an `error` and `error_description` parameter in case of an error. Your server needs this `code` to receive the user info.

```
https://localhost:10443/demo/login/authcode?code=S6GKv5dJNwy6SXlRrllay6fcaoWeUWjA6ar5gahrGSI823sFa4&state=123456
```

> _**Warning:** If you decide to call the url on your own (and not pass it to a browser) you need to make sure to store and send cookies between the redirects._

> **AusweisIdent Backend Examples:** Our [twigbit/ausweisident-backend-examples](https://github.com/twigbit/ausweisident-backend-examples) repository contains example implementations (currently for NodeJS only, Go and other languages coming soon) to receive the user info on the server side.

If you are using the same server side architecture as the example, you can use the `AusweisIdentResultHanlder` to take care of handline the result for you. 

```kotlin
val resultHandler: AusweisIdentResultHandler =
        AusweisIdentResultHandler(object : AusweisIdentResultHandler.Callback {
            override fun onError(message: String) {
                Log.d(Tags.TAG_IDENT_DEBUG, "An error occured")
            }

            override fun onComplete(userInfo: UserInfo) {
                Log.d(Tags.TAG_IDENT_DEBUG, userInfo.toString())
            }
        })
       
resultHandler.fetchResult(resultUrl);

```


### Server side implementation

TODO remove general info. Stronger pointer to server side sample repo. 
1. Use the _code_ to obtain an _access token_ from the AusweisIdent OAuth2 Token Endpoint.
2. Use the _access token_ to get an _user info token_ via the OAuth2 User Info Endpoint containing the personal data from the identification document.

TODO rephrase
Please see the AusweisIdent documentation for further details or check out our [server example](https://github.com/twigbit/ausweisident-backend-examples).


### Example

A working implementation can be found in the `/example` directory. Please note that you need a test PA to test the identification flow in the reference system.

### Limitations

- The [AusweisApp2 SDK](https://www.ausweisapp.bund.de/sdk/) only supports arm64-v8a architecures since version 15.03. Unfortunalety, we are bound to that limitation. 

# Roadmap 

## This project is actively under development.

For informations, contact [post@twigbit.com](mailto:post@twigbit.com) .

| Status    | Version          |
| --------- | ---------------- |
| released | 0.1.2 unreleased |

## Changelog/Milestones

### 0.1.1
* [ausweisident] Return Result URL directly, refactor call redirects into optional method in AusweisIdent helper.
* [ausweisident] Configuration helper
* [core] Refactor state into callbacks.
* [core] Persistant abstractions for the command and message system
* [core] Review inheritance model and draft alternative livecycle-aware architecture that offers more flexibility.
* [core] Explicitly handle result URL. 
* [dropin] Dropin UI basic implementation
* [dropin] Dropin styling

### 0.1.2
* [dropin] Certificate view
* [core] Test simplified configuration procedure.

### 0.1.3
* [core] Usability improvements
* [documentation] Improve docs

### 0.1.4
* [core] Small usability improvements
* [core] Result URL resolution example
* [documentation] Improve docs

### Backlog 
* [ausweisident] Server implementation guide.
* [ausweisident] Provide Util for evaluating the result URL.


### Nice to have 

* Vibrate on NFC message.
* Capability check- check whether the users device has the required architecture and NFC capabilities
* Provides a fallback to prompt the user to install the official [AusweisApp2] (https://www.ausweisapp.bund.de/) in case of unsupported architecture (see section `Limitations`)

---

### Copyright

```
(c) Copyright 2018 twigbit technologies GmbH. All rights reserved.
```

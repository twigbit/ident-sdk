# Twigbit Ident SDK

The Twigbit Ident SDK is a lightweight convenience layer on top of the [AusweisApp2 SDK](https://www.ausweisapp.bund.de/fuer-diensteanbieter/software-development-kit-sdk/) written in Kotlin.
We are aiming to extract and eliminate the recurring code and configuration that every developer faces integrating AusweisIdent functionality in their apps. 
Moreover, we are providing convenience tooling for the [AusweisIdent mobile identification service](https://www.ausweisident.de/) provided by Bundesdruckerei GmbH and Governikus KG. 

[![twigbit technologies GmbH](docs/images/logo-twigbit.png)](https://www.twigbit.com)
[![Governikus GmbH & Co. KG](docs/images/logo-governikus.png)](https://www.governikus.de/)
[![AusweisIDent](docs/images/logo-ausweisident.png)](https://www.ausweisident.de/)


## Features

- Simplify the tedious [AusweisApp2 SDK](https://www.ausweisapp.bund.de/sdk/) configuration
- Replace the JSON based messaging system by convenient wrapper methods, giving developers to must-have convenience such as code completion
- Lightweight — besides the [AusweisApp2 SDK](https://www.ausweisapp.bund.de/sdk/), the only other dependency is [Google GSON](https://github.com/google/gson) for JSON parsing
- Drop-in UI — Provide a simple, customizable drop in UI as a quick integration with identification processes

# Roadmap 

##This project is actively under development. 

For informations, contact [ident@twigbit.com](mailto:ident@twigbit.com) .

| Status    | Version          |
| --------- | ---------------- |
| **WIP** | 0.1.1 unreleased |

## Changelog/Milestones

### 0.1.1
* [ausweisident] Return Result URL directly, refactor call redirects into optional method in AusweisIdent helper.
* [ausweisident] Configuration helper
* [core] Refactor state into callbacks.
* [core] Persistant abstractions for the command and message system
* [dropin] Dropin UI basic implementation


### Backlog 
* [dropin] Dropin styling & certificate view
* [core] Explicitly handle result URL. 
* [core] Review inheritance model and draft alternative livecycle-aware architecture that offers more flexibility.
* [core] Test simplified configuration procedure.
* [ausweisident] Server implementation guide.
* [ausweisident] Provide Util for evaluating the result URL.


### Nice to have 

* Vibrate on NFC message.
* Capability check- check whether the users device has the required architecture and NFC capabilities
* A custom identification app as a zero-dependency option for the integration
* Provides a fallback to prompt the user to install the official [AusweisApp2] (https://www.ausweisapp.bund.de/) in case of unsupported architecture (see section ``Limitations`)

---

# Documentation 

All code is provided in Kotlin. The integration works in Java analogously, all samples are interchangable. 

## Quick start 
Using the drop in UI, you can implement a fully functional AusweisIDent identification cycle within minutes. 

First, add the drop in ui as a dependency to your projects `build.gradle` file. 

```gradle
dependencies {
  implementation 'com.twigbit.identsdk:drop-in:0.1.1'
}
```

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
            .scope(AusweisIdentScopes.FAMILY_NAME)
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
                
                val resultUrl = data.getParcelableExtra(IdentificationManager.EXTRA_DROPIN_RESULT)
                // to deliver the data to the server, call this URL and follow the redirect chain
                                
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the identification
            } else {
                // An error occured during the identification
            }
        }
    }
```

You will get the result urlas described above. Calling this url will result in several redirects with the last redirect pointing to your redirectUrl with the `code` query parameter after a successful identification or an `error` and `error_description` parameter in case of an error. Your server needs this `code` to receive the user info. 

```
https://localhost:10443/demo/login/authcode?code=S6GKv5dJNwy6SXlRrllay6fcaoWeUWjA6ar5gahrGSI823sFa4&state=123456
```

> _**Warning:** If you decide to call the url on your own (and not pass it to a browser) you need to make sure to store and send cookies between the redirects._

> _**Note:** We are working on implementing helper methods to simplify this process._


## Implement your own UI 

### COMING SOON

First, add the drop in ui as a dependency to your projects `build.gradle` file. 

```gradle
dependencies {
  implementation 'com.twigbit.identsdk:ident-sdk:0.1.1'
}
```

To receive the SDK's identification state callbacks in your activity, implement the `IdentificationManager.Callback` interface and extend the `IdentificationActivty` to bind an `IdentificationManager` instance to your activities lifecycle. 

 In your activities `onCreate` method, add the callback to the manager and start the identifcation process. 

 > _**Note:** As the callback method might be called from a different thread, be sure to run all UI operations on your UI thread explicitly._

<!--**Example Activity**
```kotlin
class ExampleActivity: AppCompatActivity() {

    lateinit val mIdent: Ident

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mIdent = Ident.newInstance(this, ...)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val tag = intent!!.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        if (tag != null) {
            mIdent.dispatchNfcTag(tag)
        }
    }
}
```-->

```kotlin


class MainActivity : IdentificationActivity() {

   val identificationCallback = object: IdentificationManager.Callback{
        override fun onCompleted(resultUrl: String) {
            // The identification was complete, display a success message to the user and fetch the identification result from the server using the resultUrl
        }

        override fun onRequestAccessRights(accessRights: ArrayList<String>) {
            // A list of the fields that the sdk is trying to access has arrived. Display them to the user and await his confirmation.
            // TODO continue with runIdent()
        }

        override fun onCardRecognized(card: IdentificationCard) {
            // A card was attached to the NFC reader
            // TODO @dev implement card model from JSON message params.
        }

        override fun onRequestPin() {
            // The id cards PIN was requested. Display a PIN dialog to the user.
            // To continue the identification process, call identificationManager.setPin(pin: String)
        }

        override fun onRequestPuk() {
            // The id cards PUK was requested. Display a PUK dialog to the user.
            // To continue the identification process, call identificationManager.setPuk(puk: String)
        }

        override fun onRequestCan() {
            // The id cards CAN was requested. Display a CAN dialog to the user.
            // To continue the identification process, call identificationManager.setCan(can: String)
        }

        override fun onError(error: IdentificationError) {
            // An error occured. Display an error/issue dialog to the user.
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        identificationManager.addCallback(identificationCallback)
    }
}
```

To start the identification process call the `identificationManagers.startIdent` method with your `tcTokenUrl`. 


```kotlin
identificationManager.startIdent(tcTokenUrl)
```



> _**Note:** We are working on a lifecycle aware alternative to the IdentificationActivity to give you the flexibility to inherit from your own base activity. This requires you to to override the `onNewIntent` method of the activity and pass down intents to the identificationManager:_
> ```kotlin
> override fun onNewIntent(intent: Intent?) {
>     super.onNewIntent(intent)
>     val tag = intent!!.getParcelableExtra<Tag>fdapter.EXTRA_TAG)
>     if (tag != null) {
>         identificationManager.dispatchTag(tag)
>     }
> }
> ```

#### Server side implementation

1. Use the _code_ to obtain an _access token_ from the AusweisIdent OAuth2 Token Endpoint.
2. Use the _access token_ to get an _user info token_ via the OAuth2 User Info Endpoint containing the personal data from the identification document.

Please see the AusweisIdent documentation for further details or check out our [server sample](#) (coming soon).
<!-- TODO: server sample link -->


### Sample

A working implementation can be found in the `/samples` directory. Please note that you need a test PA to test the identification flow in the reference system.

### Copyright

### Limitations

- The [AusweisApp2 SDK](https://www.ausweisapp.bund.de/sdk/) only supports arm64-v8a architecures since version 15.03. Unfortunalety, we are bound to that limitation. 


```
(c) Copyright 2018 twigbit technologies GmbH. All rights reserved.
```

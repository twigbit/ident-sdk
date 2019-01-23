# Twigbit Ident SDK

| Status    | Version          |
| --------- | ---------------- |
| **DRAFT** | 0.1.0 unreleased |

**NOTICE:** This is a confidential document.
<!-- @moritz TODO: cooler disclosure satz -->

The Twigbit Ident SDK is a lightweight convenience layer on top of the [AusweisApp2 SDK](https://www.ausweisapp.bund.de/fuer-diensteanbieter/software-development-kit-sdk/) written in Kotlin.
We are aiming to extract and eliminate the recurring code and configuration that every developer faces integrating the SDK.

### Features

- Simplify the tedious [AusweisApp2 SDK](https://www.ausweisapp.bund.de/sdk/) configuration
- Replace the JSON based messanging system by convenient wrapper methods, giving developers to must-have convenience such as code completion
- Lightweight- besides the [AusweisApp2 SDK](https://www.ausweisapp.bund.de/sdk/), the only other dependency is [Google GSON](https://github.com/google/gson) for JSON parsing
- Drop-in UI - Provide a simple, customizable drop in UI as a quick integration with identification processes
- (coming soon) Capability check- check whether the users device has the required architecture and NFC capabilities
- (coming soon) Build an identification app as a zero-dependency option for the integration

### Limitations

- The [AusweisApp2 SDK](https://www.ausweisapp.bund.de/sdk/) only supports arm64-v8a architecures since version 15.01. Unfortunalety, we are bound to that limitation. (comming soon) This SDK provides a fallback to prompt the user to install the official [AusweisApp2](https://www.ausweisapp.bund.de/).

## Usage

The usage examples are provided in Kotlin. The integration works in Java analogously.

### Download

To get access to the SDK, please [get in touch](https://www.twigbit.com/ident).

Gradle:

```gradle
dependencies {
  implementation 'com.twigbit.identsdk:identsdk:1.0.0'
}
```

Maven:

```xml
<dependency>
  <groupId>com.twigbit.identsdk</groupId>
  <artifactId>identsdk</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Identify users with the Drop-In UI (beta)

To get started quickly and have the SDK take care of the entire identification process for you, you can use the build-in Drop-in UI.

To start an identification process, simply create a DropinRequest with your client tokent and start the activity for the result.

```kotlin
val REQUEST_CODE_IDENTIFICATION = 0;

private fun startDropInIdentification(){
    val dropInRequest = DropInRequest("jlisiasegljisgilj") // your client token
    startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE_IDENTIFICATION)
}
```

To receive the identification result, you should override your activities `onActivityResult`.

```kotlin
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_IDENTIFICATION) {
            if (resultCode == Activity.RESULT_OK) {
                // Success. Update the UI to reflect the successful identification
                // and fetch the user data from the server where they were delivered.
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the identification
            } else {
                // An error occured during the identification
            }
        }
    }
```

### Implement your own UI

To receive the SDK's identification state callbacks in your activity, extend the `IdentificationActivty`. This will bind an`IdentificationManager` instance to your activities lifecycle. 

To receive the identification state events, you must implement the `onStateChanged` method. As this method might be called from a different thread, be sure to run all UI operations on your UI thread explicity. 




```kotlin
class MainActivity : IdentificationActivity() {
    override fun onStateChanged(state: String, data: String?) {
        runOnUiThread {
            when (state) {
                IdentificationManager.STATE_COMPLETE -> {
                    // The identification was complete, display a success message to the user and fetch the identification result from the server
                }
                IdentificationManager.STATE_ACCESSRIGHTS -> {
                    // A list of the id-card fields that the sdk is trying to access has arrived. Display them to the user and await his confirmation.
                    // TODO continue with runIdent()
                    // TODO better parameter typing
                }
                IdentificationManager.STATE_CARD_INSERTED -> {
                    // A card was attached to the NFC reader
                    // TODO show empty card and detach data.
                }
                IdentificationManager.STATE_ENTER_PIN -> {
                    // The id cards PIN was requested. Display a PIN dialog to the user.
                    // To continue the identification process, call identificationManager.setPin(pin: String)
                }
                IdentificationManager.STATE_ENTER_PUK -> {
                    // The id cards PUK was requested. Display a PUK dialog to the user.
                    // To continue the identification process, call identificationManager.setPuk(puk: String)
                }
                IdentificationManager.STATE_ENTER_CAN -> {
                    // The id cards CAN was requested. Display a CAN dialog to the user.
                    // To continue the identification process, call identificationManager.setCan(can: String)
                }
                IdentificationManager.STATE_BAD -> {
                    // Bad state. Display an error/issue dialog to the user.
                    // TODO figure out reasons for bad state, offer solutions, i.e. id card blocked, id card detached. More granular apporach needed. 
                }
            }
        }
    }
}
```

### Get JSON Messanges for deeper control (coming soon)

If you want to get access to the AusweisApp2 SDK's string messages for deeper control over the hardware or some other reasons, implement the `onMessage` callback. 

```kotlin
class MainActivity : IdentificationActivity() {
    override fun onMessage(message: Message) {
        
    }
}

```

### Sample

A working implementation can be found in the `/samples` directory. Please note that you need a test PA to test the identification flow in the reference system. 

### Copyright

```
Copyright 2018 twigbit technologies GmbH. All rights reserved.
```

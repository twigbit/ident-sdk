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

### Identify users with the Drop-In UI (coming soon)

To get started quickly and have the SDK take care of the entire identification process for you, you can use the build-in Drop-in UI.

To start an identification process, simply create a DropinRequest with your client tokent and start the activity for the result.

```kotlin
val REQUEST_CODE_IDENTIFICATION = 0;

private fun startDropInIdentification(){
    val dropInRequest = DropInRequest("RmluZ2VycHJpbnQiOiI") // your client token
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

To host you have the Activity integrating the identifcation flow extend the ``IdentificationActivty` and implement the abstract methods.

```kotlin
class MainActivity : IdentificationActivity() {
    override fun onError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMessage(message: Message) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onComplete(url: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

```

### Sample (coming soon)

### Copyright

```
Copyright 2018 twigbit technologies GmbH. All rights reserved.
```

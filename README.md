# Twigbit Ident SDK

The Twigbit Ident SDK is a lightweight convenience layer on top of the [AusweisApp2 SDK](https://www.ausweisapp.bund.de/fuer-diensteanbieter/software-development-kit-sdk/) written in Kotlin.
We are aiming to extract and eliminate the recurring code and configuration that every developer faces integrating the SDK.

### Features
  * Simplify the tedious [AusweisApp2 SDK](https://www.ausweisapp.bund.de/sdk/) configuration
  * Replace the JSON based messanging system by convenient wrapper methods, giving developers to must-have convenience such as code completion
  * Lightweight- besides the [AusweisApp2 SDK](https://www.ausweisapp.bund.de/sdk/), the only other dependency is [Google GSON](https://github.com/google/gson) for JSON parsing
  * (coming soon) Drop-In-UI - Provide a simple, customizable drop in UI as a quick integration with identification processes
  * (coming soon) Build an identification app as a zero-dependency option for the integration

### Download

To get acces to the SDK, please [get in touch](https://www.twigbit.com/ident). 

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

### Usage

### Sample

### Copyright

```
Copyright 2018 Moritz Morgenroth. All rights reserved. 
```

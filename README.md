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

Gradle:
```gradle
dependencies {
  implementation 'com.google.code.gson:gson:2.8.5'
}
```

Maven:
```xml
<dependency>
  <groupId>com.google.code.gson</groupId>
  <artifactId>gson</artifactId>
  <version>2.8.5</version>
</dependency>
```

[Gson jar downloads](https://maven-badges.herokuapp.com/maven-central/com.google.code.gson/gson) are available from Maven Central.

[![Build Status](https://travis-ci.org/google/gson.svg?branch=master)](https://travis-ci.org/google/gson)

### Documentation
  * [API Javadoc](http://www.javadoc.io/doc/com.google.code.gson/gson): Documentation for the current release
  * [User guide](https://github.com/google/gson/blob/master/UserGuide.md): This guide contains examples on how to use Gson in your code.
  * [Change log](https://github.com/google/gson/blob/master/CHANGELOG.md): Changes in the recent versions
  * [Design document](https://github.com/google/gson/blob/master/GsonDesignDocument.md): This document discusses issues we faced while designing Gson. It also includes a comparison of Gson with other Java libraries that can be used for Json conversion

Please use the 'gson' tag on StackOverflow or the [google-gson Google group](http://groups.google.com/group/google-gson) to discuss Gson or to post questions.

### Related Content Created by Third Parties
  * [Gson Tutorial](http://www.studytrails.com/java/json/java-google-json-introduction.jsp) by `StudyTrails`
  * [Gson Tutorial Series](https://futurestud.io/tutorials/gson-getting-started-with-java-json-serialization-deserialization) by `Future Studio`
  * [Gson API Report](https://abi-laboratory.pro/java/tracker/timeline/gson/)

### License

Gson is released under the [Apache 2.0 license](LICENSE).

```
Copyright 2008 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

### Disclaimer

This is not an officially supported Google product.

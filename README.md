# Kapun SDK

The Kapun SDK is a Kotlin Multiplatform toolkit for building digital-identity wallets and verifiers on Android and iOS. It includes credential formats, DCQL, issuance, presentation, wallet, cryptography, trust, proximity, and visualization modules.

The repository also contains sample Android wallet and verifier applications and an iOS sample. The samples are demonstrations and may require application-specific wallet or backend configuration.

## Prerequisites

- JDK 17 or newer
- Android Studio for the Android samples
- Xcode 15.4 or newer for the iOS sample

## Build the samples

### Android

```bash
./gradlew :examples:android-wallet:assembleDebug
./gradlew :examples:android-wallet:installDebug

./gradlew :examples:android-verifier:assembleDebug
./gradlew :examples:android-verifier:installDebug
```

### iOS

Open `sample-ios-kapun.xcodeproj` in Xcode, select a simulator or device, and run the `sample-ios-kapun` target. Device builds require your own provisioning profile. The sample requires iOS 16 or newer.

If the build needs Android SDK information, set `ANDROID_HOME` and create a root `local.properties` file containing:

```properties
sdk.dir=/path/to/your/android/sdk
```

## Use the published SDK

Released artifacts are available from Maven Central. Add `mavenCentral()` to your repositories and depend on the module you need, for example:

```kotlin
dependencies {
    implementation("org.kapunsdk:kapun-wallet:1.0.0")
}
```

Replace `1.0.0` with the release you want.

## Publish locally

To publish JVM artifacts to your local Maven repository:

```bash
./gradlew publishJvmPublicationToMavenLocal -PARTIFACT_VERSION=1.0.0-LOCAL
```

The resulting JVM coordinates use the `org.kapunsdk` group, for example `org.kapunsdk:kapun-trust-jvm:1.0.0-LOCAL`. Publishing all targets requires the corresponding Rust toolchains. Release signing is disabled by default.

## License

This project is licensed under the Apache License 2.0. See [LICENSE](./LICENSE).

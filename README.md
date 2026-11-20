# Would You Rather Extreme?

A native Android party game for groups sharing one phone. Dark, modern look. Works fully offline, with no accounts, no ads and no subscriptions.

## Status

**Stage 1 - first playable prototype.** A home screen with a Play button and a Premium card (placeholder), and a game screen with two large answer cards and a Next button. It uses 3 temporary sample questions; the real question packs and saved progress come in later stages.

## Planned

- 50 free and 50 premium original questions across five categories
- More premium packs added over time
- One-time premium unlock through Google Play Billing

## Building

Builds run in the cloud with GitHub Actions (`.github/workflows/android-build.yml`), so no local Android Studio is needed.

1. Open the **Actions** tab and pick the latest **Android build** run.
2. When it is green, download the `debug-apk` file from the **Artifacts** section.
3. Copy the APK to an Android phone and install it.

## Project facts

- Kotlin and Jetpack Compose
- App ID: `com.wouldyouratherextreme.game`
- Minimum Android 8.0 (API 26), targets Android 16 (API 36)

## Secrets

No keys, keystores or credentials are stored in this repository. Release signing keys will live in GitHub Secrets.

# Would You Rather Extreme?

A native Android party game for groups sharing one phone. Dark, modern look. Works fully offline, with no accounts, no ads and no subscriptions.

## Status

**Stage 1 - first playable prototype.** A home screen with a Play button and a Premium card, a game screen with two large answer cards and a Next button (questions come in a random order), an end-of-round screen with "Reshuffle questions" and "Get more questions" buttons, and a Premium preview page (the unlock button is switched off until Google Play Billing is added). An adults-only (18+) warning screen appears first when the app starts. The 50 free questions are loaded from a file inside the app (`app/src/main/assets/questions/free_pack.json`). The 50 premium questions are in `premium_pack_01.json`. **In the test version** (the debug APK from the cloud build) the Premium page has an "Unlock Premium (test)" button that simulates the purchase, so all 100 questions can be played without paying. It exists only in debug builds; in a public release build the unlock stays switched off until Google Play Billing is added. Saved progress comes in a later stage.

## Questions

Questions live in JSON files, not in the code. Each question has two answer cards (`optionA`, `optionB`) and an optional `intro` for questions that start with a short set-up story. A new premium pack is a new file named `premium_pack_02.json` (and so on) in `app/src/main/assets/questions/`; the app finds it automatically. Unit tests check the real file on every build (exact count, unique ids, no empty text).

## Planned

- Save each phone's shuffled question order and progress locally (now that all 100 questions are in)
- Remember the 18+ confirmation between app starts
- More premium packs added over time
- One-time premium unlock through Google Play Billing

## Building

Builds run in the cloud with GitHub Actions (`.github/workflows/android-build.yml`), so no local Android Studio is needed.

### Install the test version on a phone

Every push to `main` that builds successfully publishes the newest test APK here:

**https://github.com/adrianwethli/would-you-rather-extreme/releases/download/test-latest/WouldYouRatherExtreme-test.apk**

1. Open that link in the phone's browser (if the repository is private, log in to GitHub in the browser first).
2. Tap the downloaded file. Android asks once to allow installing from this source; allow it.
3. If Google Play Protect warns about an unknown app, choose "Install anyway" (it is a test build that is not on the Play Store).

The test build is signed with a different temporary key each time, so **uninstall the old version before installing a newer one**.

The same APK is also attached to every run on the **Actions** tab, in the **Artifacts** section (`debug-apk`, as a zip file).

## Project facts

- Kotlin and Jetpack Compose
- App ID: `com.wouldyouratherextreme.game`
- Minimum Android 8.0 (API 26), targets Android 16 (API 36)

## Secrets

No keys, keystores or credentials are stored in this repository. Release signing keys will live in GitHub Secrets.

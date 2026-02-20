# Firebase Admin SDK - Workload Identity Federation Demo

[![Node.js](https://github.com/lahirumaramba/bookish-rotary-phone/actions/workflows/nodejs-test.yaml/badge.svg)](https://github.com/lahirumaramba/bookish-rotary-phone/actions/workflows/nodejs-test.yaml)
[![Java](https://github.com/lahirumaramba/bookish-rotary-phone/actions/workflows/java-test.yaml/badge.svg)](https://github.com/lahirumaramba/bookish-rotary-phone/actions/workflows/java-test.yaml)
[![Python](https://github.com/lahirumaramba/bookish-rotary-phone/actions/workflows/python-test.yaml/badge.svg)](https://github.com/lahirumaramba/bookish-rotary-phone/actions/workflows/python-test.yaml)
[![Go](https://github.com/lahirumaramba/bookish-rotary-phone/actions/workflows/go-test.yaml/badge.svg)](https://github.com/lahirumaramba/bookish-rotary-phone/actions/workflows/go-test.yaml)
[![.NET](https://github.com/lahirumaramba/bookish-rotary-phone/actions/workflows/dotnet-test.yaml/badge.svg)](https://github.com/lahirumaramba/bookish-rotary-phone/actions/workflows/dotnet-test.yaml)

This repository demonstrates how to initialize the [Firebase Admin SDK](https://firebase.google.com/docs/admin/setup) using **Workload Identity Federation (WIF)** / Application Default Credentials (ADC) across multiple programming languages. 

Authenticating via WIF allows CI/CD systems like GitHub Actions to interact with Google Cloud and Firebase securely *without* requiring long-lived exported Service Account JSON keys.

## Supported Languages

This project contains minimal test suites demonstrating initialization, authentication (fetching users), Firestore (reading documents), and Custom Token creation for the following environments:

- **Node.js**: Located in `/nodejs` (Uses `vitest`)
- **Java**: Located in `/java` (Uses `JUnit 5` & `Maven`)
- **Python**: Located in `/python` (Uses `pytest`)
- **Go**: Located in `/go` (Uses standard `testing`)
- **.NET (C#)**: Located in `/dotnet` (Uses `MSTest`)

## GitHub Actions Workflows

Each implemented language has a corresponding GitHub Actions workflow located in `.github/workflows/`, which authenticates to Google Cloud via WIF using `google-github-actions/auth@v2` and verifies the SDK works as expected in a real CI environment.

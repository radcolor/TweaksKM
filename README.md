# Tweaks
[![forthebadge](https://forthebadge.com/images/badges/built-for-android.svg)](https://www.android.com)
[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://www.github.com/theradcolor)


**Tweaks** is a simple [Linux](https://www.kernel.org) kernel manager app for Android™ devices running on Android L or newer. 
Designed beautifully with Material design.

**Note:** The contributors are requested to read CONTRIBUTING.md and all required documentation, and send pull requests over dev/staging branch!

### Setup

### Requirements
- JDK 8
- Latest Android SDK and platform tools
- Minimum SDK 21 or newer
- AndroidX

### Building

```
$ git clone https://github.com/theradcolor/TweaksKM.git
$ cd TweaksKM
$ bash gradlew assembleDebug
```

## Working

App needs the Android's root permission to attain [privileged control](https://en.wikipedia.org/wiki/Privilege_escalation) (known as [root access](https://en.wikipedia.org/wiki/Superuser)) over various Android subsystems. As Android uses the Linux kernel, rooting an Android device gives similar access to administrative (superuser) permissions as on Linux or any other Unix-like operating system.

## ScreenShots

![ScreenShot 1](/assets/app_ss.png)

## Reporting bug or feature request

You can easily report a bug or request a feature by opening a [pull request](https://github.com/theradcolor/TweaksKM/compare) or [opening an issue](https://github.com/theradcolor/TweaksKM/issues/new/choose)

#### How to report a bug

- Make sure of taking a logs in detail
- Make sure no other similar bugs already reported

#### How to request a feature

- A detail description of feature
- Paths to sysFS interface, How to apply and use of it.

## Opensource licenses

RootUtils/Tools from Kernel Adiutor by [Wille Ye](https://github.com/Grarak) - [GPLv3](https://www.gnu.org/licenses/gpl-3.0)

libsu by [John Wu](https://github.com/topjohnwu) - [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)

hellochart-android by [Leszek Wach](https://github.com/lecho) - [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)


## Contributing

Read [Contributing.md](https://github.com/theradcolor/tweaks/blob/master/CONTRIBUTING.md) to get the app running locally and ways to help us.


## License

[![GPL](https://img.shields.io/badge/License-GPL--v3.0-green?style=for-the-badge)](https://github.com/theradcolor/Tweaks/blob/master/LICENSE)


TweaksKM is licensed under the under version 3 of the [GNU GPL License](https://github.com/theradcolor/Tweaks/blob/master/LICENSE).

The GNU General Public License is a free, copyleft license for software and other kinds of works.
When we speak of free software, we are referring to freedom, not price. Our General Public Licenses are designed to make sure that you have the freedom to distribute copies of free software (and charge for them if you wish), that you receive source code or can get it if you want it, that you can change the software or use pieces of it in new free programs, and that you know you can do these things.

Copyright (c) 2020 Shashank Baghel. All rights reserved.

<p align="center">Made with ❤ by <a href="https://github.com/theradcolor">Shashank Baghel</a></p>

# Brief Introduction
This is a simple Android application that aims at downloading data about sale bundles using API REST calls and show the user the total money to pay in different currencies.

### Getting started
This code needs to be executed on an Android device or an Android emulator.

### Installation Requirements
The following requirements are needed:
- `gradle` tool for compiling



### Installation
First ensure `gradle` is installed. The following works in macOS using `Homebrew`:
```Bash
if (! gradle --version 2> /dev/null); then brew install gradle; fi
```

Once gradle is installed, do one of the following:
* If you haven't cloned this repository already:
```Bash
git clone git@github.com:piloudu/sample-sales-app.git
cd sample-sales-app
gradle build
```

* If you have cloned this repository in `<path_to_repository>`:
```Bash
cd <path_to_repository>
gradle build
```

Once the `gradle build` finishes, an Android installable `.apk` file will be generated in `<path_to_repository>/app/build/outputs/apk/release/` folder.

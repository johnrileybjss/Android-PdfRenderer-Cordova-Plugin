## Android PDFRenderer Cordova Plugin

This is a simple Cordova Plugin to surface the display functionality of the Android PDFRenderer API.

As an example, the application loads a sample PDF file from the assets folder of the test project.

The application was tested on an emulator running a Nexus 6 image using API 24.

Both the Plugin and the Test Project were built using Cordova 6.5.0.

## Building the Project

I've included a series of Windows command files to facilitate building the project.

In order to build the Android APK, you can simply run "build-project" from the root directory of the project.

"build-plugin" will rebuild the cordova plugin, and "build-app" will update the cordova plugin in the app directory and rebuild the test application.

## Running the Application

Once your project has been built, simply open /testProject/platforms/android/ in Android Studio, and run the application with your chosen emulator.

Alternatively, once the apk is built, load the apk directly onto your Android Device.
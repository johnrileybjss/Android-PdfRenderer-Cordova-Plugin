## Android PDFRenderer Cordova Plugin

This is a simple Cordova Plugin to surface the display functionality of the Android PDFRenderer API.

As an example, the application loads a sample PDF file from the assets folder of the test project.

The application was tested on an emulator running a Nexus 6 image using API 24.

Both the Plugin and the Test Project were built using Cordova 6.5.0.

## Building the Project

I've included a series of Windows command files to facilitate building the project.

In order to build the Android APK, you can simply run ```build-project``` from the root directory of the project.

```build-plugin``` will rebuild the cordova plugin, and ```build-app``` will update the cordova plugin in the app directory and rebuild the test application.

## Running the Application

Once your project has been built, simply open /testProject/platforms/android/ in Android Studio, and run the application with your chosen emulator (or a USB connected device).

Alternatively, once the apk is built, load the apk directly onto your Android Device.

## Testing the Plugin

The plugin functionality can be tested by running the ```test-plugin``` command from the root directory of the project.

This command will install cordova-paramedic" in the directory of the custom plugin, which is a framework that allows automated (and manual) testing of a Cordova plugin via Emulation.

Once Cordova Paramedic is installed, it will load an Emulator from the AVD and run the unit tests located in the /cordova-android-pdf-renderer-plugin/tests/ directory.
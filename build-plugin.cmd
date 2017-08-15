cd cordova-android-pdf-renderer-plugin/
echo "Adding Android Platform"
call cordova platform add android
echo "Running Cordova Build"
call cordova build
echo "Cordova Build Completed"
cd ../
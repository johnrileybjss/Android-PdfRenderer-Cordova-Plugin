cd testProject/
echo "Adding Android Platform"
call cordova platform add android
echo "Updating PDF Renderer Plugin"
call cordova plugin rm cordova-android-pdf-renderer-plugin
call cordova plugin add ../cordova-android-pdf-renderer-plugin
echo "Building App"
call cordova build
echo "App Build Completed"
cd ..
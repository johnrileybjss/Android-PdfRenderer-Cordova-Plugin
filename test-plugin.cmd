cd cordova-android-pdf-renderer-plugin/
echo "Installing Cordova Paramedic"
call npm install cordova-paramedic
echo "Running Unit Tests"
call npm run test:android
echo "Testing Completed"
cd ../
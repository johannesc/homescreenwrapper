homescreenwrapper
=================

Simple Android homescreen application that launches another (com.fibaro.tablet/com.fibaro.tablet.SplashActivity) application.

It also listens for SCREEN_ON and SCREEN_OFF and launches com.android.settings/.Settings if the screen is turned on again within 10s.

This is used in Android based photo frame (from http://www.elclcd.com/).

Some common tricks if you have an USB cable inserted:

# Simpulate home key
adb shell input keyevent 3

# Start settings
adb shell am start -n com.android.settings/.Settings

# Start Fibaro
adb shell am start -n com.fibaro.tablet/com.fibaro.tablet.SplashActivity

# Uninstall this app
adb shell pm uninstall jc.homescreenwrapper

# Upgrade application
adb install -r HomeScreenWrapper.apk


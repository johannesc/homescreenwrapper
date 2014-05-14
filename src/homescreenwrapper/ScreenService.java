package homescreenwrapper;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class ScreenService extends Service {
    private static final String LOG_TAG = "Service";
    private final IBinder binder = new InductionBinder();
    private static final String SCREEN_ON = "screen_on";
    private long lastOff = 0;
    private boolean settingsStarted = false;

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "onCreate...");
        super.onCreate();
        // register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver receiver = new ScreenReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra(SCREEN_ON)) {
            boolean screenOn = intent.getBooleanExtra(SCREEN_ON, true);
            Log.i(LOG_TAG, "onStartCommand " + screenOn);
            long now = SystemClock.uptimeMillis();
            Log.i(LOG_TAG, "Screen was turned off " + (now - lastOff) + "ms ago");

            if (!screenOn) {
                lastOff = now;
            } else if (settingsStarted) {
                settingsStarted = false;
                Log.i(LOG_TAG, "Launching fibaro again since settings was started");
                startFibaro();
            } else if (now - lastOff < 10*1000L) {
                Log.i(LOG_TAG, "Screen was turned off recently, launch settings!");
                startSettings();
                settingsStarted = true;
            } else {
                Log.i(LOG_TAG, "Doing nothing");
            }
        } else {
            Log.i(LOG_TAG, "Start fibaro!");
            startFibaro();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startFibaro() {
        startApplication("com.fibaro.tablet/com.fibaro.tablet.SplashActivity");
    }

    private void startSettings() {
        startApplication("com.android.settings/.Settings");
    }

    private void startApplication(String componentName) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(ComponentName.unflattenFromString(componentName));
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    public class InductionBinder extends Binder {
        public ScreenService getService() {
            return ScreenService.this;
        }
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "onDestroy");
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy - done");
    };

    public class ScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean screenOn = true;
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                screenOn = false;
            }
            Intent i = new Intent(context, ScreenService.class);
            i.putExtra(SCREEN_ON, screenOn);
            context.startService(i);
        }
    }
}

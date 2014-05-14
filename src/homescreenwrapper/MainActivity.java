package homescreenwrapper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate...");
        startScreenService();
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate...done");
    }

    private void startScreenService() {
        Intent serviceIntent = new Intent(this, ScreenService.class);
        startService(serviceIntent);
    }
}

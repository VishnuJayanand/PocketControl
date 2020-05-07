package com.droidlabs.pocketcontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * Main activity of the app.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Method to create an app instance.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        Log.v("CREATE APP", "App started");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper _db = new DBHelper(this);
    }
}

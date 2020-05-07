package com.droidlabs.pocketcontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Main activity of the app.
 */
public class MainActivity extends AppCompatActivity {

    private DBHelper db;

    /**
     * Method to create an app instance.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);
    }
}

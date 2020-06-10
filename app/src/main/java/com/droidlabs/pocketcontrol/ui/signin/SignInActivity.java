package com.droidlabs.pocketcontrol.ui.signin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.ui.home.HomeActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SignInActivity extends AppCompatActivity {

    private View mContentView;

    /**
     * Create a new activity.
     * @param savedInstanceState bundle.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mContentView = findViewById(R.id.signin_container);

        Button button = mContentView.findViewById(R.id.signin_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                signIn();
            }
        });
    }

    /**
     * Execute sign in and move to home screen.
     */
    private void signIn() {

        Intent intent = new Intent(getApplication(), HomeActivity.class);
        startActivity(intent);

    }
}

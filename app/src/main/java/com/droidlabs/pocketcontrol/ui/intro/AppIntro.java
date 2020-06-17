package com.droidlabs.pocketcontrol.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.ui.signin.SignInActivity;

/**
 * This class populates the screen having infor about the app.
 */
public class AppIntro extends AppCompatActivity {

    private static final int TIMER = 4000;
    private TextView textView;
    private Animation animation;

    /**
     * Create a new activity.
     * @param savedInstanceState bundle.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_intro);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        animation = AnimationUtils.loadAnimation(AppIntro.this, R.anim.bottom_animation);

        textView = findViewById(R.id.appIntro);
        textView.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplication(), SignInActivity.class);
                startActivity(intent);
                finish();
            }
        }, TIMER);
    }
}

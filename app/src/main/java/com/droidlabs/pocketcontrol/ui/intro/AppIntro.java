package com.droidlabs.pocketcontrol.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.ui.signin.SignInActivity;

/**
 * This class populates the screen having infor about the app.
 */
public class AppIntro extends AppCompatActivity {

    private LinearLayout appIntro;
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

        appIntro = findViewById(R.id.appInto);
        appIntro.setAnimation(animation);

        Button appIntroButton = findViewById(R.id.appIntroButton);

        appIntroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(getApplication(), SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

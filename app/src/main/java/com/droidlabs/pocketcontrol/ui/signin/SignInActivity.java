package com.droidlabs.pocketcontrol.ui.signin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.user.User;
import com.droidlabs.pocketcontrol.ui.home.HomeActivity;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SignInActivity extends AppCompatActivity {

    private View signInContainer;
    private View signUpContainer;
    private UserViewModel userViewModel;
    private List<User> userList;
    private TextView currentUser;
    private LinearLayout selectedUser;
    private LinearLayout enterUserPin;
    private LinearLayout createNewUser;
    private Button signUpButton;
    private Button validateCredentialsButton;
    private Button createNewAccountButton;
    private Button alreadyHaveAnAccountButton;
    private User user;
    private TextInputEditText fourDigitPinValue;
    private TextInputLayout accessPinInputGroup;

    /**
     * Create a new activity.
     * @param savedInstanceState bundle.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in_screen);

        signInContainer = findViewById(R.id.signinContainer);
        signUpContainer = findViewById(R.id.signupContainer);

        currentUser = signInContainer.findViewById(R.id.currentUserEmail);
        selectedUser = signInContainer.findViewById(R.id.selectedUser);
        enterUserPin = signInContainer.findViewById(R.id.enterUserPin);
        createNewAccountButton = signInContainer.findViewById(R.id.createNewAccountButton);
        validateCredentialsButton = signInContainer.findViewById(R.id.validateCredentialsButton);
        fourDigitPinValue = signInContainer.findViewById(R.id.fourDigitPinValue);
        accessPinInputGroup = signInContainer.findViewById(R.id.accessPinInputGroup);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        alreadyHaveAnAccountButton = signUpContainer.findViewById(R.id.alreadyHaveAnAccountButton);
        signUpButton = signUpContainer.findViewById(R.id.signUpButton);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.v("USER", String.valueOf(users.size()));

                if (users.size() > 0) {
                    new SharedPreferencesUtils(getApplication()).setCurrentUserId(String.valueOf(users.get(0).getId()));
                    currentUser.setText("" + users.get(0).getEmail() + users.get(0).getEmail());
                    user = userViewModel.getUserById(users.get(0).getId());
                }
            }
        });

        createNewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpContent();
            }
        });

        alreadyHaveAnAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignInContent();
            }
        });

        validateCredentialsButton.setOnClickListener(new View.OnClickListener() {
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
        if (fourDigitPinValue.getText().toString().equals(user.getAccessPin())) {
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
        } else {
            // TODO: show error message
        }
    }

    private void showSignUpContent() {
        signInContainer.setVisibility(View.GONE);
        signUpContainer.setVisibility(View.VISIBLE);
    }

    private void showSignInContent() {
        signInContainer.setVisibility(View.VISIBLE);
        signUpContainer.setVisibility(View.GONE);
    }
}

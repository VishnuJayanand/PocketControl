package com.droidlabs.pocketcontrol.ui.signin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.user.User;
import com.droidlabs.pocketcontrol.ui.home.HomeActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SignInActivity extends AppCompatActivity {

    private View mContentView;
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
    private Button signInWithPasswordButton;
    private Button signInWithPinButton;
    private User user;
    private TextInputEditText fourDigitPinValue;
    private TextInputEditText passwordSignInValue;
    private TextInputLayout passwordInputSignIn;
    private TextInputLayout accessPinInputGroup;
    private boolean authenticateWithPassword;

    /**
     * Create a new activity.
     * @param savedInstanceState bundle.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in_screen);

        mContentView = findViewById(R.id.signin_container);

        currentUser = mContentView.findViewById(R.id.currentUserEmail);
        selectedUser = mContentView.findViewById(R.id.selectedUser);
        createNewUser = mContentView.findViewById(R.id.createNewUser);
        enterUserPin = mContentView.findViewById(R.id.enterUserPin);
        createNewAccountButton = mContentView.findViewById(R.id.createNewAccountButton);
        alreadyHaveAnAccountButton = mContentView.findViewById(R.id.alreadyHaveAnAccountButton);
        signUpButton = mContentView.findViewById(R.id.signUpButton);
        validateCredentialsButton = mContentView.findViewById(R.id.validateCredentialsButton);


        fourDigitPinValue = mContentView.findViewById(R.id.fourDigitPinValue);
        passwordSignInValue = mContentView.findViewById(R.id.passwordSignInValue);


        signInWithPasswordButton = mContentView.findViewById(R.id.signInWithPasswordButton);
        signInWithPinButton = mContentView.findViewById(R.id.signInWithPinButton);

        passwordInputSignIn = mContentView.findViewById(R.id.passwordInputSignInGroup);
        accessPinInputGroup = mContentView.findViewById(R.id.accessPinInputGroup);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.v("USER", String.valueOf(users.size()));

                if (users.size() > 0) {
                    currentUser.setText(users.get(0).getEmail());
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

        signInWithPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateWithPassword = true;
                showPasswordSignIn();
            }
        });

        signInWithPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateWithPassword = false;
                showPinSignIn();
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
        boolean authenticated = false;

        if (authenticateWithPassword) {
            if (passwordSignInValue.getText().toString().equals(user.getPassword())) {
                authenticated = true;
            }
        } else {
            if (fourDigitPinValue.getText().toString().equals(user.getAccessPin())) {
                authenticated = true;
            }
        }

        if (authenticated) {
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
        }
    }

    private void showSignUpContent() {
        createNewUser.setVisibility(View.VISIBLE);
        selectedUser.setVisibility(View.GONE);
        enterUserPin.setVisibility(View.GONE);
    }

    private void showSignInContent() {
        createNewUser.setVisibility(View.GONE);
        selectedUser.setVisibility(View.VISIBLE);
        enterUserPin.setVisibility(View.VISIBLE);
    }

    private void showPasswordSignIn() {
        accessPinInputGroup.setVisibility(View.GONE);
        signInWithPasswordButton.setVisibility(View.GONE);
        signInWithPinButton.setVisibility(View.VISIBLE);
        passwordInputSignIn.setVisibility(View.VISIBLE);
    }

    private void showPinSignIn() {
        passwordInputSignIn.setVisibility(View.GONE);
        signInWithPinButton.setVisibility(View.GONE);
        signInWithPasswordButton.setVisibility(View.VISIBLE);
        accessPinInputGroup.setVisibility(View.VISIBLE);
    }
}

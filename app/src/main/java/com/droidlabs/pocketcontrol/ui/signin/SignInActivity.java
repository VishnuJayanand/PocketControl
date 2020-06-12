package com.droidlabs.pocketcontrol.ui.signin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.db.category.CategoryDao;
import com.droidlabs.pocketcontrol.db.user.User;
import com.droidlabs.pocketcontrol.ui.categories.CategoryViewModel;
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
    private View noUserInDbContainer;
    private UserViewModel userViewModel;
    private List<User> userList;
    private TextView currentUser;
    private LinearLayout selectedUser;
    private LinearLayout enterAccessToken;
    private LinearLayout createNewUser;
    private Button signUpButton;
    private Button validateCredentialsButton;
    private Button createNewAccountButton;
    private Button createNewAccountButton2;
    private Button alreadyHaveAnAccountButton;
    private User user;
    private TextInputEditText accessTokenValueText;
    private TextInputLayout enterAccessTokenInputGroup;
    private TextInputLayout emailInputGroup;
    private TextInputEditText emailInputText;
    private TextInputLayout accessTokenInputGroup;
    private TextInputEditText accessTokenInputText;
    private TextInputLayout repeatAccessTokenInputGroup;
    private TextInputEditText repeatAccessTokenInputText;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private CategoryViewModel categoryViewModel;

    /**
     * Create a new activity.
     * @param savedInstanceState bundle.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferencesUtils = new SharedPreferencesUtils(getApplication());
        categoryViewModel = new CategoryViewModel(getApplication());

        setContentView(R.layout.activity_sign_in_screen);

        signInContainer = findViewById(R.id.signinContainer);
        signUpContainer = findViewById(R.id.signupContainer);
        noUserInDbContainer = findViewById(R.id.noUserInDbContainer);

        currentUser = signInContainer.findViewById(R.id.currentUserEmail);
        selectedUser = signInContainer.findViewById(R.id.selectedUser);

        accessTokenValueText = signInContainer.findViewById(R.id.accessTokenValueText);
        enterAccessTokenInputGroup = signInContainer.findViewById(R.id.enterAccessTokenInputGroup);

        createNewAccountButton = signInContainer.findViewById(R.id.createNewAccountButton);
        validateCredentialsButton = signInContainer.findViewById(R.id.validateCredentialsButton);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        alreadyHaveAnAccountButton = signUpContainer.findViewById(R.id.alreadyHaveAnAccountButton);
        signUpButton = signUpContainer.findViewById(R.id.signUpButton);

        emailInputGroup = signUpContainer.findViewById(R.id.emailInputGroup);
        emailInputText = signUpContainer.findViewById(R.id.emailInputText);

        accessTokenInputGroup = signUpContainer.findViewById(R.id.accessTokenInputGroup);
        accessTokenInputText = signUpContainer.findViewById(R.id.accessTokenInputText);

        repeatAccessTokenInputGroup = signUpContainer.findViewById(R.id.repeatAccessTokenInputGroup);
        repeatAccessTokenInputText = signUpContainer.findViewById(R.id.repeatAccessTokenInputText);

        createNewAccountButton2 = noUserInDbContainer.findViewById(R.id.createNewAccountButton2);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.v("USER", String.valueOf(users.size()));

                if (users.size() > 0) {
                    showSignInContent();

                    String currentUserId = sharedPreferencesUtils.getCurrentUserId();
                    if (currentUserId.equals("")) {
                        currentUser.setText("No user selected.");
                        currentUser.setTextColor(getApplication().getColor(R.color.colorExpense));
                    } else {
                        user = userViewModel.getUserById(Long.parseLong(currentUserId));
                        Log.v("USER", user.getEmail());
                        currentUser.setText(user.getEmail());
                        currentUser.setTextColor(getApplication().getColor(android.R.color.darker_gray));
                    }
                } else {
                    showNoUserInDbContent();
                }
            }
        });

        createNewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpContent();
            }
        });

        createNewAccountButton2.setOnClickListener(new View.OnClickListener() {
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

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    /**
     * Execute sign in and move to home screen.
     */
    private void signUp() {


        if (!Patterns.EMAIL_ADDRESS.matcher(emailInputText.getText().toString()).matches()) {
            emailInputGroup.setError("Please enter a valid email address.");
            return;
        } else {
            emailInputGroup.setError(null);
        }

        if (userViewModel.getUserByEmail(emailInputText.getText().toString()) != null) {
            emailInputGroup.setError("Email already registered.");
            return;
        } else {
            emailInputGroup.setError(null);
        }

        if (accessTokenInputText.getText().toString().length() != 4) {
            accessTokenInputGroup.setError("Access token must be 4 digits long.");
            return;
        } else {
            accessTokenInputGroup.setError(null);
        }

        if (!repeatAccessTokenInputText.getText().toString().equals(accessTokenInputText.getText().toString())) {
            repeatAccessTokenInputGroup.setError("Access tokens do not match.");
            return;
        } else {
            repeatAccessTokenInputGroup.setError(null);
        }

        User user = new User();
        user.setAccessPin(accessTokenInputText.getText().toString());
        user.setEmail(emailInputText.getText().toString());

        long userId = userViewModel.insert(user);
        sharedPreferencesUtils.setCurrentUserId(String.valueOf(userId));

        createDefaultUserCategories(String.valueOf(userId));
    }

    /**
     * Execute sign in and move to home screen.
     */
    private void signIn() {
        if (accessTokenValueText.getText().toString().equals(user.getAccessPin())) {
            enterAccessTokenInputGroup.setError(null);
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
        } else {
            enterAccessTokenInputGroup.setError("Access token is not valid.");
        }
    }

    private void showSignUpContent() {
        resetSignUpState();
        signInContainer.setVisibility(View.GONE);
        signUpContainer.setVisibility(View.VISIBLE);
        noUserInDbContainer.setVisibility(View.GONE);
    }

    private void showSignInContent() {
        resetSignInState();
        signInContainer.setVisibility(View.VISIBLE);
        signUpContainer.setVisibility(View.GONE);
        noUserInDbContainer.setVisibility(View.GONE);
    }

    private void showNoUserInDbContent() {
        signInContainer.setVisibility(View.GONE);
        signUpContainer.setVisibility(View.GONE);
        noUserInDbContainer.setVisibility(View.VISIBLE);
    }

    private void resetSignUpState() {
        emailInputText.setText("");
        accessTokenInputText.setText("");
        repeatAccessTokenInputText.setText("");

        emailInputGroup.setError(null);
        accessTokenInputGroup.setError(null);
        repeatAccessTokenInputGroup.setError(null);
    }

    private void resetSignInState() {
        accessTokenValueText.setText("");
        enterAccessTokenInputGroup.setError(null);
    }

    private void createDefaultUserCategories(String userId) {
        Category health = new Category("Health", R.drawable.health);
        Category transport = new Category("Transport", R.drawable.transport);
        Category shopping = new Category( "Shopping", R.drawable.shopping);
        Category food = new Category( "Food", R.drawable.food);
        Category study = new Category( "Study", R.drawable.study);
        Category rent = new Category("Rent", R.drawable.rent);

        categoryViewModel.insert(health);
        categoryViewModel.insert(transport);
        categoryViewModel.insert(shopping);
        categoryViewModel.insert(food);
        categoryViewModel.insert(study);
        categoryViewModel.insert(rent);

    }
}

package com.droidlabs.pocketcontrol.ui.signin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.account.Account;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.db.defaults.Defaults;
import com.droidlabs.pocketcontrol.db.user.User;
import com.droidlabs.pocketcontrol.ui.categories.CategoryViewModel;
import com.droidlabs.pocketcontrol.ui.home.AccountViewModel;
import com.droidlabs.pocketcontrol.ui.home.HomeActivity;
import com.droidlabs.pocketcontrol.ui.intro.OnBoarding;
import com.droidlabs.pocketcontrol.ui.settings.DefaultsViewModel;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.regex.Pattern;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SignInActivity extends AppCompatActivity {

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"
                    + "\\@"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"
                    + "("
                    + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"
                    + ")+"
    );

    private View signInContainer;
    private View signUpContainer;
    private UserViewModel userViewModel;
    private List<User> userList;
    private TextView currentUser;
    private LinearLayout selectedUser;
    private Button changeUserButton;
    private Button signUpButton;
    private Button validateCredentialsButton;
    private Button createNewAccountButton;
    private Button alreadyHaveAnAccountButton;
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
    private DefaultsViewModel defaultsViewModel;
    private AccountViewModel accountViewModel;

    private MaterialAlertDialogBuilder switchUserDialog;
    private User user;

    /**
     * Create a new activity.
     * @param savedInstanceState bundle.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferencesUtils = new SharedPreferencesUtils(getApplication());
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        defaultsViewModel = new ViewModelProvider(this).get(DefaultsViewModel.class);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        setContentView(R.layout.activity_sign_in_screen);

        signInContainer = findViewById(R.id.signinContainer);
        signUpContainer = findViewById(R.id.signupContainer);

        currentUser = signInContainer.findViewById(R.id.currentUserEmail);
        selectedUser = signInContainer.findViewById(R.id.selectedUser);
        changeUserButton = signInContainer.findViewById(R.id.changeUserButton);

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

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(final List<User> users) {
                userList = users;

                if (users.size() > 0) {
                    showSignInContent();
                    initializeSwitchUserDialog();

                    String currentUserId = sharedPreferencesUtils.getCurrentUserId();
                    if (currentUserId.equals("")) {
                        currentUser.setText("No user selected.");
                        currentUser.setTextColor(getApplication().getColor(R.color.colorExpense));
                    } else {
                        setUser();
                        currentUser.setTextColor(getApplication().getColor(android.R.color.darker_gray));
                    }
                } else {
                    showSignUpContent();
                }
            }
        });

        createNewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                showSignUpContent();
            }
        });

        alreadyHaveAnAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (userList.size() == 0) {
                    Toast.makeText(
                            getBaseContext(),
                            "No users registered! Please create an account.",
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    showSignInContent();
                }
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
            public void onClick(final View v) {
                signUp();
            }
        });

        changeUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switchUserDialog.show();
            }
        });
    }

    /**
     * Execute sign in and move to home screen.
     */
    private void signUp() {
        if (!EMAIL_ADDRESS_PATTERN.matcher(emailInputText.getText().toString()).matches()) {
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

        User newUser = new User();
        newUser.setAccessPin(accessTokenInputText.getText().toString());
        newUser.setEmail(emailInputText.getText().toString());

        long userId = userViewModel.insert(newUser);
        sharedPreferencesUtils.setCurrentUserId(String.valueOf(userId));

        createDefaultUserCategories();
        createDefaultUserDefaults();
        createDefaultUserAccount();
    }

    /**
     * Execute sign in and move to home screen.
     */
    private void signIn() {
        if (accessTokenValueText.getText().toString().equals(user.getAccessPin())) {
            enterAccessTokenInputGroup.setError(null);

            sharedPreferencesUtils.setIsSignedIn(true);
            sharedPreferencesUtils.setCurrentUserId(String.valueOf(user.getId()));
            sharedPreferencesUtils.setCurrentAccountId(user.getSelectedAccount());

            //Checking if the app is running for the 1st time
            boolean isFirstTime = sharedPreferencesUtils.getFirstTimeSet();

            if (isFirstTime) {
                sharedPreferencesUtils.setFirstTimeSet(false);

                Intent intent = new Intent(getApplication(), OnBoarding.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(getApplication(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            enterAccessTokenInputGroup.setError("Access token is not valid.");
        }
    }

    /**
     * Initialize switch user dialog.
     */
    private void initializeSwitchUserDialog() {
        String[] userListEmails = new String[userList.size()];

        for (int i = 0; i < userList.size(); i++) {
            userListEmails[i] = userList.get(i).getEmail();
        }

        switchUserDialog = new MaterialAlertDialogBuilder(SignInActivity.this)
                .setTitle("Select user account")
                .setItems(userListEmails, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        User selected = userList.get(which);
                        sharedPreferencesUtils.setCurrentUserId(String.valueOf(selected.getId()));
                        setUser();
                    }
                });

    }

    /**
     * Set user locally and in the text.
     */
    private void setUser() {
        user = userViewModel.getCurrentUser();
        currentUser.setText(user.getEmail());
    }

    /**
     * Show sign up content.
     */
    private void showSignUpContent() {
        resetSignUpState();
        signInContainer.setVisibility(View.GONE);
        signUpContainer.setVisibility(View.VISIBLE);
    }

    /**
     * Show sign in content.
     */
    private void showSignInContent() {
        resetSignInState();
        signInContainer.setVisibility(View.VISIBLE);
        signUpContainer.setVisibility(View.GONE);
    }

    /**
     * Clear sign up errors and values.
     */
    private void resetSignUpState() {
        emailInputText.setText("");
        accessTokenInputText.setText("");
        repeatAccessTokenInputText.setText("");

        emailInputGroup.setError(null);
        accessTokenInputGroup.setError(null);
        repeatAccessTokenInputGroup.setError(null);
    }

    /**
     * Clear sign in errors and values.
     */
    private void resetSignInState() {
        accessTokenValueText.setText("");
        enterAccessTokenInputGroup.setError(null);
    }

    /**
     * Create user's default categories.
     */
    private void createDefaultUserCategories() {
        Category health = new Category("Health", R.drawable.category_icons_health);
        Category transport = new Category("Transport", R.drawable.category_icons_transport);
        Category shopping = new Category("Shopping", R.drawable.category_icons_shopping);
        Category food = new Category("Food", R.drawable.category_icons_food);
        Category study = new Category("Study", R.drawable.category_icons_study);
        Category rent = new Category("Rent", R.drawable.category_icons_rent);

        health.setPublic(true);
        transport.setPublic(true);
        shopping.setPublic(true);
        food.setPublic(true);
        study.setPublic(true);
        rent.setPublic(true);

        health.setPublic(true);
        transport.setPublic(true);
        shopping.setPublic(true);
        food.setPublic(true);
        study.setPublic(true);
        rent.setPublic(true);

        categoryViewModel.insert(health);
        categoryViewModel.insert(transport);
        categoryViewModel.insert(shopping);
        categoryViewModel.insert(food);
        categoryViewModel.insert(study);
        categoryViewModel.insert(rent);

    }

    /**
     * Create user's default defaults.
     */
    private void createDefaultUserDefaults() {
        Defaults defaultCurrency = new Defaults("Currency", "EUR");
        Defaults defaultPaymentMethod = new Defaults("Payment Mode", "Cash");
        Defaults defaultCategory = new Defaults("Category", "Health");

        defaultsViewModel.insert(defaultCurrency);
        defaultsViewModel.insert(defaultPaymentMethod);
        defaultsViewModel.insert(defaultCategory);
    }

    /**
     * Create default user account.
     */
    private void createDefaultUserAccount() {
        Account defaultAccount = new Account();

        defaultAccount.setName("Default account");

        Long newAccountId = accountViewModel.insert(defaultAccount);

        userViewModel.updateUserSelectedAccount(String.valueOf(newAccountId));
        sharedPreferencesUtils.setCurrentAccountId(String.valueOf(newAccountId));
    }
}

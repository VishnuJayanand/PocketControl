package com.droidlabs.pocketcontrol.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.ui.budget.BudgetFragment;
import com.droidlabs.pocketcontrol.ui.categories.CategoriesFragment;
import com.droidlabs.pocketcontrol.ui.settings.SettingsFragment;
import com.droidlabs.pocketcontrol.ui.signin.SignInActivity;
import com.droidlabs.pocketcontrol.ui.transaction.TransactionFragment;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private SharedPreferencesUtils sharedPreferencesUtils;
    /**
     * Create home activity.
     * @param savedInstanceState bundle.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferencesUtils = new SharedPreferencesUtils(getApplication());

        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container, new HomeFragment()).commit();

    }

    /**
     * Bottom navigation menu.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(
                        final @NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_budget:
                            selectedFragment = new BudgetFragment();
                            break;
                        case R.id.nav_categories:
                            selectedFragment = new CategoriesFragment();
                            break;
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_settings:
                            selectedFragment = new SettingsFragment();
                            break;
                        case R.id.nav_transaction:
                            selectedFragment = new TransactionFragment();
                            break;
                        default :
                            selectedFragment = new HomeFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(
                            R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };

    /**
     * If user is not signed in, request token.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (!sharedPreferencesUtils.getIsSignedIn()) {
            Intent intent = new Intent(getApplication(), SignInActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Sign user out whenever he / she leaves the application.
     */
    @Override
    protected void onPause() {
        super.onPause();

        sharedPreferencesUtils.setIsSignedIn(false);
    }
}

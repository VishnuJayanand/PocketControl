package com.droidlabs.pocketcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.MenuItem;
import com.droidlabs.pocketcontrol.ui.budget.BudgetFragment;
import com.droidlabs.pocketcontrol.ui.categories.CategoriesFragment;
import com.droidlabs.pocketcontrol.ui.home.HomeFragment;
import com.droidlabs.pocketcontrol.ui.settings.SettingsFragment;
import com.droidlabs.pocketcontrol.ui.transaction.TransactionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Main activity of the app.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Method to create an app instance.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(
                final @NonNull MenuItem menuItem) {
            Fragment selecetedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_budget:
                    selecetedFragment = new BudgetFragment();
                    break;
                case R.id.nav_categories:
                    selecetedFragment = new CategoriesFragment();
                    break;
                case R.id.nav_home:
                    selecetedFragment = new HomeFragment();
                    break;
                case R.id.nav_settings:
                    selecetedFragment = new SettingsFragment();
                    break;
                case R.id.nav_transaction:
                    selecetedFragment = new TransactionFragment();
                    break;
                default :
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragment_container, selecetedFragment).commit();

            return true;
        }
    };
}

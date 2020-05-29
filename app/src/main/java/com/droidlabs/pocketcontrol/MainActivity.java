package com.droidlabs.pocketcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.recurrent.Recurrent;
import com.droidlabs.pocketcontrol.db.recurrent.RecurrentDao;
import com.droidlabs.pocketcontrol.ui.budget.BudgetFragment;
import com.droidlabs.pocketcontrol.ui.categories.CategoriesFragment;
import com.droidlabs.pocketcontrol.ui.home.HomeFragment;
import com.droidlabs.pocketcontrol.ui.settings.SettingsFragment;
import com.droidlabs.pocketcontrol.ui.transaction.TransactionFragment;
import com.droidlabs.pocketcontrol.utils.DateUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


/**
 * Main activity of the app.
 */
public class MainActivity extends AppCompatActivity {

    //Variables for splash screen
    private Animation topAnimation, bottomAnimation;
    private ImageView appImage, teamImage;
    private static final int TIMER = 4000;

    /**
     * Method to create an app instance.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        //Animation
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Hooks
        appImage = findViewById(R.id.appname);
        teamImage = findViewById(R.id.teamname);

        appImage.setAnimation(topAnimation);
        teamImage.setAnimation(bottomAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.activity_main);
                BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                bottomNav.setOnNavigationItemSelectedListener(navListener);

                getSupportFragmentManager().beginTransaction().replace(
                        R.id.fragment_container, new HomeFragment()).commit();
            }
        }, TIMER);

        PocketControlDB db = PocketControlDB.getDatabase(this);
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

    @Override
    protected void onResume() {
        super.onResume();

        createRecurringTransactions();
    }

    private void createRecurringTransactions() {
        long startOfDay = DateUtils.getStartOfCurrentDay().getTimeInMillis();

        RecurrentDao recurrentDao = PocketControlDB.getDatabase(this).recurrentDao();

        Recurrent today = recurrentDao.getRecurrentByDate(startOfDay);

        if (today == null) {
            Log.v("RESUME", "ADD RECURRENT");
            Recurrent newRecurrent = new Recurrent();
            newRecurrent.setDate(startOfDay);
            recurrentDao.insert(newRecurrent);
        } else {
            Log.v("RESUME", String.valueOf(today));
        }
    }
}

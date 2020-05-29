package com.droidlabs.pocketcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.recurrent.Recurrent;
import com.droidlabs.pocketcontrol.db.recurrent.RecurrentDao;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.ui.budget.BudgetFragment;
import com.droidlabs.pocketcontrol.ui.categories.CategoriesFragment;
import com.droidlabs.pocketcontrol.ui.home.HomeFragment;
import com.droidlabs.pocketcontrol.ui.settings.SettingsFragment;
import com.droidlabs.pocketcontrol.ui.transaction.TransactionFragment;
import com.droidlabs.pocketcontrol.ui.transaction.TransactionViewModel;
import com.droidlabs.pocketcontrol.utils.DateUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Main activity of the app.
 */
public class MainActivity extends AppCompatActivity {

    //Variables for splash screen
    private Animation topAnimation, bottomAnimation;
    private ImageView appImage, teamImage;
    private static final int TIMER = 4000;
    private TransactionViewModel transactionViewModel;

    /**
     * Method to create an app instance.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

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
            transactionViewModel.getTransactions().observe(this, new Observer<List<Transaction>>() {
                @Override
                public void onChanged(List<Transaction> transactions) {
                    List<Transaction> filteredTransactions = new ArrayList<>();

                    for (Transaction transaction : transactions) {
                        if (transaction != null) {
                            Integer recurringType = transaction.getRecurringIntervalType();
                            Boolean isRecurring = transaction.isRecurring();

                            if (isRecurring != null && recurringType != null) {
                                if (recurringType != 0) {
                                    Log.v("RECURRING", "ADDING TRANSACTION: " + transaction.getId());
                                    filteredTransactions.add(transaction);
                                }
                            }
                        }
                    }

                    for (Transaction transaction : filteredTransactions) {
                        processAddRecurringTransaction(transaction);
                    }
                }
            });

            transactionViewModel.setCategoryFilter(false, "");
            transactionViewModel.setAmountFilter(false, Float.MIN_VALUE, Float.MAX_VALUE);
            transactionViewModel.setDateFilter(false, Long.MIN_VALUE, Long.MAX_VALUE);

            Recurrent newRecurrent = new Recurrent();
            newRecurrent.setDate(startOfDay);
            recurrentDao.insert(newRecurrent);
        }
    }

    private Transaction processAddRecurringTransaction(final Transaction transaction) {
        Transaction copyTransaction = new Transaction();

        Integer recurringIntervalType = transaction.getRecurringIntervalType();
        Integer recurringIntervalCustomDays = transaction.getRecurringIntervalDays();

        Calendar recurringDate = DateUtils.getStartOfDay(transaction.getDate());
        Calendar today = DateUtils.getStartOfCurrentDay();
        int counter = 0;

        copyTransaction.setAmount(transaction.getAmount());
        copyTransaction.setCategory(transaction.getCategory());
        copyTransaction.setMethod(transaction.getMethod());
        copyTransaction.setType(transaction.getType());
        copyTransaction.setTextNote(transaction.getTextNote());
        copyTransaction.setFlagIconRecurring(transaction.getFlagIconRecurring());
        copyTransaction.setRecurring(transaction.isRecurring());
        copyTransaction.setRecurringIntervalType(transaction.getRecurringIntervalType());
        copyTransaction.setRecurringIntervalDays(transaction.getRecurringIntervalDays());
        copyTransaction.setId(transaction.getId());
        copyTransaction.setDate(transaction.getDate());

        int whatToAdd;
        int howMuchToAdd;

        if (recurringIntervalType != null) {

            switch (recurringIntervalType) {
                case 1:
                    whatToAdd = Calendar.DAY_OF_YEAR;
                    howMuchToAdd = 1;
                    break;
                case 2:
                    whatToAdd = Calendar.WEEK_OF_YEAR;
                    howMuchToAdd = 1;
                    break;
                case 3:
                    whatToAdd = Calendar.MONTH;
                    howMuchToAdd = 1;
                    break;
                case 4:
                    whatToAdd = Calendar.DAY_OF_YEAR;
                    if (recurringIntervalCustomDays != null) {
                        howMuchToAdd = recurringIntervalCustomDays;
                    } else {
                        howMuchToAdd = 1;
                    }
                    break;
                default:
                    whatToAdd = Calendar.DAY_OF_YEAR;
                    howMuchToAdd = 1;
            }

            do {
                recurringDate.add(whatToAdd, howMuchToAdd);

                if (recurringDate.getTimeInMillis() <= today.getTimeInMillis()) {
                    // add a new transaction
                    // save currentTransaction as non-recurring.

                    copyTransaction.setRecurring(false);
                    copyTransaction.setRecurringIntervalType(0);
                    copyTransaction.setRecurringIntervalDays(0);

                    transactionViewModel.updateTransactionRecurringFields(
                            copyTransaction.getId(),
                            copyTransaction.isRecurring(),
                            copyTransaction.getRecurringIntervalType(),
                            copyTransaction.getRecurringIntervalDays()
                    );

                    Transaction newTransaction = new Transaction();

                    newTransaction.setAmount(copyTransaction.getAmount());
                    newTransaction.setCategory(copyTransaction.getCategory());
                    newTransaction.setMethod(copyTransaction.getMethod());
                    newTransaction.setType(copyTransaction.getType());
                    newTransaction.setTextNote(copyTransaction.getTextNote());
                    newTransaction.setFlagIconRecurring(true);
                    newTransaction.setRecurring(true);
                    newTransaction.setRecurringIntervalType(recurringIntervalType);
                    newTransaction.setRecurringIntervalDays(recurringIntervalCustomDays);

                    newTransaction.setDate(DateUtils.getStartOfDay(recurringDate.getTimeInMillis()).getTimeInMillis());

                    long newTransactionId = transactionViewModel.insert(newTransaction);

                    copyTransaction = transactionViewModel.getTransactionById(newTransactionId);

                    counter++;
                }

            } while (recurringDate.getTimeInMillis() <= today.getTimeInMillis());
        }

        Log.v("RECURRING", "TRANSACTIONS ADDED: " + (counter + 1));

        return copyTransaction;
    }
}

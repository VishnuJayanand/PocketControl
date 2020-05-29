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
    private List<Transaction> transactionsList;

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
                    transactionsList = transactions;
                    List<Transaction> filteredTransactions = new ArrayList<>();

                    for (Transaction transaction : transactions) {
                        if (transaction != null) {
                            Integer recurringType = transaction.getRecurringIntervalType();
                            Boolean isRecurring = transaction.isRecurring();

                            if (isRecurring != null && recurringType != null) {
                                if (recurringType != 0) {
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

    private void processAddRecurringTransaction(final Transaction transaction) {
        Transaction initialTransaction = new Transaction();

        Integer recurringIntervalType = transaction.getRecurringIntervalType();
        Integer recurringIntervalCustomDays = transaction.getRecurringIntervalDays();

        Log.v("RECURRING", String.valueOf(recurringIntervalCustomDays));

        Calendar originalDate = DateUtils.getStartOfDay(transaction.getDate());
        Calendar today = DateUtils.getStartOfCurrentDay();
        int counter = 0;

        initialTransaction.setAmount(transaction.getAmount());
        initialTransaction.setCategory(transaction.getCategory());
        initialTransaction.setMethod(transaction.getMethod());
        initialTransaction.setType(transaction.getType());
        initialTransaction.setTextNote(transaction.getTextNote());
        initialTransaction.setFlagIconRecurring(transaction.getFlagIconRecurring());
        initialTransaction.setRecurring(transaction.isRecurring());
        initialTransaction.setRecurringIntervalType(transaction.getRecurringIntervalType());
        initialTransaction.setRecurringIntervalDays(transaction.getRecurringIntervalDays());
        initialTransaction.setId(transaction.getId());
        initialTransaction.setDate(transaction.getDate());

        transaction.setRecurringIntervalType(0);
        transaction.setRecurringIntervalDays(0);
        transaction.setRecurring(false);

        transactionViewModel.updateTransactionRecurringFields(
                transaction.getId(),
                transaction.isRecurring(),
                transaction.getRecurringIntervalType(),
                transaction.getRecurringIntervalDays()
        );

        if (recurringIntervalType != null) {
            while (originalDate.getTimeInMillis() <= today.getTimeInMillis()) {
                Transaction newTransaction = new Transaction();

                newTransaction.setAmount(initialTransaction.getAmount());
                newTransaction.setCategory(initialTransaction.getCategory());
                newTransaction.setMethod(initialTransaction.getMethod());
                newTransaction.setType(initialTransaction.getType());
                newTransaction.setTextNote(initialTransaction.getTextNote());
                newTransaction.setFlagIconRecurring(true);
                newTransaction.setRecurring(initialTransaction.isRecurring());
                newTransaction.setRecurringIntervalType(initialTransaction.getRecurringIntervalType());
                newTransaction.setRecurringIntervalDays(initialTransaction.getRecurringIntervalDays());

                switch (recurringIntervalType) {
                    case 1:
                        originalDate.add(Calendar.DAY_OF_YEAR, 1);
                        break;
                    case 2:
                        originalDate.add(Calendar.WEEK_OF_YEAR, 1);
                        break;
                    case 3:
                        originalDate.add(Calendar.MONTH, 1);
                        break;
                    case 4:
                        if (recurringIntervalCustomDays != null) {
                            originalDate.add(Calendar.DAY_OF_YEAR, recurringIntervalCustomDays);
                        } else {
                            originalDate.add(Calendar.DAY_OF_YEAR, 1);
                        }
                        break;
                    default:
                        originalDate.add(Calendar.DAY_OF_YEAR, 1);
                }

                if (originalDate.getTimeInMillis() <= today.getTimeInMillis()) {
                    newTransaction.setDate(DateUtils.getStartOfDay(originalDate.getTimeInMillis()).getTimeInMillis());

                    transactionViewModel.insert(newTransaction);

                    initialTransaction.setRecurringIntervalType(0);
                    initialTransaction.setRecurringIntervalDays(0);
                    initialTransaction.setRecurring(false);

                    transactionViewModel.updateTransactionRecurringFields(
                            initialTransaction.getId(),
                            initialTransaction.isRecurring(),
                            initialTransaction.getRecurringIntervalType(),
                            initialTransaction.getRecurringIntervalDays()
                    );

                    initialTransaction = newTransaction;

                    counter++;
                }
            }
        }

        Log.v("RECURRING", "TRANSACTIONS ADDED: " + (counter + 1));
    }
}

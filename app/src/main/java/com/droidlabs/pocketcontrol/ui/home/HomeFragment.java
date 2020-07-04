package com.droidlabs.pocketcontrol.ui.home;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.account.Account;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.ui.categories.CategoryViewModel;
import com.droidlabs.pocketcontrol.ui.settings.DefaultsViewModel;
import com.droidlabs.pocketcontrol.ui.signin.UserViewModel;
import com.droidlabs.pocketcontrol.ui.transaction.TransactionViewModel;
import com.droidlabs.pocketcontrol.utils.CurrencyUtils;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Animation topAnimation;
    private TextView textViewAmount, textViewNetBalance, selectedAccountTitle, summary;
    private TextView accountIncomeText, accountExpenseText, accountBalanceText;
    private CardView selectedAccountColor;
    private List<Account> accountList;
    private Button addAccountButton;
    private UserViewModel userViewModel;
    private AccountViewModel accountViewModel;
    private String[] accountNames;
    private MaterialAlertDialogBuilder dialogBuilder;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private TransactionViewModel transactionViewModel;
    private CategoryViewModel categoryViewModel;
    private DefaultsViewModel defaultsViewModel;
    private String stringCurrency;
    private RecyclerView featuredRecycler;
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.fragment_home, container, false);

        defaultsViewModel = new ViewModelProvider(this).get(DefaultsViewModel.class);

        LinearLayout accountsWrapper = view.findViewById(R.id.accountsWrapper);
        Button switchAccount = accountsWrapper.findViewById(R.id.switchAccountButton);
        selectedAccountColor = view.findViewById(R.id.selectedAccountColor);

        sharedPreferencesUtils = new SharedPreferencesUtils(getActivity().getApplication());
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        selectedAccountTitle = view.findViewById(R.id.selectedAccountTitle);
        textViewAmount = view.findViewById(R.id.homeScreenTop);
        textViewNetBalance = view.findViewById(R.id.homeScreenNetBalanceText);

        addAccountButton = view.findViewById(R.id.addAccountButton);
        accountIncomeText = view.findViewById(R.id.accountIncomeText);
        accountExpenseText = view.findViewById(R.id.accountExpenseText);
        accountBalanceText = view.findViewById(R.id.accountBalanceText);

        topAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.top_animation);
        textViewAmount.setAnimation(topAnimation);
        textViewNetBalance.setAnimation(topAnimation);

        summary = view.findViewById(R.id.home_summary);
        summary.setVisibility(view.GONE);

        String stringCurrencyCode = defaultsViewModel.getDefaultValue("Currency");
        stringCurrency = defaultsViewModel.getCurrencySymbol(stringCurrencyCode);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.getAccounts().observe(getViewLifecycleOwner(), new Observer<List<Account>>() {
            @Override
            public void onChanged(final List<Account> accounts) {
                accountNames = new String[accounts.size()];

                for (int i = 0; i < accounts.size(); i++) {
                    accountNames[i] = accounts.get(i).getName();
                }

                buildSelectAccountDialog(accountNames);
            }
        });

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        transactionViewModel.setCategoryFilter(false, "-1");
        calculateAccountBalances();

        addAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Fragment fragment = new AddAccountFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        switchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });

        updateSelectedAccountInformation();

        //Hooks
        featuredRecycler = view.findViewById(R.id.featured_recycler);

        featuredRecycler(view);

        return view;
    }

    /**
     * Populate card view.
     * @param rView view
     */
    private void featuredRecycler(final View rView) {

        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedHelperClass> featuredLocation = new ArrayList<>();

        //Top Spent Transaction
        Float transactionExpenseId = 0f;
        transactionExpenseId = transactionViewModel.getTransactionIdByHighestExpenseAmount();

        if (transactionExpenseId != 0) {
            Transaction transaction = transactionViewModel.getTransactionById(transactionExpenseId.longValue());
            Category category = categoryViewModel.getSingleCategory(Integer.parseInt(transaction.getCategory()));

            String amount = CurrencyUtils.formatAmount(transaction.getAmount(), stringCurrency);
            String categoryName = category.getName();
            int icon = category.getIcon();

            summary.setVisibility(rView.VISIBLE);
            featuredLocation.add(new FeaturedHelperClass("Top Spent Transaction", categoryName, amount, icon));

        }

        //Top spend Category
        Float sumTotal = 0f;
        String name = "";
        int iconc = 0;
        List<Category> listCat = categoryViewModel.getAllCategories();
        if (listCat != null) {
            for (Category cat : listCat) {
                if (!cat.getName().equalsIgnoreCase("Income")) {
                    if (sumTotal <= transactionViewModel.getTotalIAmountByCategoryId(cat.getId().toString())) {
                        sumTotal = transactionViewModel.getTotalIAmountByCategoryId(cat.getId().toString());
                        name = cat.getName();
                        iconc = cat.getIcon();
                    }
                }
            }
            if (sumTotal != 0) {
                summary.setVisibility(rView.VISIBLE);
                String amount = CurrencyUtils.formatAmount(sumTotal, stringCurrency);
                featuredLocation.add(new FeaturedHelperClass("Top Spent Category", name, amount, iconc));
            }

        }

        //Highest Income
        Float transactionIncomeId = 0f;
        transactionIncomeId = transactionViewModel.getTransactionIdByHighestIncomeAmount();

        if (transactionIncomeId != 0) {
            Transaction transaction = transactionViewModel.getTransactionById(transactionIncomeId.longValue());

            String amount = CurrencyUtils.formatAmount(transaction.getAmount(), stringCurrency);
            String categoryName = "Income";
            int icon = R.drawable.category_icons_saving;

            summary.setVisibility(rView.VISIBLE);
            featuredLocation.add(new FeaturedHelperClass("Highest Income", categoryName, amount, icon));

        }

        adapter = new FeaturedAdapter(featuredLocation);

        featuredRecycler.setAdapter(adapter);
    }

    /**
     * Calculate balances.
     */
    private void calculateAccountBalances() {
        float accountIncome, accountExpense, accountBalance, totalBalance;

        accountIncome = transactionViewModel.getTotalIncomeByAccountId();
        accountExpense = transactionViewModel.getTotalExpenseByAccountId();
        accountBalance = accountIncome - accountExpense;
        totalBalance = transactionViewModel.getTotalIncomeByUserId() - transactionViewModel.getTotalExpenseByUserId();

        accountIncomeText.setText(CurrencyUtils.formatAmount(accountIncome, stringCurrency));
        accountExpenseText.setText(CurrencyUtils.formatAmount(accountExpense, stringCurrency));
        accountBalanceText.setText(CurrencyUtils.formatAmount(accountBalance, stringCurrency));

        textViewAmount.setText(CurrencyUtils.formatAmount(totalBalance, stringCurrency));
    }

    /**
     * Populate account dialog.
     * @param names account names.
     */
    private void buildSelectAccountDialog(final String[] names) {
        dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select your account:")
                .setItems(names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        Account selectedAccount = accountViewModel.getAccountByName(names[which]);

                        userViewModel.updateUserSelectedAccount(String.valueOf(selectedAccount.getId()));
                        sharedPreferencesUtils.setCurrentAccountId(String.valueOf(selectedAccount.getId()));

                        updateSelectedAccountInformation();
                        calculateAccountBalances();

                        Fragment fragment = new HomeFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.commit();
                    }
                });
    }

    /**
     * Update selected account info.
     */
    private void updateSelectedAccountInformation() {
        Account selectedAcc = accountViewModel
                .getAccountById(Integer.parseInt(sharedPreferencesUtils.getCurrentAccountIdKey()));

        String accountColor = selectedAcc.getColor();

        selectedAccountTitle.setText(selectedAcc.getName());

        if (accountColor != null) {
            selectedAccountColor.setCardBackgroundColor(Color.parseColor(accountColor));
        } else {
            selectedAccountColor.setCardBackgroundColor(getResources().getColor(R.color.projectColorDefault, null));
        }

    }
}

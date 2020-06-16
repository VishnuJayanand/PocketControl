package com.droidlabs.pocketcontrol.ui.home;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.account.Account;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.db.user.User;
import com.droidlabs.pocketcontrol.ui.signin.UserViewModel;
import com.droidlabs.pocketcontrol.ui.transaction.TransactionViewModel;
import com.droidlabs.pocketcontrol.utils.CurrencyUtils;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class HomeFragment extends Fragment {

    private Animation topAnimation;
    private TextView textViewAmount, textViewNetBalance, selectedAccountTitle;
    private CardView selectedAccountColor;
    private List<Account> accountList;
    private Button addAccountButton;
    private UserViewModel userViewModel;
    private AccountViewModel accountViewModel;
    private String[] accountNames;
    private MaterialAlertDialogBuilder dialogBuilder;
    private SharedPreferencesUtils sharedPreferencesUtils;

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.fragment_home, container, false);

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

        topAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.top_animation);
        textViewAmount.setAnimation(topAnimation);
        textViewNetBalance.setAnimation(topAnimation);

        AccountViewModel accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
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

        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        transactionViewModel.getTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(final List<Transaction> transactions) {
                float totalExpense = 0f;
                float totalIncome = 0f;

                if (!transactions.isEmpty()) {
                    for (Transaction transaction: transactions) {
                        if (transaction.getType().equals(1)) {
                            totalExpense += transaction.getAmount();
                        } else {
                            totalIncome += transaction.getAmount();
                        }
                    }
                }

                float totalAmount =  totalIncome - totalExpense;

                textViewAmount.setText(CurrencyUtils.formatAmount(totalAmount));
            }
        });

        transactionViewModel.setCategoryFilter(false, "-1");

        addAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            public void onClick(View v) {
                dialogBuilder.show();
            }
        });

        updateSelectedAccountInformation();

        return view;
    }

    private void buildSelectAccountDialog(String[] names) {
        dialogBuilder = new MaterialAlertDialogBuilder(getContext()).setTitle("Select your account:").setItems(names, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Account selectedAccount = accountViewModel.getAccountByName(names[which]);

                userViewModel.updateUserSelectedAccount(String.valueOf(selectedAccount.getId()));
                sharedPreferencesUtils.setCurrentAccountId(String.valueOf(selectedAccount.getId()));

                updateSelectedAccountInformation();
            }
        });
    }

    private void updateSelectedAccountInformation() {
        Log.v("SELECTED ACCOUNT", sharedPreferencesUtils.getCurrentAccountIdKey());
        Account selectedAcc = accountViewModel.getAccountById(Integer.parseInt(sharedPreferencesUtils.getCurrentAccountIdKey()));

        String accountColor = selectedAcc.getColor();

        selectedAccountTitle.setText(selectedAcc.getName());

        if (accountColor != null) {
            selectedAccountColor.setCardBackgroundColor(Color.parseColor(accountColor));
        } else {
            selectedAccountColor.setCardBackgroundColor(getResources().getColor(R.color.projectColorDefault, null));
        }

    }
}

package com.droidlabs.pocketcontrol.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.account.Account;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.ui.transaction.TransactionViewModel;
import com.droidlabs.pocketcontrol.utils.CurrencyUtils;

import java.util.List;

public class HomeFragment extends Fragment {

    private PocketControlDB db = PocketControlDB.getDatabase(getContext());
    private Animation topAnimation;
    private TextView textViewAmount, textViewNetBalance;
    private List<Account> projectList;
    private AccountListAdapter accountListAdapter;

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.fragment_home, container, false);
        RecyclerView projectsRecyclerView = view.findViewById(R.id.projectsList);

        textViewAmount = view.findViewById(R.id.homeScreenTop);
        textViewNetBalance = view.findViewById(R.id.homeScreenNetBalanceText);

        topAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.top_animation);
        textViewAmount.setAnimation(topAnimation);
        textViewNetBalance.setAnimation(topAnimation);

        AccountViewModel projectViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        projectViewModel.getAccounts().observe(getViewLifecycleOwner(), new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> projects) {
                accountListAdapter.setAccounts(projects);
            }
        });

        accountListAdapter = new AccountListAdapter(getActivity(), getActivity().getApplication());
        projectsRecyclerView.setAdapter(accountListAdapter);
        projectsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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

        return view;
    }
}

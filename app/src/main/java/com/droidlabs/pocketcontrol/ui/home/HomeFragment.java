package com.droidlabs.pocketcontrol.ui.home;

import android.os.Bundle;
import android.util.Log;
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

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.db.transaction.TransactionDao;
import com.droidlabs.pocketcontrol.ui.transaction.TransactionViewModel;
import com.droidlabs.pocketcontrol.utils.CurrencyUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private PocketControlDB db = PocketControlDB.getDatabase(getContext());
    private Animation topAnimation;
    private TextView textViewAmount, textViewExpense, textViewIncome, textViewNetBalance;

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.fragment_home, container, false);

        textViewAmount = view.findViewById(R.id.homeScreenTop);
        textViewNetBalance = view.findViewById(R.id.homeScreenNetBalanceText);
        textViewIncome = view.findViewById(R.id.homeScreenTotalIncome);
        textViewExpense = view.findViewById(R.id.homeScreenTotalExpense);

        topAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.top_animation);
        textViewAmount.setAnimation(topAnimation);
        textViewNetBalance.setAnimation(topAnimation);

        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        transactionViewModel.getTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                float totalAmount = 0f;
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

                totalAmount =  totalIncome - totalExpense;

                textViewExpense.setText("Total Expense:  " + CurrencyUtils.formatAmount(totalExpense));
                textViewIncome.setText("Total Income:    " + CurrencyUtils.formatAmount(totalIncome));
                textViewAmount.setText(CurrencyUtils.formatAmount(totalAmount));
            }
        });

        transactionViewModel.getAllTransactions();

        return view;
    }
}

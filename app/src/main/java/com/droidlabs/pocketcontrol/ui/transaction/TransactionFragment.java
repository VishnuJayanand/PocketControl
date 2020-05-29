package com.droidlabs.pocketcontrol.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.ui.categories.CategoryViewModel;

import java.util.List;


public class TransactionFragment extends Fragment implements TransactionListAdapter.OnTransactionNoteListener {

    private TransactionViewModel transactionViewModel;
    private TransactionListAdapter transactionListAdapter;
    private FilterBottomSheetDialog filterBottomSheetDialog;

    @Override
    public final View onCreateView(
        final LayoutInflater inf,
        final @Nullable ViewGroup container,
        final @Nullable Bundle savedInstanceState
    ) {
        View view = inf.inflate(R.layout.transaction_listview, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.transactionListView);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        transactionListAdapter = new TransactionListAdapter(getActivity(), this, transactionViewModel);

        recyclerView.setAdapter(transactionListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        transactionViewModel.getTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(final List<Transaction> transactions) {
                transactionListAdapter.setTransactions(transactions);
            }
        });

        transactionViewModel.setCategoryFilter(false, "");
        transactionViewModel.setAmountFilter(false, Float.MIN_VALUE, Float.MAX_VALUE);
        transactionViewModel.setDateFilter(false, Long.MIN_VALUE, Long.MAX_VALUE);

        filterBottomSheetDialog = new FilterBottomSheetDialog(transactionViewModel, categoryViewModel);

        AppCompatButton addTransactionLayout = view.findViewById(R.id.addTransactionButton);

        Button expandFilterButton = view.findViewById(R.id.expandFilterButton);
        expandFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                filterBottomSheetDialog.show(getParentFragmentManager(), "Tag");
            }
        });

        addTransactionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v)  {
                Fragment fragment = new AddTransactionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    /**
     * This method is to view the transaction detail fragment.
     * @param transaction Transaction selected transaction
     * @param position int selected position
     */
    @Override
    public void onTransactionClick(final Transaction transaction, final int position) {
        Bundle bundle = new Bundle();
        bundle.putLong("transactionDate", transaction.getDate());
        bundle.putFloat("transactionAmount", transaction.getAmount());
        bundle.putString("transactionNote", transaction.getTextNote());
        bundle.putInt("transactionType", transaction.getType());
        bundle.putString("transactionCategory", transaction.getCategory());
        //Move to transaction detail fragment
        Fragment fragment = new DetailTransacionFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

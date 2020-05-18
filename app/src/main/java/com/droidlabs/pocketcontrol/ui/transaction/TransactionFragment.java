package com.droidlabs.pocketcontrol.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.db.transaction.TransactionListAdapter;
import com.droidlabs.pocketcontrol.db.transaction.TransactionViewModel;

import java.util.ArrayList;


public class TransactionFragment extends Fragment {
    private ArrayList<Transaction> transactionsList = new ArrayList<>();
    private ListView listView;

    @Override
    public final View onCreateView(
        final LayoutInflater inf,
        final @Nullable ViewGroup container,
        final @Nullable Bundle savedInstanceState
    ) {
        View view = inf.inflate(R.layout.transaction_listview, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.transactionListView);

        //Create the adapter for Transaction
        final TransactionListAdapter adapter = new TransactionListAdapter(getActivity());

        //Set the adapter to list view
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        adapter.setTransactions(transactionViewModel.getAllTransactions());

        return view;
    }
}

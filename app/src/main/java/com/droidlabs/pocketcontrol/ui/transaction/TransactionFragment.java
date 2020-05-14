package com.droidlabs.pocketcontrol.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.db.transaction.TransactionListAdapter;
import java.util.ArrayList;


public class TransactionFragment extends Fragment {
    private ArrayList<Transaction> transactionsList = new ArrayList<>();
    private ListView listView;
    @Nullable
    @Override
    public final View onCreateView(
        final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.transaction_listview, container, false);
//        createTransactionList();
        //Create the adapter for Transaction
        TransactionListAdapter adapter = new TransactionListAdapter(getContext(),
                R.layout.transaction_listitem, transactionsList);
        //Set the adapter to list view
        listView = view.findViewById(R.id.transactionListView);
        listView.setAdapter(adapter);
        return view;
    }

    /**
     * Create a Transaction list for example.
     */
//    public void createTransactionList() {
//        String today = "13-05-2020";
//        Transaction transactionA = new Transaction(1, (float) 300, 2, "Rent", today);
//        Transaction transactionB = new Transaction(2, (float) -200, 1, "Shopping", today);
//        Transaction transactionC = new Transaction(3, (float) 100, 2, "Study", today);
//        Transaction transactionD = new Transaction(4, (float) -250, 1, "Transport", today);
//
//        transactionsList.add(transactionA);
//        transactionsList.add(transactionB);
//        transactionsList.add(transactionC);
//        transactionsList.add(transactionD);
//    }
}

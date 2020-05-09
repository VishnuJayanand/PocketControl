package com.droidlabs.pocketcontrol.db.transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.droidlabs.pocketcontrol.R;

import java.util.List;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransactionViewHolder> {

    class TransactionViewHolder extends RecyclerView.ViewHolder {
        private final TextView transactionItemView;

        private TransactionViewHolder(View itemView) {
            super(itemView);
            transactionItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Transaction> mTransactions; // Cached copy of transactions

    public TransactionListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new TransactionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        if (mTransactions != null) {
            Transaction current = mTransactions.get(position);
            holder.transactionItemView.setText(current.getTextNote());
        } else {
            // Covers the case of data not being ready yet.
            holder.transactionItemView.setText("No Transaction");
        }
    }

    public void setTransactions(List<Transaction> transactions){
        mTransactions = transactions;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mTransactions != null)
            return mTransactions.size();
        else return 0;
    }
}

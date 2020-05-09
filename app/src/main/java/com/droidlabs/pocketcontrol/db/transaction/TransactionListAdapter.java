package com.droidlabs.pocketcontrol.db.transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidlabs.pocketcontrol.R;

import java.util.List;

public final class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransactionViewHolder> {

    final class TransactionViewHolder extends RecyclerView.ViewHolder {
        private final TextView transactionItemView;

        /**
         * Transaction view holder.
         * @param itemView view that will hold the transaction list.
         */
        private TransactionViewHolder(final View itemView) {
            super(itemView);
            transactionItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater layoutInflater;
    private List<Transaction> transactions; // Cached copy of transactions

    /**
     * Constructor.
     * @param context context to be used (normally "this").
     */
    public TransactionListAdapter(final Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public @NonNull TransactionViewHolder onCreateViewHolder(final @NonNull ViewGroup parent, final int viewType) {
        View itemView = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new TransactionViewHolder(itemView);
    }

    /**
     * Initializer for transactions to be displayed.
     * @param holder view holder.
     * @param position position of transactions.
     */
    @Override
    public void onBindViewHolder(final @NonNull TransactionViewHolder holder, final int position) {
        if (transactions != null) {
            Transaction current = transactions.get(position);
            holder.transactionItemView.setText(current.getTextNote());
        } else {
            // Covers the case of data not being ready yet.
            holder.transactionItemView.setText("No Transaction");
        }
    }

    /**
     * Transactions setter.
     * @param transactionList transactions from db.
     */
    public void setTransactions(final List<Transaction> transactionList) {
        this.transactions = transactionList;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // transactions has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (transactions != null) {
            return transactions.size();
        } else {
            return 0;
        }
    }
}

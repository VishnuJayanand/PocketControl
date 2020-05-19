package com.droidlabs.pocketcontrol.db.transaction;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.db.category.CategoryDao;

import java.util.List;

import static java.lang.Integer.parseInt;

public final class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransactionViewHolder> {

    private List<Transaction> transactions; // Cached copy of transactions
    private final LayoutInflater layoutInflater;
    private final CategoryDao categoryDao;


    /**
     * Creating adapter for Transaction.
     * @param context context
     */
    public TransactionListAdapter(final @NonNull Context context) {
        layoutInflater = LayoutInflater.from(context);
        categoryDao = PocketControlDB.getDatabase(context).categoryDao();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(final @NonNull ViewGroup parent, final int viewType) {
        View itemView = layoutInflater.inflate(R.layout.transaction_listitem, parent, false);
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

            //get the Transaction information:
            String date = current.getDate();
            Integer type = current.getType();
            Float amount = current.getAmount();
            String category = current.getCategory();

            // turn float to string
            String amountToString = Float.toString(amount);
            String typeAsString = "";


            if (type == 1) {
                holder.transactionType.setTextColor(Color.parseColor("#F44336"));
                typeAsString = "Expense";
            } else {
                holder.transactionType.setTextColor(Color.parseColor("#4CAF50"));
                typeAsString = "Income";
                holder.transactionAmount.setTextColor(Color.parseColor("#4CAF50"));
                amountToString = " +" + amountToString;
            }

            if (category != null) {

                Category category1 = categoryDao.getSingleCategory(parseInt(category));
                holder.transactionCategoryTitle.setText(category1.getName());
                holder.transactionCategoryImage.setImageResource(category1.getIcon());
            }

            holder.transactionDate.setText(date);
            holder.transactionAmount.setText(amountToString);
            holder.transactionType.setText(typeAsString);

            // TODO: combine currency and amount
            //       transactionCurrency.setText(currency);
            //       holder.transactionCurrency.setText(currencyAsString);
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

    @Override
    public int getItemCount() {
        if (transactions != null) {
            return transactions.size();
        } else {
            return 0;
        }
    }

    final class TransactionViewHolder extends RecyclerView.ViewHolder {
        private final ImageView transactionCategoryImage;
        private final TextView transactionCategoryTitle;
        private final TextView transactionDate;
        private final TextView transactionAmount;
        private final TextView transactionType;
        // private final TextView transactionCurrency;

        /**
         * Transaction view holder.
         * @param itemView view that will hold the transaction list.
         */
        private TransactionViewHolder(final View itemView) {
            super(itemView);
            transactionCategoryImage = itemView.findViewById(R.id.transactionCategoryImage);
            transactionCategoryTitle = itemView.findViewById(R.id.transactionCategoryTitle);
            transactionDate = itemView.findViewById(R.id.transactionDate);
            transactionAmount = itemView.findViewById(R.id.transactionAmount);
            transactionType = itemView.findViewById(R.id.transactionType);
        }
    }

}

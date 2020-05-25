package com.droidlabs.pocketcontrol.ui.transaction;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.db.category.CategoryDao;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;

import java.util.List;

import static java.lang.Integer.parseInt;

public final class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransactionViewHolder> {

    private List<Transaction> transactions; // Cached copy of transactions
    private final LayoutInflater layoutInflater;
    private final CategoryDao categoryDao;
    private final TransactionViewModel transactionViewModel;
    private final OnTransactionNoteListener mOnNoteListener;


    /**
     * Creating adapter for Transaction.
     * @param context context
     * @param onNoteListener the onNotelistener
     * @param transactionVM the view model for creating new transactions
     */
    public TransactionListAdapter(
            final @NonNull Context context,
            final OnTransactionNoteListener onNoteListener,
            final TransactionViewModel transactionVM
    ) {
        layoutInflater = LayoutInflater.from(context);
        categoryDao = PocketControlDB.getDatabase(context).categoryDao();
        transactionViewModel = transactionVM;
        mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(final @NonNull ViewGroup parent, final int viewType) {
        View itemView = layoutInflater.inflate(R.layout.transaction_listitem, parent, false);
        return new TransactionViewHolder(itemView, mOnNoteListener);
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
                amountToString = " -" + amountToString;
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
            holder.duplicateTransactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Transaction newTransaction = createTransactionDuplicate(current);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext());

                    datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(final DatePicker view, final int year, final int monthOfYear,
                                              final int dayOfMonth) {
                            // TODO Auto-generated method stub

                            newTransaction.setDate(parseSelectedDate(dayOfMonth, monthOfYear, year));
                            transactionViewModel.insert(newTransaction);
                        }

                    });
                    datePickerDialog.show();
                }
            });

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


    final class TransactionViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        private final ImageView transactionCategoryImage;
        private final TextView transactionCategoryTitle;
        private final TextView transactionDate;
        private final TextView transactionAmount;
        private final TextView transactionType;
        private final OnTransactionNoteListener onNoteListener;
        private final ImageButton duplicateTransactionButton;
        // private final TextView transactionCurrency;

        /**
         * Transaction view holder.
         * @param itemView view that will hold the transaction list.
         * @param onTransactionNoteListener the onNoteListener
         */
        private TransactionViewHolder(final View itemView, final OnTransactionNoteListener onTransactionNoteListener) {
            super(itemView);
            transactionCategoryImage = itemView.findViewById(R.id.transactionCategoryImage);
            transactionCategoryTitle = itemView.findViewById(R.id.transactionCategoryTitle);
            transactionDate = itemView.findViewById(R.id.transactionDate);
            transactionAmount = itemView.findViewById(R.id.transactionAmount);
            transactionType = itemView.findViewById(R.id.transactionType);
            duplicateTransactionButton = itemView.findViewById(R.id.duplicate_transaction);
            this.onNoteListener = onTransactionNoteListener;

            itemView.setOnClickListener(this);
        }

        /**
         * method to catch onlick event of list adapter.
         * @param v view
         */
        @Override
        public void onClick(final View v) {
            onNoteListener.onTransactionClick(transactions.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnTransactionNoteListener {
        /**
         * This method is to get the transaction and position of selected transaction.
         * @param transaction Transaction selected transaction
         * @param position int selected position
         */
        void onTransactionClick(Transaction transaction, int position);
    }

    /**
     * Helper method to create a new transaction with same information from previous transaction.
     * @param oldTransaction transaction to duplicate.
     * @return duplicate of the transaction.
     */
    private Transaction createTransactionDuplicate(final Transaction oldTransaction) {
        Transaction newTransaction = new Transaction();

        if (oldTransaction.getAmount() != null) {
            newTransaction.setAmount(oldTransaction.getAmount());
        }

        if (oldTransaction.getTextNote() != null) {
            newTransaction.setTextNote(oldTransaction.getTextNote());
        }

        if (oldTransaction.isRecurring() != null) {
            newTransaction.setRecurring(oldTransaction.isRecurring());
        }

        if (oldTransaction.getRecurringIntervalDays() != null) {
            newTransaction.setRecurringIntervalDays(oldTransaction.getRecurringIntervalDays());
        }

        if (oldTransaction.getType() != null) {
            newTransaction.setType(oldTransaction.getType());
        }

        if (oldTransaction.getMethod() != null) {
            newTransaction.setMethod(oldTransaction.getMethod());
        }

        if (oldTransaction.getDate() != null) {
            newTransaction.setDate(oldTransaction.getDate());
        }

        if (oldTransaction.getCategory() != null) {
            newTransaction.setCategory(oldTransaction.getCategory());
        }

        return newTransaction;
    }

    /**
     * Helper method to parse the date.
     * @param day day of the month
     * @param month month of the year (should be added +1 to correct for 0 to 11)
     * @param year year
     * @return parsed string
     */
    private String parseSelectedDate(final int day, final int month, final int year) {
        String parsedDay = String.valueOf(day);
        String parsedMonth = String.valueOf(month + 1);
        String parsedYear = String.valueOf(year);

        if (parsedDay.length() == 1) {
            parsedDay = "0" + parsedDay;
        }

        if (parsedMonth.length() == 1) {
            parsedMonth = "0" + parsedMonth;
        }

        return "" + parsedDay + "-" + parsedMonth + "-" + parsedYear;
    }
}

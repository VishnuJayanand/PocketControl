package com.droidlabs.pocketcontrol.ui.transaction;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.db.category.CategoryDao;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.utils.CurrencyUtils;
import com.droidlabs.pocketcontrol.utils.DateUtils;

import java.util.Calendar;
import java.util.List;

import static java.lang.Integer.parseInt;

public final class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransactionViewHolder> {

    private List<Transaction> transactions; // Cached copy of transactions
    private final LayoutInflater layoutInflater;
    private final CategoryDao categoryDao;
    private final TransactionViewModel transactionViewModel;
    private final OnTransactionNoteListener mOnNoteListener;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

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
            Context context = holder.transactionAmount.getContext();

            //get the Transaction information:
            Long date = current.getDate();
            Integer type = current.getType();
            Float amount = current.getAmount();
            String category = current.getCategory();
            Boolean recurring = current.getFlagIconRecurring();

            // turn float to string
            String amountToString = CurrencyUtils.formatAmount(amount, "€");

            if (recurring != null && recurring) {
                holder.recurringTransactionWrapper.setVisibility(View.VISIBLE);
            } else {
                holder.recurringTransactionWrapper.setVisibility(View.GONE);
            }

            if (type == 1) {
                holder.transactionAmount.setTextColor(ContextCompat.getColor(context, R.color.colorExpense));
                amountToString = "- " + amountToString;
            } else {
                holder.transactionAmount.setTextColor(ContextCompat.getColor(context, R.color.colorIncome));
                amountToString = "+ " + amountToString;
            }

            if (category != null) {

                Category category1 = categoryDao.getSingleCategory(parseInt(category));
                holder.transactionCategoryTitle.setText(category1.getName());
                holder.transactionCategoryImage.setImageResource(category1.getIcon());
            }

            holder.transactionDate.setText(DateUtils.formatDate(date));
            holder.transactionAmount.setText(amountToString);

            holder.deleteTransactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show();
                }
            });

            holder.updateTransactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show();
                }
            });

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

                            Calendar newTransactionDate = Calendar.getInstance();
                            newTransactionDate.set(Calendar.YEAR, year);
                            newTransactionDate.set(Calendar.MONTH, monthOfYear);
                            newTransactionDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            newTransaction.setDate(DateUtils.getStartOfDayInMS(newTransactionDate.getTimeInMillis()));

                            transactionViewModel.insert(newTransaction);
                        }

                    });
                    datePickerDialog.show();
                }
            });

            viewBinderHelper.bind(holder.swipeRevealLayout, current.getId().toString());

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
        Log.v("TRANSACTIONS", String.valueOf(transactionList.size()));
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
        private final OnTransactionNoteListener onNoteListener;
        private final LinearLayout duplicateTransactionButton;
        private final LinearLayout updateTransactionButton;
        private final LinearLayout deleteTransactionButton;
        private final LinearLayout recurringTransactionWrapper;
        private final LinearLayout fillerEmptySpace;
        private final SwipeRevealLayout swipeRevealLayout;
        // private final TextView transactionCurrency;

        /**
         * Transaction view holder.
         * @param itemView view that will hold the transaction list.
         * @param onTransactionNoteListener the onNoteListener
         */
        private TransactionViewHolder(final View itemView, final OnTransactionNoteListener onTransactionNoteListener) {
            super(itemView);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    onNoteListener.onTransactionClick(transactions.get(getAdapterPosition()), getAdapterPosition());
                }
            };

            transactionCategoryImage = itemView.findViewById(R.id.transactionCategoryImage);
            transactionCategoryTitle = itemView.findViewById(R.id.transactionCategoryTitle);
            transactionDate = itemView.findViewById(R.id.transactionDate);
            transactionAmount = itemView.findViewById(R.id.transactionAmount);
            duplicateTransactionButton = itemView.findViewById(R.id.duplicate_transaction);
            recurringTransactionWrapper = itemView.findViewById(R.id.recurringTransactionWrapper);
            fillerEmptySpace = itemView.findViewById(R.id.blankSpace);
            swipeRevealLayout = itemView.findViewById(R.id.swipeRevealLayout);
            updateTransactionButton = itemView.findViewById(R.id.update_transaction);
            deleteTransactionButton = itemView.findViewById(R.id.delete_transaction);

            transactionCategoryTitle.setOnClickListener(clickListener);
            transactionCategoryImage.setOnClickListener(clickListener);
            transactionAmount.setOnClickListener(clickListener);
            transactionDate.setOnClickListener(clickListener);
            recurringTransactionWrapper.setOnClickListener(clickListener);
            fillerEmptySpace.setOnClickListener(clickListener);

            this.onNoteListener = onTransactionNoteListener;
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
}

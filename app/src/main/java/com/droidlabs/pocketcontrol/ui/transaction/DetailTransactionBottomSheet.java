package com.droidlabs.pocketcontrol.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.db.category.CategoryDao;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.utils.CurrencyUtils;
import com.droidlabs.pocketcontrol.utils.DateUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import static java.lang.Integer.parseInt;

public class DetailTransactionBottomSheet extends BottomSheetDialogFragment {

    private Transaction mTransaction;

    /**
     * Public constructor to initialize viewModels.
     */
    public DetailTransactionBottomSheet(
            Transaction transaction
    ) {
        mTransaction = transaction;
    }

    /**
     * Executed when creating the view.
     * @param inflater inflater.
     * @param container container.
     * @param savedInstanceState saved instance state.
     * @return created view.
     */
    @Nullable
    @Override
    public View onCreateView(
            final @NonNull LayoutInflater inflater,
            final @Nullable ViewGroup container,
            final @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.transaction_detail, container, false);

        TextView transactionDate = view.findViewById(R.id.transactionDate);
        TextView transactionAmount = view.findViewById(R.id.transactionAmount);
        TextView transactionType = view.findViewById(R.id.transactionType);
        TextView transactionNote = view.findViewById(R.id.transactionNote);
        TextView transactionMethod = view.findViewById(R.id.transactionMethod);
        TextView transactionCategoryTitle = view.findViewById(R.id.transactionCategoryTitle);
        ImageView transactionCategoryImage = view.findViewById(R.id.transactionCategoryImage);

        // turn float to string
        String amountToString = CurrencyUtils.formatAmount(mTransaction.getAmount(), "€");
        // transaction Type
        String typeAsString = "";
        if (mTransaction.getType() == 1) {
            transactionType.setTextColor(ContextCompat.getColor(getContext(), R.color.colorExpense));
            typeAsString = "Expense";
            amountToString = "- " + amountToString;
        } else {
            transactionType.setTextColor(ContextCompat.getColor(getContext(), R.color.colorIncome));
            typeAsString = "Income";
            amountToString = "+ " + amountToString;
        }

        //get Category Title and Image
        if (mTransaction.getCategory() != null) {
            CategoryDao categoryDao = PocketControlDB.getDatabase(getContext()).categoryDao();
            Category category1 = categoryDao.getSingleCategory(parseInt(mTransaction.getCategory()));
            transactionCategoryTitle.setText(category1.getName());
            transactionCategoryImage.setImageResource(category1.getIcon());
        }

        transactionDate.setText(DateUtils.formatDate(mTransaction.getDate()));
        transactionAmount.setText(amountToString);
        transactionType.setText(typeAsString);
        transactionNote.setText(mTransaction.getTextNote());

        return view;
    }
}

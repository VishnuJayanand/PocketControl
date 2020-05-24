package com.droidlabs.pocketcontrol.ui.transaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.db.category.CategoryDao;

import static java.lang.Integer.parseInt;

public class DetailTransacionFragment extends Fragment {
    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.transaction_detail, container, false);

        Bundle bundle = this.getArguments();
        String date = bundle.getString("transactionDate");
        float amount = bundle.getFloat("transactionAmount");
        String note = bundle.getString("transactionNote");
        int type = bundle.getInt("transactionType");
        String category = bundle.getString("transactionCategory");

        TextView transactionDate = view.findViewById(R.id.transactionDate);
        TextView transactionAmount = view.findViewById(R.id.transactionAmount);
        TextView transactionType = view.findViewById(R.id.transactionType);
        TextView transactionNote = view.findViewById(R.id.transactionNote);
        TextView transactionMethod = view.findViewById(R.id.transactionMethod);
        TextView transactionCategoryTitle = view.findViewById(R.id.transactionCategoryTitle);
        ImageView transactionCategoryImage = view.findViewById(R.id.transactionCategoryImage);

        // turn float to string
        String amountToString = Float.toString(amount);
        // transaction Type
        String typeAsString = "";
        if (type == 1) {
            transactionType.setTextColor(Color.parseColor("#F44336"));
            typeAsString = "Expense";
            amountToString = " -" + amountToString;
        } else {
            transactionType.setTextColor(Color.parseColor("#4CAF50"));
            typeAsString = "Income";
            amountToString = " +" + amountToString;
        }

        //get Category Title and Image
        if (category != null) {
            CategoryDao categoryDao = PocketControlDB.getDatabase(getContext()).categoryDao();
            Category category1 = categoryDao.getSingleCategory(parseInt(category));
            transactionCategoryTitle.setText(category1.getName());
            transactionCategoryImage.setImageResource(category1.getIcon());
        }

        transactionDate.setText(date);
        transactionAmount.setText(amountToString);
        transactionType.setText(typeAsString);
        transactionNote.setText(note);
        return view;
    }
}

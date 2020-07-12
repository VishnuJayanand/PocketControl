package com.droidlabs.pocketcontrol.ui.transaction;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.ui.categories.CategoryViewModel;
import com.droidlabs.pocketcontrol.ui.settings.DefaultsViewModel;
import com.droidlabs.pocketcontrol.utils.CurrencyUtils;
import com.droidlabs.pocketcontrol.utils.DateUtils;

public class DetailTransactionFragment extends Fragment {
    private CategoryViewModel categoryViewModel;
    private DefaultsViewModel defaultsViewModel;

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.transaction_detail, container, false);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        defaultsViewModel = new ViewModelProvider(this).get(DefaultsViewModel.class);

        Bundle bundle = this.getArguments();
        Long date = bundle.getLong("transactionDate");
        float amount = bundle.getFloat("transactionAmount");
        String note = bundle.getString("transactionNote");
        String textStyle = bundle.getString("transactionStyle");
        int type = bundle.getInt("transactionType");
        String category = bundle.getString("transactionCategory");
        String friend = bundle.getString("transactionFriend");
        String methodForFriend = bundle.getString("transactionMethodForFriend");
        int method = bundle.getInt("transactionMethod");

        TextView transactionDate = view.findViewById(R.id.transactionDate);
        TextView transactionAmount = view.findViewById(R.id.transactionAmount);
        TextView transactionType = view.findViewById(R.id.transactionType);
        TextView transactionNote = view.findViewById(R.id.transactionNote);
        TextView transactionMethod = view.findViewById(R.id.transactionMethod);
        TextView transactionCategoryTitle = view.findViewById(R.id.transactionCategoryTitle);
        TextView transactionFriend = view.findViewById(R.id.transactionFriend);
        TextView transactionMethodForFriend = view.findViewById(R.id.transactionMethodForFriend);
        TextView transactionFriendPhoneNumber = view.findViewById(R.id.transactionFriendPhoneNumber);
        ImageView transactionCategoryImage = view.findViewById(R.id.transactionCategoryImage);
        LinearLayout friendWrapper = view.findViewById(R.id.friendWrapper);

        // turn float to string
        String stringCurrencyCode = defaultsViewModel.getDefaultValue("Currency");
        String stringCurrency = defaultsViewModel.getCurrencySymbol(stringCurrencyCode);
        String amountToString = CurrencyUtils.formatAmount(amount, stringCurrency);
        // transaction Type
        String typeAsString = "";
        if (type == 1) {
            transactionType.setTextColor(ContextCompat.getColor(getContext(), R.color.colorExpense));
            typeAsString = "Expense";
            amountToString = "- " + amountToString;
        } else {
            transactionType.setTextColor(ContextCompat.getColor(getContext(), R.color.colorIncome));
            typeAsString = "Income";
            amountToString = "+ " + amountToString;
        }

        // transaction Method
        String methodAsString = "";
        if (method == 1) {
            transactionMethod.setText("Cash");
        } else if (method == 2) {
            transactionMethod.setText("Credit Card");
        } else if (method == 3) {
            transactionMethod.setText("Digital Wallet");
        }

        //get Category Title and Image
        if (category != null) {
            Category category1 = categoryViewModel.getSingleCategory(Integer.parseInt(category));
            transactionCategoryTitle.setText(category1.getName());
            transactionCategoryImage.setImageResource(category1.getIcon());
        }
        if (methodForFriend != null) {
            if (!methodForFriend.equals("")) {
                friendWrapper.setVisibility(View.VISIBLE);
                if (methodForFriend.equals("Borrow")) {
                    methodForFriend = methodForFriend + " From ";
                } else {
                    System.out.println(methodForFriend);
                    methodForFriend = methodForFriend + " To ";
                }
                if (friend.contains(",")) {
                    String friendName = friend.split(",")[0];
                    String phoneNumber = friend.split(":")[1];
                    transactionFriend.setText(friendName);
                    transactionFriendPhoneNumber.setText(phoneNumber);
                } else if (friend.equals("") || friend.equals("There are no contacts available on your phone")) {
                    transactionFriend.setText("No contact selected");
                    transactionFriendPhoneNumber.setText("");
                } else {
                    transactionFriend.setText(friend);
                    transactionFriendPhoneNumber.setText("");
                }
            }
        }
        //Edit the friend string

        transactionDate.setText(DateUtils.formatDate(date));
        transactionAmount.setText(amountToString);
        transactionType.setText(typeAsString);
        transactionNote.setText(note);
        if (textStyle != null) {
            if (textStyle.equals("NORMAL")) {
                transactionNote.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            } else if (textStyle.equals("BOLD")) {
                transactionNote.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                transactionNote.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            }
        }
        transactionMethodForFriend.setText(methodForFriend);

        return view;
    }
}

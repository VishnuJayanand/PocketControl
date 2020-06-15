package com.droidlabs.pocketcontrol.ui.transaction;

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
import com.droidlabs.pocketcontrol.utils.CurrencyUtils;
import com.droidlabs.pocketcontrol.utils.DateUtils;

public class DetailTransactionFragment extends Fragment {
    private CategoryViewModel categoryViewModel;
    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.transaction_detail, container, false);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        Bundle bundle = this.getArguments();
        Long date = bundle.getLong("transactionDate");
        float amount = bundle.getFloat("transactionAmount");
        String note = bundle.getString("transactionNote");
        int type = bundle.getInt("transactionType");
        String category = bundle.getString("transactionCategory");
        String friend = bundle.getString("transactionFriend");
        String methodForFriend = bundle.getString("transactionMethodForFriend");

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
        String amountToString = CurrencyUtils.formatAmount(amount, "€");
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

        //get Category Title and Image
        if (category != null) {
            Category category1 = categoryViewModel.getSingleCategory(Integer.parseInt(category));
            transactionCategoryTitle.setText(category1.getName());
            transactionCategoryImage.setImageResource(category1.getIcon());
        }

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
        //Edit the friend string


        transactionDate.setText(DateUtils.formatDate(date));
        transactionAmount.setText(amountToString);
        transactionType.setText(typeAsString);
        transactionNote.setText(note);
        transactionMethodForFriend.setText(methodForFriend);
        return view;
    }
}

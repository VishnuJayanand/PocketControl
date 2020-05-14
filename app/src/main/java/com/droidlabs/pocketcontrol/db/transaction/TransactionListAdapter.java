package com.droidlabs.pocketcontrol.db.transaction;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.droidlabs.pocketcontrol.R;
import java.util.List;

public final class TransactionListAdapter extends ArrayAdapter<Transaction> {
    private Context mContext;
    private int mResource;

    /**
     * Creating adapter for Transaction.
     * @param context context
     * @param resource int
     * @param objects list of Transaction
     */
    public TransactionListAdapter(
            final @NonNull Context context, final int resource, final  @NonNull List<Transaction> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    /**
     * method to set the attribute of Transaciton class to transactionListView.
     * @param position int
     * @param view view
     * @param parent View Group
     * @return convert View
     */
    public View getView(final int position, final View view, final  @NonNull ViewGroup parent) {
        //get the Category information:
        String date = getItem(position).getDate();
        int type = getItem(position).getType();
        float amount = getItem(position).getAmount();
        String category = getItem(position).getCategory();



        // turn float to string
        String amountToString = Float.toString(amount);


        LayoutInflater inflater = LayoutInflater.from(mContext);
        View convertView = inflater.inflate(mResource, parent, false);


        ImageView transactionCategoryImage = (ImageView) convertView.findViewById(R.id.transactionCategoryImage);
        TextView transactionCategoryTitle = (TextView) convertView.findViewById(R.id.transactionCategoryTitle);
        TextView transactionDate = (TextView) convertView.findViewById(R.id.transactionDate);
//        TextView transactionCurrency = (TextView) convertView.findViewById(R.id.transactionCurrency);
        TextView transactionAmount = (TextView) convertView.findViewById(R.id.transactionAmount);
        TextView transactionType = (TextView) convertView.findViewById(R.id.transactionType);


        transactionDate.setText(date);
        transactionCategoryTitle.setText(category);
//        transactionCurrency.setText(currency);
        if (amount >= 0) {
            transactionAmount.setTextColor(Color.parseColor("#4CAF50"));
            amountToString = " +" + amountToString;
        }
        // combine currency and amount
        transactionAmount.setText(amountToString);
        String typeAsString = "";
        if (type == 1) {
            transactionType.setTextColor(Color.parseColor("#F44336"));
             typeAsString = "Expense";
        } else {
            transactionType.setTextColor(Color.parseColor("#4CAF50"));
            typeAsString = "Income";
        }
        transactionType.setText(typeAsString);
        return convertView;
    }
}

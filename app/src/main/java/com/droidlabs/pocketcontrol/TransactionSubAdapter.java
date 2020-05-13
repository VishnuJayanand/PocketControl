package com.droidlabs.pocketcontrol;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransactionSubAdapter extends ArrayAdapter<Transaction> {
    private Context mContext;
    private int mResource;

    /**
     * Creating adapter for Transaction.
     * @param context context
     * @param resource int
     * @param objects list of Transaction
     */
    public TransactionSubAdapter(final @NonNull Context context, final int resource, final  @NonNull List<Transaction> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    /**
     * method to set the attribute of Transaciton class to transactionListView.
     * @param position int
     * @param convertView view
     * @param parent View Group
     * @return convert View
     */
    public View getView(final int position, View convertView, final  @NonNull ViewGroup parent) {
        //get the Category information:
        String title = getItem(position).getTitle();
        Date date = getItem(position).getDate();
        String type = getItem(position).getType();
        float amount = getItem(position).getAmount();
        int categoryImage = getItem(position).getCategoryImage();
        String categoryTitle = getItem(position).getCategoryTitle();
        String currency = getItem(position).getCurrency();

        // turn float to string
        String amountToString = Float.toString(amount);
        // turn date to string
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String dateToString = formatter.format(date);


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView transactionTitle = (TextView) convertView.findViewById(R.id.transactionTitle);
        ImageView transactionCategoryImage = (ImageView) convertView.findViewById(R.id.transactionCategoryImage);
        TextView transactionCategoryTitle = (TextView) convertView.findViewById(R.id.transactionCategoryTitle);
        TextView transactionDate = (TextView) convertView.findViewById(R.id.transactionDate);
//        TextView transactionCurrency = (TextView) convertView.findViewById(R.id.transactionCurrency);
        TextView transactionAmount = (TextView) convertView.findViewById(R.id.transactionAmount);
        TextView transactionType = (TextView) convertView.findViewById(R.id.transactionType);

        transactionTitle.setText(title);
        transactionDate.setText(dateToString);
        transactionCategoryImage.setImageResource(categoryImage);
        transactionCategoryTitle.setText(categoryTitle);
//        transactionCurrency.setText(currency);
        if (amount >= 0) {
            transactionAmount.setTextColor(Color.parseColor("#4CAF50"));
            amountToString = " +" + amountToString;
        }
        // combine currency and amount
        amountToString = currency + " " + amountToString;
        transactionAmount.setText(amountToString);
        if (type == "income") {
            transactionType.setTextColor(Color.parseColor("#4CAF50"));
        }
        else {
            transactionType.setTextColor(Color.parseColor("#F44336"));
        }
        transactionType.setText(type);

        return convertView;
    }

}

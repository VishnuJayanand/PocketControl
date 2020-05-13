package com.droidlabs.pocketcontrol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {

    private Context mContext;
    private int mResource;
    private static final String TAG = "CategoryAdapter";

    /**
     * Creating a adapter for the category.
     * @param context context
     * @param resource resource
     * @param objects list of category
     */
    public CategoryAdapter(final @NonNull Context context, final int resource, final  @NonNull List<Category> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    /**
     * method to get the view and set imageId, title for ImageView and Title.
     * @param position position
     * @param convertView the view
     * @param parent parent
     * @return the view
     */
    @NonNull
    @Override
    public View getView(final int position, View convertView, final  @NonNull ViewGroup parent) {
        //get the Category information:
        int imageId = getItem(position).getImageId();
        String title = getItem(position).getTitle();

        //Create the Category object with the information
//        Category category = new Category(imageId, title);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView textView = (TextView) convertView.findViewById(R.id.txtTitle);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

        textView.setText(title);
        imageView.setImageResource(imageId);

        return convertView;
    }
}

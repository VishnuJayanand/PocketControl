package com.droidlabs.pocketcontrol.db.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.droidlabs.pocketcontrol.R;

import java.util.List;

public class CategoryGridAdapter extends ArrayAdapter<com.droidlabs.pocketcontrol.db.category.Category> {

    private Context mContext;
    private int mResource;
    private static final String TAG = "CategoryAdapter";

    /**
     * Creating a adapter for the category.
     * @param context context
     * @param resource resource
     * @param objects list of category
     */
    public CategoryGridAdapter(final @NonNull Context context, final int resource, final  @NonNull List<com.droidlabs.pocketcontrol.db.category.Category> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    /**
     * method to get the view and set imageId, title for ImageView and Title.
     * @param position position
     * @param view the view
     * @param parent parent
     * @return the view
     */
    @NonNull
    @Override
    public View getView(final int position, View view, final  @NonNull ViewGroup parent) {
        //get the Category information:
        int imageId = getItem(position).getIcon();
        String title = getItem(position).getName();

        //Create the Category object with the information
//        Category category = new Category(imageId, title);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View convertView = inflater.inflate(mResource, parent, false);

        TextView textView = (TextView) convertView.findViewById(R.id.txtTitle);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

        textView.setText(title);
        imageView.setImageResource(imageId);

        return convertView;
    }
}

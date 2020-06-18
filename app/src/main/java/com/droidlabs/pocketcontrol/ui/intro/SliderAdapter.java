package com.droidlabs.pocketcontrol.ui.intro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.droidlabs.pocketcontrol.R;

/**
 * This class populates the slider with the images and texts.
 */
public class SliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    /**
     * This class populates the slider with the images and texts.
     * @param cont context
     */
    public SliderAdapter(final Context cont) {
        this.context = cont;
    }

    private int[] images = {
            R.drawable.ic_add_transactions_icon,
            R.drawable.ic_add_categories_icon,
            R.drawable.ic_multiple_accounts_icon,
            R.drawable.ic_monitor_expense_icon
    };

    private int[] headings = {
            R.string.first_slide_title,
            R.string.second_slide_title,
            R.string.third_slide_title,
            R.string.fourth_slide_title
    };

    private int[] description = {
            R.string.first_slide_desc,
            R.string.second_slide_desc,
            R.string.third_slide_desc,
            R.string.fourth_slide_desc
    };

    /**
     * To get the count of pages.
     * @return length.
     */
    @Override
    public int getCount() {
        return headings.length;
    }

    /**
     * To check if the view belongs to the object.
     * @return view.
     */
    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view == (ScrollView) object;
    }

    /**
     * To intantiate item.
     * @param container container.
     * @param position postion.
     */
    @NonNull
    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slides_layout, container, false);

        //Hooks
        ImageView imageView = view.findViewById(R.id.slider_image);
        TextView heading = view.findViewById(R.id.slider_heading);
        TextView desc = view.findViewById(R.id.slider_desc);

        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);
        desc.setText(description[position]);

        container.addView(view);

        return view;
    }

    /**
     * To destroy.
     * @param position postion.
     * @param object object.
     */
    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((ScrollView) object);
    }
}

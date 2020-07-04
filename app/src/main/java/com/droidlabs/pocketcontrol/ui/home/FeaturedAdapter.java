package com.droidlabs.pocketcontrol.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidlabs.pocketcontrol.R;

import java.util.ArrayList;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {

    private ArrayList<FeaturedHelperClass> featuredLocations;

    /**
     * Featured adapter.
     * @param featuredLocationsFunction array of postions
     */
    public FeaturedAdapter(final ArrayList<FeaturedHelperClass> featuredLocationsFunction) {
        this.featuredLocations = featuredLocationsFunction;
    }

    /**
     * to assign the layout id.
     */
    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(final @NonNull ViewGroup parent, final int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_card_design, parent, false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view);
        return featuredViewHolder;
    }

    /**
     * get holder overrided function.
     */
    @Override
    public void onBindViewHolder(final @NonNull FeaturedViewHolder holder, final int position) {

        FeaturedHelperClass featuredHelperClass = featuredLocations.get(position);

        holder.image.setImageResource(featuredHelperClass.getImage());
        holder.title.setText(featuredHelperClass.getTitle());
        holder.category.setText(featuredHelperClass.getCategory());
        holder.amount.setText(featuredHelperClass.getAmount());
    }

    /**
     * Get item count.
     * @return size
     */
    @Override
    public int getItemCount() {
        return featuredLocations.size();
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder {

        private TextView title, category, amount;
        private ImageView image;

        /**
         * View holder.
         * @param itemView view
         */
        public FeaturedViewHolder(final @NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.featured_icon);
            title = itemView.findViewById(R.id.featured_text);
            category = itemView.findViewById(R.id.featured_category);
            amount = itemView.findViewById(R.id.featured_amount);
        }
    }
}

package com.droidlabs.pocketcontrol.ui.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.category.Category;
import java.util.List;

public final class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.CategoryViewHolder> {

    private List<Category> categories; // Cached copy of transactions
    private final LayoutInflater layoutInflater;
    private static final String TAG = "CategoryAdapter";
    private OnCategoryNoteListener mOnNoteListener;

    /**
     * Creating adapter for Transaction.
     * @param context context
     * @param onNoteListener onNotelistener
     */
    public CategoryGridAdapter(final @NonNull Context context, final OnCategoryNoteListener onNoteListener) {
        layoutInflater = LayoutInflater.from(context);
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(final @NonNull ViewGroup parent, final int viewType) {
        View itemView = layoutInflater.inflate(R.layout.category_griditem, parent, false);
        return new CategoryViewHolder(itemView, mOnNoteListener);
    }

    /**
     * Initializer for transactions to be displayed.
     * @param holder view holder.
     * @param position position of transactions.
     */
    @Override
    public void onBindViewHolder(final @NonNull CategoryViewHolder holder, final int position) {
        if (categories != null) {
            Category current = categories.get(position);

            //get the Transaction information:
            int imageId = current.getIcon();
            String title = current.getName();

            holder.categoryTitle.setText(title);
            holder.categoryImage.setImageResource(imageId);
        }
    }

    /**
     * Categories setter.
     * @param categoryList categories from db.
     */
    public void setCategories(final List<Category> categoryList) {
        this.categories = categoryList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (categories != null) {
            return categories.size();
        } else {
            return 0;
        }
    }

    final class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView categoryImage;
        private final TextView categoryTitle;
        private final OnCategoryNoteListener onNoteListener;

        /**
         * Transaction view holder.
         * @param itemView view that will hold the category list.
         * @param onCategoryNoteListener OnNoteListener
         */
        private CategoryViewHolder(final View itemView, final OnCategoryNoteListener onCategoryNoteListener) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.imageView);
            categoryTitle = itemView.findViewById(R.id.txtTitle);
            this.onNoteListener = onCategoryNoteListener;

            itemView.setOnClickListener(this);
        }

        /**
         * method to get catch onclick event of grid adapter.
         * @param v view
         */
        @Override
        public void onClick(final View v) {
            onNoteListener.onCategoryClick(categories.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnCategoryNoteListener {
        /**
         * This method to transfer category and position from the selected category.
         * @param category Category selected category
         * @param position int selected position
         */
        void onCategoryClick(Category category, int position);
    }
}

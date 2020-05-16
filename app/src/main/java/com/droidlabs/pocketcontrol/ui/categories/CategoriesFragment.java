package com.droidlabs.pocketcontrol.ui.categories;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.db.category.CategoryGridAdapter;

import android.widget.GridView;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {
    private ArrayList<Category> cartegoriesList = new ArrayList<>();
    private GridView gridView;
    @Nullable
    @Override
    public final View onCreateView(
        final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.category_gridview, container, false);
//        createListCategory();
        //Create the adapter for Cartegory.
        CategoryGridAdapter adapter = new CategoryGridAdapter(getContext(),
                R.layout.category_griditem, cartegoriesList);
        //Set the adapter to gridView
        gridView = view.findViewById(R.id.categoryGridView);
        gridView.setAdapter(adapter);
        return view;
    }

    //This Category list is to help visualize the UI

    /**
     * Create a Category list for example.
     */
//    public void createListCategory() {
//        Category health = new Category(1, "Health", R.drawable.health);
//        Category transport = new Category(2, "Transport", R.drawable.transport);
//        Category shopping = new Category(3, "Shopping", R.drawable.shopping);
//        Category food = new Category(4, "Food", R.drawable.food);
//        Category study = new Category(5, "Study", R.drawable.study);
//        Category rent = new Category(6, "Rent", R.drawable.rent);
//
//        cartegoriesList.add(health);
//        cartegoriesList.add(transport);
//        cartegoriesList.add(shopping);
//        cartegoriesList.add(food);
//        cartegoriesList.add(study);
//        cartegoriesList.add(rent);
//    }
}

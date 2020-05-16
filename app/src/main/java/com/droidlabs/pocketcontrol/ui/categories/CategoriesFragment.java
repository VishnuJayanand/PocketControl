package com.droidlabs.pocketcontrol.ui.categories;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.db.category.CategoryGridAdapter;
import com.droidlabs.pocketcontrol.db.category.CategoryViewModel;

import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public final class CategoriesFragment extends Fragment {
    private ArrayList<Category> cartegoriesList = new ArrayList<>();
    private GridView gridView;
    private CategoryGridAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(
        final LayoutInflater inf,
        final @Nullable ViewGroup container,
        final @Nullable Bundle savedInstanceState
    ) {
        View view = inf.inflate(R.layout.category_gridview, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.categoryGridView);

        //Create the adapter for Cartegory.
        adapter = new CategoryGridAdapter(getActivity());
        //Set the adapter to gridView
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        final CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(final List<Category> categories) {
                adapter.setCategories(categories);
            }
        });

        return view;
    }
}

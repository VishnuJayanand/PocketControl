package com.droidlabs.pocketcontrol.ui.categories;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.category.Category;

import android.widget.GridView;
import android.widget.LinearLayout;

public final class CategoriesFragment extends Fragment implements CategoryGridAdapter.OnCategoryNoteListener {
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
        adapter = new CategoryGridAdapter(getActivity(), this);
        //Set the adapter to gridView
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        final CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        adapter.setCategories(categoryViewModel.getAllCategories());
        LinearLayout layout = view.findViewById(R.id.addCategoryButton);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v)  {
                Fragment fragment = new AddCategoryFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    /**
     * This method is to view the category detail fragment.
     * @param category Category selected category
     * @param position int selected position
     */
    @Override
    public void onCategoryClick(final Category category, final int position) {
        Bundle bundle = new Bundle();
        bundle.putString("categoryTitle", category.getName());
        bundle.putInt("categoryImage", category.getIcon());
        bundle.putInt("categoryId", category.getId());
        //Move to category detail fragment
        Fragment fragment = new DetailCategoryFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

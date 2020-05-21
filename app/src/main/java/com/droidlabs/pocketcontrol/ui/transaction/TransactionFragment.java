package com.droidlabs.pocketcontrol.ui.transaction;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.ui.categories.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;


public class TransactionFragment extends Fragment {

    private TransactionViewModel transactionViewModel;
    private CategoryViewModel categoryViewModel;
    private TransactionListAdapter transactionListAdapter;

    @Override
    public final View onCreateView(
        final LayoutInflater inf,
        final @Nullable ViewGroup container,
        final @Nullable Bundle savedInstanceState
    ) {
        View view = inf.inflate(R.layout.transaction_listview, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.transactionListView);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        transactionListAdapter = new TransactionListAdapter(getActivity());

        recyclerView.setAdapter(transactionListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        transactionListAdapter.setTransactions(transactionViewModel.getAllTransactions());
        LinearLayout addTransactionLayout = view.findViewById(R.id.addTransactionButton);

        addTransactionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v)  {
                Fragment fragment = new AddTransactionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Spinner categoryFilterSpinner = view.findViewById(R.id.category_filter_spinner);
        ArrayAdapter<Category> categoryFilterAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                buildCategoryFilterList()
        );

        categoryFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryFilterSpinner.setAdapter(categoryFilterAdapter);
        categoryFilterSpinner.setOnItemSelectedListener(new SpinnerListener());

        return view;
    }

    /**
     * Helper method to build the category filter list and add a default "Select all" pseudo category.
     * @return category list.
     */
    private List<Category> buildCategoryFilterList() {
        List<Category> categoryFilterList = new ArrayList<>();

        Category noCategory = new Category(-1, "Select all", -1);
        List<Category> categories = categoryViewModel.getAllCategories();

        categoryFilterList.add(noCategory);
        categoryFilterList.addAll(categories);

        return categoryFilterList;
    }

    private class SpinnerListener implements AdapterView.OnItemSelectedListener {

        /**
         * Default constructor.
         */
        SpinnerListener() { }

        @Override
        public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
            Category selectedCategory = (Category) parent.getItemAtPosition(position);

            if (selectedCategory.getId() == -1) {
                transactionListAdapter.setTransactions(transactionViewModel.getAllTransactions());
            } else {
                transactionListAdapter.setTransactions(
                        transactionViewModel.getTransactionsByCategoryId(
                                selectedCategory.getId().toString()
                        )
                );
            }
        }

        @Override
        public void onNothingSelected(final AdapterView<?> parent) {
            Log.v("SPINNER", "NOTHING SELECTED");
        }
    }
}

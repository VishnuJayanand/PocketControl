package com.droidlabs.pocketcontrol.ui.transaction;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;


import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.ui.categories.CategoryViewModel;
import com.droidlabs.pocketcontrol.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TransactionFragment extends Fragment implements TransactionListAdapter.OnTransactionNoteListener {

    private TransactionViewModel transactionViewModel;
    private CategoryViewModel categoryViewModel;
    private TransactionListAdapter transactionListAdapter;
    private EditText editTextToDate, editTextFromDate;
    private Category selectedCategory;
    private boolean filterByCategory = false, filterByDate = false;
    private final Calendar fromDate = Calendar.getInstance(), toDate = Calendar.getInstance();

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
        transactionListAdapter = new TransactionListAdapter(getActivity(), this, transactionViewModel);

        recyclerView.setAdapter(transactionListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        transactionViewModel.getTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                transactionListAdapter.setTransactions(transactions);
            }
        });

        AppCompatButton addTransactionLayout = view.findViewById(R.id.addTransactionButton);

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

        // Date filter

        editTextFromDate = view.findViewById(R.id.fromDate);
        editTextToDate = view.findViewById(R.id.toDate);

        DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(
                    final DatePicker view,
                    final int year,
                    final int monthOfYear,
                    final int dayOfMonth
            ) {
                // TODO Auto-generated method stub
                fromDate.set(Calendar.YEAR, year);
                fromDate.set(Calendar.MONTH, monthOfYear);
                fromDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTransactionDateLabel(editTextFromDate, fromDate);
            }
        };

        editTextFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(
                        getContext(),
                        fromDateListener,
                        fromDate.get(Calendar.YEAR),
                        fromDate.get(Calendar.MONTH),
                        fromDate.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(
                    final DatePicker view,
                    final int year,
                    final int monthOfYear,
                    final int dayOfMonth
            ) {
                // TODO Auto-generated method stub
                toDate.set(Calendar.YEAR, year);
                toDate.set(Calendar.MONTH, monthOfYear);
                toDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTransactionDateLabel(editTextToDate, toDate);
            }
        };

        editTextToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(
                        getContext(),
                        toDateListener,
                        toDate.get(Calendar.YEAR),
                        toDate.get(Calendar.MONTH),
                        toDate.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        ImageButton imageButton = view.findViewById(R.id.imageDateButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                filterByDate = !(
                        editTextFromDate.getText().toString().equals("")
                        && editTextToDate.getText().toString().equals("")
                );

                filterTransactions();
            }
        });

        return view;
    }

    /**
     * Method to update the transaction date with the selected date.
     * @param editTextLayout editText transaction date
     * @param myCalendar calendar the calendar to choose date
     */
    private void updateTransactionDateLabel(final EditText editTextLayout, final Calendar myCalendar) {
        String myFormat = "dd-MM-yy"; //In which you need put here
        editTextLayout.setText(DateUtils.formatDate(myCalendar.getTimeInMillis(), myFormat));
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
            selectedCategory = (Category) parent.getItemAtPosition(position);
            filterByCategory = selectedCategory.getId() != -1;

            filterTransactions();
        }

        @Override
        public void onNothingSelected(final AdapterView<?> parent) {
            Log.v("SPINNER", "NOTHING SELECTED");
        }
    }

    /**
     * This method is to view the transaction detail fragment.
     * @param transaction Transaction selected transaction
     * @param position int selected position
     */
    @Override
    public void onTransactionClick(final Transaction transaction, final int position) {
        Bundle bundle = new Bundle();
        bundle.putFloat("transactionDate", transaction.getDate());
        bundle.putFloat("transactionAmount", transaction.getAmount());
        bundle.putString("transactionNote", transaction.getTextNote());
        bundle.putInt("transactionType", transaction.getType());
        bundle.putString("transactionCategory", transaction.getCategory());
        //Move to transaction detail fragment
        Fragment fragment = new DetailTransacionFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void filterTransactions() {
        if (filterByCategory) {
            transactionViewModel.filterTransactionsByCategoryId(selectedCategory.getId().toString());
        }
    }
    /*
    /**
     * Helper method to check filters and filter transactions.
     * @return list of filtered transactions.

    private List<Transaction> getFilteredTransactions() {
        Log.v("FILTER", "Filter by date: " + filterByDate);
        Log.v("FILTER", "Filter by category: " + filterByCategory);
        if (filterByDate) {
            long fromDateInMS, toDateInMS;

            if (!editTextFromDate.getText().toString().equals("")) {
                fromDateInMS = DateUtils.getStartOfDayInMS(fromDate.getTimeInMillis());
            } else {
                if (!editTextToDate.getText().toString().equals("")) {
                    fromDateInMS = DateUtils.getStartOfDayInMS(0);
                } else {
                    fromDateInMS = DateUtils.getStartOfCurrentDay().getTimeInMillis();
                }
            }

            if (!editTextToDate.getText().toString().equals("")) {
                toDateInMS = DateUtils.getEndOfDayInMS(toDate.getTimeInMillis());
            } else {
                if (!editTextFromDate.getText().toString().equals("")) {
                    if (
                        DateUtils.getStartOfDayInMS(fromDate.getTimeInMillis())
                        > DateUtils.getStartOfCurrentDay().getTimeInMillis()
                    ) {
                        toDateInMS = DateUtils.getEndOfDayInMS(fromDate.getTimeInMillis());
                    } else {
                        toDateInMS = DateUtils.getEndOfCurrentDay().getTimeInMillis();
                    }
                } else {
                    toDateInMS = DateUtils.getEndOfCurrentDay().getTimeInMillis();
                }
            }

            if (filterByCategory) {
                return transactionViewModel.filterTransactionsByCategoryAndDate(
                        selectedCategory.getId().toString(), fromDateInMS, toDateInMS
                );
            } else {
                return transactionViewModel.filterTransactionsByDate(fromDateInMS, toDateInMS);
            }
        } else if (filterByCategory) {
            if (selectedCategory.getId() == -1) {
                return transactionViewModel.getAllTransactions();
            } else {
                return transactionViewModel.filterTransactionsByCategoryId(selectedCategory.getId().toString());
            }
        } else {
            return transactionViewModel.getAllTransactions();
        }
    }
    */
}

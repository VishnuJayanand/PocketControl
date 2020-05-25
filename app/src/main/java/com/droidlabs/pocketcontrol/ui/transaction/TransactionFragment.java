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
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.ui.categories.CategoryViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class TransactionFragment extends Fragment implements TransactionListAdapter.OnTransactionNoteListener {

    private List<Transaction> transactionsList = new ArrayList<>();
    private ListView listView;
    private TransactionViewModel transactionViewModel;
    private CategoryViewModel categoryViewModel;
    private TransactionListAdapter transactionListAdapter;
    private int fromDay, fromMonth, fromYear;
    private int toDay, toMonth, toYear;

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

        //Create the adapter for Transaction
        final TransactionListAdapter adapter = new TransactionListAdapter(getActivity(), this, transactionViewModel);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        transactionListAdapter = new TransactionListAdapter(getActivity(), this, transactionViewModel);


        recyclerView.setAdapter(transactionListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        transactionsList = transactionViewModel.getAllTransactions();
        transactionListAdapter.setTransactions(transactionsList);

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

        //date filter

        EditText editTextToDate, editTextFromDate;

        final Calendar myCalendar1 = Calendar.getInstance();
        editTextFromDate = view.findViewById(R.id.fromDate);
        DatePickerDialog.OnDateSetListener fromdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, final int year, final int monthOfYear,
                                  final int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                fromDay = dayOfMonth;
                fromMonth = monthOfYear + 1;
                fromYear = year;
               updateTransactionDateLabel(editTextFromDate, myCalendar1);
            }

        };

        editTextFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), fromdate, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final Calendar myCalendar2 = Calendar.getInstance();
        editTextToDate = view.findViewById(R.id.toDate);
        DatePickerDialog.OnDateSetListener toDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, final int year, final int monthOfYear,
                                  final int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                toDay = dayOfMonth;
                toMonth = monthOfYear + 1;
                toYear = year;
                updateTransactionDateLabel(editTextToDate, myCalendar2);
                }
        };

        editTextToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), toDate, myCalendar2
                        .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                        myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ImageButton imageButton = view.findViewById(R.id.imageDateButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                List<Transaction> transactionsListFilterDate = new ArrayList<>();
                for (Transaction transaction : transactionsList) {

                    String date = transaction.getDate();
                    String[] arrOfDates = date.split("-");
                    if (isInDateRange(arrOfDates)) {
                        transactionsListFilterDate.add(transaction);
                    }
                }
                transactionListAdapter.setTransactions(transactionsListFilterDate);
            }
        });

        return view;
    }


    /**
     * Method to check if the date is within the period.
     * @param arrOfDates contains the dy , month and year
     * @return boolean
     */
    private boolean isInDateRange(final String[] arrOfDates) {
        // String date format used is "dd-mm-yyyy"

        int day = Integer.parseInt(arrOfDates[0]);
        int month = Integer.parseInt(arrOfDates[1]);
        int year = Integer.parseInt(arrOfDates[2]);

        return fromDay <= day && day <= toDay && fromMonth <= month
                && month <= toMonth && fromYear <= year && year <= toYear;

    }

    /**
     * Method to update the transaction date with the selected date.
     * @param editTextLayout editText transaction date
     * @param myCalendar calendar the calendar to choose date
     */
    private void updateTransactionDateLabel(final EditText editTextLayout, final Calendar myCalendar) {
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTextLayout.setText(sdf.format(myCalendar.getTime()));
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

                transactionsList = transactionViewModel.getAllTransactions();
                transactionListAdapter.setTransactions(transactionsList);

            } else {

                transactionsList = transactionViewModel.getTransactionsByCategoryId(
                        selectedCategory.getId().toString());
                transactionListAdapter.setTransactions(transactionsList);

            }
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
        bundle.putString("transactionDate", transaction.getDate());
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
}

package com.droidlabs.pocketcontrol.ui.transaction;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.ui.categories.CategoryViewModel;
import com.droidlabs.pocketcontrol.utils.DateUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FilterBottomSheetDialog extends BottomSheetDialogFragment {

    private TransactionViewModel transactionViewModel;
    private CategoryViewModel categoryViewModel;

    private boolean categoryFilterEnabled;
    private boolean amountFilterEnabled;
    private boolean dateFilterEnabled;

    private EditText editTextFromAmount;
    private EditText editTextToAmount;
    private EditText editTextFromDate;
    private EditText editTextToDate;
    private Category categoryFilter;

    private final Calendar fromDate = Calendar.getInstance(), toDate = Calendar.getInstance();

    /**
     * Public constructor to initialize viewModels.
     * @param tViewModel transaction viewmodel.
     * @param cViewModel category viewmodel.
     */
    public FilterBottomSheetDialog(
            final TransactionViewModel tViewModel,
            final CategoryViewModel cViewModel
    ) {
        this.transactionViewModel = tViewModel;
        this.categoryViewModel = cViewModel;
    }

    /**
     * Executed when creating the view.
     * @param inflater inflater.
     * @param container container.
     * @param savedInstanceState saved instance state.
     * @return created view.
     */
    @Nullable
    @Override
    public View onCreateView(
            final @NonNull LayoutInflater inflater,
            final @Nullable ViewGroup container,
            final @Nullable Bundle savedInstanceState
    ) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.transaction_listfilterbottomsheet, null, false);

        Spinner categoryFilterSpinner = view.findViewById(R.id.category_filter_spinner);
        ArrayAdapter<Category> categoryFilterAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                buildCategoryFilterList()
        );

        // Category filter
        categoryFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryFilterSpinner.setAdapter(categoryFilterAdapter);
        categoryFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(
                    final AdapterView<?> parent,
                    final View view,
                    final int position,
                    final long id) {
                categoryFilter = (Category) parent.getItemAtPosition(position);
                categoryFilterEnabled = categoryFilter.getId() != -1;
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
                Log.v("SPINNER", "NOTHING SELECTED");
            }
        });

        // Amount filter
        editTextFromAmount = view.findViewById(R.id.fromAmount);
        editTextToAmount = view.findViewById(R.id.toAmount);

        editTextFromAmount.addTextChangedListener(amountTextWatcher);
        editTextToAmount.addTextChangedListener(amountTextWatcher);

        // Date filter
        editTextFromDate = view.findViewById(R.id.fromDate);
        editTextToDate = view.findViewById(R.id.toDate);

        editTextFromDate.addTextChangedListener(dateTextWatcher);
        editTextToDate.addTextChangedListener(dateTextWatcher);

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

        Button submitFilterButton = view.findViewById(R.id.applyFiltersTransaction);

        submitFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (amountFilterEnabled) {
                    transactionViewModel.setAmountFilter(
                            true,
                            Float.parseFloat(editTextFromAmount.getText().toString()),
                            Float.parseFloat(editTextToAmount.getText().toString())
                    );
                } else {
                    transactionViewModel.setAmountFilter(false, Float.MIN_VALUE, Float.MAX_VALUE);
                }

                if (categoryFilterEnabled) {
                    transactionViewModel.setCategoryFilter(true, categoryFilter.getId().toString());
                } else {
                    transactionViewModel.setCategoryFilter(false, categoryFilter.getId().toString());
                }

                if (dateFilterEnabled) {
                    transactionViewModel.setDateFilter(true, parseFromDate(), parseToDate());
                } else {
                    transactionViewModel.setDateFilter(false, parseFromDate(), parseToDate());
                }
                dismissBottomSheet();
            }
        });
        return view;
    }

    /**
     * Helper method to dismiss current bottom sheet.
     */
    private void dismissBottomSheet() {
        this.dismiss();
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

    private TextWatcher amountTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

        }

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
            amountFilterEnabled = !(editTextFromAmount.getText().toString().isEmpty()
                    || editTextToAmount.getText().toString().isEmpty());
        }

        @Override
        public void afterTextChanged(final Editable s) {

        }
    };

    private TextWatcher dateTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

        }

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
            dateFilterEnabled = !(editTextFromDate.getText().toString().isEmpty()
                    || editTextToDate.getText().toString().isEmpty());
        }

        @Override
        public void afterTextChanged(final Editable s) {

        }
    };


    /**
     * Parse fromDate for date filter.
     * @return parsed from date.
     */
    private long parseFromDate() {
        if (!editTextFromDate.getText().toString().equals("")) {
            return DateUtils.getStartOfDayInMS(fromDate.getTimeInMillis());
        } else {
            if (!editTextToDate.getText().toString().equals("")) {
                return DateUtils.getStartOfDayInMS(0);
            } else {
                return DateUtils.getStartOfCurrentDay().getTimeInMillis();
            }
        }
    }

    /**
     * Parse toDate for date filter.
     * @return parsed to date.
     */
    private long parseToDate() {
        if (!editTextToDate.getText().toString().equals("")) {
            return DateUtils.getEndOfDayInMS(toDate.getTimeInMillis());
        } else {
            if (!editTextFromDate.getText().toString().equals("")) {
                if (
                        DateUtils.getStartOfDayInMS(fromDate.getTimeInMillis())
                                > DateUtils.getStartOfCurrentDay().getTimeInMillis()
                ) {
                    return DateUtils.getEndOfDayInMS(fromDate.getTimeInMillis());
                } else {
                    return DateUtils.getEndOfCurrentDay().getTimeInMillis();
                }
            } else {
                return DateUtils.getEndOfCurrentDay().getTimeInMillis();
            }
        }
    }

}

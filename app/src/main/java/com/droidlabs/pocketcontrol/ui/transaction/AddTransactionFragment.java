package com.droidlabs.pocketcontrol.ui.transaction;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.db.category.CategoryDao;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.utils.DateUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTransactionFragment extends Fragment {
    private TextInputEditText tiedtTransactionAmount, tiedtTransactionNote;
    private TextInputEditText customRecurringDaysInterval;
    private TextInputLayout tilTransactionAmount, tilTransactionNote, tilCustomRecurringDaysInterval;
    private boolean isTransactionRecurring;
    private int transactionType;
    private int transactionMethod;
    private int transactionRecurringIntervalType;
    private Spinner dropdownTransactionType;
    private Spinner dropdownTransactionMethod;
    private Spinner dropdownTransactionCategory;
    private Spinner dropdownRecurringTransaction;
    private EditText editText;
    private Long transactionDate;
    private CategoryDao categoryDao;
    private TransactionViewModel transactionViewModel;
    private Switch recurringSwitch;
    private LinearLayout recurringTransactionWrapper;
    private LinearLayout customDaysIntervalWrapper;

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.transaction_add, container, false);
        categoryDao = PocketControlDB.getDatabase(getContext()).categoryDao();
        tiedtTransactionAmount = view.findViewById(R.id.tiedt_transactionAmount);
        tiedtTransactionNote = view.findViewById(R.id.tiedt_transactionNote);
        tilTransactionAmount = view.findViewById(R.id.til_transactionAmount);
        tilTransactionNote = view.findViewById(R.id.til_transactionNote);
        tilCustomRecurringDaysInterval = view.findViewById(R.id.til_customRecurringDaysInterval);
        recurringSwitch = view.findViewById(R.id.recurringSwitch);
        recurringTransactionWrapper = view.findViewById(R.id.recurringTransactionWrapper);
        customDaysIntervalWrapper = view.findViewById(R.id.customDaysIntervalWrapper);
        customRecurringDaysInterval = view.findViewById(R.id.customRecurringDaysInterval);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        Button btnAdd = view.findViewById(R.id.addNewTransaction);

        //set the spinner for transactionType from the xml.
        setTransactionTypeSpinner(view);
        //set the spinner for transactionMethod from the xml.
        setTransactionMethodSpinner(view);
        //set thee spinner for transactionIcon from the xml.
        setTransactionCategorySpinner(view);

        setRecurringTransactionsSpinner(view);

        recurringSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recurringTransactionWrapper.setVisibility(View.VISIBLE);
                } else {
                    recurringTransactionWrapper.setVisibility(View.GONE);
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                submitForm();
            }
        });

        final Calendar myCalendar = Calendar.getInstance();
        editText = view.findViewById(R.id.transactionDate);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, final int year, final int monthOfYear,
                                  final int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTransactionDateLabel(editText, myCalendar);
            }

        };

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        transactionDate = myCalendar.getTimeInMillis();

        editTextLayout.setText(sdf.format(transactionDate));
    }

    /**
     * This method to set the spinner of Transaction Type.
     * @param view the transaction add layout
     */
    private void setTransactionTypeSpinner(final View view) {
        //get the spinner from the xml.
        dropdownTransactionType = view.findViewById(R.id.spinnerTransactionType);
        //create a list of items for the spinner.
        String[] dropdownItems = new String[]{"Expense", "Income"};
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, dropdownItems);
        //set the spinners adapter to the previously created one.
        dropdownTransactionType.setAdapter(adapterType);
    }

    /**
     * This method to set the spinner of Transaction Method.
     * @param view the transaction add layout
     */
    private void setTransactionMethodSpinner(final View view) {
        //get the spinner from the xml.
        dropdownTransactionMethod = view.findViewById(R.id.spinnerTransactionMethod);
        //create a list of items for the spinner.
        String[] dropdownItems = new String[]{"Cash", "Card"};
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, dropdownItems);
        //set the spinners adapter to the previously created one.
        dropdownTransactionMethod.setAdapter(adapterType);
    }

    /**
     * This method to set the spinner of Transaction Category.
     * @param view the transaction add layout
     */
    private void setTransactionCategorySpinner(final View view) {
        //get the spinner from the xml.
        dropdownTransactionCategory = view.findViewById(R.id.spinnerTransactionCategory);
        //create a list of items for the spinner.
        String[] dropdownItems = categoryDao.getCategoriesName();
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, dropdownItems);
        //set the spinners adapter to the previously created one.
        dropdownTransactionCategory.setAdapter(adapterCategory);
    }

    /**
     * This method to set the spinner of Transaction Category.
     * @param view the transaction add layout
     */
    private void setRecurringTransactionsSpinner(final View view) {
        //get the spinner from the xml.
        dropdownRecurringTransaction = view.findViewById(R.id.spinnerRecurringInterval);
        //create a list of items for the spinner.
        String[] dropdownItems = { "Daily", "Weekly", "Monthly", "Custom" };
        ArrayAdapter<String> adapterRecurring = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, dropdownItems);
        //set the spinners adapter to the previously created one.
        dropdownRecurringTransaction.setAdapter(adapterRecurring);
        dropdownRecurringTransaction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);

                if (selected.equals("Custom")) {
                    customDaysIntervalWrapper.setVisibility(View.VISIBLE);
                } else {
                    customDaysIntervalWrapper.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Method to focus on the view that have the wrong input.
     * @param view the view
     */
    public void requestFocus(final View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /**
     * Method to check the input of transaction amount.
     * @return Boolean if the input is qualify or not
     */
    private boolean checkTransactionAmount() {
        if (tiedtTransactionAmount.getText().toString().trim().isEmpty()) {
            tilTransactionAmount.setError("Please enter the amount of your transaction");
            requestFocus(tiedtTransactionAmount);
            return false;
        }

        if (Integer.parseInt(tiedtTransactionAmount.getText().toString().trim()) <= 0) {
            tilTransactionAmount.setError("Amount should be larger than 0");
            requestFocus(tiedtTransactionAmount);
            return false;
        }
        return true;
    }


    /**
     * Method to check the input of transaction Date.
     * @return Boolean if the input is qualify or not
     */
    private boolean checkTransactionDate() {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError("Please enter the date of the transaction");
            requestFocus(editText);
            return false;
        }
        return true;
    }

    private boolean checkRecurringConditions() {
        if (dropdownRecurringTransaction.getSelectedItem().equals("Custom")) {
            if (customRecurringDaysInterval.getText().toString().trim().isEmpty()) {
                tilCustomRecurringDaysInterval.setError("Required");
                requestFocus(customRecurringDaysInterval);
                return false;
            }

            if (Integer.parseInt(customRecurringDaysInterval.getText().toString().trim()) <= 0) {
                tilCustomRecurringDaysInterval.setError("Should be larger than 0");
                requestFocus(customRecurringDaysInterval);
                return false;
            }
        }

        return true;
    }

    /**
     * Method to convert transactionType.
     */
    private void convertTransactionType() {
        String text = dropdownTransactionType.getSelectedItem().toString();
        if (text == "Expense") {
            transactionType = 1;
        } else {
            transactionType = 2;
        }
    }

    /**
     * Method to convert transactionType.
     */
    private void convertTransactionMethod() {
        String text = dropdownTransactionMethod.getSelectedItem().toString();
        if (text == "Cash") {
            transactionMethod = 1;
        } else {
            transactionMethod = 2;
        }
    }

    private void convertRecurringTransaction() {
        if (recurringSwitch.isChecked()) {
            String text = dropdownRecurringTransaction.getSelectedItem().toString();
            switch (text) {
                case "Daily":
                    transactionRecurringIntervalType = 1;
                    break;
                case "Weekly":
                    transactionRecurringIntervalType = 2;
                    break;
                case "Monthly":
                    transactionRecurringIntervalType = 3;
                    break;
                case "Custom":
                    transactionRecurringIntervalType = 4;
                    break;
                default:
                    transactionRecurringIntervalType = 0;
            }
        } else {
            transactionRecurringIntervalType = 0;
        }
    }

    /**
     * Submit method to submit the input from user.
     */
    private void submitForm() {
        if (!checkTransactionAmount()) {
            return;
        }
        if (!checkTransactionDate()) {
            return;
        }
        if (!checkRecurringConditions()) {
            return;
        }
        convertTransactionType();
        convertTransactionMethod();
        convertRecurringTransaction();

        String transactionCategory = dropdownTransactionCategory.getSelectedItem().toString();
        Category selectedCategory = categoryDao.getSingleCategory(transactionCategory);
        int categoryId = selectedCategory.getId();

        int transactionAmount = Integer.parseInt(tiedtTransactionAmount.getText().toString());
        String transactionNote  = tiedtTransactionNote.getText().toString().trim() + "";
        Transaction newTransaction = new Transaction((float) transactionAmount,
                transactionType, Integer.toString(categoryId), DateUtils.getStartOfDayInMS(transactionDate), transactionNote, transactionMethod);

        if (transactionRecurringIntervalType != 0) {
            newTransaction.setRecurring(true);
            newTransaction.setRecurringIntervalType(transactionRecurringIntervalType);

            if (transactionRecurringIntervalType == 4) {
                int customRecurringInterval = Integer.parseInt(customRecurringDaysInterval.getText().toString().trim());

                if (customRecurringInterval != 0) {
                    newTransaction.setRecurringIntervalDays(customRecurringInterval);
                } else {
                    newTransaction.setRecurringIntervalDays(1);
                }
            }

            newTransaction.setFlagIconRecurring(true);
        } else {
            newTransaction.setRecurring(false);
            newTransaction.setRecurringIntervalType(0);
            newTransaction.setFlagIconRecurring(false);
        }

        //Insert new Category in to the database
        transactionViewModel.insert(newTransaction);

        if (newTransaction.isRecurring()) {
            processAddRecurringTransaction(newTransaction);
        }

        Fragment fragment = new TransactionFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        String total = "Added new transaction";
        Toast.makeText(getContext(), total, Toast.LENGTH_LONG).show();
    }

    private void processAddRecurringTransaction(final Transaction transaction) {
        Transaction initialTransaction = new Transaction();

        Integer recurringIntervalType = transaction.getRecurringIntervalType();
        Integer recurringIntervalCustomDays = transaction.getRecurringIntervalDays();

        Calendar originalDate = DateUtils.getStartOfDay(transaction.getDate());
        Calendar today = DateUtils.getStartOfCurrentDay();
        int counter = 0;

        initialTransaction.setAmount(transaction.getAmount());
        initialTransaction.setCategory(transaction.getCategory());
        initialTransaction.setMethod(transaction.getMethod());
        initialTransaction.setType(transaction.getType());
        initialTransaction.setTextNote(transaction.getTextNote());
        initialTransaction.setFlagIconRecurring(transaction.getFlagIconRecurring());
        initialTransaction.setRecurring(transaction.isRecurring());
        initialTransaction.setRecurringIntervalType(transaction.getRecurringIntervalType());
        initialTransaction.setRecurringIntervalDays(transaction.getRecurringIntervalDays());
        initialTransaction.setId(transaction.getId());
        initialTransaction.setDate(transaction.getDate());

        transaction.setRecurringIntervalType(0);
        transaction.setRecurringIntervalDays(0);
        transaction.setRecurring(false);

        transactionViewModel.updateTransactionRecurringFields(
                transaction.getId(),
                transaction.isRecurring(),
                transaction.getRecurringIntervalType(),
                transaction.getRecurringIntervalDays()
        );

        if (recurringIntervalType != null) {
            while (originalDate.getTimeInMillis() <= today.getTimeInMillis()) {
                Transaction newTransaction = new Transaction();

                newTransaction.setAmount(initialTransaction.getAmount());
                newTransaction.setCategory(initialTransaction.getCategory());
                newTransaction.setMethod(initialTransaction.getMethod());
                newTransaction.setType(initialTransaction.getType());
                newTransaction.setTextNote(initialTransaction.getTextNote());
                newTransaction.setFlagIconRecurring(true);
                newTransaction.setRecurring(initialTransaction.isRecurring());
                newTransaction.setRecurringIntervalType(initialTransaction.getRecurringIntervalType());
                newTransaction.setRecurringIntervalDays(initialTransaction.getRecurringIntervalDays());

                switch (recurringIntervalType) {
                    case 1:
                        originalDate.add(Calendar.DAY_OF_YEAR, 1);
                        break;
                    case 2:
                        originalDate.add(Calendar.WEEK_OF_YEAR, 1);
                        break;
                    case 3:
                        originalDate.add(Calendar.MONTH, 1);
                        break;
                    case 4:
                        if (recurringIntervalCustomDays != null) {
                            originalDate.add(Calendar.DAY_OF_YEAR, recurringIntervalCustomDays);
                        } else {
                            originalDate.add(Calendar.DAY_OF_YEAR, 1);
                        }
                        break;
                    default:
                        originalDate.add(Calendar.DAY_OF_YEAR, 1);
                }

                if (originalDate.getTimeInMillis() <= today.getTimeInMillis()) {
                    newTransaction.setDate(DateUtils.getStartOfDay(originalDate.getTimeInMillis()).getTimeInMillis());

                    transactionViewModel.insert(newTransaction);

                    initialTransaction.setRecurringIntervalType(0);
                    initialTransaction.setRecurringIntervalDays(0);
                    initialTransaction.setRecurring(false);

                    transactionViewModel.updateTransactionRecurringFields(
                            initialTransaction.getId(),
                            initialTransaction.isRecurring(),
                            initialTransaction.getRecurringIntervalType(),
                            initialTransaction.getRecurringIntervalDays()
                    );

                    initialTransaction = newTransaction;

                    counter++;
                }
            }
        }

        Log.v("RECURRING", "TRANSACTIONS ADDED: " + (counter + 1));
    }
}


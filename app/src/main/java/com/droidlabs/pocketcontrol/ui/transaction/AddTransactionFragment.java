package com.droidlabs.pocketcontrol.ui.transaction;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.budget.Budget;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.ui.budget.BudgetViewModel;
import com.droidlabs.pocketcontrol.ui.categories.CategoryViewModel;
import com.droidlabs.pocketcontrol.utils.DateUtils;
import com.droidlabs.pocketcontrol.utils.FormatterUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class AddTransactionFragment extends Fragment {
    private TextInputEditText tiedtTransactionAmount, tiedtTransactionNote;
    private TextInputEditText customRecurringDaysInterval;
    private TextInputLayout tilTransactionAmount, tilTransactionNote, tilCustomRecurringDaysInterval, tilCategory;
    private boolean isTransactionRecurring;
    private int transactionType;
    private int transactionMethod;
    private int transactionRecurringIntervalType;
    private TextInputEditText dropdownTransactionType;
    private TextInputEditText dropdownTransactionMethod;
    private TextInputEditText dropdownTransactionCategory;
    private TextInputEditText dropdownRecurringTransaction;
    private TextInputEditText editText;
    private Long transactionDate;
    private TransactionViewModel transactionViewModel;
    private CategoryViewModel categoryViewModel;
    private Switch recurringSwitch;
    private LinearLayout recurringTransactionWrapper;
    private LinearLayout customDaysIntervalWrapper;
    private Transaction lastAddedTransaction;

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.transaction_add, container, false);
        tiedtTransactionAmount = view.findViewById(R.id.tiedt_transactionAmount);
        tiedtTransactionNote = view.findViewById(R.id.tiedt_transactionNote);
        tilTransactionAmount = view.findViewById(R.id.til_transactionAmount);
        tilTransactionNote = view.findViewById(R.id.til_transactionNote);
        tilCategory = view.findViewById(R.id.tilCategory);
        tilCustomRecurringDaysInterval = view.findViewById(R.id.til_customRecurringDaysInterval);
        recurringSwitch = view.findViewById(R.id.recurringSwitch);
        recurringTransactionWrapper = view.findViewById(R.id.recurringTransactionWrapper);
        customDaysIntervalWrapper = view.findViewById(R.id.customDaysIntervalWrapper);
        customRecurringDaysInterval = view.findViewById(R.id.customRecurringDaysInterval);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

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
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
                    recurringTransactionWrapper.setVisibility(View.VISIBLE);
                } else {
                    recurringTransactionWrapper.setVisibility(View.GONE);
                }
            }
        });

        tiedtTransactionAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (!hasFocus) {
                    Editable editableContent = tiedtTransactionAmount.getText();

                    if (editableContent != null) {
                        try {
                            float value = Float.parseFloat(editableContent.toString());
                            tiedtTransactionAmount.setText(FormatterUtils.roundToTwoDecimals(value));
                        } catch (Exception e) { }
                    }
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

        dropdownTransactionType = view.findViewById(R.id.spinnerTransactionType);
        String[] dropdownItems = new String[]{"Expense", "Income"};
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
            .setTitle("Select the transaction type")
            .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    dropdownTransactionType.setText(dropdownItems[which]);

                }
            });

        dropdownTransactionType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        dropdownTransactionType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
        dropdownTransactionType.setInputType(0);
    }

    /**
     * This method to set the spinner of Transaction Method.
     * @param view the transaction add layout
     */
    private void setTransactionMethodSpinner(final View view) {

        dropdownTransactionMethod = view.findViewById(R.id.spinnerTransactionMethod);
        String[] dropdownItems = new String[]{"Cash", "Card"};

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the payment method")
                .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dropdownTransactionMethod.setText(dropdownItems[which]);

                    }
                });

        dropdownTransactionMethod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        dropdownTransactionMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
        dropdownTransactionMethod.setInputType(0);
    }

    /**
     * This method to set the spinner of Transaction Category.
     * @param view the transaction add layout
     */
    private void setTransactionCategorySpinner(final View view) {

        dropdownTransactionCategory = view.findViewById(R.id.spinnerTransactionCategory);
        String[] dropdownItems = categoryViewModel.getCategoriesName();

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the transaction category")
                .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dropdownTransactionCategory.setText(dropdownItems[which]);
                    }
                });

        dropdownTransactionCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        dropdownTransactionCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
        dropdownTransactionCategory.setInputType(0);
    }

    /**
     * This method to set the spinner of Transaction Recurring.
     * @param view the transaction add layout
     */
    private void setRecurringTransactionsSpinner(final View view) {

        dropdownRecurringTransaction = view.findViewById(R.id.spinnerRecurringInterval);
        String[] dropdownItems = {"Daily", "Weekly", "Monthly", "Custom"};

        //set the spinners adapter to the previously created one.
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the transaction category")
                .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        if (dropdownItems[which].equals("Custom")) {
                            customDaysIntervalWrapper.setVisibility(View.VISIBLE);
                        } else {
                            customDaysIntervalWrapper.setVisibility(View.GONE);
                        }

                        dropdownRecurringTransaction.setText(dropdownItems[which]);
                    }
                });

        dropdownRecurringTransaction.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        dropdownRecurringTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
        dropdownRecurringTransaction.setInputType(0);
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

        if (Float.parseFloat(tiedtTransactionAmount.getText().toString().trim()) <= 0) {
            tilTransactionAmount.setError("Amount should be larger than 0");
            requestFocus(tiedtTransactionAmount);
            return false;
        }
        return true;
    }

    /**
     * Method to check the input of transaction amount is Acceptable or not.
     * @return Boolean if the input is Acceptable or not
     */
    private boolean checkTransactionAmountIsAcceptable() {
        float amount = Float.valueOf(tiedtTransactionAmount.getText().toString());
        boolean isInfinite = Float.isInfinite(amount);
        double amountLimited = 999999999;
        if (isInfinite) {
            tilTransactionAmount.setError("The amount of transaction is infinity please enter different amount");
            requestFocus(tiedtTransactionAmount);
            return false;
        }
        if (amount > amountLimited) {
            tilTransactionAmount.setError("The amount of transaction need to be between 0 - 999999999");
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

    /**
     * Method to check the input of transaction Date.
     * @return Boolean if the input is qualify or not
     */
    private boolean checkTransactionCategory() {
        String[] dropdownItems = categoryViewModel.getCategoriesName();
        if (!Arrays.asList(dropdownItems).contains(dropdownTransactionCategory.getText().toString().trim())) {
            tilCategory.setError("Category is not valid");
            requestFocus(dropdownTransactionCategory);
            return false;
        }
        return true;
    }

    /**
     * Check if recurring conditions are met.
     * @return validation result.
     */
    private boolean checkRecurringConditions() {
        if (dropdownRecurringTransaction.getText().toString().equals("Custom")) {
            if (customRecurringDaysInterval.getText().toString().equals("")) {
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
        String text = dropdownTransactionType.getText().toString();
        if (text.equals("Income")) {
            transactionType = 2;
        } else {
            transactionType = 1;
        }
    }

    /**
     * Method to convert transactionType.
     */
    private void convertTransactionMethod() {
        String text = dropdownTransactionMethod.getText().toString();
        if (text.equals("Cash")) {
            transactionMethod = 1;
        } else {
            transactionMethod = 2;
        }
    }

    /**
     * Parser for recurring transaction type.
     */
    private void convertRecurringTransaction() {
        if (recurringSwitch.isChecked()) {
            String text = dropdownRecurringTransaction.getText().toString();
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
        if (!checkTransactionAmountIsAcceptable()) {
            return;
        }
        if (!checkTransactionDate()) {
            return;
        }
        if (!checkRecurringConditions()) {
            return;
        }
        if (!checkTransactionCategory()) {
            return;
        }
        convertTransactionType();
        convertTransactionMethod();
        convertRecurringTransaction();

        String transactionCategory = dropdownTransactionCategory.getText().toString();
        Category selectedCategory = categoryViewModel.getSingleCategory(transactionCategory);
        int categoryId = selectedCategory.getId();

        float transactionAmount = Float.parseFloat(tiedtTransactionAmount.getText().toString());
        String transactionNote  = tiedtTransactionNote.getText().toString().trim() + "";
        Transaction newTransaction = new Transaction((float) transactionAmount,
                transactionType,
                Integer.toString(categoryId),
                DateUtils.getStartOfDayInMS(transactionDate),
                transactionNote,
                transactionMethod
        );

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
        long newTransactionId = transactionViewModel.insert(newTransaction);

        Transaction insertedTransaction = transactionViewModel.getTransactionById(newTransactionId);

        if (insertedTransaction.isRecurring()) {
            lastAddedTransaction = processAddRecurringTransaction(insertedTransaction);
        }

        Fragment fragment = new TransactionFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        // Show notification after checking budget
        checkBudget(transactionCategory, categoryId);

        String total = "Added new transaction";
        Toast.makeText(getContext(), total, Toast.LENGTH_LONG).show();
    }
    /**
     * This method to check the budget of the selected Transaction Category.
     * @param transactionCategory the transaction category
     * @param categoryId the transaction category id
     */
    private void checkBudget(final String transactionCategory, final int categoryId) {

        String message = "";
        BudgetViewModel budgetViewModel;
        String sCatId = String.valueOf(categoryId);
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        Budget budget = budgetViewModel.getBudgetForCategory(sCatId);

        if (budget != null) {
            Float budgetAmount = budget.getMaxAmount();
            Float totalAmount = transactionViewModel.getTotalIAmountByCategoryId(sCatId);

            if (budgetAmount - totalAmount <= 50) {
                message = "Low Budget: Monitor your expenses for " +  transactionCategory;
            }
            if (budgetAmount - totalAmount == 0) {
                message = "Budget limit is full for " + transactionCategory;
            }
            if (budgetAmount - totalAmount < 0) {
                message = "Budget limit exceeded for " + transactionCategory;
            }
        }

        if (!message.isEmpty()) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    getContext()
            )
                    .setSmallIcon(R.drawable.ic_message)
                    .setContentTitle("New Notification")
                    .setContentText(message)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
            notificationManager.notify(001, builder.build());
        }

    }

    /**
     * Process and create recurring transactions.
     * @param transaction initial transaction.
     * @return last transaction.
     */
    private Transaction processAddRecurringTransaction(final Transaction transaction) {
        Transaction copyTransaction = new Transaction();

        Integer recurringIntervalType = transaction.getRecurringIntervalType();
        Integer recurringIntervalCustomDays = transaction.getRecurringIntervalDays();

        Calendar recurringDate = DateUtils.getStartOfDay(transaction.getDate());
        Calendar today = DateUtils.getStartOfCurrentDay();
        int counter = 0;

        copyTransaction.setAmount(transaction.getAmount());
        copyTransaction.setCategory(transaction.getCategory());
        copyTransaction.setMethod(transaction.getMethod());
        copyTransaction.setType(transaction.getType());
        copyTransaction.setTextNote(transaction.getTextNote());
        copyTransaction.setFlagIconRecurring(transaction.getFlagIconRecurring());
        copyTransaction.setRecurring(transaction.isRecurring());
        copyTransaction.setRecurringIntervalType(transaction.getRecurringIntervalType());
        copyTransaction.setRecurringIntervalDays(transaction.getRecurringIntervalDays());
        copyTransaction.setId(transaction.getId());
        copyTransaction.setDate(transaction.getDate());

        int whatToAdd;
        int howMuchToAdd;

        if (recurringIntervalType != null) {

            switch (recurringIntervalType) {
                case 1:
                    whatToAdd = Calendar.DAY_OF_YEAR;
                    howMuchToAdd = 1;
                    break;
                case 2:
                    whatToAdd = Calendar.WEEK_OF_YEAR;
                    howMuchToAdd = 1;
                    break;
                case 3:
                    whatToAdd = Calendar.MONTH;
                    howMuchToAdd = 1;
                    break;
                case 4:
                    whatToAdd = Calendar.DAY_OF_YEAR;
                    if (recurringIntervalCustomDays != null) {
                        howMuchToAdd = recurringIntervalCustomDays;
                    } else {
                        howMuchToAdd = 1;
                    }
                    break;
                default:
                    whatToAdd = Calendar.DAY_OF_YEAR;
                    howMuchToAdd = 1;
            }

            do {
                recurringDate.add(whatToAdd, howMuchToAdd);

                if (recurringDate.getTimeInMillis() <= today.getTimeInMillis()) {
                    // add a new transaction
                    // save currentTransaction as non-recurring.

                    copyTransaction.setRecurring(false);
                    copyTransaction.setRecurringIntervalType(0);
                    copyTransaction.setRecurringIntervalDays(0);

                    transactionViewModel.updateTransactionRecurringFields(
                            copyTransaction.getId(),
                            copyTransaction.isRecurring(),
                            copyTransaction.getRecurringIntervalType(),
                            copyTransaction.getRecurringIntervalDays()
                    );

                    Transaction newTransaction = new Transaction();

                    newTransaction.setAmount(copyTransaction.getAmount());
                    newTransaction.setCategory(copyTransaction.getCategory());
                    newTransaction.setMethod(copyTransaction.getMethod());
                    newTransaction.setType(copyTransaction.getType());
                    newTransaction.setTextNote(copyTransaction.getTextNote());
                    newTransaction.setFlagIconRecurring(true);
                    newTransaction.setRecurring(true);
                    newTransaction.setRecurringIntervalType(recurringIntervalType);
                    newTransaction.setRecurringIntervalDays(recurringIntervalCustomDays);

                    newTransaction.setDate(DateUtils.getStartOfDay(recurringDate.getTimeInMillis()).getTimeInMillis());

                    long newTransactionId = transactionViewModel.insert(newTransaction);

                    copyTransaction = transactionViewModel.getTransactionById(newTransactionId);

                    counter++;
                }

            } while (recurringDate.getTimeInMillis() <= today.getTimeInMillis());
        }

        Log.v("RECURRING", "TRANSACTIONS ADDED: " + (counter + 1));

        return copyTransaction;
    }
}


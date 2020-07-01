package com.droidlabs.pocketcontrol.ui.settings;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.droidlabs.pocketcontrol.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.account.Account;
import com.droidlabs.pocketcontrol.db.budget.Budget;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.db.currency.CurrencyDao;

import com.droidlabs.pocketcontrol.db.defaults.Defaults;
import com.droidlabs.pocketcontrol.db.paymentmode.PaymentModeDao;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.ui.budget.BudgetViewModel;
import com.droidlabs.pocketcontrol.ui.home.AccountViewModel;
import com.droidlabs.pocketcontrol.ui.transaction.TransactionViewModel;
import com.droidlabs.pocketcontrol.ui.categories.CategoryViewModel;
import com.droidlabs.pocketcontrol.utils.FormatterUtils;
import com.droidlabs.pocketcontrol.utils.NetworkUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsFragment extends Fragment {

    private static final String CATEGORY_DEFAULT_NAME = "Category";
    private static final String PAYMENT_METHOD_DEFAULT_NAME = "Payment Mode";
    private static final String CURRENCY_DEFAULT_NAME = "Currency";

    private TextInputEditText defaultCategory;
    private TextInputEditText defaultCurrency;
    private TextInputEditText defaultPayment;
    private LinearLayout networkStatusWrapper;

    private AccountViewModel accountViewModel;
    private BudgetViewModel budgetViewModel;
    private CategoryViewModel categoryViewModel;
    private DefaultsViewModel defaultsViewModel;
    private TransactionViewModel transactionViewModel;

    private CurrencyDao currencyDao;
    private PaymentModeDao paymentModeDao;
    private RequestQueue requestQueue;
    private Float exchangeRate;
    private Activity activity;

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View v = inf.inflate(R.layout.fragment_settings, container, false);
        Button exportButton = (Button) v.findViewById(R.id.button);

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                exportDatabase();
            }
        });

        requestQueue = Volley.newRequestQueue(getContext());
        activity = getActivity();

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        defaultsViewModel = new ViewModelProvider(this).get(DefaultsViewModel.class);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);

        currencyDao = PocketControlDB.getDatabase(getContext()).currencyDao();
        paymentModeDao = PocketControlDB.getDatabase(getContext()).paymentModeDao();

        defaultCategory = v.findViewById(R.id.defaultCategorySpinner);
        defaultCurrency = v.findViewById(R.id.defaultCurrencySpinner);
        defaultPayment = v.findViewById(R.id.defaultPaymentSpinner);
        networkStatusWrapper = v.findViewById(R.id.networkStatusWrapper);

        NetworkUtils
                .getConnectivityManager(getContext())
                .registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {

            @Override
            public void onAvailable(final @NonNull Network network) {
                super.onAvailable(network);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        defaultCurrency.setEnabled(true);
                        networkStatusWrapper.setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public void onLost(final @NonNull Network network) {
                super.onLost(network);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        defaultCurrency.setEnabled(false);
                        networkStatusWrapper.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onLosing(final @NonNull Network network, final int maxMsToLive) {
                super.onLosing(network, maxMsToLive);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        defaultCurrency.setEnabled(false);
                        networkStatusWrapper.setVisibility(View.VISIBLE);
                    }
                });
            }
        });


        if (!NetworkUtils.isNetworkConnectionAvailable(getContext())) {
            defaultCurrency.setEnabled(false);
            networkStatusWrapper.setVisibility(View.VISIBLE);
        }

        setDefaultCategorySpinner();
        setDefaultCurrencySpinner();
        setDefaultPaymentModeSpinner();
        AppCompatButton sendEmailButton = v.findViewById(R.id.send_email_button);
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v)  {
                Fragment fragment = new SendEmailFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return v;
    }
    /**
     * Creating adapter for Settings.
     */

    private void exportDatabase() {
        final int bufferSize = 2048;
        int length;

        //defining the data
        StringBuilder accountData = new StringBuilder();
        StringBuilder budgetData = new StringBuilder();
        StringBuilder categoryData = new StringBuilder();
        StringBuilder defaultsData = new StringBuilder();
        StringBuilder transactionData = new StringBuilder();

        accountData.append("id, "
                + "name, "
                + "color"
                + "\n"
        );

        budgetData.append("id, "
                + "max_amount, "
                + "description, "
                + "is_global, "
                + "category, "
                + "account"
                + "\n"
        );

        categoryData.append("id, "
                + "name, "
                + "icon, "
                + "is_public, "
                + "account"
                + "\n"
        );

        defaultsData.append("id, "
                + "default_entity, "
                + "default_value"
                + "\n"
        );

        transactionData.append("id, "
                + "amount, "
                + "date, "
                + "text_note, "
                + "category, "
                + "method, "
                + "type, "
                + "is_recurring, "
                + "recurring_interval_type, "
                + "recurring_interval_days, "
                + "flag_icon_recurring, "
                + "friend, "
                + "method_for_friend, "
                + "account"
                + "\n"
        );

        List<Account> accounts = accountViewModel.getAccountsForExport();
        List<Budget> budgets = budgetViewModel.getBudgetsForExport();
        List<Category> categories = categoryViewModel.getCategoriesForExport();
        List<Defaults> defaults = defaultsViewModel.getDefaultsForExport();
        List<Transaction> transactions = transactionViewModel.getTransactionsForExport();

        if (
                accounts.size() == 0
                && budgets.size() == 0
                && categories.size() == 0
                && defaults.size() == 0
                && transactions.size() == 0
        ) {
            Toast.makeText(getContext(), "Looks like you have no data to export!", Toast.LENGTH_LONG).show();
            return;
        }

        for (Account account : accounts) {
            accountData.append(account.toExportString() + "\n");
        }

        for (Budget budget : budgets) {
            budgetData.append(budget.toExportString() + "\n");
        }

        for (Category category: categories) {
            categoryData.append(category.toExportString() + "\n");
        }

        for (Defaults default1 : defaults) {
            defaultsData.append(default1.toExportString() + "\n");
        }

        for (Transaction transaction : transactions) {
            transactionData.append(transaction.toExportString() + "\n");
        }

        Log.v("EXPORT", transactionData.toString());

        try {
            //saving the file into device
            FileOutputStream accountOut = getContext().openFileOutput("accounts.csv", Context.MODE_PRIVATE);
            FileOutputStream budgetOut = getContext().openFileOutput("budgets.csv", Context.MODE_PRIVATE);
            FileOutputStream categoryOut = getContext().openFileOutput("categories.csv", Context.MODE_PRIVATE);
            FileOutputStream defaultsOut = getContext().openFileOutput("defaults.csv", Context.MODE_PRIVATE);
            FileOutputStream transactionOut = getContext().openFileOutput("transactions.csv", Context.MODE_PRIVATE);
            FileOutputStream pocketControlData = getContext().openFileOutput(
                    "pocketcontrol_data.zip",
                    Context.MODE_PRIVATE
            );

            accountOut.write((accountData.toString()).getBytes(Charset.forName("UTF-8")));
            budgetOut.write((budgetData.toString()).getBytes(Charset.forName("UTF-8")));
            categoryOut.write((categoryData.toString()).getBytes(Charset.forName("UTF-8")));
            defaultsOut.write((defaultsData.toString()).getBytes(Charset.forName("UTF-8")));
            transactionOut.write((transactionData.toString()).getBytes(Charset.forName("UTF-8")));

            accountOut.close();
            budgetOut.close();
            categoryOut.close();
            defaultsOut.close();
            transactionOut.close();

            ZipOutputStream zipOutputStream = new ZipOutputStream(pocketControlData);

            File accountsFile = new File(getContext().getFilesDir(), "accounts.csv");
            File budgetsFile = new File(getContext().getFilesDir(), "budgets.csv");
            File categoriesFile = new File(getContext().getFilesDir(), "categories.csv");
            File defaultsFile = new File(getContext().getFilesDir(), "defaults.csv");
            File transactionsFile = new File(getContext().getFilesDir(), "transactions.csv");

            byte[] buffer = new byte[bufferSize];

            zipOutputStream.putNextEntry(new ZipEntry(accountsFile.getName()));
            FileInputStream accountsFIS = new FileInputStream(accountsFile);
            while ((length = accountsFIS.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, length);
            }
            zipOutputStream.closeEntry();

            zipOutputStream.putNextEntry(new ZipEntry(budgetsFile.getName()));
            FileInputStream budgetsFIS = new FileInputStream(budgetsFile);
            while ((length = budgetsFIS.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, length);
            }
            zipOutputStream.closeEntry();

            zipOutputStream.putNextEntry(new ZipEntry(categoriesFile.getName()));
            FileInputStream categoriesFIS = new FileInputStream(categoriesFile);
            while ((length = categoriesFIS.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, length);
            }
            zipOutputStream.closeEntry();

            zipOutputStream.putNextEntry(new ZipEntry(defaultsFile.getName()));
            FileInputStream defaultsFIS = new FileInputStream(defaultsFile);
            while ((length = defaultsFIS.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, length);
            }
            zipOutputStream.closeEntry();

            zipOutputStream.putNextEntry(new ZipEntry(transactionsFile.getName()));
            FileInputStream transactionsFIS = new FileInputStream(transactionsFile);
            while ((length = transactionsFIS.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, length);
            }
            zipOutputStream.closeEntry();

            zipOutputStream.close();
            pocketControlData.close();

            //exporting
            Context context = getContext().getApplicationContext();
            File pocketControlFile = new File(getContext().getFilesDir(), "pocketcontrol_data.zip");
            Uri path = FileProvider.getUriForFile(
                    context,
                    "com.droidlabs.pocketcontrol.fileprovider",
                    pocketControlFile
            );
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("application/zip");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "PocketControl Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Export data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method to set the default category.
     */
    private void setDefaultCategorySpinner() {
        String[] dropdownItems = categoryViewModel.getCategoriesName();

        //set payment mode spinner value
        String stringCategory = defaultsViewModel.getDefaultValue(CATEGORY_DEFAULT_NAME);
        defaultCategory.setText(stringCategory);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the transaction category")
                .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        defaultsViewModel.updateDefaultValue(CATEGORY_DEFAULT_NAME, dropdownItems[which]);
                        defaultCategory.setText(dropdownItems[which]);
                    }
                });

        defaultCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        defaultCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
        defaultCategory.setInputType(0);
    }

    /**
     * This method to set the default currency.
     */
    private void setDefaultCurrencySpinner() {
        String[] dropdownItems = currencyDao.getAllCurrencyCodes();

        //set currency spinner value

        String stringCurrency = defaultsViewModel.getDefaultValue(CURRENCY_DEFAULT_NAME);
        defaultCurrency.setText(stringCurrency);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the transaction currency")
                .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        updateDefaultCurrency(dropdownItems[which]);
                    }
                });

        defaultCurrency.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        defaultCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
        defaultCurrency.setInputType(0);
    }

    /**
     * This method to set the default payment mode.
     */
    private void setDefaultPaymentModeSpinner() {
        String[] dropdownItems = paymentModeDao.getAllPaymentModeNames();

        //set payment mode spinner value
        String stringPayment = defaultsViewModel.getDefaultValue(PAYMENT_METHOD_DEFAULT_NAME);
        defaultPayment.setText(stringPayment);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the payment mode")
                .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        defaultsViewModel.updateDefaultValue(PAYMENT_METHOD_DEFAULT_NAME, dropdownItems[which]);
                        defaultPayment.setText(dropdownItems[which]);
                    }
                });

        defaultPayment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        defaultPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
        defaultPayment.setInputType(0);
    }

    /**
     * Method to update default currency.
     * @param newCurrency new currency.
     */
    private void updateDefaultCurrency(final String newCurrency) {
        if (!NetworkUtils.isNetworkConnectionAvailable(getContext())) {
            Toast.makeText(
                    getContext(),
                    "No network connectivity, can't change the default currency!",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        requestExchangeRate(newCurrency);
    }

    /**
     * Exchange rate conversion.
     * @param targetCurrency target currency.
     */
    private void requestExchangeRate(final String targetCurrency) {
        String originalCurrency = defaultsViewModel.getDefaultValue(CURRENCY_DEFAULT_NAME);

        MaterialAlertDialogBuilder exchangeRateDialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Update default currency?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        Toast.makeText(getContext(), "Update of default currency cancelled", Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        if (exchangeRate != null) {
                            defaultCurrency.setText(targetCurrency);
                            defaultsViewModel.updateDefaultValue(CURRENCY_DEFAULT_NAME, targetCurrency);
                            transactionViewModel.updateTransactionAmountsDefaultCurrency(exchangeRate);
                            budgetViewModel.updateBudgetAmountsDefaultCurrency(exchangeRate);
                        }
                    }
                });

        String requestUrl = "https://api.exchangeratesapi.io/latest?base="
                + originalCurrency
                + "&symbols="
                + targetCurrency;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                requestUrl,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            exchangeRate = Float.parseFloat(
                                    response.getJSONObject("rates").get(targetCurrency).toString()
                            );
                            exchangeRateDialogBuilder
                                    .setMessage("Exchange rate: 1 "
                                            + originalCurrency
                                            + " = "
                                            + FormatterUtils.roundToFourDecimals(exchangeRate)
                                            + " "
                                            + targetCurrency
                                    ).show();
                        } catch (final JSONException e) {
                            Toast.makeText(
                                    getContext(),
                                    "Something went wrong, please try again!",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(
                                getContext(),
                                "Something went wrong, please try again!",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });


        requestQueue.add(jsonObjectRequest);
    }
}

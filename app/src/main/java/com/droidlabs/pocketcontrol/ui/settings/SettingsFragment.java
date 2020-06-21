package com.droidlabs.pocketcontrol.ui.settings;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.droidlabs.pocketcontrol.R;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;


import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.currency.CurrencyDao;

import com.droidlabs.pocketcontrol.db.paymentmode.PaymentModeDao;
import com.droidlabs.pocketcontrol.ui.budget.BudgetViewModel;
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

    private CategoryViewModel categoryViewModel;
    private DefaultsViewModel defaultsViewModel;
    private TransactionViewModel transactionViewModel;
    private BudgetViewModel budgetViewModel;

    private CurrencyDao currencyDao;
    private PaymentModeDao paymentModeDao;
    private RequestQueue requestQueue;
    private Float exchangeRate;
    private Activity activity;

    private ConnectivityManager.NetworkCallback networkCallback;


    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View v = inf.inflate(R.layout.fragment_settings, container, false);
        Button button = (Button) v.findViewById(R.id.button);
        // button.setOnClickListener(this::export);

        requestQueue = Volley.newRequestQueue(getContext());
        activity = getActivity();

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

        return v;
    }
    /**
     * Creating adapter for Settings.
     */

    public void export() {
        //defining the data
        StringBuilder transactionData = new StringBuilder();
        transactionData.append("id, amount, text_node, is_recurring, flag_icon_recurring,"
                + "recurring_interval_Type, recurring_interval_days, type, method, date, category, "
                + "account, owner_id");
        Cursor res = (Cursor) transactionViewModel.getTransactions();
        ArrayList<String> listItem = new ArrayList<>();
        if (res.getCount() == 0) {
            Toast.makeText(getContext(), "Looks like you have no data to export", Toast.LENGTH_SHORT).show();
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            transactionData.append(res.getString(0) + "," + res.getString(1)
                    + "," + res.getString(2) + ", " + res.getString(3) + ","
                    + res.getString(4) + res.getString(5)
                    + res.getString(6) + res.getString(7)
                    + res.getString(8) + res.getString(9)
                    + res.getString(10) + res.getString(11)
                    + res.getString(12) + "\n");
        }

        try {
            //saving the file into device
            FileOutputStream out = getContext().openFileOutput("data.csv", Context.MODE_PRIVATE);
            out.write((transactionData.toString()).getBytes(Charset.forName("UTF-8")));
            out.close();

            //exporting
            Context context = getContext().getApplicationContext();
            File filelocation = new File(getContext().getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.droidlabs.pocketcontrol.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Send mail"));
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

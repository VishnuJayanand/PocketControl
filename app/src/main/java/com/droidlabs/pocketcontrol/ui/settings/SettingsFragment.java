package com.droidlabs.pocketcontrol.ui.settings;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.droidlabs.pocketcontrol.R;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;


import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.currency.CurrencyDao;
import com.droidlabs.pocketcontrol.db.defaults.Defaults;
import com.droidlabs.pocketcontrol.db.paymentmode.PaymentModeDao;
import com.droidlabs.pocketcontrol.ui.categories.CategoryViewModel;

public class SettingsFragment extends Fragment {
    private Spinner defaultCategory;
    private Spinner defaultCurrency;
    private Spinner defaultPayment;
    private CategoryViewModel categoryViewModel;
    private DefaultsViewModel defaultsViewModel;
    private CurrencyDao currencyDao;
    private PaymentModeDao paymentModeDao;
    private PocketControlDB db = PocketControlDB.getDatabase(getContext());

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View v = inf.inflate(R.layout.fragment_settings, container, false);
        Button button = (Button) v.findViewById(R.id.button);
        button.setOnClickListener(this::export);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        defaultsViewModel = new ViewModelProvider(this).get(DefaultsViewModel.class);

        currencyDao = PocketControlDB.getDatabase(getContext()).currencyDao();
        paymentModeDao = PocketControlDB.getDatabase(getContext()).paymentModeDao();

        Button saveSettings = v.findViewById(R.id.addDefaults);

        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String stringCategory = defaultCategory.getSelectedItem().toString();
                String stringPaymentMode = defaultPayment.getSelectedItem().toString();
                String stringCurrency = defaultCurrency.getSelectedItem().toString();

                Defaults categoryEntry = new Defaults("Category", stringCategory);
                Defaults paymentModeEntry = new Defaults("Payment Mode", stringPaymentMode);
                Defaults currencyEntry = new Defaults("Currency", stringCurrency);
                /*TODO - change the insert statement'*/
                db.defaultsDao().deleteAll();
                db.defaultsDao().insert(categoryEntry);
                db.defaultsDao().insert(paymentModeEntry);
                db.defaultsDao().insert(currencyEntry);
                Toast.makeText(getContext(), "Settings Saved", Toast.LENGTH_SHORT).show();
            }
        });
        setDefaultCategorySpinner(v);
        setDefaultCurrencySpinner(v);
        setDefaultPaymentModeSpinner(v);
        return v;
    }
    /**
     * Creating adapter for Settings.
     * @param view view
     */

    public void export(final View view) {
        //generate data
        StringBuilder data = new StringBuilder();
        data.append("Time,Distance");
        for (int i = 0; i < 5; i++) {
            data.append("\n" + String.valueOf(i) + "," + String.valueOf(i * i));
        }

        try {
            //saving the file into device
            FileOutputStream out = getContext().openFileOutput("data.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes(Charset.forName("UTF-8")));
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
     * @param view the transaction add layout
     */
    private void setDefaultCategorySpinner(final View view) {
        //get the spinner from the xml.
        defaultCategory = view.findViewById(R.id.defaultCategorySpinner);
        //create a list of items for the spinner.
        String[] dropdownItems = categoryViewModel.getCategoriesName();
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, dropdownItems);

        //set category spinner value
        String defaultCategoryValue = defaultsViewModel.getDefaultValue("Category");
        int selectionPosition = adapterCategory.getPosition(defaultCategoryValue);

        //set the spinners adapter to the previously created one.
        defaultCategory.setAdapter(adapterCategory);
        defaultCategory.setSelection(selectionPosition);
    }

    /**
     * This method to set the default currency.
     * @param view the transaction add layout
     */
    private void setDefaultCurrencySpinner(final View view) {
        //get the spinner from the xml.
        defaultCurrency = view.findViewById(R.id.defaultCurrencySpinner);
        //create a list of items for the spinner.
        String[] dropdownItems = currencyDao.getAllCurrencyCodes();
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, dropdownItems);
        //set the spinners adapter to the previously created one.

        //set currency spinner value
        String defaultCurrencyValue = defaultsViewModel.getDefaultValue("Currency");
        int selectionPosition = adapterCategory.getPosition(defaultCurrencyValue);

        //set the spinners adapter to the previously created one.
        defaultCurrency.setAdapter(adapterCategory);
        defaultCurrency.setSelection(selectionPosition);
    }

    /**
     * This method to set the default payment mode.
     * @param view the transaction add layout
     */
    private void setDefaultPaymentModeSpinner(final View view) {
        //get the spinner from the xml.
        defaultPayment = view.findViewById(R.id.defaultPaymentModeSpinner);
        //create a list of items for the spinner.
        String[] dropdownItems = paymentModeDao.getAllPaymentModeNames();
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, dropdownItems);
        //set the spinners adapter to the previously created one.

        //set payment mode spinner value
        String defaultCurrencyValue = defaultsViewModel.getDefaultValue("Payment Mode");
        int selectionPosition = adapterCategory.getPosition(defaultCurrencyValue);

        //set the spinners adapter to the previously created one.
        defaultPayment.setAdapter(adapterCategory);
        defaultPayment.setSelection(selectionPosition);
    }
}

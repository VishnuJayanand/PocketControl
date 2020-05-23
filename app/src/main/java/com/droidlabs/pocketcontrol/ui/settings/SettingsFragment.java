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

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.droidlabs.pocketcontrol.R;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;


import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.category.CategoryDao;

public class SettingsFragment extends Fragment {
    private Spinner defaultCategory;
    private CategoryDao categoryDao;

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View v = inf.inflate(R.layout.fragment_settings, container, false);
        Button button = (Button) v.findViewById(R.id.button);
        button.setOnClickListener(this::export);

        categoryDao = PocketControlDB.getDatabase(getContext()).categoryDao();
        setDefaultCategorySpinner(v);
        return v;
    }
    /**
     * Creating adapter for Budget.
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
        String[] dropdownItems = categoryDao.getCategoriesName();
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, dropdownItems);
        //set the spinners adapter to the previously created one.
        defaultCategory.setAdapter(adapterCategory);
    }


}

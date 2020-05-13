package com.droidlabs.pocketcontrol.ui.settings;

//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
//import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.droidlabs.pocketcontrol.R;



//import java.io.File;
//import java.io.FileOutputStream;

public class SettingsFragment extends Fragment {
    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        return inf.inflate(R.layout.fragment_settings, container, false);
    }

//    public void export(View view){
//        //generate data
//        StringBuilder data = new StringBuilder();
//        data.append("Time,Distance");
//        for(int i = 0; i<5; i++){
//            data.append("\n"+String.valueOf(i)+","+String.valueOf(i*i));
//        }
//
//        try{
//            //saving the file into device
//            FileOutputStream out = getContext().openFileOutput("data.csv", Context.MODE_PRIVATE);
//            out.write((data.toString()).getBytes());
//            out.close();
//
//            //exporting
//            Context context = getContext().getApplicationContext();
//            File filelocation = new File(getContext().getFilesDir(), "data.csv");
//            Uri path = FileProvider.getUriForFile(context, "com.droidlabs.pocketcontrol.fileprovider", filelocation);
//            Intent fileIntent = new Intent(Intent.ACTION_SEND);
//            fileIntent.setType("text/csv");
//            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
//            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
//            startActivity(Intent.createChooser(fileIntent, "Send mail"));
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//
//
//    }
}

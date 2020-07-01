package com.droidlabs.pocketcontrol.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.droidlabs.pocketcontrol.R;

public class SendEmailFragment extends Fragment {
    private TextView mailTo;
    private EditText mailSubject, mailMessage;
    private Button sendButton;

    /**
     * To send the email, input subject and message to gmail.
     * @param inf LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(
            final @NonNull LayoutInflater inf,
            final  @Nullable ViewGroup container,
            final  @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.setting_send_email, container, false);

        mailTo = view.findViewById(R.id.email_to);
        mailSubject = view.findViewById(R.id.email_subject);
        mailMessage = view.findViewById(R.id.email_message);
        sendButton = view.findViewById(R.id.email_send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("mailto:" + mailTo.getText().toString()));
                intent.putExtra(Intent.EXTRA_SUBJECT, mailSubject.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, mailMessage.getText().toString());
                startActivity(intent);
            }
        });

        return view;
    }
}

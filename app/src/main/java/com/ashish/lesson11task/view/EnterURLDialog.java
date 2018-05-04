package com.ashish.lesson11task.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ashish.lesson11task.DownloadImageIntentService;
import com.ashish.lesson11task.ImageDownloadTask;
import com.ashish.lesson11task.utils.NetworkUtils;
import com.ashish.lesson11task.R;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

public class EnterURLDialog extends AppCompatDialogFragment {

    private EditText urlEditText;
    private EditText nameEditText;

    public interface EnterURLDialogListener extends Serializable {
        void onDialogPositiveClick(AppCompatDialogFragment dialog, String urlString, String nameString);
    }

    EnterURLDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_url, null);
        urlEditText = dialogView.findViewById(R.id.url_edit_text);
        nameEditText = dialogView.findViewById(R.id.name_edit_text);
        mListener = (EnterURLDialogListener) getArguments().get("listener");
        builder.setView(dialogView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogPositiveClick(EnterURLDialog.this, urlEditText.getText().toString(),
                                nameEditText.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EnterURLDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
    
}

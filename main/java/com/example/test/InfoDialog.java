package com.example.test;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

public class InfoDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        int str = getArguments().getInt("str");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder  = builder.setTitle(title).setMessage(str);
        return builder.create();
    }
}
package com.fsu.mobile.storyapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by davides on 10/27/13.
 */
public class CheckRequestSettingsDialog extends DialogFragment {
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            dListener = (CheckRequestSettingsDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    +" must implement CheckRequestSettingsDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.update_settings_dialog,null);

        final EditText update_next_EditText = (EditText)view.findViewById(R.id.update_next);

        //builder.setView(inflater.inflate(R.layout.dialog_layout,null));
        builder.setView(view);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String next_contributor = update_next_EditText.getText().toString();
                dListener.onDialogClick(next_contributor);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        return builder.create();
    }

    public interface CheckRequestSettingsDialogListener {
        public void onDialogClick(String nextContributor);
    }

    CheckRequestSettingsDialogListener dListener;
}
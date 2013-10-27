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
public class CreateSettingsDialog  extends DialogFragment{
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            dListener = (CreateSettingsDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    +" must implement CreateSettingsDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.create_settings_dialog,null);

        final EditText max_words_EditText = (EditText)view.findViewById(R.id.max_words);
        final EditText next_contributor_EditText = (EditText)view.findViewById(R.id.next_contributor);

        //builder.setView(inflater.inflate(R.layout.dialog_layout,null));
        builder.setView(view);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String max_words = max_words_EditText.getText().toString();
                String next_contributor = next_contributor_EditText.getText().toString();
                dListener.onDialogClick(max_words,next_contributor);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        return builder.create();
    }

    public interface CreateSettingsDialogListener {
        public void onDialogClick(String maxWords, String nextContributor);
    }

    CreateSettingsDialogListener dListener;
}


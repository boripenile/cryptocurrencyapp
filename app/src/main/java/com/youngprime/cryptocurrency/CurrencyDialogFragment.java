package com.youngprime.cryptocurrency;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

/**
 * Created by bd_dev_murtala on 21/10/2017.
 */

public class CurrencyDialogFragment extends DialogFragment {

    public interface CurrencyDialogListener {
        public void onCurrencyDialogPositiveClick(DialogFragment dialog);
        public void onCurrencyDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    CurrencyDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.currency_value_layout, null))
                // Add action buttons
                .setPositiveButton(R.string.convert, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onCurrencyDialogPositiveClick(CurrencyDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onCurrencyDialogNegativeClick(CurrencyDialogFragment.this);
                    }
                });
        return builder.create();
    }


    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (CurrencyDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}

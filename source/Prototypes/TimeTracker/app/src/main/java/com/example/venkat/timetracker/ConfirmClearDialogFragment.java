package com.example.venkat.timetracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by venkat on 11/5/2015.
 */
public class ConfirmClearDialogFragment extends DialogFragment {
    private TimeListAdapter mAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.confirm_clear_all_title)
                .setMessage(R.string.confirm_clear_all_message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mAdapter.clear();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }
    public static ConfirmClearDialogFragment newInstance(TimeListAdapter adapter) {
        ConfirmClearDialogFragment frag = new ConfirmClearDialogFragment();
        frag.mAdapter = adapter;
        return frag;
    }
}

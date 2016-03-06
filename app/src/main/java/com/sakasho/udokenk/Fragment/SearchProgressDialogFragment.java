package com.sakasho.udokenk.Fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

/**
 * Created by nappannda on 2016/02/04.
 */
public class SearchProgressDialogFragment extends DialogFragment {

    private ProgressDialog progressDialog;

    public static SearchProgressDialogFragment newInstance(int title, int message) {
        SearchProgressDialogFragment fragment = new SearchProgressDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putInt("message", message);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle safedInstanceState) {
        int title = getArguments().getInt("title");
        int message = getArguments().getInt("message");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(title);
        progressDialog.setMessage(getResources().getText(message));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);

        return progressDialog;
    }
}

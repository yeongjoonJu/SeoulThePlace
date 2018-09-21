package com.ensharp.seoul.seoultheplace.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.ensharp.seoul.seoultheplace.R;

public class DimFragment extends DialogFragment {

    Button button;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.simple_fragment_layout, container, false);

        getDialog().setTitle("DialogFragment Tutorial");

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null) return;

        int dialogWidth = 700;
        int dialogHeight = 600;

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }
}

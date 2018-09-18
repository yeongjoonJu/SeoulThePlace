package com.ensharp.seoul.seoultheplace.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ensharp.seoul.seoultheplace.R;

public class SavesFragment extends Fragment {

    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_saves, container, false);

        Button liked = (Button) rootView.findViewById(R.id.liked_button);
        Button customized = (Button) rootView.findViewById(R.id.customized_button);

        liked.setBackground(getContext().getResources().getDrawable(R.drawable.item_curved_rectangle));
        liked.setTextColor(R.color.saves_unchoiced_tab);

        return rootView;
    }
}
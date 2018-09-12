package com.ensharp.seoul.seoultheplace.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.R;

import org.w3c.dom.Text;

@SuppressLint("ValidFragment")
public class PlaceDetailFragment extends Fragment {

    private String imageSource;
    private String category;
    private String detail;

    @SuppressLint("ValidFragment")
    public PlaceDetailFragment(String imageSource, String category, String detail) {
        this.imageSource = imageSource;
        this.category = category;
        this.detail = detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_place_category, container, false);

        ImageView image = (ImageView) rootView.findViewById(R.id.icon);
        TextView name = (TextView) rootView.findViewById(R.id.catetory);
        TextView content = (TextView) rootView.findViewById(R.id.information);

        int id = getResources().getIdentifier("com.ensharp.seoul.seoultheplace:drawable/" + imageSource, null, null);
        image.setImageResource(id);
        name.setText(category);
        content.setText(detail);

        return rootView;
    }
}

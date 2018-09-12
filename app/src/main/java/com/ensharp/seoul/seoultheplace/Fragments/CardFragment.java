package com.ensharp.seoul.seoultheplace.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.Course.PlaceView.CardAdapter;

import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.R;

public class CardFragment extends Fragment {

    private CardView cardView;
    private ImageButton place;

    public static Fragment getInstance(int position) {
        CardFragment f = new CardFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        f.setArguments(args);

        return f;
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (getArguments().getInt("position") == 0)
            return createIntroduceFragment(inflater, container);
        else
            return createCardFragment(inflater, container);
    }

    public View createIntroduceFragment(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.item_introduce, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView description = (TextView) view.findViewById(R.id.description);

        title.setText(getString(R.string.course_title));
        title.setTextColor(Color.rgb(255,255,255));
        description.setText(getString(R.string.course_description));
        description.setTextColor(Color.rgb(255,255,255));

        return view;
    }

    public View createCardFragment(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.item_viewpager, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView address = (TextView) view.findViewById(R.id.address);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView index = (TextView) view.findViewById(R.id.index);

        title.setText(getString(R.string.title));
        title.setTextColor(Color.rgb(0,0,0));
        address.setText(getString(R.string.address));
        address.setTextColor(Color.rgb(0,0,0));
        description.setText(getString(R.string.description));
        description.setTextColor(Color.rgb(0,0,0));
        index.setTextColor(Color.rgb(255,255,255));
        index.setText(String.format("%d", getArguments().getInt("position")));

        return view;
    }

    public ImageButton getPlace() { return place;}

    public CardView getCardView() {
        return cardView;
    }
}

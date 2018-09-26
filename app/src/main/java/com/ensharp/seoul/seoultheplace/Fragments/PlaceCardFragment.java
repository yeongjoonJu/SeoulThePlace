package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.CardAdapter;
import com.ensharp.seoul.seoultheplace.*;
import com.ensharp.seoul.seoultheplace.UIElement.CustomAnimationDialog;

public class PlaceCardFragment extends Fragment {

    private CardView cardView;
    private FrameLayout placeButton;
    private int position;
    private PlaceVO place;
    private ImageButton heartButton;
    private Drawable unchoicedHeart;
    private Drawable choicedHeart;
    private DAO dao;
    private String useremail;

    public PlaceCardFragment() {
    }

    public void setData(PlaceVO place) {
        this.place = place;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CardView getCardView() {
        return cardView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new DAO();
        SharedPreferences preferences = getContext().getSharedPreferences("data", getContext().MODE_PRIVATE);
        useremail = preferences.getString("email", null);
        choicedHeart = getResources().getDrawable(R.drawable.choiced_heart);
        unchoicedHeart = getResources().getDrawable(R.drawable.unchoiced_heart);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_mainplace, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView address = (TextView) view.findViewById(R.id.address);
        TextView index = (TextView) view.findViewById(R.id.index);
        ImageView image = (ImageView) view.findViewById(R.id.placeImage);

        title.setText(place.getName());
        title.setTextColor(Color.rgb(0,0,0));
        address.setText(place.getLocation());
        address.setTextColor(Color. rgb(0,0,0));

        heartButton = (ImageButton) view.findViewById(R.id.like_button);
        if(place != null) {
            if(dao.checkLikedPlace(place.getCode(), useremail).equals("true"))
                heartButton.setImageDrawable(choicedHeart);
            else
                heartButton.setImageDrawable(unchoicedHeart);
        }

        heartButton.setOnClickListener(onHeartButtonClickListener);

        if(place.getImageURL() != null) {
            PicassoImage.DownLoadImage(place.getImageURL(),image);
        }

        final MainActivity activity = (MainActivity)getActivity();

        placeButton = (FrameLayout) view.findViewById(R.id.place);
        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CustomAnimationDialog customAnimationDialog = new CustomAnimationDialog(activity);
                customAnimationDialog.show();
                activity.changeToPlaceFragment(place.getCode(), PlaceFragment.VIA_SEARCH);
                customAnimationDialog.dismiss();
            }
        });

        return view;
    }

    public ImageButton.OnClickListener onHeartButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (heartButton.getDrawable().equals(choicedHeart))
                heartButton.setImageDrawable(unchoicedHeart);
            else
                heartButton.setImageDrawable(choicedHeart);
            dao.likePlace(place.getCode(), useremail);
        }
    };
}

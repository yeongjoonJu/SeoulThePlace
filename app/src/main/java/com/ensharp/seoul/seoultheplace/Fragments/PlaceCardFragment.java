package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.CardAdapter;
import com.ensharp.seoul.seoultheplace.*;

public class PlaceCardFragment extends Fragment {

    private View view;
    private CardView cardView;
    private LinearLayout placeButton;
    private PlaceVO place;
    private ImageView heartButton;
    private DAO dao;
    private String useremail;
    private boolean isLiked = false;

    public PlaceCardFragment() {
    }

    public String getPlaceName() {
        if(place != null)
            return place.getName();
        return "";
    }

    public void setData(PlaceVO place) {
        this.place = place;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_mainplace, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        placeButton = (LinearLayout) view.findViewById(R.id.place);
//        placeButton.setBackground(getContext().getResources().getDrawable(R.drawable.item_place_card_upper_background));
        placeButton.setOnClickListener(onPlaceButtonClickListener);

//        FrameLayout imageContainer = (FrameLayout) view.findViewById(R.id.image_container);
//        imageContainer.setBackground(getContext().getResources().getDrawable(R.drawable.item_place_card_upper_background));

        //LinearLayout bottomLayout = (LinearLayout) view.findViewById(R.id.text_container);
//        bottomLayout.setBackground(getContext().getResources().getDrawable(R.drawable.item_place_card_bottom_background));

        heartButton = (ImageView) view.findViewById(R.id.like_button);
        heartButton.setOnClickListener(onHeartButtonClickListener);
        setHeart();

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView address = (TextView) view.findViewById(R.id.address);
        ImageView image = (ImageView) view.findViewById(R.id.placeImage);

        title.setText(place.getName());
        address.setText(place.getLocation());
        image.setClipToOutline(true);

        if(place.getImageURL() != null)
            PicassoImage.DownLoadImage(place.getImageURL(),image);

        return view;
    }

    private void setHeart() {
        if (place == null) return;

        if(dao.checkLikedPlace(place.getCode(), useremail).equals("true"))
        {
            heartButton.setImageResource(R.drawable.choiced_heart);
            isLiked = true;
        } else {
            heartButton.setImageResource(R.drawable.unchoiced_heart);
            isLiked = false;
        }
    }

    public void changeToPlaceFragment() {
        final MainActivity activity = (MainActivity)getActivity();
        activity.changeToPlaceFragment(place.getCode(), PlaceFragment.VIA_SEARCH);

    }

    public LinearLayout.OnClickListener onPlaceButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changeToPlaceFragment();
        }
    };

    public ImageView.OnClickListener onHeartButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isLiked) {
                heartButton.setImageResource(R.drawable.unchoiced_heart);
                isLiked = false;
            }
            else {
                heartButton.setImageResource(R.drawable.choiced_heart);
                isLiked = true;
            }
            dao.likePlace(place.getCode(), useremail);
        }
    };
}

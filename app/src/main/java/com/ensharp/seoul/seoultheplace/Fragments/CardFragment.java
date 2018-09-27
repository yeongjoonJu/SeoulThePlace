package com.ensharp.seoul.seoultheplace.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.CardAdapter;
import com.ensharp.seoul.seoultheplace.*;

@SuppressLint("ValidFragment")
public class CardFragment extends Fragment {

    private View view;
    private CourseVO course;
    private int index;
    private PlaceVO place;
    private CardView cardView;
    private ImageButton placeButton;
    private DAO dao;
    private ImageView heartButton;
    private String useremail;
    private boolean isLiked = false;

    @SuppressLint("ValidFragment")
    public CardFragment(CourseVO course, int index, PlaceVO place) {
        this.index = index;
        this.place = place;
        this.course = course;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new DAO();
        SharedPreferences preferences = getContext().getSharedPreferences("data", getContext().MODE_PRIVATE);
        useremail = preferences.getString("email", null);
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (index == 0)
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

        title.setText(course.getName());
        description.setText(course.getDetails());

        return view;
    }

    public View createCardFragment(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.item_viewpager, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        ImageView image = (ImageView) view.findViewById(R.id.placeImage);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView address = (TextView) view.findViewById(R.id.address);
        TextView placeIndex = (TextView) view.findViewById(R.id.index);

        PicassoImage.DownLoadImage(place.getImageURL(),image);
        title.setText(place.getName());
        address.setText(place.getLocation());
        placeIndex.setText(Integer.toString(index));
        setHeart();

        final MainActivity activity = (MainActivity)getActivity();

        FrameLayout placeButton = (FrameLayout) view.findViewById(R.id.place);
        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeToPlaceFragment(course, index, PlaceFragment.VIA_COURSE);
            }
        });

        return view;
    }

    public void setHeart() {
        if(place == null) return;

        heartButton = (ImageView) view.findViewById(R.id.like_button);
        heartButton.setOnClickListener(onHeartButtonClickListener);

        if(dao.checkLikedPlace(place.getCode(), useremail).equals("true"))
        {
            heartButton.setImageResource(R.drawable.choiced_heart);
            isLiked = true;
        }
        else
        {
            heartButton.setImageResource(R.drawable.unchoiced_heart);
            isLiked = false;
        }
    }

    public CardView getCardView() {
        return cardView;
    }

    public ImageView.OnClickListener onHeartButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isLiked) {
                heartButton.setImageResource(R.drawable.unchoiced_heart);
                isLiked = false;
            } else {
                heartButton.setImageResource(R.drawable.choiced_heart);
                isLiked = true;
            }

            dao.likePlace(place.getCode(), useremail);
        }
    };
}

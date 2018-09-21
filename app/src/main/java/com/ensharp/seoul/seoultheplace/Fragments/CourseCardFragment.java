package com.ensharp.seoul.seoultheplace.Fragments;

import android.graphics.drawable.Drawable;
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
import com.ensharp.seoul.seoultheplace.*;
import com.squareup.picasso.Picasso;

public class CourseCardFragment extends Fragment {
    public static final int HEART_BUTTON = 0;
    public static final int COURSE_NAME = 1;
    public static final int COURSE_LOCATION = 2;
    public static final int COURSE_IMAGE = 3;

    private View view;
    private CardView cardView;
    private ImageButton heartButton;
    private CourseVO course;
    private Drawable unchoicedHeart;
    private Drawable choicedHeart;
    private int position;

    private ImageButton image;
    private TextView name;
    private TextView location;

    public CourseCardFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        choicedHeart = getResources().getDrawable(R.drawable.choiced_heart);
        unchoicedHeart = getResources().getDrawable(R.drawable.unchoiced_heart);
    }

    public void setData(CourseVO course) {
        this.course = course;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CardView getCardView() {
        return cardView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_course, container, false);

        cardView = (CardView) view.findViewById(R.id.courseView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        image = (ImageButton) view.findViewById(R.id.course_image);
        name = (TextView) view.findViewById(R.id.course_name);
        location = (TextView) view.findViewById(R.id.course_location);
        heartButton = (ImageButton) view.findViewById(R.id.like_button);

        setElements();

        heartButton.setOnClickListener(onHeartButtonClickListener);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MainActivity activity = (MainActivity)getActivity();
                activity.changeToCourseFragment(course);
            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MainActivity activity = (MainActivity)getActivity();
                activity.changeToCourseFragment(course);
            }
        });

        return view;
    }

    public void setElements() {
        DAO dao = new DAO();
        PlaceVO firstPlace = dao.getPlaceData(course.getPlaceCode(0));
        String imageURL = firstPlace.getImageURL()[0];
        Picasso.get().load(imageURL).into(image);
        name.setText(course.getName());
        location.setText(firstPlace.getLocation());
    }

    public ImageButton.OnClickListener onHeartButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (heartButton.getDrawable().equals(choicedHeart))
                heartButton.setImageDrawable(unchoicedHeart);
            else
                heartButton.setImageDrawable(choicedHeart);
        }
    };
}
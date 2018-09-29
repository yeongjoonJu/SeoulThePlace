package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.CardAdapter;
import com.ensharp.seoul.seoultheplace.*;

public class CourseCardFragment extends Fragment {
    public static final int HEART_BUTTON = 0;
    public static final int COURSE_NAME = 1;
    public static final int COURSE_LOCATION = 2;
    public static final int COURSE_IMAGE = 3;

    private CardView cardView;
    private ImageView heartButton;
    private CourseVO course;
    private int position;
    private DAO dao;
    private String useremail;
    private boolean isLiked = false;

    private ImageView image;
    private TextView name;
    private TextView location;

    public CourseCardFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new DAO();
        SharedPreferences preferences = getContext().getSharedPreferences("data", getContext().MODE_PRIVATE);
        useremail = preferences.getString("email", null);
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
        View view = inflater.inflate(R.layout.item_course, container, false);

        cardView = (CardView) view.findViewById(R.id.courseView);
//        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);
        cardView.setOnClickListener(onCardViewClickListener);
        cardView.setCardElevation(0);
        CardView imageBox = (CardView) view.findViewById(R.id.image_container);
        imageBox.setCardElevation(0);
        CardView backgroundBox = (CardView) view.findViewById(R.id.container);
        backgroundBox.setCardElevation(0);

        heartButton = (ImageView) view.findViewById(R.id.like_button);
        heartButton.setOnClickListener(onHeartButtonClickListener);
        setHeart();

        image = (ImageView) view.findViewById(R.id.course_image);
        name = (TextView) view.findViewById(R.id.course_name);
        location = (TextView) view.findViewById(R.id.course_location);
        setElements();

        return view;
    }

    private void setHeart() {
        if (course == null) return;

        if(dao.checkLikedCourse(course.getCode(), useremail).equals("true")) {
            heartButton.setImageResource(R.drawable.choiced_heart);
            isLiked = true;
        } else {
            heartButton.setImageResource(R.drawable.unchoiced_heart);
            isLiked = false;
        }
    }

    public void setElements() {
        PlaceVO firstPlace = dao.getPlaceData(course.getPlaceCode(0));
        PicassoImage.DownLoadImage(firstPlace.getImageURL(),image);
        name.setText(course.getName());

        String[] area = firstPlace.getLocation().split(" ");
        if(area.length >= 2)
            location.setText(area[1]);
        else
            location.setText(area[0]);
    }

    public void changeToCourseFragmment() {
        final MainActivity activity = (MainActivity)getActivity();
        activity.changeToCourseFragment(course, CourseFragment.VIA_NORMAL);
    }

    public CardView.OnClickListener onCardViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changeToCourseFragmment();
        }
    };

    public ImageButton.OnClickListener onHeartButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isLiked) {
                heartButton.setImageResource(R.drawable.unchoiced_heart);
                isLiked = false;
            } else {
                heartButton.setImageResource(R.drawable.choiced_heart);
                isLiked = true;
            }
            dao.likeCourse(course.getCode(), useremail);
        }
    };
}
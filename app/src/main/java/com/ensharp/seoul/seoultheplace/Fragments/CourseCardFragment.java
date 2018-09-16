package com.ensharp.seoul.seoultheplace.Fragments;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
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
import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.R;

public class CourseCardFragment extends Fragment implements CardFragment {
    private CardView cardView;
    private ImageButton heartButton;
    private CourseVO course;
    private Drawable unchoicedHeart;
    private Drawable choicedHeart;
    private int position;

    public CourseCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        choicedHeart = getResources().getDrawable(R.drawable.choiced_heart);
        unchoicedHeart = getResources().getDrawable(R.drawable.unchoiced_heart);
    }

    public void setData(CourseVO course) { this.course = course; }

    public void setPosition(int position) { this.position = position; }

    public CardView getCardView() {
        return cardView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_course, container, false);

        cardView = (CardView) view.findViewById(R.id.courseView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        TextView title = (TextView) view.findViewById(R.id.course_name);
        TextView description = (TextView) view.findViewById(R.id.course_location);

        title.setText(course.getName());
        description.setText(course.getDetails());

        heartButton = (ImageButton) view.findViewById(R.id.like_button);
        heartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(heartButton.getDrawable().equals(unchoicedHeart))
                    heartButton.setImageDrawable(choicedHeart);
                else
                    heartButton.setImageDrawable(unchoicedHeart);
            }
        });

        return view;
    }
}

package com.ensharp.seoul.seoultheplace.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.Course.PlaceView.CardAdapter;
import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.R;

public class CourseCardFragment extends Fragment implements CardFragment {
    private CardView cardView;
    private CourseVO course;
    private int position;

    public CourseCardFragment() {
        // Required empty public constructor
    }

    public void setData(CourseVO course) { this.course = course; }

    public void setPosition(int position) { this.position = position; }

    public CardView getCardView() {
        return cardView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
}

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
import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.R;
import com.squareup.picasso.Picasso;

public class CourseCardFragment extends Fragment {
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
        ImageButton representImage = (ImageButton) view.findViewById(R.id.ex_image);
        //new DownloadImageTask(representImage).execute(course.getImage());
        Picasso.get().load(course.getImage()).into(representImage);

        title.setText(course.getName());
        description.setText(course.getDetails());
        heartButton = (ImageButton) view.findViewById(R.id.like_button);
        heartButton.setVisibility(View.GONE);

        return view;
    }
}

package com.ensharp.seoul.seoultheplace.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.Constant;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.CardAdapter;

import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DownloadImageTask;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;

@SuppressLint("ValidFragment")
public class CardFragment extends Fragment {

    private String courseCode;
    private CourseVO course;
    private int index;
    private PlaceVO place;
    private CardView cardView;
    private ImageButton placeButton;

    @SuppressLint("ValidFragment")
    public CardFragment(String courseCode, int index, PlaceVO place) {
        this.courseCode = courseCode;
        this.index = index;
        this.place = place;

        course = Constant.getCourse();
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
        View view = inflater.inflate(R.layout.item_viewpager, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        ImageView image = (ImageView) view.findViewById(R.id.placeImage);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView address = (TextView) view.findViewById(R.id.address);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView placeIndex = (TextView) view.findViewById(R.id.index);

        new DownloadImageTask(image).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,place.getImageURL()[0]);
        title.setText(place.getName());
        address.setText(place.getLocation());
        description.setText("");
        placeIndex.setText(Integer.toString(index));

        final MainActivity activity = (MainActivity)getActivity();

        placeButton = (ImageButton) view.findViewById(R.id.place);
        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeFragment(courseCode, index);
            }
        });

        return view;
    }

    public CardView getCardView() {
        return cardView;
    }
}

package com.ensharp.seoul.seoultheplace.Fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
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

import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.DownloadImageTask;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;

@SuppressLint("ValidFragment")
public class CardFragment extends Fragment {

    private CourseVO course;
    private int index;
    private PlaceVO place;
    private CardView cardView;
    private ImageButton placeButton;

    @SuppressLint("ValidFragment")
    public CardFragment(CourseVO course, int index, PlaceVO place) {
        this.index = index;
        this.place = place;
        this.course = course;
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
        TextView placeIndex = (TextView) view.findViewById(R.id.index);

        new DownloadImageTask(image).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,place.getImageURL());
        title.setText(place.getName());
        address.setText(place.getLocation());
        placeIndex.setText(Integer.toString(index));

        final MainActivity activity = (MainActivity)getActivity();

        FrameLayout placeLayout = (FrameLayout) view.findViewById(R.id.place_layout);
        placeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeToPlaceFragment(course, index);
            }
        });

        return view;
    }

    public CardView getCardView() {
        return cardView;
    }
}

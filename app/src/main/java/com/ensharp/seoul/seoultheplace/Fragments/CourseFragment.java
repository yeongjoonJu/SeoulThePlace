package com.ensharp.seoul.seoultheplace.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ensharp.seoul.seoultheplace.R;

import java.util.List;

public class CourseFragment extends Fragment {

    private List<ImageButton> imageButtons;

    public CourseFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_course, container, false);

        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment, new MapFragment())
                .commit();

        return rootView;
    }

    public List<ImageButton> getImageButtons() { return imageButtons; }
}

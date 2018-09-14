package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ensharp.seoul.seoultheplace.Course.PlaceView.CardFragmentPagerAdapter;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.ShadowTransformer;
import com.ensharp.seoul.seoultheplace.R;

import java.util.List;

import static com.ensharp.seoul.seoultheplace.MainActivity.dpToPixels;

public class CourseFragment extends Fragment {

    private List<ImageButton> imageButtons;

    ViewPager viewPager;
    CardFragmentPagerAdapter pagerAdapter;

    public CourseFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_course, container, false);

        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment, new MapFragment())
                .commit();

        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);

        pagerAdapter = new CardFragmentPagerAdapter(getChildFragmentManager(), dpToPixels(2, getActivity()));
        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(viewPager, pagerAdapter);
        fragmentCardShadowTransformer.enableScaling(true);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(false, fragmentCardShadowTransformer);
        viewPager.setOffscreenPageLimit(3);

        return rootView;
    }

    public List<ImageButton> getImageButtons() { return imageButtons; }

    @Override
    public void onActivityResult(int requestCode , int resultCode , Intent data){

    }
}

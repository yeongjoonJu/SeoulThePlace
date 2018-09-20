package com.ensharp.seoul.seoultheplace.Course.PlaceView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ViewGroup;

import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.Fragments.CardFragment;
import com.ensharp.seoul.seoultheplace.PlaceVO;

import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapter extends FragmentPagerAdapter implements CardAdapter {

    private List<CardFragment> fragments;
    private float baseElevation;
    private String courseCode;
    private CourseVO course;
    private List<PlaceVO> places;

    public CardFragmentPagerAdapter(FragmentManager fm, float baseElevation, String code, CourseVO course) {
        super(fm);
        fragments = new ArrayList<>();
        this.baseElevation = baseElevation;
        courseCode = code;
        places = new ArrayList<PlaceVO>();
        places = course.getPlaceVO();
        this.course = course;

        generateCardFragment();
    }

    public void generateCardFragment() {
        fragments.add(new CardFragment(course, 0, null));
        for (int i = 0; i < course.getPlaceCount(); i++) {
            fragments.add(new CardFragment(course, i+1, places.get(i)));
            Log.e("Place : ",places.get(i).getName());
        }
    }

    @Override
    public float getBaseElevation() {
        return baseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return fragments.get(position).getCardView();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (CardFragment) fragment);
        return fragment;
    }
}

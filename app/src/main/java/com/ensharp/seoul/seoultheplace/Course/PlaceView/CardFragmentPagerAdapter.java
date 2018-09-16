package com.ensharp.seoul.seoultheplace.Course.PlaceView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ViewGroup;

import com.ensharp.seoul.seoultheplace.Constant;
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

    public CardFragmentPagerAdapter(FragmentManager fm, float baseElevation, String code, List<PlaceVO> places) {
        super(fm);
        fragments = new ArrayList<>();
        this.baseElevation = baseElevation;
        courseCode = code;
        this.places = places;

        course = Constant.getCourse();

        generateCardFragment();
    }

    public void generateCardFragment() {
        String placeCode;
        PlaceVO place;

        fragments.add(new CardFragment(courseCode, 0, null));
        for (int i = 1; i <= course.getPlaceCount(); i++) {
            fragments.add(new CardFragment(courseCode, i, places.get(i - 1)));
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

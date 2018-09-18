package com.ensharp.seoul.seoultheplace.Fragments;

import android.annotation.SuppressLint;
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

import com.ensharp.seoul.seoultheplace.Constant;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.CardFragmentPagerAdapter;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.ShadowTransformer;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;

import java.util.ArrayList;
import java.util.List;

import static com.ensharp.seoul.seoultheplace.MainActivity.dpToPixels;

@SuppressLint("ValidFragment")
public class CourseFragment extends Fragment {

    private String code;
    private int index;
    private ViewPager viewPager;
    private CardFragmentPagerAdapter pagerAdapter;
    private List<PlaceVO> places;
    private CourseMapFragment courseMapFragment;

    public CourseFragment() {
        code = "j111";
        index = 0;
        places = Constant.getPlaces(code);
    }

    public CourseFragment(int index) {
        code = "j111";
        this.index = index;
        places = Constant.getPlaces(code);
    }

    @SuppressLint("ValidFragment")
    public CourseFragment(String code) {
        this.code = code;
        places = Constant.getPlaces(code);
        index = 0;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_course, container, false);

        courseMapFragment = new CourseMapFragment(Constant.getLongitudes(), Constant.getLatitudes());

        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment, courseMapFragment)
                .commit();

        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);

        pagerAdapter = new CardFragmentPagerAdapter(getChildFragmentManager(), dpToPixels(2, getActivity()), code, places);
        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(viewPager, pagerAdapter);
        fragmentCardShadowTransformer.enableScaling(true);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(index);
        viewPager.setPageTransformer(false, fragmentCardShadowTransformer);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(onPageChangeListener);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode , int resultCode , Intent data){

    }

    public List<PlaceVO> getPlaces() {
        List<PlaceVO> places = new ArrayList<PlaceVO>();
        List<String> placeCodes = Constant.getCourse().getPlaceCode();

        for (int i=0; i<placeCodes.size(); i++) {
            places.add(Constant.getPlace(placeCodes.get(i)));
        }

        return places;
    }

    public void setPageritem(int index) {
        viewPager.setCurrentItem(index);
    }

    public ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            courseMapFragment.changeMapCenter(position);
            index = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public int getIndex() {
        return index;
    }
}

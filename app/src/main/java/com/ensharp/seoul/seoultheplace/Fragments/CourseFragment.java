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
import android.widget.Button;
import android.widget.ImageButton;

import com.ensharp.seoul.seoultheplace.Constant;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.CardFragmentPagerAdapter;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.ShadowTransformer;
import com.ensharp.seoul.seoultheplace.MainActivity;
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
    MainActivity mActivity;

    private Button modifyCourse;

    public CourseFragment() {
        code = "j111";
        index = 0;
    }

    public CourseFragment(int index) {
        this.index = index;
    }

    @SuppressLint("ValidFragment")
    public CourseFragment(String code) {
        this.code = code;
        index = 0;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_course, container, false);

        mActivity = (MainActivity)getActivity();

        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment, new MapFragment())
                .commit();

        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        modifyCourse = (Button)rootView.findViewById(R.id.modifycoursebtn);
        modifyCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.changeModifyFragment(getPlaces());
            }
        });

        pagerAdapter = new CardFragmentPagerAdapter(getChildFragmentManager(), dpToPixels(2, getActivity()), code);
        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(viewPager, pagerAdapter);
        fragmentCardShadowTransformer.enableScaling(true);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(index);
        viewPager.setPageTransformer(false, fragmentCardShadowTransformer);
        viewPager.setOffscreenPageLimit(3);

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

}

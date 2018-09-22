package com.ensharp.seoul.seoultheplace.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ensharp.seoul.seoultheplace.Course.PlaceView.CardFragmentPagerAdapter;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.ShadowTransformer;
import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;

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
    private List<PlaceVO> places;
    private CourseMapFragment courseMapFragment;
    private CourseVO course;

    public CourseFragment() {

    }

    @SuppressLint("ValidFragment")
    public CourseFragment(String code) {
        DAO dao = new DAO();
        this.code = code;
        course = dao.getCourseData(code);
        index = 0;
    }

    public CourseFragment(CourseVO course) {
        this.code = course.getCode();
        this.course = course;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_course, container, false);

        mActivity = (MainActivity)getActivity();
        courseMapFragment = new CourseMapFragment(course.getLongitudes(), course.getlatitudes());

        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment, courseMapFragment)
                .commit();

        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        modifyCourse = (Button)rootView.findViewById(R.id.modifycoursebtn);
        modifyCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.changeModifyFragment(course.getPlaceVO());
            }
        });

        pagerAdapter = new CardFragmentPagerAdapter(getChildFragmentManager(), dpToPixels(2, getActivity()), code,course);
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

    public void setPageritem(int index) {
        viewPager.setCurrentItem(index);
    }

    @Override
    public void onResume() {
        super.onResume();
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

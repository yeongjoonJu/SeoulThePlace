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
import com.ensharp.seoul.seoultheplace.Course.PlaceView.CardFragmentPagerAdapter;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.ShadowTransformer;
import com.ensharp.seoul.seoultheplace.*;
import com.ensharp.seoul.seoultheplace.UIElement.FloatingButton.FloatingActionButton;
import com.ensharp.seoul.seoultheplace.UIElement.FloatingButton.FloatingActionsMenu;
import org.json.JSONException;

import java.util.List;

import static com.ensharp.seoul.seoultheplace.MainActivity.dpToPixels;

@SuppressLint("ValidFragment")
public class CourseFragment extends Fragment {
    public static final int VIA_NORMAL = 0;
    public static final int VIA_CUSTOMIZED_COURSE = 1;

    private int enterRoute;
    private DAO dao = new DAO();
    private String code;
    private int index;
    private ViewPager viewPager;
    private CardFragmentPagerAdapter pagerAdapter;
    MainActivity mActivity;
    private List<PlaceVO> places;
    private CourseMapFragment courseMapFragment;
    private CourseVO course;
    private FloatingActionButton editCourseButton;
    private FloatingActionButton likeButton;

    @SuppressLint("ValidFragment")
    public CourseFragment(String code, int enterRoute) {
        DAO dao = new DAO();
        this.code = code;
        course = dao.getCourseData(code);
        Log.e("abcd", course.getDetails());
        index = 0;
        this.enterRoute = enterRoute;
    }

    public CourseFragment(CourseVO course, int enterRoute) {
        this.code = course.getCode();
        this.course = course;
        Log.e("abcd", course.getDetails());
        this.enterRoute = enterRoute;
    }

    public CourseFragment(CourseVO course, int enterRoute, int index) {
        this.code = course.getCode();
        this.course = course;
        Log.e("abcd", course.getDetails());
        this.enterRoute = enterRoute;
        this.index = index;
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

        editCourseButton = (FloatingActionButton) rootView.findViewById(R.id.edit_course);
        likeButton = (FloatingActionButton) rootView.findViewById(R.id.like_button);

        if (enterRoute == VIA_NORMAL) {
            if (dao.checkLikedCourse(code, mActivity.getUserID()).equals("true"))
                likeButton.setIcon(R.drawable.choiced_heart);
            likeButton.setOnClickListener(onLikeButtonClickListener);
        } else {
            likeButton.setVisibility(View.GONE);
        }

        editCourseButton.setOnClickListener(onEditCourseButtonClickListener);

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

    private FloatingActionButton.OnClickListener onEditCourseButtonClickListener = new FloatingActionsMenu.OnClickListener() {
        @Override
        public void onClick(View view) {
            mActivity.changeModifyFragment(course.getPlaceVO());
        }
    };

    private FloatingActionButton.OnClickListener onLikeButtonClickListener = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                if (dao.likeCourse(code, mActivity.getUserID()).getString("isCourseLiked").equals("true"))
                    likeButton.setIcon(R.drawable.unchoiced_heart);
                else
                    likeButton.setIcon(R.drawable.choiced_heart);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

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

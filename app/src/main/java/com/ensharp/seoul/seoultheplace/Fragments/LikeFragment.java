package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.CourseFragmentPagerAdapter;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.PlaceFragmentPagerAdapter;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.ShadowTransformer;
import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.CustomAnimationDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.ensharp.seoul.seoultheplace.MainActivity.dpToPixels;

public class LikeFragment extends Fragment {
    private TextView courseText;
    private TextView placeText;
    private String useremail;
    private ViewPager courseViewPager;
    private ViewPager placeViewPager;
    private CourseFragmentPagerAdapter courseViewAdapter;
    private PlaceFragmentPagerAdapter placeViewAdapter;
    private TextView noCourseMessage;
    private TextView noPlaceMessage;

    private int currentPlacePosition = 0;
    private int currentCoursePosition = 0;

    private DAO dao;

    public LikeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        showCourseCardView(currentCoursePosition);
        showPlaceCardView(currentPlacePosition);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(placeViewPager != null)
            currentPlacePosition = placeViewPager.getCurrentItem();
        if(courseViewPager != null)
            currentCoursePosition = courseViewPager.getCurrentItem();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new DAO();
        SharedPreferences preferences = getContext().getSharedPreferences("data", getContext().MODE_PRIVATE);
        useremail = preferences.getString("email", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_like, container, false);

        // 화면 구성 요소
        courseText = (TextView) rootView.findViewById(R.id.cource_text);
        placeText = (TextView) rootView.findViewById(R.id.place_text);

        // 플레이스 카드 뷰
        placeViewPager = (ViewPager) rootView.findViewById(R.id.place_search_result);
        // 코스 카드 뷰
        courseViewPager = (ViewPager) rootView.findViewById(R.id.course_search_result);

        // 안내메시지
        noCourseMessage = (TextView) rootView.findViewById(R.id.no_like_course);
        noPlaceMessage = (TextView) rootView.findViewById(R.id.no_like_place);

        return rootView;
    }

    // 플레이스 카드 뷰
    protected void showPlaceCardView(int currentPosition) {
        ArrayList<PlaceVO> places = dao.getLikedPlaceList(useremail);
        if(places == null || places.size() == 0) {
            noPlaceMessage.setVisibility(View.VISIBLE);
            placeViewPager.setVisibility(View.GONE);
            return;
        }

        placeViewPager.setVisibility(View.VISIBLE);

        placeViewAdapter = new PlaceFragmentPagerAdapter(getChildFragmentManager(), dpToPixels(places.size(), getActivity()));
        placeViewAdapter.setPlaceData(places);
        ShadowTransformer placeCardShadowTransformer = new ShadowTransformer(placeViewPager, placeViewAdapter);
        placeViewPager.setPageTransformer(false, placeCardShadowTransformer);
        placeViewPager.setAdapter(placeViewAdapter);
    }

    protected void showCourseCardView(int currentPosition) {
        ArrayList<CourseVO> courses = dao.getLikedCourseList(useremail);
        if(courses == null || courses.size() == 0){
            noCourseMessage.setVisibility(View.VISIBLE);
            courseViewPager.setVisibility(View.GONE);
            return;
        }

        courseViewPager.setVisibility(View.VISIBLE);

        courseViewAdapter = new CourseFragmentPagerAdapter(getChildFragmentManager(), dpToPixels(2, getActivity()));
        courseViewAdapter.setCourseData(courses);
        ShadowTransformer courseCardShadowTransformer = new ShadowTransformer(courseViewPager, courseViewAdapter);
        courseViewPager.setPageTransformer(false, courseCardShadowTransformer);
        courseViewPager.setAdapter(courseViewAdapter);
    }
}

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
import com.ensharp.seoul.seoultheplace.UIElement.HorizontalListView;
import com.ensharp.seoul.seoultheplace.UIElement.TagAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.ensharp.seoul.seoultheplace.MainActivity.dpToPixels;

public class MainFragment extends Fragment {
    private CustomAnimationDialog customAnimationDialog;
    private DAO dao;
    private String useremail;
    TextView courseText;
    TextView placeText;
    ViewPager courseViewPager;
    ViewPager placeViewPager;
    CourseFragmentPagerAdapter courseViewAdapter;
    PlaceFragmentPagerAdapter placeViewAdapter;

    int currentPlacePosition = 0;
    int currentCoursePosition = 0;

    public MainFragment() {
    }

    public ArrayList<CourseVO> getCourseByType(String type, String user) {
        JSONObject jsonObject = dao.getUserCourseData(type, user);
        if (jsonObject == null)
            return null;
        ArrayList<CourseVO> courses = new ArrayList<CourseVO>();
        Log.i("yeongjoon",  "서버 코스 로드");
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("jsonArr");
            for(int i=0; i<jsonArray.length(); i++)
                courses.add(new CourseVO(jsonArray.getJSONObject(i)));
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return courses;
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // 화면 구성 요소
        courseText = (TextView) rootView.findViewById(R.id.cource_text);
        placeText = (TextView) rootView.findViewById(R.id.place_text);

        // 플레이스 카드 뷰
        placeViewPager = (ViewPager) rootView.findViewById(R.id.place_search_result);
        // 코스 카드 뷰
        courseViewPager = (ViewPager) rootView.findViewById(R.id.course_search_result);

        ArrayList<String> tags = new ArrayList<String>(
                Arrays.asList(new String[]{"친구끼리", "가족끼리"}));

        HorizontalListView tagListView = (HorizontalListView) rootView.findViewById(R.id.tagListView);
        TagAdapter tagAdapter = new TagAdapter(getActivity(), tags);
        tagAdapter.setMainFragment(this);
        tagListView.setAdapter(tagAdapter);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(placeViewPager != null)
            currentPlacePosition = placeViewPager.getCurrentItem();
        if(courseViewPager != null)
            currentCoursePosition = courseViewPager.getCurrentItem();
    }

    public void renewCardView(String type) {
        customAnimationDialog = new CustomAnimationDialog(getActivity());
        customAnimationDialog.show();
        showPlaceCardView(type);
        showCourseCardView(type);
        customAnimationDialog.dismiss();
    }

    // 플레이스 카드 뷰
    protected void showPlaceCardView(String type) {
        ArrayList<PlaceVO> places = dao.searchPlace("");
        if(places == null || places.size() == 0)
            return;
        placeViewAdapter = new PlaceFragmentPagerAdapter(getChildFragmentManager(), dpToPixels(2, getActivity()));
        placeViewAdapter.setPlaceData(places);
        ShadowTransformer placeCardShadowTransformer = new ShadowTransformer(placeViewPager, placeViewAdapter);
        placeViewPager.setPageTransformer(false, placeCardShadowTransformer);
        try {
            placeViewPager.setAdapter(placeViewAdapter);
        }catch(Exception e) {
            e.printStackTrace();
            placeViewAdapter = new PlaceFragmentPagerAdapter(getFragmentManager(), dpToPixels(2, getActivity()));
            placeViewAdapter.setPlaceData(places);
        }
        placeViewPager.setOffscreenPageLimit(3);
        placeViewPager.setCurrentItem(currentPlacePosition);
        placeCardShadowTransformer.enableScaling(true);
    }

    protected void showCourseCardView(String type) {
        ArrayList<CourseVO> courses = dao.searchCourse("");
        if(courses == null || courses.size() == 0)
            return;
        courseViewAdapter = new CourseFragmentPagerAdapter(getChildFragmentManager(), dpToPixels(2, getActivity()));
        courseViewAdapter.setCourseData(courses);
        ShadowTransformer courseCardShadowTransformer = new ShadowTransformer(courseViewPager, courseViewAdapter);
        courseViewPager.setPageTransformer(false, courseCardShadowTransformer);
        try {
            courseViewPager.setAdapter(courseViewAdapter);
        }catch(Exception e) {
            e.printStackTrace();
            courseViewAdapter = new CourseFragmentPagerAdapter(getFragmentManager(), dpToPixels(2, getActivity()));
            courseViewAdapter.setCourseData(courses);
        }
        courseViewPager.setOffscreenPageLimit(3);
        courseViewPager.setCurrentItem(currentCoursePosition);
        courseCardShadowTransformer.enableScaling(true);
    }
}

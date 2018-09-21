package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.CourseFragmentPagerAdapter;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.PlaceFragmentPagerAdapter;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.ShadowTransformer;
import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.RecentSearchAdapter;

import java.util.ArrayList;

import static com.ensharp.seoul.seoultheplace.MainActivity.dpToPixels;

public class SearchFragment extends Fragment {
    static final String KEY_COURSE = "KEY_COURSE";
    static final String KEY_PLACE = "KEY_PLACE";

    InputMethodManager inputMethodManager;
    TextView courseText;
    TextView placeText;
    EditText searchEditText;
    LinearLayout recentList;
    ListView recentListView;

    ViewPager courseViewPager;
    ViewPager placeViewPager;
    CourseFragmentPagerAdapter courseViewAdapter;
    PlaceFragmentPagerAdapter placeViewAdapter;

    RecentSearchAdapter listAdapter = null;
    ArrayList<String> recentSearchList = null;

    ArrayList<PlaceVO> searchPlaceResult = null;
    ArrayList<CourseVO> searchCourseResult = null;
    int currentPlacePosition = 0;
    int currentCoursePosition = 0;

    private DAO dao;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        if(searchPlaceResult != null) {
            placeText.setVisibility(View.VISIBLE);
            showPlaceCardView(searchPlaceResult, currentPlacePosition);
        }
        if(searchCourseResult != null) {
            courseText.setVisibility(View.VISIBLE);
            showCourseCardView(searchCourseResult, currentCoursePosition);
        }
    }

    public void viewVisible() {
        if(recentList != null)
            recentList.setVisibility(View.GONE);
        if(searchPlaceResult != null) {
            placeText.setVisibility(View.VISIBLE);
            placeViewPager.setVisibility(View.VISIBLE);
        }
        if(searchCourseResult != null) {
            courseText.setVisibility(View.VISIBLE);
            courseViewPager.setVisibility(View.VISIBLE);
        }
    }

    public void viewInvisible() {
        if(recentList != null)
            recentList.setVisibility(View.VISIBLE);
        if(searchPlaceResult != null) {
            placeText.setVisibility(View.INVISIBLE);
            placeViewPager.setVisibility(View.INVISIBLE);
        }
        if(searchCourseResult != null) {
            courseText.setVisibility(View.INVISIBLE);
            courseViewPager.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(placeViewPager != null)
            currentPlacePosition = placeViewPager.getCurrentItem();
        if(courseViewPager != null)
            currentCoursePosition = courseViewPager.getCurrentItem();

        // 최근 검색어 SharedPreference에 저장
        SharedPreferences preferences = getActivity().getSharedPreferences("SeoulThePlace", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor =  preferences.edit();
        for(int i = 0; i < listAdapter.getCount(); i++) {
            if(i >= 6)
                break;
            if(listAdapter.getItem(i) != null)
                editor.putString("RecentSearch" + i, listAdapter.getItem(i));
        }
        editor.commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new DAO();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(recentSearchList == null)
            recentSearchList = new ArrayList<>();

        // 최근 검색어 SharedPreference에서 가져온다
        SharedPreferences preferences = getActivity().getSharedPreferences("SeoulThePlace", getActivity().MODE_PRIVATE);
        for(int i=0; i < 6; i++) {
            String word = preferences.getString("RecentSearch" + i, null);
            if(word != null)
                recentSearchList.add(word);
        }
        listAdapter = new RecentSearchAdapter(getActivity(), recentSearchList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        // 화면 구성 요소
        ImageButton searchButton = (ImageButton) rootView.findViewById(R.id.search_button);
        courseText = (TextView) rootView.findViewById(R.id.cource_text);
        placeText = (TextView) rootView.findViewById(R.id.place_text);
        searchEditText = (EditText) rootView.findViewById(R.id.search_edittext);
        listAdapter.setEditText(searchEditText);
        recentList = (LinearLayout) rootView.findViewById(R.id.recent_search);
        recentListView = (ListView) rootView.findViewById(R.id.recent_listview);
        recentListView.setAdapter(listAdapter);

        // 플레이스 카드 뷰
        placeViewPager = (ViewPager) rootView.findViewById(R.id.place_search_result);
        // 코스 카드 뷰
        courseViewPager = (ViewPager) rootView.findViewById(R.id.course_search_result);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseText.setVisibility(View.VISIBLE);
                placeText.setVisibility(View.VISIBLE);

                String searchWord = String.valueOf(searchEditText.getText());
                listAdapter.insert(searchWord, 0);

                // 코스 검색
                ArrayList<CourseVO> courseData = dao.searchCourse(searchWord);
                searchCourseResult = courseData;
                if(searchCourseResult != null) {
                    showCourseCardView(searchCourseResult, 0);
                }

                // 플레이스 검색
                ArrayList<PlaceVO> placeData = dao.searchPlace(searchWord);
                searchPlaceResult = placeData;
                if(searchPlaceResult != null) {
                    showPlaceCardView(searchPlaceResult, 0);
                }

                // 키보드 숨김
                inputMethodManager.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
            }
        });
        return rootView;
    }

    // 플레이스 카드 뷰
    protected void showPlaceCardView(ArrayList<PlaceVO> places, int currentPosition) {
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
        placeViewPager.setCurrentItem(currentPosition);
        placeCardShadowTransformer.enableScaling(true);
    }

    protected void showCourseCardView(ArrayList<CourseVO> courses, int currentPosition) {
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
        courseViewPager.setCurrentItem(currentPosition);
        courseCardShadowTransformer.enableScaling(true);
    }
}
package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.Context;
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
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.RecentSearchAdapter;

import java.util.ArrayList;
import java.util.Arrays;

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

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if(searchPlaceResult != null) {
            placeText.setVisibility(View.VISIBLE);
            showPlaceCardView(searchPlaceResult);
            placeViewPager.setCurrentItem(currentPlacePosition);
        }
        if(searchCourseResult != null) {
            courseText.setVisibility(View.VISIBLE);
            showCourseCardView(searchCourseResult);
            courseViewPager.setCurrentItem(currentCoursePosition);
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 최근 검색어 SharedPreference에서 가져온다
        recentSearchList = new ArrayList<String>(Arrays.asList(new String[]{"경복궁", "우정당", "상 타자", "장학금", "성실"}));
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

                // 플레이스 임시 데이터
                PlaceVO[] placeData = new PlaceVO[]{
                        new PlaceVO("j12315", "롯데월드1", "서울 송파구 올림픽로 240", new String[]{"https://upload.wikimedia.org/wikipedia/commons/c/c2/Lotte_World_Theme_Park.jpg", "http://img.insight.co.kr/static/2017/10/13/700/6r4f9ti8jo8837c8e900.jpg", "http://m.lottehotel.com/upload/imagePool/201605/PACKAGE/20160518112121956_1(5).jpg"}, "02-1661-2000", "어른 52000원 청소년 46000원 어린이 43000원 베이비 14000원", "주차장", "이용시 무료, 일반 30분 1000원 이후 10분에 1000원", 0, "국내 최초,최대 실내테마타크 및 놀이공원으로 서울시내에서 즐길 수 있는 놀이공원중 하나이다. 잠실역에 위치하여 바로 놀이공원으로 갈 수 있다는 점이 좋으며 실내와 실외 두군데로 구성이 되어있어 우천시에도 이용이 매우 유용하다. 젊은 사람들이 좋아하는 익스트림한 놀이기구들도 많아 젊은 10대나 커플들이 데이트하기에도 정말 좋은 장소 이다.", "커플끼리,가족끼리", "09:30~22:00"),
                        new PlaceVO("j12315", "롯데월드2", "서울 송파구 올림픽로 240", new String[]{"https://upload.wikimedia.org/wikipedia/commons/c/c2/Lotte_World_Theme_Park.jpg", "http://img.insight.co.kr/static/2017/10/13/700/6r4f9ti8jo8837c8e900.jpg", "http://m.lottehotel.com/upload/imagePool/201605/PACKAGE/20160518112121956_1(5).jpg"}, "02-1661-2000", "어른 52000원 청소년 46000원 어린이 43000원 베이비 14000원", "주차장", "이용시 무료, 일반 30분 1000원 이후 10분에 1000원", 0, "국내 최초,최대 실내테마타크 및 놀이공원으로 서울시내에서 즐길 수 있는 놀이공원중 하나이다. 잠실역에 위치하여 바로 놀이공원으로 갈 수 있다는 점이 좋으며 실내와 실외 두군데로 구성이 되어있어 우천시에도 이용이 매우 유용하다. 젊은 사람들이 좋아하는 익스트림한 놀이기구들도 많아 젊은 10대나 커플들이 데이트하기에도 정말 좋은 장소 이다.", "커플끼리,가족끼리", "09:30~22:00"),
                        new PlaceVO("j12315", "롯데월드3", "서울 송파구 올림픽로 240", new String[]{"https://upload.wikimedia.org/wikipedia/commons/c/c2/Lotte_World_Theme_Park.jpg", "http://img.insight.co.kr/static/2017/10/13/700/6r4f9ti8jo8837c8e900.jpg", "http://m.lottehotel.com/upload/imagePool/201605/PACKAGE/20160518112121956_1(5).jpg"}, "02-1661-2000", "어른 52000원 청소년 46000원 어린이 43000원 베이비 14000원", "주차장", "이용시 무료, 일반 30분 1000원 이후 10분에 1000원", 0, "국내 최초,최대 실내테마타크 및 놀이공원으로 서울시내에서 즐길 수 있는 놀이공원중 하나이다. 잠실역에 위치하여 바로 놀이공원으로 갈 수 있다는 점이 좋으며 실내와 실외 두군데로 구성이 되어있어 우천시에도 이용이 매우 유용하다. 젊은 사람들이 좋아하는 익스트림한 놀이기구들도 많아 젊은 10대나 커플들이 데이트하기에도 정말 좋은 장소 이다.", "커플끼리,가족끼리", "09:30~22:00"),
                        new PlaceVO("j12315", "롯데월드4", "서울 송파구 올림픽로 240", new String[]{"https://upload.wikimedia.org/wikipedia/commons/c/c2/Lotte_World_Theme_Park.jpg", "http://img.insight.co.kr/static/2017/10/13/700/6r4f9ti8jo8837c8e900.jpg", "http://m.lottehotel.com/upload/imagePool/201605/PACKAGE/20160518112121956_1(5).jpg"}, "02-1661-2000", "어른 52000원 청소년 46000원 어린이 43000원 베이비 14000원", "주차장", "이용시 무료, 일반 30분 1000원 이후 10분에 1000원", 0, "국내 최초,최대 실내테마타크 및 놀이공원으로 서울시내에서 즐길 수 있는 놀이공원중 하나이다. 잠실역에 위치하여 바로 놀이공원으로 갈 수 있다는 점이 좋으며 실내와 실외 두군데로 구성이 되어있어 우천시에도 이용이 매우 유용하다. 젊은 사람들이 좋아하는 익스트림한 놀이기구들도 많아 젊은 10대나 커플들이 데이트하기에도 정말 좋은 장소 이다.", "커플끼리,가족끼리", "09:30~22:00"),
                        new PlaceVO("j12315", "롯데월드5", "서울 송파구 올림픽로 240", new String[]{"https://upload.wikimedia.org/wikipedia/commons/c/c2/Lotte_World_Theme_Park.jpg", "http://img.insight.co.kr/static/2017/10/13/700/6r4f9ti8jo8837c8e900.jpg", "http://m.lottehotel.com/upload/imagePool/201605/PACKAGE/20160518112121956_1(5).jpg"}, "02-1661-2000", "어른 52000원 청소년 46000원 어린이 43000원 베이비 14000원", "주차장", "이용시 무료, 일반 30분 1000원 이후 10분에 1000원", 0, "국내 최초,최대 실내테마타크 및 놀이공원으로 서울시내에서 즐길 수 있는 놀이공원중 하나이다. 잠실역에 위치하여 바로 놀이공원으로 갈 수 있다는 점이 좋으며 실내와 실외 두군데로 구성이 되어있어 우천시에도 이용이 매우 유용하다. 젊은 사람들이 좋아하는 익스트림한 놀이기구들도 많아 젊은 10대나 커플들이 데이트하기에도 정말 좋은 장소 이다.", "커플끼리,가족끼리", "09:30~22:00"),
                        new PlaceVO("j12315", "롯데월드6", "서울 송파구 올림픽로 240", new String[]{"https://upload.wikimedia.org/wikipedia/commons/c/c2/Lotte_World_Theme_Park.jpg", "http://img.insight.co.kr/static/2017/10/13/700/6r4f9ti8jo8837c8e900.jpg", "http://m.lottehotel.com/upload/imagePool/201605/PACKAGE/20160518112121956_1(5).jpg"}, "02-1661-2000", "어른 52000원 청소년 46000원 어린이 43000원 베이비 14000원", "주차장", "이용시 무료, 일반 30분 1000원 이후 10분에 1000원", 0, "국내 최초,최대 실내테마타크 및 놀이공원으로 서울시내에서 즐길 수 있는 놀이공원중 하나이다. 잠실역에 위치하여 바로 놀이공원으로 갈 수 있다는 점이 좋으며 실내와 실외 두군데로 구성이 되어있어 우천시에도 이용이 매우 유용하다. 젊은 사람들이 좋아하는 익스트림한 놀이기구들도 많아 젊은 10대나 커플들이 데이트하기에도 정말 좋은 장소 이다.", "커플끼리,가족끼리", "09:30~22:00"),
                        new PlaceVO("j12315", "롯데월드7", "서울 송파구 올림픽로 240", new String[]{"https://upload.wikimedia.org/wikipedia/commons/c/c2/Lotte_World_Theme_Park.jpg", "http://img.insight.co.kr/static/2017/10/13/700/6r4f9ti8jo8837c8e900.jpg", "http://m.lottehotel.com/upload/imagePool/201605/PACKAGE/20160518112121956_1(5).jpg"}, "02-1661-2000", "어른 52000원 청소년 46000원 어린이 43000원 베이비 14000원", "주차장", "이용시 무료, 일반 30분 1000원 이후 10분에 1000원", 0, "국내 최초,최대 실내테마타크 및 놀이공원으로 서울시내에서 즐길 수 있는 놀이공원중 하나이다. 잠실역에 위치하여 바로 놀이공원으로 갈 수 있다는 점이 좋으며 실내와 실외 두군데로 구성이 되어있어 우천시에도 이용이 매우 유용하다. 젊은 사람들이 좋아하는 익스트림한 놀이기구들도 많아 젊은 10대나 커플들이 데이트하기에도 정말 좋은 장소 이다.", "커플끼리,가족끼리", "09:30~22:00")
                };

                ArrayList<PlaceVO> tempData = new ArrayList<PlaceVO>();
                for(int i=0;i<placeData.length; i++) {
                    tempData.add(placeData[i]);
                }

                searchPlaceResult = tempData;
                showPlaceCardView(searchPlaceResult);

                CourseVO[] courseData = new CourseVO[] { new CourseVO(), new CourseVO(), new CourseVO(), new CourseVO()};
                ArrayList<CourseVO> courses = new ArrayList<>();
                for(int i=0; i<courseData.length; i++)
                    courses.add(courseData[i]);

                searchCourseResult = courses;
                showCourseCardView(searchCourseResult);

                // 키보드 숨김
                inputMethodManager.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
            }
        });
        return rootView;
    }

    // 플레이스 카드 뷰
    protected void showPlaceCardView(ArrayList<PlaceVO> places) {
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
        placeCardShadowTransformer.enableScaling(true);
    }

    protected void showCourseCardView(ArrayList<CourseVO> courses) {
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
        courseCardShadowTransformer.enableScaling(true);
    }
}

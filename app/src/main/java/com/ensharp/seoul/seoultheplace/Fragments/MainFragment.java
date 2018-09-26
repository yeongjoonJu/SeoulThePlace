package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.CourseFragmentPagerAdapter;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.PlaceFragmentPagerAdapter;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.ShadowTransformer;
import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.CustomAnimationDialog;
import com.ensharp.seoul.seoultheplace.UIElement.HorizontalListView;
import com.ensharp.seoul.seoultheplace.UIElement.RecentSearchAdapter;
import com.ensharp.seoul.seoultheplace.UIElement.TagAdapter;
import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.Arrays;

import static com.ensharp.seoul.seoultheplace.MainActivity.dpToPixels;

public class MainFragment extends Fragment {
    ArrayList<String> tags = new ArrayList<String>(
            Arrays.asList(new String[]{"혼자서", "연인끼리", "친구끼리", "가족끼리", "먹방투어", "옛날로", "연예인처럼", "힐링·산책", "인생샷", "문화·예술", "활동적인"}));
    private static final String[] CHANNELS = new String[]{"타입별", "검색"};
    private MagicIndicator magicIndicator;
    private CommonNavigator commonNavigator;
    private FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();
    private boolean isPlusButtonExpanded = false;
    private MainFragment mainFragment;

    InputMethodManager inputMethodManager;
    private CustomAnimationDialog customAnimationDialog;
    private DAO dao;
    private String useremail;
    LinearLayout searchBar;
    HorizontalListView tagListView;
    TagAdapter tagAdapter;
    TextView courseText;
    TextView placeText;
    ViewPager courseViewPager;
    ViewPager placeViewPager;
    CourseFragmentPagerAdapter courseViewAdapter;
    PlaceFragmentPagerAdapter placeViewAdapter;

    // 검색
    EditText searchEditText;
    ImageButton searchButton;
    ArrayList<PlaceVO> searchPlaceResult = null;
    ArrayList<CourseVO> searchCourseResult = null;
    // 최근 검색
    LinearLayout recentList;
    ListView recentListView;
    RecentSearchAdapter listAdapter = null;
    ArrayList<String> recentSearchList = null;

    int currentPlacePosition = 0;
    int currentCoursePosition = 0;

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

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("home_tag_error/MainFragment", "came here in MainFragment");

        mainFragment = this;
        dao = new DAO();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        SharedPreferences preferences = getContext().getSharedPreferences("data", getContext().MODE_PRIVATE);
        useremail = preferences.getString("email", null);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // 최근 검색어 SharedPreference에서 가져온다
        if(recentSearchList == null)
            recentSearchList = new ArrayList<>();
        preferences = getActivity().getSharedPreferences("SeoulThePlace", getActivity().MODE_PRIVATE);
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //initMagicIndicator(rootView);
        //mFragmentContainerHelper.handlePageSelected(0, true);

        // 화면 구성 요소
        courseText = (TextView) rootView.findViewById(R.id.cource_text);
        placeText = (TextView) rootView.findViewById(R.id.place_text);
        searchBar = (LinearLayout) rootView.findViewById(R.id.search_bar);
        tagListView = (HorizontalListView) rootView.findViewById(R.id.tagListView);
        searchEditText = (EditText) rootView.findViewById(R.id.search_edittext);
        searchButton = (ImageButton) rootView.findViewById(R.id.search_button);
        final Button transformButton = (Button) rootView.findViewById(R.id.transform_button);
        transformButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(transformButton.getText().equals("추천")) {
                    searchBar.setVisibility(View.GONE);
                    tagListView.setVisibility(View.VISIBLE);
                    courseViewPager.setVisibility(View.VISIBLE);
                    placeViewPager.setVisibility(View.VISIBLE);
                    TagAdapter tagAdapter = new TagAdapter(getActivity(), tags);
                    tagAdapter.setMainFragment(mainFragment);
                    tagListView.setAdapter(tagAdapter);
                    inputMethodManager.hideSoftInputFromInputMethod(searchEditText.getWindowToken(), 0);
                    transformButton.setText("검색");
                }
                else {
                    searchEditText.setText("");
                    searchBar.setVisibility(View.VISIBLE);
                    tagListView.setVisibility(View.GONE);
                    courseViewPager.setVisibility(View.INVISIBLE);
                    placeViewPager.setVisibility(View.INVISIBLE);
                    transformButton.setText("추천");
                }
            }
        });

        // 최근 검색
        listAdapter.setEditText(searchEditText);
        recentList = (LinearLayout) rootView.findViewById(R.id.recent_search);
        recentListView = (ListView) rootView.findViewById(R.id.recent_listview);
        recentListView.setAdapter(listAdapter);

        // 플레이스 카드 뷰
        placeViewPager = (ViewPager) rootView.findViewById(R.id.place_search_result);
        // 코스 카드 뷰
        courseViewPager = (ViewPager) rootView.findViewById(R.id.course_search_result);

        tagAdapter = new TagAdapter(getActivity(), tags);
        tagAdapter.setMainFragment(this);

        tagListView.setAdapter(tagAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAnimationDialog = new CustomAnimationDialog(getActivity());
                customAnimationDialog.show();

                courseViewPager.setVisibility(View.VISIBLE);
                placeViewPager.setVisibility(View.VISIBLE);

                String searchWord = String.valueOf(searchEditText.getText());
                listAdapter.insert(searchWord, 0);

                // 코스 검색
                showCourseCardView(searchWord);
                // 플레이스 검색
                showPlaceCardView(searchWord);

                // 키보드 숨김
                inputMethodManager.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                customAnimationDialog.dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        Log.e("home_tag_error/MainFragment", "onDestroy");

        super.onDestroy();
        if(placeViewPager != null)
            currentPlacePosition = placeViewPager.getCurrentItem();
        if(courseViewPager != null)
            currentCoursePosition = courseViewPager.getCurrentItem();
    }

    public void renewCardView(String type) {
        customAnimationDialog = new CustomAnimationDialog(getActivity());

        customAnimationDialog.show();
        placeViewPager.setVisibility(View.VISIBLE);
        courseViewPager.setVisibility(View.VISIBLE);
        showPlaceCardView(type);
        showCourseCardView(type);

        customAnimationDialog.dismiss();
    }

    // 플레이스 카드 뷰
    protected void showPlaceCardView(String word) {
        ArrayList<PlaceVO> places = dao.getUserPlaceData(word);
        if(places == null || places.size() == 0) {
            placeViewPager.setVisibility(View.INVISIBLE);
            return;
        }
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
        //placeViewPager.setCurrentItem(currentPlacePosition);
        placeCardShadowTransformer.enableScaling(true);
    }

    protected void showCourseCardView(String word) {
        ArrayList<CourseVO> courses = dao.getUserCourseData(word);
        if(courses == null || courses.size() == 0) {
            courseViewPager.setVisibility(View.INVISIBLE);
            return;
        }
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
        //courseViewPager.setCurrentItem(currentCoursePosition);
        courseCardShadowTransformer.enableScaling(true);
    }
}

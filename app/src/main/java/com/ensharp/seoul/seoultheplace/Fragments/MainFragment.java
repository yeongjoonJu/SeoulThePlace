package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.CourseFragmentPagerAdapter;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.PlaceFragmentPagerAdapter;
import com.ensharp.seoul.seoultheplace.*;
import com.ensharp.seoul.seoultheplace.UIElement.*;
import com.ensharp.seoul.seoultheplace.UIElement.SearchBar.JJBarWithErrorIconController;
import com.ensharp.seoul.seoultheplace.UIElement.SearchBar.JJSearchView;
import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class MainFragment extends Fragment {
    ArrayList<String> tags = new ArrayList<String>(
            Arrays.asList(new String[]{"연인끼리", "혼자서", "친구끼리", "먹방투어", "가족끼리", "옛날로", "연예인처럼", "힐링·산책", "인생샷", "문화·예술", "활동적인"}));
    private static final String[] CHANNELS = new String[]{"타입별", "검색"};
    private View rootView;
    private MagicIndicator magicIndicator;
    private CommonNavigator commonNavigator;
    private FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();
    private boolean isPlusButtonExpanded = false;
    private MainFragment mainFragment;
    final int SEARCH = 0;
    final int TYPE = 1;
    private int screenStatus = 0;

    InputMethodManager inputMethodManager;
    private DAO dao;
    private String useremail;
    FrameLayout searchBar;
    HorizontalListView tagListView;
    TagAdapter tagAdapter;
    TextView courseText;
    TextView placeText;
    RecyclerView courseViewPager;
    RecyclerView placeViewPager;
    CourseFragmentPagerAdapter courseViewAdapter;
    PlaceFragmentPagerAdapter placeViewAdapter;
    LinearLayout searchButton;
    ImageView transformButton;

    // 검색
    EditText searchEditText;
    ArrayList<PlaceVO> searchPlaceResult = null;
    ArrayList<CourseVO> searchCourseResult = null;
    TextView noSearchResult;
    JJSearchView searchView;
    Button end;

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
            noSearchResult.setVisibility(View.GONE);
        }
        if(searchCourseResult != null) {
            courseText.setVisibility(View.VISIBLE);
            courseViewPager.setVisibility(View.VISIBLE);
            noSearchResult.setVisibility(View.GONE);
        }
    }

    public void viewInvisible() {
        if (recentList != null)
            recentList.setVisibility(View.VISIBLE);
        if (searchPlaceResult != null) {
            placeText.setVisibility(View.INVISIBLE);
            placeViewPager.setVisibility(View.INVISIBLE);
            noSearchResult.setVisibility(View.GONE);
        }
        if (searchCourseResult != null) {
            courseText.setVisibility(View.INVISIBLE);
            courseViewPager.setVisibility(View.INVISIBLE);
            noSearchResult.setVisibility(View.GONE);
        }
    }

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainFragment = this;
        dao = new DAO();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        SharedPreferences preferences = getContext().getSharedPreferences("data", getContext().MODE_PRIVATE);
        useremail = preferences.getString("email", null);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public void loadRecentSearch() {
        // 최근 검색어 SharedPreference에서 가져온다
        recentSearchList = new ArrayList<>();
        SharedPreferences preferences = getActivity().getSharedPreferences("SeoulThePlace", getActivity().MODE_PRIVATE);
        for(int i=0; i < 6; i++) {
            String word = preferences.getString("RecentSearch" + i, null);
            if(word != null)
                recentSearchList.add(word);
        }
        listAdapter = new RecentSearchAdapter(getActivity(), this, recentSearchList);

        // 최근 검색
        listAdapter.setEditText(searchEditText);
        recentListView.setAdapter(listAdapter);
        recentList.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // 화면 구성 요소
        courseText = (TextView) rootView.findViewById(R.id.course_text);
        placeText = (TextView) rootView.findViewById(R.id.place_text);
        searchBar = (FrameLayout) rootView.findViewById(R.id.search_bar);
        tagListView = (HorizontalListView) rootView.findViewById(R.id.tagListView);
        searchEditText = (EditText) rootView.findViewById(R.id.search_edittext);
        noSearchResult = (TextView) rootView.findViewById(R.id.no_search_result);
        recentList = (LinearLayout) rootView.findViewById(R.id.recent_search);
        recentListView = (ListView) rootView.findViewById(R.id.recent_listview);
        end = rootView.findViewById(R.id.end);
        searchView = (JJSearchView) rootView.findViewById(R.id.jjsv);
        placeViewPager = (RecyclerView) rootView.findViewById(R.id.place_search_result);
        courseViewPager = (RecyclerView) rootView.findViewById(R.id.course_search_result);
        searchButton = (LinearLayout) rootView.findViewById(R.id.search_button);
        transformButton = (ImageView) rootView.findViewById(R.id.transform_button);
        transformButton.setTag("검색");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(transformButton.getTag().equals("추천")) {
                    screenStatus = TYPE;
                    recentList.setVisibility(View.GONE);
                    searchBar.setVisibility(View.GONE);
                    tagListView.setVisibility(View.VISIBLE);
                    courseViewPager.setVisibility(View.VISIBLE);
                    placeViewPager.setVisibility(View.VISIBLE);
                    TagAdapter tagAdapter = new TagAdapter(getActivity(), tags);
                    tagAdapter.setMainFragment(mainFragment);
                    tagListView.setAdapter(tagAdapter);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    transformButton.setBackground(getContext().getResources().getDrawable(R.drawable.item_home_search));
                    transformButton.setTag("검색");
                }
                else {
                    screenStatus = SEARCH;
                    recentList.setVisibility(View.GONE);
                    courseText.setVisibility(View.INVISIBLE);
                    placeText.setVisibility(View.INVISIBLE);
                    searchEditText.setText("");
                    searchBar.setVisibility(View.VISIBLE);
                    tagListView.setVisibility(View.GONE);
                    courseViewPager.setVisibility(View.INVISIBLE);
                    placeViewPager.setVisibility(View.INVISIBLE);
                    noSearchResult.setVisibility(View.GONE);
                    transformButton.setBackground(getContext().getResources().getDrawable(R.drawable.home_colored));
                    transformButton.setTag("추천");
                    initSearchBar();
                    loadRecentSearch();
                }
            }
        });

        // 검색
        searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (!isFocused) return;
                searchEditText.getText().clear();
                inputMethodManager.showSoftInput(view, 0);
                //recentList.setVisibility(View.VISIBLE);
                viewInvisible();
            }
        });

        // 플레이스 카드 뷰
        searchView.setController(new JJBarWithErrorIconController());
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPlaceAndCourse();
            }
        });

        tagAdapter = new TagAdapter(getActivity(), tags);
        tagAdapter.setMainFragment(this);
        tagListView.setAdapter(tagAdapter);

        return rootView;
    }

    public void searchPlaceAndCourse() {
        viewVisible();
        search(rootView);
        // 최근 검색어 SharedPreference에 저장
        if(listAdapter != null) {
            SharedPreferences preferences = getActivity().getSharedPreferences("SeoulThePlace", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            for (int i = 0; i < listAdapter.getCount(); i++) {
                if (i >= 6)
                    break;
                if (listAdapter.getItem(i) != null)
                    editor.putString("RecentSearch" + i, listAdapter.getItem(i));
            }
            editor.commit();
        }
    }

    private void initSearchBar() {
        searchEditText.setVisibility(View.VISIBLE);
        end.setVisibility(View.VISIBLE);
    }

    public void search(View v) {
        recentList.setVisibility(View.GONE);
        courseViewPager.setVisibility(View.VISIBLE);
        placeViewPager.setVisibility(View.VISIBLE);
        noSearchResult.setVisibility(View.GONE);
        searchEditText.clearFocus();

        String searchWord = String.valueOf(searchEditText.getText());
        // 15글자 이상이거나 공백문자가 두 개 이상 포함되어있으면 검색하지 않음
        if( searchWord.length() > 15)
            searchWord = searchWord.substring(0, 14);
        if(Pattern.matches("^[ ]{2,}", searchWord))
            searchWord = "";

        // 검색어를 입력하지 않은 경우 제외
        if(searchWord.length() != 0 && !searchWord.equals(" ")) {
            listAdapter.insert(searchWord, 0);

            // 코스 검색
            showCourseCardView(SEARCH, searchWord);
            // 플레이스 검색
            showPlaceCardView(SEARCH, searchWord);
        }
        else {
            courseViewPager.setVisibility(View.INVISIBLE);
            placeViewPager.setVisibility(View.INVISIBLE);
        }

        // 결과가 없으면 결과없음 출력
        if(courseViewPager.getVisibility() == View.INVISIBLE && placeViewPager.getVisibility() == View.INVISIBLE) {
            noSearchResult.setText("검색 결과가 없습니다");
            noSearchResult.setVisibility(View.VISIBLE);
        }

        // 키보드 숨김
        inputMethodManager.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void renewCardView(String type) {
        placeViewPager.setVisibility(View.VISIBLE);
        courseViewPager.setVisibility(View.VISIBLE);
        showCourseCardView(TYPE, type);
        showPlaceCardView(TYPE, type);
        if(placeViewPager.getVisibility() == View.INVISIBLE && courseViewPager.getVisibility() == View.INVISIBLE) {
            noSearchResult.setText("추천 플레이스 및 코스가 없습니다");
            noSearchResult.setVisibility(View.VISIBLE);
        }
        else
            noSearchResult.setVisibility(View.GONE);
    }

    // 플레이스 카드 뷰
    protected void showPlaceCardView(int dataType, String word) {
        ArrayList<PlaceVO> places = null;
        if(dataType == SEARCH)
            places = dao.searchPlace(word);
        else
            places = dao.getUserPlaceData(word);
        if(places == null || places.size() == 0) {
            placeViewPager.setVisibility(View.INVISIBLE);
            placeText.setVisibility(View.INVISIBLE);
            return;
        }
        placeText.setVisibility(View.VISIBLE);
        placeViewPager.setLayoutManager(RecycleViewUtil.createHorizontalLayoutManager(getContext()));
        PlaceListAdapter placeListAdapter = new PlaceListAdapter((MainActivity)getActivity(), getContext(), places, useremail, 1);
        placeViewPager.setAdapter(placeListAdapter);
    }

    protected void showCourseCardView(int dataType, String word) {
        ArrayList<CourseVO> courses = null;
        if(dataType == SEARCH)
            courses = dao.searchCourse(word);
        else
            courses = dao.getUserCourseData(word);
        if(courses == null || courses.size() == 0) {
            courseViewPager.setVisibility(View.INVISIBLE);
            courseText.setVisibility(View.INVISIBLE);
            return;
        }
        courseText.setVisibility(View.VISIBLE);
        courseViewPager.setLayoutManager(RecycleViewUtil.createHorizontalLayoutManager(getContext()));
        CourseListAdapter courseListAdapter = new CourseListAdapter((MainActivity)getActivity(), getContext(), courses, useremail, 1);
        courseViewPager.setAdapter(courseListAdapter);
    }
}

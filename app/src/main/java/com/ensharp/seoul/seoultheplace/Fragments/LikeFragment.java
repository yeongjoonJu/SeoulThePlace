package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
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
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.CourseListAdapter;
import com.ensharp.seoul.seoultheplace.UIElement.CustomAnimationDialog;
import com.ensharp.seoul.seoultheplace.UIElement.PlaceListAdapter;
import com.ensharp.seoul.seoultheplace.UIElement.RecycleViewOnItemTouchListener;
import com.ensharp.seoul.seoultheplace.UIElement.RecycleViewUtil;
import com.ensharp.seoul.seoultheplace.UIElement.ViewFindUtils;
import com.flyco.tablayout.SegmentTabLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.ensharp.seoul.seoultheplace.MainActivity.dpToPixels;

public class LikeFragment extends Fragment {
    private TextView courseText;
    private TextView placeText;
    private String useremail;
    private RecyclerView courseViewPager;
    private RecyclerView placeViewPager;
    private TextView noCourseMessage;
    private TextView noPlaceMessage;

    private String[] titles = {"코스, 플레이스"};
    private ArrayList<Fragment> fragments = new ArrayList<>();

    private DAO dao;

    public LikeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        showCourseCardView();
        showPlaceCardView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

        // 탭
//        fragments.add(new LikedCourseFragment());
//        fragments.add(new LikedPlaceFragment());
//        SegmentTabLayout tabLayout = ViewFindUtils.find(rootView, R.id.fl_change);
//        tabLayout.setTabData(titles, getActivity(), R.id.fl_change, fragments);

        // 화면 구성 요소
        courseText = (TextView) rootView.findViewById(R.id.cource_text);
        placeText = (TextView) rootView.findViewById(R.id.place_text);

        // 플레이스 카드 뷰
        placeViewPager = (RecyclerView) rootView.findViewById(R.id.place_search_result);
        // 코스 카드 뷰
        courseViewPager = (RecyclerView) rootView.findViewById(R.id.course_search_result);

        // 안내메시지
        noCourseMessage = (TextView) rootView.findViewById(R.id.no_like_course);
        noPlaceMessage = (TextView) rootView.findViewById(R.id.no_like_place);

        return rootView;
    }

    // 플레이스 카드 뷰
    protected void showPlaceCardView() {
        ArrayList<PlaceVO> places = dao.getLikedPlaceList(useremail);
        if(places == null || places.size() == 0) {
            noPlaceMessage.setVisibility(View.VISIBLE);
            placeViewPager.setVisibility(View.GONE);
            return;
        }

        placeViewPager.setVisibility(View.VISIBLE);
        placeViewPager.setLayoutManager(RecycleViewUtil.createHorizontalLayoutManager(getContext()));
        PlaceListAdapter placeListAdapter = new PlaceListAdapter((MainActivity)getActivity(), getContext(), places, useremail);
        placeViewPager.setAdapter(placeListAdapter);
    }

    // 코스 카드 뷰
    protected void showCourseCardView() {
        ArrayList<CourseVO> courses = dao.getLikedCourseList(useremail);
        if(courses == null || courses.size() == 0){
            noCourseMessage.setVisibility(View.VISIBLE);
            courseViewPager.setVisibility(View.GONE);
            return;
        }

        courseViewPager.setVisibility(View.VISIBLE);
        courseViewPager.setLayoutManager(RecycleViewUtil.createHorizontalLayoutManager(getContext()));
        CourseListAdapter courseListAdapter = new CourseListAdapter((MainActivity)getActivity(), getContext(), courses, useremail);
        courseViewPager.setAdapter(courseListAdapter);
    }
}

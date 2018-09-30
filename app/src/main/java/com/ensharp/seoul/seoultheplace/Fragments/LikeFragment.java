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
import android.widget.FrameLayout;
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
    private String useremail;
    private String[] titles = {"코스", "플레이스"};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private SegmentTabLayout tabLayout;
    private View rootView;

    private DAO dao;

    public LikeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
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
        rootView = inflater.inflate(R.layout.fragment_like, container, false);

        // 탭
        fragments.clear();
        fragments.add(new LikedCourseFragment());
        fragments.add(new LikedPlaceFragment());
        tabLayout = ViewFindUtils.find(rootView, R.id.toggle_tab);
        tabLayout.setTabData(titles, getActivity(), R.id.course_place_fragment, fragments);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (tabLayout.getCurrentTab() == 1) {
            tabLayout.setCurrentTab(0);
        }
    }
}

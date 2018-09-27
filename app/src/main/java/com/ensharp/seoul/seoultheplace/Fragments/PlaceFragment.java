package com.ensharp.seoul.seoultheplace.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.DetailInformationVO;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.DetailInformationAdapter;
import com.ensharp.seoul.seoultheplace.UIElement.FloatingButton.FloatingActionButton;
import com.ensharp.seoul.seoultheplace.UIElement.PlaceViewPagerAdapter;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint("ValidFragment")
public class PlaceFragment extends Fragment {
    public static final int VIA_SEARCH = 0;
    public static final int VIA_COURSE = 1;
    public static final int DURING_EDITTING_COURSE = 2;

    private DAO dao = new DAO();
    private int enterRoute;
    private String courseCode;
    private int index;
    private PlaceVO place;
    private View rootView;
    private CourseVO courseVO;
    private FloatingActionButton likeButton;
    private FloatingActionButton createCourseButton;
    private boolean isliked = false;
    private String userID;

    @SuppressLint("ValidFragment")
    public PlaceFragment(String placeCode, int enterRoute) {
        this.enterRoute = enterRoute;
        courseCode = "";
        index = 0;
        courseVO = null;
        place = dao.getPlaceData(placeCode);
    }

    @SuppressLint("ValidFragment")
    public PlaceFragment(CourseVO course, int index, int enterRoute) {
        this.enterRoute = enterRoute;
        this.courseCode = courseCode;
        this.index = index;
        courseVO = course;
        place = dao.getPlaceData(courseVO.getPlaceCode(index - 1));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_place, container, false);

        PlaceChildFragment placeChildFragment = new PlaceChildFragment(courseVO, place, index, enterRoute);
        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment, placeChildFragment)
                .commit();

        setUserID();
        setFloatingButtons();

        return rootView;
    }

    private void setFloatingButtons() {
        likeButton = (FloatingActionButton) rootView.findViewById(R.id.like_button);
        createCourseButton = (FloatingActionButton) rootView.findViewById(R.id.create_course);

        if(dao.checkLikedPlace(place.getCode(), userID).equals("true")) {
            likeButton.setIcon(R.drawable.choiced_heart);
            isliked = true;
        } else {
            likeButton.setIcon(R.drawable.unchoiced_heart);
            isliked = false;
        }

        if (enterRoute == DURING_EDITTING_COURSE)
            createCourseButton.setVisibility(View.GONE);

        likeButton.setOnClickListener(onLikeListener);
        createCourseButton.setOnClickListener(onCreateCourseListener);
    }

    private void setUserID() {
        MainActivity activity = (MainActivity) getActivity();
        userID = activity.getUserID();
    }

    private FloatingActionButton.OnClickListener onLikeListener = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isliked) {
                likeButton.setIcon(R.drawable.unchoiced_heart);
                isliked = false;
            } else {
                likeButton.setIcon(R.drawable.choiced_heart);
                isliked = true;
            }

            dao.likePlace(place.getCode(), userID);
        }
    };

    private FloatingActionButton.OnClickListener onCreateCourseListener = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            List<PlaceVO> places = new ArrayList<PlaceVO>();
            places.add(place);

            changeModifyFragment(places);
        }
    };

    public void changeModifyFragment(List<PlaceVO> places) {
        MainActivity activity = (MainActivity) getActivity();
        activity.changeModifyFragment(places);
    }
}

package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.CourseListAdapter;
import com.ensharp.seoul.seoultheplace.UIElement.RecycleViewUtil;

import java.util.ArrayList;

public class LikedCourseFragment extends Fragment {
    private DAO dao;
    private String useremail;

    private TextView noCourseMessage;
    private RecyclerView courseViewPager;

    @Override
    public void onStart() {
        super.onStart();
        showCourseCardView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new DAO();
        SharedPreferences preferences = getContext().getSharedPreferences("data", getContext().MODE_PRIVATE);
        useremail = preferences.getString("email", null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_liked_course, container, false);

        noCourseMessage = (TextView) rootView.findViewById(R.id.no_like_course);
        courseViewPager = (RecyclerView) rootView.findViewById(R.id.course_search_result);

        return rootView;
    }

    // 코스 카드 뷰
    protected void showCourseCardView() {
        ArrayList<CourseVO> courses = dao.getLikedCourseList(useremail);

        if(courses == null || courses.size() == 0){
            showMessage();
            return;
        }

        courseViewPager.setVisibility(View.VISIBLE);
        courseViewPager.setLayoutManager(RecycleViewUtil.createVerticalLayoutManager(getContext()));
        CourseListAdapter courseListAdapter = new CourseListAdapter((MainActivity)getActivity(), getContext(), this, courses, useremail);
        courseViewPager.setAdapter(courseListAdapter);
    }

    public void showMessage() {
        noCourseMessage.setVisibility(View.VISIBLE);
        courseViewPager.setVisibility(View.GONE);
    }
}

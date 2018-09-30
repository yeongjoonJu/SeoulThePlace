package com.ensharp.seoul.seoultheplace.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.EdittedCourseVO;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.CustomizedCourseAdapter;
import com.ensharp.seoul.seoultheplace.UIElement.CustomizedCourseAdapter2;
import com.ensharp.seoul.seoultheplace.UIElement.CustomizedListViewAdapter;
import com.ensharp.seoul.seoultheplace.UIElement.FloatingButton.FloatingActionButton;
import com.ensharp.seoul.seoultheplace.UIElement.SwipeDismissListViewTouchListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomizedFragment extends Fragment {

    private DAO dao = new DAO();
    private View rootView;
    private ListView listView;
    private TextView message;
    private CustomizedListViewAdapter listViewAdapter;
    private List<EdittedCourseVO> customizedCourses = new ArrayList<EdittedCourseVO>();
    private CustomizedCourseAdapter2 adapter;
    private boolean isExpanded = false;
    private FloatingActionButton createCourse;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customized, container, false);

        customizedCourses = dao.getCustomizedCourses(getUserID());

        listView = (ListView) rootView.findViewById(R.id.customized_list);
        listViewAdapter = new CustomizedListViewAdapter(getContext(), customizedCourses, getUserID());
        listViewAdapter.setCustomizedFragment(this);
        listView.setAdapter(listViewAdapter);
        listViewAdapter.setMode(Attributes.Mode.Single);

        message = (TextView) rootView.findViewById(R.id.message);
        if (customizedCourses.size() == 0 || customizedCourses == null)
            message.setVisibility(View.VISIBLE);

        createCourse = (FloatingActionButton) rootView.findViewById(R.id.create_course);
        createCourse.setOnClickListener(onCreateCourseListener);

        return rootView;
    }

    public void notifyDataSetChanged(List<EdittedCourseVO> newCourseList) {
        listViewAdapter = new CustomizedListViewAdapter(getContext(), newCourseList, getUserID());
        listViewAdapter.setCustomizedFragment(this);
        listView.setAdapter(listViewAdapter);
        listViewAdapter.setMode(Attributes.Mode.Single);
    }

    public void notifyEmpty() {
        message.setVisibility(View.VISIBLE);
    }

    public void changeToCourseFragment(EdittedCourseVO course) {
        MainActivity activity = (MainActivity) getActivity();
        PlaceVO place = dao.getPlaceData(course.getPlaceCode().get(0));

        activity.changeToCourseFragment(new CourseVO(course, place), CourseFragment.VIA_CUSTOMIZED_COURSE);
    }

    private FloatingActionButton.OnClickListener onCreateCourseListener = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            changeModifyFragment(new ArrayList<PlaceVO>());
        }
    };

    public String getUserID () {
        MainActivity activity = (MainActivity) getActivity();
        return activity.getUserID();
    }

    public void changeModifyFragment(List<PlaceVO> places) {
        MainActivity activity = (MainActivity) getActivity();
        activity.changeModifyFragment(places);
    }
}
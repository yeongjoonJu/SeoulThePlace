package com.ensharp.seoul.seoultheplace.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.EdittedCourseVO;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.CustomizedCourseAdapter;
import com.ensharp.seoul.seoultheplace.UIElement.FloatingButton.FloatingActionButton;
import com.ensharp.seoul.seoultheplace.UIElement.SwipeDismissListViewTouchListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CustomizedFragment extends Fragment {

    private DAO dao = new DAO();
    private View rootView;
    private ListView listView;
    private List<EdittedCourseVO> customizedCourses = new ArrayList<EdittedCourseVO>();
    private CustomizedCourseAdapter adapter;
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
        listView = (ListView) rootView.findViewById(R.id.customized_list);


        customizedCourses = dao.getCustomizedCourses(getUserID());


        adapter = new CustomizedCourseAdapter(getContext(), 0, customizedCourses);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);
        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(listView, new SwipeDismissListViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                if (isExpanded) return false;

                return true;
            }

            @Override
            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                if (isExpanded) return;

                for (int position : reverseSortedPositions) {
                    dao.deleteEdittedCourse(getUserID(), customizedCourses.get(position).getCode());
                    customizedCourses.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        listView.setOnTouchListener(touchListener);

        createCourse = (FloatingActionButton) rootView.findViewById(R.id.create_course);
        createCourse.setOnClickListener(onCreateCourseListener);

        return rootView;
    }

    public ListView getListView() {
        return listView;
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (isExpanded) return;

            Object object = listView.getItemAtPosition(position);
            EdittedCourseVO edittedCourseVO = (EdittedCourseVO) object;
            PlaceVO place = dao.getPlaceData(edittedCourseVO.getPlaceCode().get(0));

            changeToCourseFragment(new CourseVO(edittedCourseVO, place));
        }
    };

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

    public void changeToCourseFragment(CourseVO course) {
        MainActivity activity = (MainActivity) getActivity();
        activity.changeToCourseFragment(course);
    }

    public void changeModifyFragment(List<PlaceVO> places) {
        MainActivity activity = (MainActivity) getActivity();
        activity.changeModifyFragment(places);
    }
}
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
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.EdittedCourseVO;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.CustomizedCourseAdapter;
import com.ensharp.seoul.seoultheplace.UIElement.CustomizedCourseAdapter2;
import com.ensharp.seoul.seoultheplace.UIElement.FloatingButton.FloatingActionButton;
import com.ensharp.seoul.seoultheplace.UIElement.SwipeDismissListViewTouchListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomizedFragment extends Fragment {

    private DAO dao = new DAO();
    private View rootView;
    private RecyclerView listView;
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
        listView = (RecyclerView) rootView.findViewById(R.id.customized_list);


        customizedCourses = dao.getCustomizedCourses(getUserID());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(layoutManager);

        adapter = new CustomizedCourseAdapter2(getContext(), 0, customizedCourses,(MainActivity)getActivity());
        listView.setAdapter(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int swipeFlags = ItemTouchHelper.LEFT;
                int dragFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                EdittedCourseVO edittedCourseVO = customizedCourses.get(viewHolder.getAdapterPosition());
                customizedCourses.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                dao.deleteEdittedCourse(getUserID(),edittedCourseVO.getCode());
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        });
        helper.attachToRecyclerView(listView);
        createCourse = (FloatingActionButton) rootView.findViewById(R.id.create_course);
        createCourse.setOnClickListener(onCreateCourseListener);

        return rootView;
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
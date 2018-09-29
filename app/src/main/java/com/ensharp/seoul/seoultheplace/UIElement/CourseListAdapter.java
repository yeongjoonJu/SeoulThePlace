package com.ensharp.seoul.seoultheplace.UIElement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;

import java.util.List;

public class CourseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private DAO dao = new DAO();
    private MainActivity activity;
    private Context context;
    private List<CourseVO> courses;
    private String userID;

    public CourseListAdapter(MainActivity activity, Context context, List<CourseVO> courses, String userID) {
        this.activity = activity;
        this.context = context;
        this.courses = courses;
        this.userID = userID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        LayoutInflater inflaterMainCategory = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflaterMainCategory.inflate(R.layout.item_course_info, parent, false);
        CourseHolder holder = new CourseHolder(view, activity);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CourseVO course = courses.get(position);
        CourseHolder viewHolder = (CourseHolder) holder;
        viewHolder.setData(course, context, userID);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
}

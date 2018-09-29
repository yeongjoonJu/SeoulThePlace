package com.ensharp.seoul.seoultheplace.UIElement;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ensharp.seoul.seoultheplace.*;
import com.ensharp.seoul.seoultheplace.Fragments.CourseFragment;

public class CourseMainHolder extends RecyclerView.ViewHolder {
    private DAO dao = new DAO();
    private MainActivity activity;
    private Context context;
    private CourseVO course;
    private String userID;

    private LinearLayout courseCard;
    private ImageView image;
    private TextView name;
    private TextView location;
    private ImageView like;
    private boolean isLiked = false;

    public CourseMainHolder(View itemView, MainActivity activity) {
        super(itemView);

        this.activity = activity;
        name = (TextView) itemView.findViewById(R.id.course_name);
        courseCard = (LinearLayout) itemView.findViewById(R.id.course_card);
        location = (TextView) itemView.findViewById(R.id.course_location);
        image = (ImageView) itemView.findViewById(R.id.ex_image);
        like = (ImageView) itemView.findViewById(R.id.like_button);
        like.setOnClickListener(onLikeClickListener);
        courseCard.setOnClickListener(onContainerClickListener);
    }

    public void setData(CourseVO course, Context context,  String userID) {
        this.course = course;
        this.context = context;
        this.userID = userID;

        setElements();

        if (dao.checkLikedCourse(course.getCode(), userID).equals("true")) {
            like.setImageDrawable(context.getDrawable(R.drawable.choiced_heart));
            isLiked = true;
        } else {
            like.setImageDrawable(context.getDrawable(R.drawable.unchoiced_heart));
            isLiked = false;
        }
    }

    public void setElements() {
        PlaceVO firstPlace = dao.getPlaceData(course.getPlaceCode(0));
        PicassoImage.DownLoadImage(firstPlace.getImageURL(),image);
        name.setText(course.getName());

        String[] area = firstPlace.getLocation().split(" ");
        if(area.length >= 2)
            location.setText(area[1]);
        else
            location.setText(area[0]);
    }

    private View.OnClickListener onContainerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            activity.changeToCourseFragment(dao.getCourseData(course.getCode()), CourseFragment.VIA_NORMAL);
        }
    };

    private View.OnClickListener onLikeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isLiked) {
                like.setImageDrawable(context.getDrawable(R.drawable.unchoiced_heart));
                isLiked = false;
            } else {
                like.setImageDrawable(context.getDrawable(R.drawable.choiced_heart));
                isLiked = true;
            }
            dao.likeCourse(course.getCode(), userID);
        }
    };
}

package com.ensharp.seoul.seoultheplace.UIElement;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ensharp.seoul.seoultheplace.*;
import com.ensharp.seoul.seoultheplace.Fragments.CourseFragment;
import com.ensharp.seoul.seoultheplace.R;

public class CourseHolder extends RecyclerView.ViewHolder {
    private DAO dao = new DAO();
    private MainActivity activity;
    private CourseListAdapter courseListAdapter;
    private Context context;
    private CourseVO course;
    private String userID;

    private CardView container;
    private CardView textBox;
    private ImageView image;
    private TextView name;
    private TextView location;
    private TextView likes;
    private ImageView like;
    private boolean isLiked = false;

    public CourseHolder(View itemView, MainActivity activity, CourseListAdapter courseListAdapter) {
        super(itemView);

        this.activity = activity;
        this.courseListAdapter = courseListAdapter;
        container = (CardView) itemView.findViewById(R.id.courseView);
        container.setElevation(0);
        container.setCardElevation(0);
        container.setOnClickListener(onContainerClickListener);
        textBox = (CardView) itemView.findViewById(R.id.container);
        textBox.setElevation(0);
        textBox.setCardElevation(0);
        image = (ImageView) itemView.findViewById(R.id.course_image);
        name = (TextView)itemView.findViewById(R.id.course_name);
        location = (TextView)itemView.findViewById(R.id.course_location);
        likes = (TextView) itemView.findViewById(R.id.liked_count);
        like = (ImageView) itemView.findViewById(R.id.like_button);
        like.setOnClickListener(onLikeClickListener);
    }

    public void setData(CourseVO course, Context context,  String userID) {
        this.course = course;
        this.context = context;
        this.userID = userID;

        if(course.getImage() == null)
            setElements();
        else {
            name.setText(course.getName());
            PicassoImage.DownLoadImage(course.getImage(), image);
            location.setText(course.getLocation());
            likes.setText(String.valueOf(course.getLikes()));
        }

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
        likes.setText(String.valueOf(course.getLikes()));

        String[] area = firstPlace.getLocation().split(" ");
        if(area.length >= 2)
            location.setText(area[1]);
        else
            location.setText(area[0]);
    }

    private View.OnClickListener onContainerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            activity.changeToCourseFragment(course, CourseFragment.VIA_NORMAL);
        }
    };

    private View.OnClickListener onLikeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isLiked) {
                like.setImageDrawable(context.getDrawable(R.drawable.unchoiced_heart));
                isLiked = false;
                dao.likeCourse(course.getCode(), userID);
                courseListAdapter.notifyDataUpdated(course);
            } else {
                like.setImageDrawable(context.getDrawable(R.drawable.choiced_heart));
                isLiked = true;
                dao.likeCourse(course.getCode(), userID);
            }
        }
    };
}

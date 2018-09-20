package com.ensharp.seoul.seoultheplace.UIElement;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.Course.PlaceView.CardAdapter;
import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.DownloadImageTask;
import com.ensharp.seoul.seoultheplace.Fragments.CourseCardFragment;
import com.ensharp.seoul.seoultheplace.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

public class CourseAdapter extends ArrayAdapter<CourseVO> {
    private DAO dao = null;
    String useremail = null;

    public CourseAdapter(@NonNull Context context, @NonNull ArrayList<CourseVO> objects) {
        super(context, 0, objects);
        dao = new DAO();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_maincourse, parent, false);
        }

        SharedPreferences preferences = getContext().getSharedPreferences("data", getContext().MODE_PRIVATE);
        useremail = preferences.getString("email", null);

        CourseVO course = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.course_name);
        TextView description = (TextView) listItemView.findViewById(R.id.course_location);
        ImageButton heartButton = (ImageButton) listItemView.findViewById(R.id.like_button);
        ImageButton representImage = (ImageButton) listItemView.findViewById(R.id.ex_image);

        title.setText(course.getName());
        description.setText(course.getDetails());
        Picasso.get().load(course.getImage()).into(representImage);

        // 좋아요 되어있으면
        if (dao.checkLikedCourse(getItem(position).getCode(), useremail).equals("true")) {
            heartButton.setImageDrawable(listItemView.getResources().getDrawable(R.drawable.choiced_heart));
            getItem(position).setLikedState(true);
        }
        else {
            heartButton.setImageDrawable(listItemView.getResources().getDrawable(R.drawable.unchoiced_heart));
            getItem(position).setLikedState(false);
        }

        View finalListItemView = listItemView;
        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseVO currentCourse = getItem(position);
                Log.i("yeongjoon", "하트 눌림");

                try {
                    if (dao.likeCourse(currentCourse.getCode(), useremail).getString("isCourseLiked").equals("true")) {
                        Log.i("yeongjoon", "좋아요 해제");
                        heartButton.setImageDrawable(finalListItemView.getResources().getDrawable(R.drawable.unchoiced_heart));
                        currentCourse.changeLiked();
                    } else {
                        Log.i("yeongjoon", "좋아요 눌림");
                        heartButton.setImageDrawable(finalListItemView.getResources().getDrawable(R.drawable.choiced_heart));
                        currentCourse.changeLiked();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return listItemView;
    }
}

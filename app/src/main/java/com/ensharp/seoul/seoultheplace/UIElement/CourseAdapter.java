package com.ensharp.seoul.seoultheplace.UIElement;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.Fragments.CourseFragment;
import com.ensharp.seoul.seoultheplace.MainActivity;
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
        TextView location = (TextView) listItemView.findViewById(R.id.course_location);
        ImageButton heartButton = (ImageButton) listItemView.findViewById(R.id.like_button);
        ImageView representImage = (ImageView) listItemView.findViewById(R.id.ex_image);

        title.setText(course.getName());
        location.setText(course.getLocation());
        Picasso.get().load(course.getImage()).into(representImage);

        representImage.setOnClickListener(new View.OnClickListener() {
            MainActivity activity = (MainActivity) getContext();

            @Override
            public void onClick(View view) {
                activity.changeToCourseFragment(dao.getCourseData(course.getCode()), CourseFragment.VIA_NORMAL);
            }
        });

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

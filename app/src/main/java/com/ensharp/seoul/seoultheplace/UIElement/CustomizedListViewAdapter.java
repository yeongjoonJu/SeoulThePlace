package com.ensharp.seoul.seoultheplace.UIElement;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.EdittedCourseVO;
import com.ensharp.seoul.seoultheplace.Fragments.CourseFragment;
import com.ensharp.seoul.seoultheplace.Fragments.CustomizedFragment;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PicassoImage;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;

import java.util.List;

public class CustomizedListViewAdapter extends BaseSwipeAdapter {
    private DAO dao = new DAO();
    private Context mContext;
    private List<EdittedCourseVO> customizedCourses;
    private String userID;
    private CustomizedFragment fragment;

    private View view;
    private ImageView image;
    private TextView title;
    private TextView location;
    private CardView cardview;

    public CustomizedListViewAdapter(Context mContext, List<EdittedCourseVO> customizedCourses, String userID) {
        this.mContext = mContext;
        this.customizedCourses = customizedCourses;
        this.userID = userID;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_customized_course, null);

        image = (ImageView) view.findViewById(R.id.customized_course_image);
        title = (TextView) view.findViewById(R.id.customized_course_name);
        location = (TextView) view.findViewById(R.id.customized_course_location);
        cardview = (CardView) view.findViewById(R.id.customized_course_cardview);

        return view;
    }

    public void setCustomizedFragment(CustomizedFragment fragmment) {
        this.fragment = fragmment;
    }

    @Override
    public void fillValues(int position, View convertView) {
        EdittedCourseVO currentInformation = customizedCourses.get(position);

        PlaceVO place = dao.getPlaceData(currentInformation.getPlaceCode().get(0));
        PicassoImage.DownLoadImage(place.getImageURL(), image);
        title.setText(currentInformation.getName());
        location.setText(place.getLocation());

        SwipeLayout swipeLayout = (SwipeLayout)view.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.changeToCourseFragment(customizedCourses.get(position));
            }
        });

        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.changeToCourseFragment(customizedCourses.get(position));
            }
        });

        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EdittedCourseVO edittedCourseVO = customizedCourses.get(position);
                swipeLayout.close();
                dao.deleteEdittedCourse(userID, edittedCourseVO.getCode());
                customizedCourses.remove(position);
                if (customizedCourses.size() == 0)
                    fragment.notifyEmpty();
                fragment.notifyDataSetChanged(customizedCourses);
            }
        });
    }

    @Override
    public int getCount() {
        return customizedCourses.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

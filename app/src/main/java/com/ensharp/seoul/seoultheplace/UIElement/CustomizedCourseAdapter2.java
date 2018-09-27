package com.ensharp.seoul.seoultheplace.UIElement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ensharp.seoul.seoultheplace.*;
import com.ensharp.seoul.seoultheplace.Fragments.CourseFragment;

import java.util.List;

public class CustomizedCourseAdapter2 extends RecyclerView.Adapter<CustomizedCourseAdapter2.ViewHolder1> {

    private Context context;
    private List<EdittedCourseVO> customizedCourseList;
    private MainActivity mainActivity;



    public CustomizedCourseAdapter2(Context context, int resource, @NonNull List<EdittedCourseVO> list,MainActivity mainActivity) {
        this.context = context;
        this.customizedCourseList = list;
        this.mainActivity = mainActivity;
    }

    @Override
    public CustomizedCourseAdapter2.ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customized_course, null);
        return new CustomizedCourseAdapter2.ViewHolder1(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder1 holder, int position) {
        EdittedCourseVO currentInformation = customizedCourseList.get(position);
        DAO dao = new DAO();
        PlaceVO placeVO = dao.getPlaceData(currentInformation.getPlaceCode().get(0));
        PicassoImage.DownLoadImage(placeVO.getImageURL(),holder.image);
        holder.title.setText(currentInformation.getName());
        holder.location.setText(placeVO.getLocation());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.changeToCourseFragment(new CourseVO(currentInformation, placeVO), CourseFragment.VIA_CUSTOMIZED_COURSE);
            }
        };
        
        holder.image.setOnClickListener(listener);
        holder.cardview.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return customizedCourseList.size();
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView location;
        CardView cardview;

        public ViewHolder1(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.customized_course_image);
            title = (TextView) itemView.findViewById(R.id.customized_course_name);
            location = (TextView) itemView.findViewById(R.id.customized_course_location);
            cardview = (CardView) itemView.findViewById(R.id.customized_course_cardview);
        }
    }
}

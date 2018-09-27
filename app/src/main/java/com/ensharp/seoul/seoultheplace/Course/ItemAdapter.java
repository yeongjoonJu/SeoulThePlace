package com.ensharp.seoul.seoultheplace.Course;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PicassoImage;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    CourseModifyFragment courseModifyFragment;
    MainActivity mActivity;
    List<PlaceVO> items;
    int item_layout;
    RecyclerView recyclerView;

    public ItemAdapter(CourseModifyFragment courseModifyFragment, List<PlaceVO> items, int item_layout, RecyclerView recyclerView) {
        this.courseModifyFragment = courseModifyFragment;
        this.items = items;
        this.item_layout = item_layout;
        this.recyclerView = recyclerView;
        this.mActivity = (MainActivity)courseModifyFragment.getActivity();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview2, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final PlaceVO item = items.get(position);

        PicassoImage.DownLoadImage(item.getImageURL(),holder.image);
        holder.title.setText(item.getName());
        String dest = courseModifyFragment.getTouchName();
        if(dest.length() != 0)
            holder.distance.setText("에서 " + String.valueOf(item.getDistance())+"m");
        else
            holder.distance.setText("0m");
        holder.Destination.setText(dest);
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseModifyFragment.ChangeData(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        CardView cardview;
        TextView distance;
        TextView Destination;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.AddCardImage);
            title = (TextView) itemView.findViewById(R.id.AddCardName);
            cardview = (CardView) itemView.findViewById(R.id.AddCardView);
            distance = (TextView)itemView.findViewById(R.id.addCardDistance);
            Destination = (TextView)itemView.findViewById(R.id.addCardDestination);
        }
    }
}
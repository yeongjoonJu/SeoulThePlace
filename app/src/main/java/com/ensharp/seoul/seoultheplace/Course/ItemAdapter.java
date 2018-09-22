package com.ensharp.seoul.seoultheplace.Course;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.DownloadImageTask;
import com.ensharp.seoul.seoultheplace.MainActivity;
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
        String ImageURL = item.getImageURL()[0];
        new DownloadImageTask(holder.image).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,item.getImageURL());
        holder.title.setText(item.getName());
        holder.distance.setText(String.valueOf(item.getDistance())+"m");
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseModifyFragment.ChangeData(item);
                courseModifyFragment.setItemData(null);
                courseModifyFragment.ChangeItemData();
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

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.AddCardImage);
            title = (TextView) itemView.findViewById(R.id.AddCardName);
            cardview = (CardView) itemView.findViewById(R.id.AddCardView);
            distance = (TextView)itemView.findViewById(R.id.addCardDistance);
        }
    }
}
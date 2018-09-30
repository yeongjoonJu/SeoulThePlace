package com.ensharp.seoul.seoultheplace.UIElement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.Fragments.LikedPlaceFragment;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;

import java.util.List;

public class PlaceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private DAO dao = new DAO();
    private MainActivity activity;
    private LikedPlaceFragment likedPlaceFragment;
    private Context context;
    private List<PlaceVO> places;
    private String userID;
    private int type = 0;

    public PlaceListAdapter(MainActivity activity, Context context, LikedPlaceFragment likedPlaceFragment, List<PlaceVO> places, String userID) {
        this.activity = activity;
        this.context = context;
        this.likedPlaceFragment = likedPlaceFragment;
        this.places = places;
        this.userID = userID;
    }

    public PlaceListAdapter(MainActivity activity, Context context, List<PlaceVO> places, String userID, int type) {
        this.activity = activity;
        this.context = context;
        this.places = places;
        this.userID = userID;
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        if(type == 0) {
            LayoutInflater inflaterMainCategory = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflaterMainCategory.inflate(R.layout.item_place_info, parent, false);
            PlaceHolder holder = new PlaceHolder(view, activity, this);
            return holder;
        }
        else {
            LayoutInflater inflaterMainCategory = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflaterMainCategory.inflate(R.layout.item_mainplace, parent, false);
            PlaceMainHolder holder = new PlaceMainHolder(view, activity);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PlaceVO place = places.get(position);
        if(type == 0) {
            PlaceHolder viewHolder = (PlaceHolder) holder;
            viewHolder.setData(place, context, userID);
        }
        else {
            PlaceMainHolder viewHolder = (PlaceMainHolder) holder;
            viewHolder.setData(place, context, userID);
        }
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void notifyDataUpdated(PlaceVO place) {
        places.remove(place);
        notifyDataSetChanged();
        if (places.size() == 0)
            likedPlaceFragment.showMessage();
    }

    public PlaceVO getItemAtPosition(int position) {
        return places.get(position);
    }
}

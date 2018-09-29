package com.ensharp.seoul.seoultheplace.UIElement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;

import java.util.List;

public class PlaceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private DAO dao = new DAO();
    private MainActivity activity;
    private Context context;
    private List<PlaceVO> places;
    private String userID;

    public PlaceListAdapter(MainActivity activity, Context context, List<PlaceVO> places, String userID) {
        this.activity = activity;
        this.context = context;
        this.places = places;
        this.userID = userID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        LayoutInflater inflaterMainCategory = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflaterMainCategory.inflate(R.layout.item_place_info, parent, false);
        PlaceHolder holder = new PlaceHolder(view, activity);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PlaceVO place = places.get(position);
        PlaceHolder viewHolder = (PlaceHolder) holder;
        viewHolder.setData(place, context, userID);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public PlaceVO getItemAtPosition(int position) {
        return places.get(position);
    }
}

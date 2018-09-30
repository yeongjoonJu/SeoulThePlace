package com.ensharp.seoul.seoultheplace.UIElement;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.Fragments.PlaceFragment;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PicassoImage;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;

/**
 * Created by cuongvo on 5/16/17.
 */

public class PlaceHolder extends RecyclerView.ViewHolder {
    private DAO dao = new DAO();
    private MainActivity activity;
    private PlaceListAdapter placeListAdapter;
    private Context context;
    private PlaceVO place;
    private String userID;

    private CardView container;
    private ImageView image;
    private TextView name;
    private TextView location;
    private ImageView like;
    private boolean isLiked = false;

    public PlaceHolder(View itemView, MainActivity activity, PlaceListAdapter placeListAdapter) {
        super(itemView);

        this.activity = activity;
        this.placeListAdapter = placeListAdapter;
        container = (CardView) itemView.findViewById(R.id.container);
        container.setOnClickListener(onContainerClickListener);
        image = (ImageView) itemView.findViewById(R.id.place_image);
        name = (TextView)itemView.findViewById(R.id.place_name);
        location = (TextView)itemView.findViewById(R.id.place_location);
        like = (ImageView) itemView.findViewById(R.id.like_button);
        like.setOnClickListener(onLikeClickListener);
    }

    public void setData(PlaceVO place, Context context,  String userID) {
        this.place = place;
        this.context = context;
        this.userID = userID;

        name.setText(place.getName());
        PicassoImage.DownLoadImage(place.getImageURL(), image);
        location.setText(place.getLocation());
        if (dao.checkLikedPlace(place.getCode(), userID).equals("true")) {
            like.setImageDrawable(context.getDrawable(R.drawable.choiced_heart));
            isLiked = true;
        } else {
            like.setImageDrawable(context.getDrawable(R.drawable.unchoiced_heart));
            isLiked = false;
        }
    }

    private View.OnClickListener onContainerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            activity.changeToPlaceFragment(place.getCode(), PlaceFragment.VIA_SEARCH);
        }
    };

    private View.OnClickListener onLikeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isLiked) {
                like.setImageDrawable(context.getDrawable(R.drawable.unchoiced_heart));
                isLiked = false;
                dao.likePlace(place.getCode(), userID);
                placeListAdapter.notifyDataUpdated(place);
            } else {
                like.setImageDrawable(context.getDrawable(R.drawable.choiced_heart));
                isLiked = true;
                dao.likePlace(place.getCode(), userID);
            }
        }
    };
}

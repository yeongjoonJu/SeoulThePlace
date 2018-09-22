package com.ensharp.seoul.seoultheplace.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.CardAdapter;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.CustomAnimationDialog;
import com.squareup.picasso.Picasso;

public class PlaceCardFragment extends Fragment {

    private CardView cardView;
    private ImageButton placeButton;
    private int position;
    private PlaceVO place;

    public PlaceCardFragment() {
    }

    public void setData(PlaceVO place) {
        this.place = place;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ImageButton getPlaceButton() { return placeButton;}

    public CardView getCardView() {
        return cardView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_viewpager, container, false);

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView address = (TextView) view.findViewById(R.id.address);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView index = (TextView) view.findViewById(R.id.index);
        ImageView image = (ImageView) view.findViewById(R.id.placeImage);

        title.setText(place.getName());
        title.setTextColor(Color.rgb(0,0,0));
        address.setText(place.getLocation());
        address.setTextColor(Color. rgb(0,0,0));
        description.setText(place.getDetails());
        description.setTextColor(Color.rgb(0,0,0));
        index.setTextColor(Color.rgb(255,255,255));
        index.setText(String.format("%d", position));

        try {
            Picasso.get().load(place.getImageURL()[0]).into(image);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                Picasso.get().load(place.getImageURL()[1]).into(image);
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                Picasso.get().load(place.getImageURL()[2]).into(image);
            }
            //new DownloadImageTask(image).execute(place.getImageURL()[0]);
        }

        final MainActivity activity = (MainActivity)getActivity();

        placeButton = (ImageButton) view.findViewById(R.id.place);
        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CustomAnimationDialog customAnimationDialog = new CustomAnimationDialog(activity);
                customAnimationDialog.show();
                activity.changeToPlaceFragment(place.getCode(), PlaceFragment.VIA_SEARCH);
                customAnimationDialog.dismiss();
            }
        });

        return view;
    }
}

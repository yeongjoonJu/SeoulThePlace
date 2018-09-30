package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.PlaceListAdapter;
import com.ensharp.seoul.seoultheplace.UIElement.RecycleViewUtil;

import java.util.ArrayList;

public class LikedPlaceFragment extends Fragment {
    private DAO dao;
    private String useremail;

    private TextView noPlaceMessage;
    private RecyclerView placeViewPager;

    @Override
    public void onStart() {
        super.onStart();
        showPlaceCardView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new DAO();
        SharedPreferences preferences = getContext().getSharedPreferences("data", getContext().MODE_PRIVATE);
        useremail = preferences.getString("email", null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_liked_place, container, false);

        noPlaceMessage = (TextView) rootView.findViewById(R.id.no_like_place);
        placeViewPager = (RecyclerView) rootView.findViewById(R.id.place_search_result);

        return rootView;
    }

    protected void showPlaceCardView() {
        ArrayList<PlaceVO> places = dao.getLikedPlaceList(useremail);
        if(places == null || places.size() == 0) {
            showMessage();
            return;
        }

        placeViewPager.setVisibility(View.VISIBLE);
        placeViewPager.setLayoutManager(RecycleViewUtil.createVerticalLayoutManager(getContext()));
        PlaceListAdapter placeListAdapter = new PlaceListAdapter((MainActivity)getActivity(), getContext(), this, places, useremail);
        placeViewPager.setAdapter(placeListAdapter);
    }

    public void showMessage() {
        noPlaceMessage.setVisibility(View.VISIBLE);
        placeViewPager.setVisibility(View.GONE);
    }
}

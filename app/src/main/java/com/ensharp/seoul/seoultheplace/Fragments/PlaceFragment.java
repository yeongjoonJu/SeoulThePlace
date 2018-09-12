package com.ensharp.seoul.seoultheplace.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.DetailInformationVO;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.DetailInformationAdapter;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class PlaceFragment extends Fragment {

    private DetailInformationAdapter adapter;

    public PlaceFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_place, container, false);

        TextView title = (TextView) rootView.findViewById(R.id.title);
        TextView address = (TextView) rootView.findViewById(R.id.address);
        TextView phone = (TextView) rootView.findViewById(R.id.phone);
        TextView description = (TextView) rootView.findViewById(R.id.description);
        TextView detail = (TextView) rootView.findViewById(R.id.detail);

        title.setText(getString(R.string.place_title));
        address.setText(getString(R.string.place_address));
        phone.setText(getString(R.string.place_phone));
        description.setText(getString(R.string.place_description));
        detail.setText(getString(R.string.place_detail));

        ListView information = (ListView) rootView.findViewById(R.id.information);

        ArrayList<DetailInformationVO> detailInformation = new ArrayList<DetailInformationVO>(
                Arrays.asList(new DetailInformationVO[] {
                    new DetailInformationVO("item_time", "운영시간", getString(R.string.place_time)),
                    new DetailInformationVO("item_parking", "주차", getString(R.string.place_parking)),
                    new DetailInformationVO("item_fee", "이용 요금", getString(R.string.place_fee))
                }));

        adapter = new DetailInformationAdapter(getContext(), detailInformation);
        information.setAdapter(adapter);

        return rootView;
    }
}

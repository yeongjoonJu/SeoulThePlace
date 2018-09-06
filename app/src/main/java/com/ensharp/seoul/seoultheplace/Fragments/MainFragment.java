package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.HorizontalListView;
import com.ensharp.seoul.seoultheplace.UIElement.TagAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class MainFragment extends Fragment {

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<String> tags = new ArrayList<String>(
                Arrays.asList(new String[]{"#오늘 서울은?", "#감성적", "#인생샷 서울", "#데이트", "#레트로", "#여름"}));

        TagAdapter tagAdapter = new TagAdapter(getActivity(), tags);

        HorizontalListView tagListView = (HorizontalListView) rootView.findViewById(R.id.tagListView);
        tagListView.setAdapter(tagAdapter);

        return rootView;
    }
}

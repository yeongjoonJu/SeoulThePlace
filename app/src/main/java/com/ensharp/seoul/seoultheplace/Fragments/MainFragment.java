package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ensharp.seoul.seoultheplace.Course.PlaceView.CourseFragmentPagerAdapter;
import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.CourseAdapter;
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
                Arrays.asList(new String[]{"친구끼리", "가족끼리"}));

        TagAdapter tagAdapter = new TagAdapter(getActivity(), tags);
        tagAdapter.setMainListView(getActivity(), (ListView) rootView.findViewById(R.id.course_list_view));
        HorizontalListView tagListView = (HorizontalListView) rootView.findViewById(R.id.tagListView);
        tagListView.setAdapter(tagAdapter);

        return rootView;
    }
}

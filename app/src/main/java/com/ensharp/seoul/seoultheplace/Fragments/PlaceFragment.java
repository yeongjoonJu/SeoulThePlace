package com.ensharp.seoul.seoultheplace.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.DetailInformationVO;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.DetailInformationAdapter;
import com.ensharp.seoul.seoultheplace.UIElement.PlaceViewPagerAdapter;
import com.yalantis.phoenix.PullToRefreshView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class PlaceFragment extends Fragment {

    private View rootView;
    private DetailInformationAdapter adapter;
    private ImageView[] dots;
    private int dotCount;

    private PullToRefreshView destroyView;

    public PlaceFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_place, container, false);
        destroyView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_destroy);

        destroyView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                destroyView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().onBackPressed();
                    }
                }, 1000);
            }
        });

        // needed information.
        String[] images = { getString(R.string.image_01), getString(R.string.image_02),
                getString(R.string.image_03), getString(R.string.image_04)};
        int placeCount = 3;

        setPlaceImages(images);
        setPlaceCountImage(placeCount);

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
        detail.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

        ListView information = (ListView) rootView.findViewById(R.id.information);

        ArrayList<DetailInformationVO> detailInformation = new ArrayList<DetailInformationVO>(
                Arrays.asList(new DetailInformationVO[] {
                    new DetailInformationVO("item_time", "운영시간", getString(R.string.place_time)),
                    new DetailInformationVO("item_parking", "주차", getString(R.string.place_parking)),
                    new DetailInformationVO("item_fee", "이용 요금", getString(R.string.place_fee))
                }));

        adapter = new DetailInformationAdapter(getContext(), detailInformation);
        information.setAdapter(adapter);

        getTotalHeightOfListView(information);

        return rootView;
    }

    private void setPlaceImages(String[] images) {
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.images);
        LinearLayout sliderDotsPanel = (LinearLayout) rootView.findViewById(R.id.sliderDots);
        PlaceViewPagerAdapter placeViewPagerAdapter = new PlaceViewPagerAdapter(getContext(), images);

        viewPager.setAdapter(placeViewPagerAdapter);

        dotCount = placeViewPagerAdapter.getCount();
        dots = new ImageView[dotCount];

        for (int i = 0; i < dotCount; i++) {
            dots[i] = new ImageView(rootView.getContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(rootView.getContext(), R.drawable.item_non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8,0,8,0);

            sliderDotsPanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(rootView.getContext(), R.drawable.item_active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotCount; i++)
                    dots[i].setImageDrawable(ContextCompat.getDrawable(rootView.getContext(), R.drawable.item_non_active_dot));

                dots[position].setImageDrawable(ContextCompat.getDrawable(rootView.getContext(), R.drawable.item_active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setPlaceCountImage(int index) {
        FrameLayout numberIndicator = (FrameLayout) rootView.findViewById(R.id.numberIndicator);

        getLayoutInflater().inflate(R.layout.item_place_index, numberIndicator);
        TextView indexInImage = (TextView) numberIndicator.findViewById(R.id.index);
        indexInImage.setText(Integer.toString(index));

        if (index != 0)
            numberIndicator.setVisibility(View.VISIBLE);
        else
            numberIndicator.setVisibility(View.GONE);
    }

    public void getTotalHeightOfListView(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        int totalHeight = 0;

        for (int i = 0; i < adapter.getCount(); i++) {
            View view = adapter.getView(i, null, listView);

            view.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}

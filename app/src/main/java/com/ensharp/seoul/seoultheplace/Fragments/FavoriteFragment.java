package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.FloatingButton.FloatingActionButton;
import com.ensharp.seoul.seoultheplace.UIElement.FloatingButton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private static final String[] CHANNELS = new String[]{"좋아요", "맞춤제작"};
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();
    private View rootView;

    private FrameLayout actionMenuLayout;
    private FloatingActionsMenu actionsMenu;
    private FloatingActionButton nearCourse;
    private FloatingActionButton createCourse;

    private MagicIndicator magicIndicator;
    private CommonNavigator commonNavigator;

    private CustomizedFragment customizedFragment;
    private boolean isPlusButtonExpanded = false;

    private MainActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        mActivity = (MainActivity)getActivity();
        initFragments();
        initMagicIndicator();

        actionMenuLayout = rootView.findViewById(R.id.plus_button_background_layout);

        mFragmentContainerHelper.handlePageSelected(0, false);
        switchPages(0);

        actionsMenu = rootView.findViewById(R.id.multiple_actions);
        nearCourse = (FloatingActionButton) rootView.findViewById(R.id.action_a);
        createCourse = (FloatingActionButton) rootView.findViewById(R.id.action_b);

        actionsMenu.passParentLayout(actionMenuLayout);
        actionsMenu.passFragment(this);
        nearCourse.setOnClickListener(onNearCourseListener);
        createCourse.setOnClickListener(onCreateCourseListener);

        return rootView;
    }

    private void switchPages(int index) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;

        for (int i = 0; i < mFragments.size(); i++) {
            if (i == index) continue;
            fragment = mFragments.get(i);
            if(i==0)
                ((LikeFragment)fragment).renew();
            if (fragment.isAdded())
                fragmentTransaction.hide(fragment);
        }

        fragment = mFragments.get(index);

        if (fragment.isAdded()) fragmentTransaction.show(fragment);
        else fragmentTransaction.add(R.id.fragment_container, fragment);

        fragmentTransaction.commitAllowingStateLoss();

        if (index == 0) actionMenuLayout.setVisibility(View.INVISIBLE);
        else actionMenuLayout.setVisibility(View.VISIBLE);
    }

    private void initFragments() {
        // test fragment
        LikeFragment likeFragment = new LikeFragment();
        mFragments.add(likeFragment);

        // user customized course fragment
        customizedFragment = new CustomizedFragment();
        mFragments.add(customizedFragment);

    }

    private void initMagicIndicator() {
        magicIndicator = (MagicIndicator) rootView.findViewById(R.id.magic_indicator);
        magicIndicator.setBackgroundResource(R.drawable.round_indicator_background);

        commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdapter(commonNavigatorAdapter);

        magicIndicator.setNavigator(commonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(magicIndicator);

    }

    public void setIsExpanded(boolean value) {
        isPlusButtonExpanded = value;
        ListView listView = ((CustomizedFragment)mFragments.get(1)).getListView();
        if (isPlusButtonExpanded) listView.setEnabled(false);
        else listView.setEnabled(true);
    }

    private FloatingActionButton.OnClickListener onNearCourseListener = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), "actionA clicked", Toast.LENGTH_SHORT).show();
        }
    };

    private FloatingActionButton.OnClickListener onCreateCourseListener = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), "actionB clicked", Toast.LENGTH_SHORT).show();
            mActivity.changeModifyFragment(new ArrayList<PlaceVO>());
        }
    };

    private CommonNavigatorAdapter commonNavigatorAdapter = new CommonNavigatorAdapter() {
        @Override
        public int getCount() {
            return CHANNELS.length;
        }

        @Override
        public IPagerTitleView getTitleView(Context context, int index) {
            ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
            clipPagerTitleView.setText(CHANNELS[index]);
            clipPagerTitleView.setTextColor(Color.parseColor("#E94220"));
            clipPagerTitleView.setClipColor(Color.WHITE);
            clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isPlusButtonExpanded) {
                        mFragmentContainerHelper.handlePageSelected(index);
                        switchPages(index);
                    }
                }
            });
            return clipPagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            float navigatorHeight = context.getResources().getDimension(R.dimen.common_navigator_height);
            float borderWidth = UIUtil.dip2px(context, 1);
            float lineHeight = navigatorHeight - 2 * borderWidth;
            indicator.setLineHeight(lineHeight);
            indicator.setRoundRadius(lineHeight / 2);
            indicator.setYOffset(borderWidth);
            indicator.setColors(Color.parseColor("#BC2A2A"));
            return indicator;
        }
    };
}

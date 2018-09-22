package com.ensharp.seoul.seoultheplace.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.R;

public class MakersFragment extends Fragment {

    LinearLayout LL ;
    Button btn;
    MainActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.makers, container, false);
        LL = (LinearLayout)view.findViewById(R.id.makersLayout);
        Animation anima = AnimationUtils.loadAnimation(getActivity(),R.anim.alpha);
        LL.startAnimation(anima);

        mActivity = (MainActivity)getActivity();
        btn = (Button)view.findViewById(R.id.makersButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.ChangeSettingFragment();
            }
        });

        return view;
    }
}

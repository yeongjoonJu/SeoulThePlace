package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.Login.LoginBackgroundActivity;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.R;

public class SettingFragment extends Fragment implements View.OnClickListener {
    public SettingFragment() {
    }

    Button[] Btn;
    Button logoutBtn;
    Button introduceBtn;
    Button makersBtn;
    MainActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        mActivity = (MainActivity)getActivity();
        logoutBtn = (Button)rootView.findViewById(R.id.logout);
        introduceBtn = (Button)rootView.findViewById(R.id.introduceApps);
        makersBtn = (Button)rootView.findViewById(R.id.makers);
        Btn = new Button[]{logoutBtn,introduceBtn,makersBtn};
        for(int i = 0 ; i < Btn.length;i++){
            Btn[i].setOnClickListener(this);
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout:
                SharedPreferences sf = getActivity().getSharedPreferences("data",0);
                SharedPreferences.Editor editor = sf.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getActivity(),LoginBackgroundActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.introduceApps:
                Toast.makeText(getContext(),"얍얍얍",Toast.LENGTH_SHORT).show();
                break;
            case R.id.makers:
                mActivity.ChangeMakersFragment();
                break;
        }
    }
}

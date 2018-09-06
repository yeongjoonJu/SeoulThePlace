package com.ensharp.seoul.seoultheplace.Login;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.Login.KakaoLogin.GlobalApplication;
import com.ensharp.seoul.seoultheplace.R;
import com.google.android.gms.common.SignInButton;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;

public class MakeIDFragment extends android.support.v4.app.Fragment {

    TextView email;
    TextView name;
    TextView age;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.makeid,null); //view를 불러온다.

        email = (TextView)view.findViewById(R.id.email);
        name =(TextView)view.findViewById(R.id.name);

        if(LoginFragment.email != null){
            email.setText(LoginFragment.email);
        }
        if(LoginFragment.name != null){
            name.setText(LoginFragment.name);
        }


        return view;//view를 불렀으니 view를 돌려준다.
    }
}

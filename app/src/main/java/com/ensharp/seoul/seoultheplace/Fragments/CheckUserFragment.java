package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.Login.LoginBackgroundActivity;
import com.ensharp.seoul.seoultheplace.R;

public class CheckUserFragment extends android.support.v4.app.Fragment {

    TextView email;
    TextView passwd;
    TextView name;
    TextView age;
    TextView sex;
    TextView type;
    Button logout;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.userdata, null); //view를 불러온다.
        email = (TextView)view.findViewById(R.id.checkemail);
        passwd = (TextView)view.findViewById(R.id.checkpasswd);
        name = (TextView)view.findViewById(R.id.checkname);
        logout = (Button)view.findViewById(R.id.logout);
        age = (TextView)view.findViewById(R.id.checkage);
        sex = (TextView)view.findViewById(R.id.checksex);
        type = (TextView)view.findViewById(R.id.checktype);

        //logout.setOnClickListener(this);

        SharedPreferences sf = getActivity().getSharedPreferences("data",0);

        email.setText(sf.getString("email",""));
        passwd.setText(sf.getString("password",""));
        name.setText(sf.getString("name",""));
        age.setText(sf.getString("age",""));
        sex.setText(sf.getString("sex",""));
        type.setText(sf.getString("type",""));


        return view;//view를 불렀으니 view를 돌려준다.
    }

}
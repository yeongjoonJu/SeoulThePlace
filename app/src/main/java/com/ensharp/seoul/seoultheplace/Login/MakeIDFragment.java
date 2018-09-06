package com.ensharp.seoul.seoultheplace.Login;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.Login.KakaoLogin.GlobalApplication;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.R;
import com.google.android.gms.common.SignInButton;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;

import java.util.ArrayList;

public class MakeIDFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    TextView email;
    TextView name;
    Spinner age;
    String stringAge;
    Button man;
    Button woman;
    boolean ishim = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.makeid,null); //view를 불러온다.

        email = (TextView)view.findViewById(R.id.email);
        name =(TextView)view.findViewById(R.id.name);
        age = (Spinner)view.findViewById(R.id.age);
        man = (Button)view.findViewById(R.id.manbtn);
        man.setOnClickListener(this);
        woman =(Button)view.findViewById(R.id.womanbtn);
        woman.setOnClickListener(this);

        ArrayList<String> list = new ArrayList<>();
        for(int lists = 14;lists<80;lists++)
        list.add(String.valueOf(lists));

        ArrayAdapter spinnerAdapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, list);
        age.setAdapter(spinnerAdapter);

        //event listener
        age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringAge = String.valueOf(age.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(LoginFragment.email != null){
            email.setText(LoginFragment.email);
        }
        if(LoginFragment.name != null){
            name.setText(LoginFragment.name);
        }


        return view;//view를 불렀으니 view를 돌려준다.
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.manbtn:
                ishim = true;
                man.setEnabled(false);
                woman.setEnabled(true);
                break;
            case R.id.womanbtn:
                ishim =false;
                woman.setEnabled(false);
                man.setEnabled(true);
                break;
        }
    }
}

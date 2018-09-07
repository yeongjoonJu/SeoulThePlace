package com.ensharp.seoul.seoultheplace.Login;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.Login.KakaoLogin.GlobalApplication;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.R;
import com.facebook.all.All;
import com.google.android.gms.common.SignInButton;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;

import java.util.ArrayList;

public class MakeIDFragment extends android.support.v4.app.Fragment implements View.OnClickListener, TextWatcher {

    EditText email;
    EditText name;
    EditText passwd;
    Spinner age;
    String stringAge;
    Button man;
    Button woman;
    Button signUp;
    boolean ishim = false;
    boolean checkSex = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.makeid,null); //view를 불러온다.

        email = (EditText)view.findViewById(R.id.email);
        email.addTextChangedListener(this);
        name =(EditText)view.findViewById(R.id.name);
        name.addTextChangedListener(this);
        passwd=(EditText)view.findViewById(R.id.password);
        passwd.addTextChangedListener(this);
        age = (Spinner)view.findViewById(R.id.age);
        man = (Button)view.findViewById(R.id.manbtn);
        man.setOnClickListener(this);
        woman =(Button)view.findViewById(R.id.womanbtn);
        woman.setOnClickListener(this);
        signUp =(Button)view.findViewById(R.id.SignUp);
        signUp.setOnClickListener(this);

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
                AllCheckData();
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
                checkSex = true;
                AllCheckData();
                break;
            case R.id.womanbtn:
                ishim =false;
                woman.setEnabled(false);
                man.setEnabled(true);
                checkSex = true;
                AllCheckData();
                break;
            case R.id.SignUp:
                Intent userData = new Intent(getActivity(),CheckUserActivity.class);
                userData.putExtra("name",String.valueOf(name.getText()));
                userData.putExtra("email",String.valueOf(email.getText()));
                userData.putExtra("password",String.valueOf(passwd.getText()));
                userData.putExtra("sex",checkSex);
                startActivity(userData);
                getActivity().finish();
                break;
        }
    }

    public void AllCheckData(){
        if(checkSex&&email!=null&&name!=null&&passwd!=null){
            signUp.setEnabled(true);
        }
        else{
            signUp.setEnabled(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        AllCheckData();
    }
}

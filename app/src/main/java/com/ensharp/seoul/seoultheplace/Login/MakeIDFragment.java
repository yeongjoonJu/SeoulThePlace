package com.ensharp.seoul.seoultheplace.Login;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.R;

import java.util.ArrayList;

public class MakeIDFragment extends android.support.v4.app.Fragment implements View.OnClickListener, TextWatcher {

    EditText email;
    EditText name;
    EditText passwd;
    Spinner age;
    Button man;
    Button woman;
    Button signUp;
    Button type1;
    Button type2;
    Button type3;
    Button type4;

    String isman = null;
    String isage = null;
    String type = null;

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
        type1 = (Button)view.findViewById(R.id.travel1);
        type1.setOnClickListener(this);
        type2 = (Button)view.findViewById(R.id.travel2);
        type2.setOnClickListener(this);
        type3 = (Button)view.findViewById(R.id.travel3);
        type3.setOnClickListener(this);
        type4 = (Button)view.findViewById(R.id.travel4);
        type4.setOnClickListener(this);

        ArrayList<String> list = new ArrayList<>();
        for(int lists = 14;lists<80;lists++)
            list.add(String.valueOf(lists));

        ArrayAdapter spinnerAdapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, list);
        age.setAdapter(spinnerAdapter);

        //event listener
        age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isage = String.valueOf(age.getItemAtPosition(position));
                AllCheckData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isage = "없음";
                AllCheckData();
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
                man.setEnabled(false);
                woman.setEnabled(true);
                isman = "man";
                break;
            case R.id.womanbtn:
                woman.setEnabled(false);
                man.setEnabled(true);
                isman = "woman";
                break;
            case R.id.travel1:
                type = "1번여행코스";
                type1.setEnabled(false);
                type2.setEnabled(true);
                type3.setEnabled(true);
                type4.setEnabled(true);
                break;
            case R.id.travel2:
                type = "2번여행코스";
                type1.setEnabled(true);
                type2.setEnabled(false);
                type3.setEnabled(true);
                type4.setEnabled(true);
                break;
            case R.id.travel3:
                type = "3번여행코스";
                type1.setEnabled(true);
                type2.setEnabled(true);
                type3.setEnabled(false);
                type4.setEnabled(true);
                break;
            case R.id.travel4:
                type = "4번여행코스";
                type1.setEnabled(true);
                type2.setEnabled(true);
                type3.setEnabled(true);
                type4.setEnabled(false);
                break;
            case R.id.SignUp:
                Intent userData = new Intent(getActivity(),MainActivity.class);
                userData.putExtra("name",String.valueOf(name.getText()));
                userData.putExtra("email",String.valueOf(email.getText()));
                userData.putExtra("password",String.valueOf(passwd.getText()));
                userData.putExtra("sex",isman);
                userData.putExtra("age",isage);
                userData.putExtra("type",type);

                SetSharedPreference();
                startActivity(userData);
                getActivity().finish();
                break;
        }
        AllCheckData();
    }

    public void SetSharedPreference(){
        SharedPreferences sf = getActivity().getSharedPreferences("data",0);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("email",String.valueOf(email.getText()));
        editor.putString("password",String.valueOf(passwd.getText()));
        editor.putString("name",String.valueOf(name.getText()));
        editor.putString("age",isage);
        editor.putString("sex",isman);
        editor.putString("type",type);
        editor.commit();
    }

    public void AllCheckData(){
        if(isman!=null&&email!=null&&name!=null&&passwd!=null&&type!=null){
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
package com.ensharp.seoul.seoultheplace.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.R;

import java.util.ArrayList;

public class SubDataFragment extends Fragment implements View.OnClickListener {

    Button type1;
    Button type2;
    Button type3;
    Button type4;
    String type = "";

    Spinner age;
    String isage = "2000";

    Button signUp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subdatasignup, null); //view를 불러온다.

        age = (Spinner)view.findViewById(R.id.spinner);

        type1 = (Button)view.findViewById(R.id.type1);
        type2 = (Button)view.findViewById(R.id.type2);
        type3 = (Button)view.findViewById(R.id.type3);
        type4 = (Button)view.findViewById(R.id.type4);
        type1.setOnClickListener(this);
        type2.setOnClickListener(this);
        type3.setOnClickListener(this);
        type4.setOnClickListener(this);

        signUp = (Button)view.findViewById(R.id.lastsignup);
        signUp.setOnClickListener(this);

        ArrayList<String> list = new ArrayList<>();
        for(int a = 2000; a>1940;a--) {
            list.add(String.valueOf(a));
        }
        ArrayAdapter spinnerAdapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, list);
        age.setAdapter(spinnerAdapter);

        //event listener
        age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isage = String.valueOf(age.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isage = "없음";
            }
        });
        return view;
    }
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.type1:
                type = "가족끼리";
                Log.d("SignUp : ","가족끼리");
                type1.setEnabled(false);
                type2.setEnabled(true);
                type3.setEnabled(true);
                type4.setEnabled(true);
                break;
            case R.id.type2:
                type = "친구끼리";
                type1.setEnabled(true);
                type2.setEnabled(false);
                type3.setEnabled(true);
                type4.setEnabled(true);
                break;
            case R.id.type3:
                type = "연인끼리";
                type1.setEnabled(true);
                type2.setEnabled(true);
                type3.setEnabled(false);
                type4.setEnabled(true);
                break;
            case R.id.type4:
                type = "혼자서";
                type1.setEnabled(true);
                type2.setEnabled(true);
                type3.setEnabled(true);
                type4.setEnabled(false);
                break;
            case R.id.lastsignup:
                if(CheckAll()) {
                    Log.d("SignUp : ","Ok");
                    SaveData();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                break;
        }
    }
    public boolean CheckAll(){
        if(isage.equals("")&&type.equals("")){
            Toast.makeText(getActivity(),"아직 모두 선택하지 않았습니다.",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public void SaveData(){
        SharedPreferences signupOri = getActivity().getSharedPreferences("data",0);
        SharedPreferences.Editor signup = signupOri.edit();
        signup.putString("age",isage);
        signup.putString("type",type);
        signup.apply();
        SendData(signupOri);
    }

    public void SendData(SharedPreferences signup){
        String[] memberCategory = new String[]{ signup.getString("email",null),
                                                signup.getString("password","snsLogin"),
                                                signup.getString("name",null),
                                                signup.getString("age", null),
                                                signup.getString("sex",null),
                                                signup.getString("type",null)};
        Log.d("SignUp : ",memberCategory[0]);
        Log.d("SignUp : ",memberCategory[1]);
        Log.d("SignUp : ",memberCategory[2]);
        Log.d("SignUp : ",memberCategory[3]);
        Log.d("SignUp : ",memberCategory[4]);
        Log.d("SignUp : ",memberCategory[5]);
    }
}

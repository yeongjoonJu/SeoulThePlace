package com.ensharp.seoul.seoultheplace.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.R;

import java.util.ArrayList;

public class OriginSignUpFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    EditText email;
    EditText name;
    EditText passwd;
    EditText passwd1;
    Button man;
    Button woman;
    Button signUp;

    String isman = null;

    LoginBackgroundActivity LActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.originsignup,null); //view를 불러온다.

        LActivity = (LoginBackgroundActivity)getActivity();

        email = (EditText)view.findViewById(R.id.oriemail);
        name =(EditText)view.findViewById(R.id.oriname);
        passwd=(EditText)view.findViewById(R.id.oripasswd);
        passwd1=(EditText)view.findViewById(R.id.oripasswd1);
        man = (Button)view.findViewById(R.id.manbtn);
        man.setOnClickListener(this);
        woman =(Button)view.findViewById(R.id.womanbtn);
        woman.setOnClickListener(this);
        signUp =(Button)view.findViewById(R.id.orisignup);
        signUp.setOnClickListener(this);

        ArrayList<String> list = new ArrayList<>();
        for(int lists = 14;lists<80;lists++)
            list.add(String.valueOf(lists));
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
            case R.id.orisignup:
                if(AllCheckData()) {
                    SetSharedPreference();
                    LActivity.SubDataFragmentChanged();
                }
                break;
        }
    }

    public void SetSharedPreference(){
        SharedPreferences sf = getActivity().getSharedPreferences("data",0);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("email",String.valueOf(email.getText()));
        editor.putString("password",String.valueOf(passwd.getText()));
        editor.putString("name",String.valueOf(name.getText()));
        editor.putString("sex",isman);
        editor.commit();
    }

    public boolean AllCheckData(){
        if(isman==null){
            Toast.makeText(getActivity(),"성별을 눌러주세요.",Toast.LENGTH_LONG).show();
            return false;
        }
        if(CheckName()&&CheckEmail()&&CheckPasswd()){
            Toast.makeText(getActivity(),"회원가입 성공",Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
    public boolean CheckEmail(){
        String emailPattren = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String mEmail = String.valueOf(email.getText());
        if(!mEmail.matches(emailPattren) && mEmail.length() > 0){
            Toast.makeText(getActivity(),"적절한 이메일이 아닙니다.",Toast.LENGTH_LONG).show();
            email.setFocusable(true);
            return false;
        }
        return true;
    }
    public boolean CheckPasswd(){
        if(!passwd.equals(passwd1)&&passwd.length()<8){
            Toast.makeText(getActivity(), "비밀번호가 서로 다르거나 8자 이하입니다.", Toast.LENGTH_LONG).show();
            passwd.setFocusable(true);
            return false;
        }
        return true;
    }
    public boolean CheckName(){
        if(String.valueOf(name.getText()).equals("")){
            Toast.makeText(getActivity(), "이름(별명)을 입력해주세요.", Toast.LENGTH_LONG).show();
            name.setFocusable(true);
            return false;
        }
        return true;
    }
}
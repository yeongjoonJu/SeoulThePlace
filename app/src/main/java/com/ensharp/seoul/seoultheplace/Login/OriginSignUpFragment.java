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

import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.R;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class OriginSignUpFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    EditText email;
    EditText name;
    EditText passwd;
    EditText passwd1;
    EditText phone;
    Button signUp;

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
        phone =(EditText)view.findViewById(R.id.oriPhone);

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
            case R.id.orisignup:
                if(AllCheckData()) {
                    SetSharedPreference();
                    LActivity.convertToTutorial();
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
        editor.putString("phone",String.valueOf(phone.getText()));
        editor.commit();
        LActivity.SendData(sf);
    }

    public boolean AllCheckData(){
        if(CheckName()&&CheckEmail()&&CheckPasswd()&&CheckPhone()){
            Toast.makeText(getActivity(),"회원가입 성공",Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
    public boolean CheckEmail(){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String mEmail = String.valueOf(email.getText());
        if(!mEmail.matches(emailPattern) || mEmail.length() < 0){
            Toast.makeText(getActivity(),"적절한 이메일이 아닙니다.",Toast.LENGTH_LONG).show();
            email.setFocusable(true);
            return false;
        }
        if(!LActivity.CheckDoubleEmail(String.valueOf(email.getText()))){
            Toast.makeText(getActivity(),"이미 가입된 이메일 입니다. SNS로그인이나 이메일로그인을 이용해주세요.",Toast.LENGTH_LONG).show();
            email.setFocusable(true);
            return false;
        }
        return true;
    }

    public boolean CheckPhone(){
        String phonePattern = "01(?:0|1|[6-9])(?:[0-9]{3,4})[0-9]{4}";
        String mPhone = String.valueOf(this.phone.getText());
        if(!Pattern.matches(phonePattern,mPhone) || mPhone.length()<0){
            Toast.makeText(getActivity(),"적절한 전화번호가 아닙니다.",Toast.LENGTH_LONG).show();
            phone.setFocusable(true);
            return false;
        }
        return true;
    }

    public boolean CheckPasswd(){
        String passwd = this.passwd.getText().toString();
        String passwd1 = this.passwd1.getText().toString();
        if(!passwd.equals(passwd1)){
            Toast.makeText(getActivity(), "비밀번호가 서로 다릅니다.", Toast.LENGTH_LONG).show();
            this.passwd.setFocusable(true);
            return false;
        }
        else if (passwd.length()<8){
            Toast.makeText(getActivity(), "비밀번호가 너무 짧습니다.", Toast.LENGTH_LONG).show();
            this.passwd.setFocusable(true);
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
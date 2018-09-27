package com.ensharp.seoul.seoultheplace.Login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.R;

public class EmailLoginFragment extends Fragment implements View.OnClickListener, TextWatcher {
    private View view;

    String TAG = "LoginEmail";
    EditText email;
    EditText password;
    Button Login;
    Button searchPW;
    private LoginBackgroundActivity LActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.emaillogin, null); //view를 불러온다.
        LActivity = (LoginBackgroundActivity)getActivity();

        email = (EditText)view.findViewById(R.id.emailloginEditText);
        email.addTextChangedListener(this);
        password=(EditText)view.findViewById(R.id.passwdloginEditText);
        Login = (Button)view.findViewById(R.id.emailloginbtn);
        Login.setOnClickListener(this);
        searchPW = (Button)view.findViewById(R.id.searchPassword);
        searchPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LActivity.SearchPassWDChanger();
            }
        });
        Log.d(TAG,"Ready To EmailLogin");
        return view;
    }

    @Override
    public void onClick(View v) {
        if(CheckEmail()){
            LActivity.NextActivity();
        }
    }
    public boolean CheckEmail(){
        if(LActivity.LoginEmail(String.valueOf(email.getText()),String.valueOf(password.getText()))){
            Log.d(TAG,"Success Email Login");
            Toast.makeText(getActivity(),"로그인 성공.",Toast.LENGTH_LONG).show();
            return true;
        }
        Log.d(TAG,"Fail Email Login");
        Toast.makeText(getActivity(),"이메일이나 비밀번호가 틀렸습니다.",Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

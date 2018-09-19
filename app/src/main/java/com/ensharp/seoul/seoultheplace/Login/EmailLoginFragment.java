package com.ensharp.seoul.seoultheplace.Login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.R;

public class EmailLoginFragment extends Fragment implements View.OnClickListener {
    private View view;

    EditText email;
    EditText password;
    Button Login;
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
        password=(EditText)view.findViewById(R.id.passwdloginEditText);
        Login = (Button)view.findViewById(R.id.emailloginbtn);
        Login.setOnClickListener(this);

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
            Toast.makeText(getActivity(),"로그인 성공.",Toast.LENGTH_LONG).show();
            return true;
        }
        Toast.makeText(getActivity(),"이메일이나 비밀번호가 틀렸습니다.",Toast.LENGTH_LONG).show();
        return false;
    }
}

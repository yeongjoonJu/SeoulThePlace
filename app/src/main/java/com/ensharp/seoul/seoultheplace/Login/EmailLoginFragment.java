package com.ensharp.seoul.seoultheplace.Login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ensharp.seoul.seoultheplace.R;

public class EmailLoginFragment extends Fragment implements View.OnClickListener {
    private View view;

    EditText email;
    EditText password;
    Button Login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.emaillogin, null); //view를 불러온다.
        email = (EditText)view.findViewById(R.id.emaillogin);
        password=(EditText)view.findViewById(R.id.passwdlogin);
        Login = (Button)view.findViewById(R.id.emailloginbtn);
        Login.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        CheckEmail();

    }
    public boolean CheckEmail(){
        email.getText();
        password.getText();
        return false;
    }
}

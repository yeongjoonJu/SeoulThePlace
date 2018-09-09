package com.ensharp.seoul.seoultheplace.Login;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.R;

public class SNSSignUpFragment extends Fragment implements View.OnClickListener {
    private View view;
    SharedPreferences prefs;
    EditText email;
    EditText name;
    Button isman;
    Button iswoman;
    Button signup;
    String sex = null;
    LoginBackgroundActivity LActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.snssignup, null); //view를 불러온다.

        LActivity = (LoginBackgroundActivity)getActivity();

        email = (EditText)view.findViewById(R.id.snsemail);
        name = (EditText)view.findViewById(R.id.snsname);
        isman = (Button)view.findViewById(R.id.snsman);
        isman.setOnClickListener(this);
        iswoman = (Button)view.findViewById(R.id.snswoman);
        iswoman.setOnClickListener(this);
        signup = (Button)view.findViewById(R.id.snssignup);
        signup.setOnClickListener(this);

        prefs = getActivity().getSharedPreferences("data", 0);
        if(!prefs.getString("email", "0").equals("")){
            email.setText(prefs.getString("email","0"));
            email.setClickable(false);
        }
        if(!prefs.getString("name","0").equals("")){
            name.setText(prefs.getString("name","0"));
            name.setClickable(false);
        }
        if(prefs.getString("sex","0").equals("man")){
            isman.setEnabled(false);
            iswoman.setEnabled(false);
            isman.setBackgroundColor(Color.GRAY);
            iswoman.setBackgroundColor(Color.DKGRAY);
            sex = "man";
        }
        else if(prefs.getString("sex","").equals("woman")){
            iswoman.setEnabled(false);
            isman.setEnabled(false);
            isman.setBackgroundColor(Color.DKGRAY);
            iswoman.setBackgroundColor(Color.GRAY);
            sex = "woman";
        }
        prefs.edit().clear();
        prefs.edit().apply();
     return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.snsman:
                iswoman.setEnabled(true);
                isman.setEnabled(false);
                isman.setBackgroundColor(Color.GRAY);
                iswoman.setBackgroundColor(Color.DKGRAY);
                sex = "man";
                break;
            case R.id.snswoman:
                iswoman.setEnabled(false);
                isman.setEnabled(true);
                isman.setBackgroundColor(Color.DKGRAY);
                iswoman.setBackgroundColor(Color.GRAY);
                sex = "woman";
                break;
            case R.id.snssignup:
                if(sex==null){
                    Toast.makeText(getActivity(),"성별을 눌러주세요",Toast.LENGTH_LONG).show();
                    break;
                }
                if(CheckEmail()&& CheckName()){
                    prefs.edit().putString("email",String.valueOf(email.getText()));
                    prefs.edit().putString("name",String.valueOf(name.getText()));
                    prefs.edit().putString("sex",sex);
                    prefs.edit().apply();
                    LActivity.SubDataFragmentChanged();
                }
                break;
        }
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
    public boolean CheckName(){
        if(String.valueOf(name.getText()).equals("")){
            Toast.makeText(getActivity(), "이름(별명)을 입력해주세요.", Toast.LENGTH_LONG).show();
            name.setFocusable(true);
            return false;
        }
        return true;
    }
}

package com.ensharp.seoul.seoultheplace.Login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.R;

import java.util.regex.Pattern;

public class SearchPasswd extends Fragment {

    EditText email;
    EditText name;
    EditText phone;
    Button btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.email_search_password, null); //view를 불러온다.
        initData(view);

        return view;
    }

    public void initData(View view){
        email = (EditText)view.findViewById(R.id.searchEmail);
        name = (EditText)view.findViewById(R.id.searchName);
        phone = (EditText)view.findViewById(R.id.searchPhone);
        btn = (Button)view.findViewById(R.id.searchPasswdOK);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AllCheckData()) {
                    CheckData();
                }
            }
        });
    }

    public boolean CheckData(){
        DAO dao = new DAO();
        String check = dao.SearchPasswd(String.valueOf(email.getText()), String.valueOf(name.getText()), String.valueOf(phone.getText()));
        Log.e("SearchPasswd : ", check);
        if(check.equals("null")) {
            Toast.makeText(getContext(), "없는 이메일이거나 정보가 틀렸습니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(getContext(),"비밀번호는 "+check+"입니다.",Toast.LENGTH_LONG).show();
        return true;
    }

    public boolean AllCheckData(){
        if(CheckName()&&CheckEmail()&&CheckPhone()){
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

}

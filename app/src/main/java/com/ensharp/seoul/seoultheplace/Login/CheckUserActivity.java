package com.ensharp.seoul.seoultheplace.Login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.R;

import org.w3c.dom.Text;

public class CheckUserActivity extends Activity implements View.OnClickListener {

    TextView id;
    TextView passwd;
    TextView name;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checksenddata);

        id = (TextView)findViewById(R.id.checkid);
        passwd = (TextView)findViewById(R.id.checkpasswd);
        name = (TextView)findViewById(R.id.checkname);
        logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(this);

        Intent intent = new Intent(this.getIntent());
        id.setText(intent.getStringExtra("email"));
        passwd.setText(intent.getStringExtra("password"));
        name.setText(intent.getStringExtra("name"));

        SharedPreferences sf = getSharedPreferences("data",0);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("id",String.valueOf(id.getText()));
        editor.putString("password",String.valueOf(passwd.getText()));
        editor.putString("name",String.valueOf(name.getText()));
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout:
                SharedPreferences sf = getSharedPreferences("data",0);
                SharedPreferences.Editor editor = sf.edit();
                editor.remove("id");
                editor.remove("password");
                editor.remove("name");
                editor.commit();
                Intent intent = new Intent(this,LoginBackgroundActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

}

package com.ensharp.seoul.seoultheplace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ensharp.seoul.seoultheplace.Login.LoginBackgroundActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent login = new Intent(this,LoginBackgroundActivity.class);
        startActivity(login);
        finish();
    }
}

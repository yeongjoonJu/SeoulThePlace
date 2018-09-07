package com.ensharp.seoul.seoultheplace;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dao = new DAO();
        dao.setUrl("http://ec2-52-78-245-211.ap-northeast-2.compute.amazonaws.com/user/register");
        dao.insertMemberData(new String[]{"Id", "Password", "Name", "Age", "Gender", "Type"});
        dao.execute();
    }
}

package com.ensharp.seoul.seoultheplace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DAO dao = new DAO();
        JSONArray result = null;

        /*
        try {
           여기에 dao.함수() 해서 테스트
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }
}

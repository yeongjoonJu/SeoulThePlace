package com.ensharp.seoul.seoultheplace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DAO dao = new DAO();
        dao.execute();
        JSONArray jsonArray = dao.getUserCourseData("친구끼리, 연인끼리, 가족끼리", "임석호");
        for(int i=0;i<jsonArray.length();i++) {
            try {
                Log.i("yeongjoon", jsonArray.getJSONObject(i).getString("User_Likes"));
                Log.i("yeongjoon", jsonArray.getJSONObject(i).getString("Code"));
                Log.i("yeongjoon", jsonArray.getJSONObject(i).getString("Name"));
                Log.i("yeongjoon", jsonArray.getJSONObject(i).getString("Likes"));
                Log.i("yeongjoon", jsonArray.getJSONObject(i).getString("Image"));
                Log.i("yeongjoon", jsonArray.getJSONObject(i).getString("location"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}

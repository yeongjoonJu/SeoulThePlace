package com.ensharp.seoul.seoultheplace;

import android.annotation.TargetApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.ensharp.seoul.seoultheplace.UIElement.HorizontalListView;
import com.ensharp.seoul.seoultheplace.UIElement.TagAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> tags = new ArrayList<String>(
                Arrays.asList(new String[]{"#오늘 서울은?", "#감성적", "#인생샷 서울"}));

        TagAdapter tagAdapter = new TagAdapter(this, tags);

        HorizontalListView tageListView = (HorizontalListView) findViewById(R.id.tagListView);
        tageListView.setAdapter(tagAdapter);
    }
}

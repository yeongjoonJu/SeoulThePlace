package com.ensharp.seoul.seoultheplace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.support.v4.app.Fragment;
import com.ensharp.seoul.seoultheplace.Fragments.BookmarkFragment;
import com.ensharp.seoul.seoultheplace.Fragments.MainFragment;
import com.ensharp.seoul.seoultheplace.Fragments.SearchFragment;
import com.ensharp.seoul.seoultheplace.Fragments.SettingFragment;
import com.ensharp.seoul.seoultheplace.Login.LoginBackgroundActivity;
import android.util.Log;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private ImageButton[] bottomButtons;
    private Fragment[] fragments;
    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dao = new DAO();
        dao.insertMemberData(getIntent().getExtras());
        fragments = new Fragment[]{
                new MainFragment(), new SearchFragment(), new BookmarkFragment(), new SettingFragment()
        };

        // 하단 버튼 객체 초기화
        bottomButtons = new ImageButton[] {
                (ImageButton) findViewById(R.id.homeButton),
                (ImageButton) findViewById(R.id.searchButton),
                (ImageButton) findViewById(R.id.bookmarkButton),
                (ImageButton) findViewById(R.id.mypageButton)
        };

        for(int i=0; i<fragments.length; i++) {
            final android.support.v4.app.Fragment fragment = fragments[i];
            bottomButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, fragment)
                            .commit();
                }
            });
        }

        // 메인 fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, new MainFragment())
                .commit();
    }
}

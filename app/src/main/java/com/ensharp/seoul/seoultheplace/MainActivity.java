package com.ensharp.seoul.seoultheplace;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.support.v4.app.Fragment;

import com.ensharp.seoul.seoultheplace.Fragments.CourseFragment;
import com.ensharp.seoul.seoultheplace.Fragments.MainFragment;
import com.ensharp.seoul.seoultheplace.Fragments.SearchFragment;
import com.ensharp.seoul.seoultheplace.Fragments.PlaceFragment;

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
                new MainFragment(), new SearchFragment(), new CourseFragment(), new PlaceFragment()
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

    public void changeFragment() {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, new PlaceFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Change value in dp to pixels
     * @param dp
     * @param context
     * @return
     */
    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }
}

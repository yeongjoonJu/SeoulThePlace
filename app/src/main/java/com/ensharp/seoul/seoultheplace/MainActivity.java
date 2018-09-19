package com.ensharp.seoul.seoultheplace;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.support.v4.app.Fragment;

import com.ensharp.seoul.seoultheplace.Fragments.CourseFragment;
import com.ensharp.seoul.seoultheplace.Fragments.FavoriteFragment;
import com.ensharp.seoul.seoultheplace.Fragments.MainFragment;
import com.ensharp.seoul.seoultheplace.Fragments.SearchFragment;
import com.ensharp.seoul.seoultheplace.Fragments.PlaceFragment;
import com.ensharp.seoul.seoultheplace.Fragments.WebViewFragment;

public class MainActivity extends AppCompatActivity {
    private ImageButton[] bottomButtons;
    private Fragment[] fragments;
    private DAO dao;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // course, place 초기화
        Constant.initCourse();
        Constant.initPlaces();

        setContentView(R.layout.activity_main);

        dao = new DAO();
        dao.insertMemberData(getIntent().getExtras());
        fragments = new Fragment[]{
                new MainFragment(), new SearchFragment(), new FavoriteFragment(), new PlaceFragment("a333")
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

        String[] neededPermissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        ActivityCompat.requestPermissions(this, neededPermissions,0);
    }

    public void changeToWebFragment(String link) {
        final Fragment fragment = new WebViewFragment(link);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment, "WEB_FRAGMENT")
                .addToBackStack(null)
                .commit();
    }

    public void chagneToCourseFragment(int index) {
        final Fragment fragment = new CourseFragment(index);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void changeFragment(String courseCode, int index) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_bottom,R.anim.anim_slide_out_top,R.anim.anim_slide_in_bottom,R.anim.anim_slide_out_top);
        fragmentTransaction.replace(R.id.fragment, new PlaceFragment(courseCode, index));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Fragment webViewFragment = getSupportFragmentManager().findFragmentByTag("WEB_FRAGMENT");
        if (webViewFragment != null && webViewFragment.isVisible()) {
            Log.e("abcd", "back button pressed");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.
        }
    }

    public void pushStack() {
        int index = this.getSupportFragmentManager().getBackStackEntryCount() - 1;
        android.support.v4.app.FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
        String tag = backEntry.getName();
        Fragment myFragment = getSupportFragmentManager().findFragmentByTag(tag);
    }
}

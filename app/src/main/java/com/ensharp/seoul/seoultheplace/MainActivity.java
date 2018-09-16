package com.ensharp.seoul.seoultheplace;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;

import com.ensharp.seoul.seoultheplace.Course.CourseModifyFragment;
import com.ensharp.seoul.seoultheplace.Fragments.CourseFragment;
import com.ensharp.seoul.seoultheplace.Fragments.MainFragment;
import com.ensharp.seoul.seoultheplace.Fragments.SearchFragment;
import com.ensharp.seoul.seoultheplace.Fragments.PlaceFragment;
import com.ensharp.seoul.seoultheplace.Fragments.SettingFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageButton[] bottomButtons;
    private Fragment[] fragments;
    private Fragment currentFragment;
    private LinearLayout rootLayout;
    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // course, place 초기화
        Constant.initCourse();
        Constant.initPlaces();

        setContentView(R.layout.activity_main);
        dao = new DAO();
        //dao.insertMemberData(getIntent().getExtras());

        fragments = new Fragment[]{
                new MainFragment(), new SearchFragment(), new CourseFragment("j111"), new SettingFragment()
        };

        // 하단 버튼 객체 초기화
        bottomButtons = new ImageButton[] {
                (ImageButton) findViewById(R.id.homeButton),
                (ImageButton) findViewById(R.id.searchButton),
                (ImageButton) findViewById(R.id.bookmarkButton),
                (ImageButton) findViewById(R.id.mypageButton)
        };

        for(int i=0; i<fragments.length; i++) {
            final Fragment fragment = fragments[i];
            bottomButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentFragment = fragment;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, fragment)
                            .commit();
                }
            });
        }

        // 메인 fragment
        currentFragment = fragments[0];
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragments[0])
                .commit();

        rootLayout = (LinearLayout) findViewById(R.id.linear_wrapper);
        rootLayout.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int rootViewHeight = rootLayout.getRootView().getHeight();
                        int linearWrapperHeight = rootLayout.getHeight();
                        int diff = rootViewHeight - linearWrapperHeight;
                        // 키보드가 내려간 상태면
                        if(currentFragment.equals(fragments[1]) && diff < dpToPx(50)) {
                            ((SearchFragment)fragments[1]).viewVisible();
                        }
                        else {
                            ((SearchFragment)fragments[1]).viewInvisible();
                        }
                    }
                });
    }

    public float dpToPx(float valueInDp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    public void chagneToCourseFragment(int index) {
        final Fragment fragment = new CourseFragment(index);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }

    public void changeFragment(String courseCode, int index) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_bottom,R.anim.anim_slide_out_top,R.anim.anim_slide_in_bottom,R.anim.anim_slide_out_top);
        fragmentTransaction.replace(R.id.fragment, new PlaceFragment(courseCode, index));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void changeModifyFragment(List<PlaceVO> list){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right,R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
        fragmentTransaction.replace(R.id.fragment, new CourseModifyFragment(list));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }
}

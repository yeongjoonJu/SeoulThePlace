package com.ensharp.seoul.seoultheplace;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.ensharp.seoul.seoultheplace.Course.CourseModifyFragment;
import com.ensharp.seoul.seoultheplace.Fragments.*;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageButton[] bottomButtons;
    private Fragment[] fragments;
    private Fragment currentFragment;
    private LinearLayout rootLayout;
    private DAO dao;
    private int currentFragmentNumber = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // course, place 초기화

        setContentView(R.layout.activity_main);

        fragments = new Fragment[]{
                new MainFragment(), new SearchFragment(), new CourseFragment("c001"), new SettingFragment()
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
            final int nextFragmentNumber = i;
            bottomButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentFragment = fragment;
                    if(currentFragmentNumber <= nextFragmentNumber) {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
                                .replace(R.id.fragment, fragment)
                                .commit();
                    }
                    else{
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right)
                                .replace(R.id.fragment, fragment)
                                .commit();
                    }
                    DeleteBackStack();
                    currentFragmentNumber = nextFragmentNumber;
                }
            });
        }

        // 메인 fragment
        currentFragment = fragments[0];
        currentFragmentNumber = 0;
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
                            Log.i("yeongjoon", "키보드 내려감");
                        }
                        else {
                            ((SearchFragment)fragments[1]).viewInvisible();
                            Log.i("yeongjoon", "키보드 올라감");
                        }
                    }
                });
        String[] neededPermissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CALL_PHONE
        };

        ActivityCompat.requestPermissions(this, neededPermissions,0);
    }

    public void changeToWebFragment(String link) {
        final Fragment fragment = new WebViewFragment(link);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment, "WEB_FRAGMENT")
                .commit();
    }

    public float dpToPx(float valueInDp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);

    }

    public void chagneToCourseFragment(int index) {
        final Fragment fragment = new CourseFragment("c001");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }

    public void changeFragment(String courseCode, int index) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_slide_in_bottom,R.anim.anim_slide_out_top,R.anim.anim_slide_in_top,R.anim.anim_slide_out_bottom)
                .replace(R.id.fragment, new PlaceFragment(courseCode, index))
                .addToBackStack(null)
                .commit();
    }

    public void changeFragment(String placeCode) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_slide_in_bottom,R.anim.anim_slide_out_top,R.anim.anim_slide_in_top,R.anim.anim_slide_out_bottom)
                .replace(R.id.fragment, new PlaceFragment(placeCode))
                .addToBackStack(null)
                .commit();
    }
    public void changeModifyFragment(List<PlaceVO> list){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right,R.anim.anim_slide_in_right,R.anim.anim_slide_out_left)
                .replace(R.id.fragment, new CourseModifyFragment(list))
                .addToBackStack(null)
                .commit();
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    public void changeCourseViewFragment(List<PlaceVO> list){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
        fragmentTransaction.replace(R.id.fragment, new CourseFragment("c001"));
        DeleteBackStack(); //뒤로가기하는거 다 없앰
        fragmentTransaction.commit();
    }

    public void DeleteBackStack() { //뒤로가기 눌렀을시 전 프래그먼트로 이동 X
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Fragment webViewFragment = getSupportFragmentManager().findFragmentByTag("WEB_FRAGMENT");
//        if (webViewFragment != null && webViewFragment.isVisible()) {
//            Log.e("abcd", "back button pressed");
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.
//        }
//    }

//    public void pushStack() {
//        int index = this.getSupportFragmentManager().getBackStackEntryCount() - 1;
//        android.support.v4.app.FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
//        String tag = backEntry.getName();
//        Fragment myFragment = getSupportFragmentManager().findFragmentByTag(tag);
//    }
}

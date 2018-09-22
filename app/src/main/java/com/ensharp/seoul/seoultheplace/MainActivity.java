package com.ensharp.seoul.seoultheplace;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.ensharp.seoul.seoultheplace.Course.CourseModifyFragment;
import com.ensharp.seoul.seoultheplace.Course.SaveCourseActivity;
import com.ensharp.seoul.seoultheplace.Fragments.*;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageButton[] bottomButtons;
    private Fragment[] fragments;
    private Fragment currentFragment;
    private LinearLayout rootLayout;
    private int currentFragmentNumber = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // course, place 초기화
        setContentView(R.layout.activity_main);

        fragments = new Fragment[]{
                new MainFragment(), new SearchFragment(), new FavoriteFragment(), new SettingFragment()
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
                    currentFragment = fragment;
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
                        if(currentFragment.equals(fragments[0]) && diff < dpToPx(50)) {
                            ((MainFragment)fragments[0]).viewVisible();
                        }
                        else {
                            ((MainFragment)fragments[0]).viewInvisible();
                        }
                    }
                });

        String[] neededPermissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        ActivityCompat.requestPermissions(this, neededPermissions,0);
    }

    public float dpToPx(float valueInDp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);

    }

    public void changeToWebFragment(String link) {
        final Fragment fragment = new WebViewFragment(link);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment, "WEB_FRAGMENT")
                .addToBackStack(null)
                .commit();
    }

    public void changeToCourseFragment(CourseVO course) {
        final Fragment fragment = new CourseFragment(course);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void changeToPlaceFragment(CourseVO course, int index) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_slide_in_bottom,R.anim.anim_slide_out_top,R.anim.anim_slide_in_top,R.anim.anim_slide_out_bottom)
                .replace(R.id.fragment, new PlaceFragment(course, index))
                .addToBackStack(null)
                .commit();
    }

    public void changeToPlaceFragment(String placeCode) {
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

    public void chagneCourseFragment(int index) {
        final Fragment fragment = new CourseFragment("c001");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack(null)
                .commit();
    }
    public void changeCourseViewFragment(List<PlaceVO> list){
        DeleteBackStack(); //뒤로가기하는거 다 없앰
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
        fragmentTransaction.replace(R.id.fragment, new CourseFragment("c001"));
        fragmentTransaction.commit();
    }

    public void ChangeFavoriteFragment(){
        DeleteBackStack();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_slide_in_top,R.anim.anim_slide_out_bottom)
                .replace(R.id.fragment, new FavoriteFragment())
                .commit();
    }

    public void DeleteBackStack() { //뒤로가기 눌렀을시 전 프래그먼트로 이동 X
        try {
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SetSaveData(String[] datas){
        for(int i = 0 ; i < datas.length;i++){
            if(datas[i]!=null)
            Log.e("Datas :",datas[i]);
        }
        Intent intent = new Intent(this, SaveCourseActivity.class);
        intent.putExtra("codes",datas);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String[] insertData = new String[]{null, null, null, null, null, null, null, null};
                SharedPreferences sp = getSharedPreferences("data", 0);
                Log.e("SaveTest :", "onActivityResult");
                Log.e("SaveTest : ", "email " + sp.getString("email", ""));
                insertData[0] = sp.getString("email", "");
                insertData[1] = data.getStringExtra("title");
                ;
                insertData[2] = data.getStringExtra("description");
                for (int i = 0; i < data.getStringArrayExtra("codes").length; i++) {
                    if (data.getStringArrayExtra("codes")[i] != null) {
                        insertData[3 + i] = data.getStringArrayExtra("codes")[i];
                    } else {
                        insertData[3 + i] = null;
                    }
                }
                DAO dao = new DAO();
                Log.e("SaveTest : ", dao.insertMemberCourseData(insertData));
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right)
                        .replace(R.id.fragment, new FavoriteFragment())
                        .commit();
            }
        }
    }
}

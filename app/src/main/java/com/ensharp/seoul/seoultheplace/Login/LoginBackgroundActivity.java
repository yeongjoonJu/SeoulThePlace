package com.ensharp.seoul.seoultheplace.Login;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import com.ensharp.seoul.seoultheplace.R;

public class LoginBackgroundActivity extends AppCompatActivity {
    private String TAG = "VideoActivity";
    private VideoView mVideoview;

    LoginFragment loginFragment;
    MakeIDFragment makeIDFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_background);
        CheckAlreadyLogin();

        mVideoview = (VideoView) findViewById(R.id.videoview);
        //play video
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.background); //영상 추가.
        mVideoview.setVideoURI(uri); //실행
        mVideoview.start();
        //loop
        mVideoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0f,0f);
                mp.setLooping(true);
            }
        }); //루프돌림

        loginFragment = new LoginFragment();
        makeIDFragment = new MakeIDFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,loginFragment);
        fragmentTransaction.commit();
    }
    public void onFragmentChanged() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, makeIDFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit(); //프래그먼트 바꿀때 쓰면됨
    }

    public void CheckAlreadyLogin(){
        SharedPreferences sf = getSharedPreferences("data",0);
        if(sf.getString("id","")!=""){
            Intent intent = new Intent(this,CheckUserActivity.class);
            intent.putExtra("email",String.valueOf(sf.getString("id","")));
            intent.putExtra("name",String.valueOf(sf.getString("name","")));
            intent.putExtra("password",String.valueOf(sf.getString("password","")));
            startActivity(intent);
            finish();
        }
    }
}

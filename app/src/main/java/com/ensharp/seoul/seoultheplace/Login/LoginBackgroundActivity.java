package com.ensharp.seoul.seoultheplace.Login;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import com.ensharp.seoul.seoultheplace.R;

public class LoginBackgroundActivity extends AppCompatActivity {
    private String TAG = "VideoActivity";
    private VideoView mVideoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_background);

        mVideoview = (VideoView) findViewById(R.id.videoview);
        //play video
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.background); //영상 추가.
        mVideoview.setVideoURI(uri); //실행
        mVideoview.start();
        //loop
        mVideoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        }); //루프돌림

//        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment,new LoginFragment());
//        fragmentTransaction.commit(); //프래그먼트 바꿀때 쓰면됨
    }
}

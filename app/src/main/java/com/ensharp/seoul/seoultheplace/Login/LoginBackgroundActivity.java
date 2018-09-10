package com.ensharp.seoul.seoultheplace.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.VideoView;

import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.R;

public class LoginBackgroundActivity extends AppCompatActivity {
    private String TAG = "VideoActivity";
    public VideoView mVideoview;

    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_background);
        CheckAlreadyLogin();

        loginFragment = new LoginFragment();

        setVideo();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,loginFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVideo();
    }
    public void onFragmentChanged() {
        mVideoview.pause();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left,R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
        fragmentTransaction.replace(R.id.main_frame, new OriginSignUpFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void SNSFragmentChanged(){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left,R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
        fragmentTransaction.replace(R.id.main_frame,new SNSSignUpFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void SubDataFragmentChanged(){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left,R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
        fragmentTransaction.replace(R.id.main_frame,new SubDataFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void EmailLoginChanger(){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left,R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
        fragmentTransaction.replace(R.id.main_frame,new EmailLoginFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //이미 로그인한 적이 있는지 확인.
    public void CheckAlreadyLogin(){
        SharedPreferences sf = getSharedPreferences("data",0);
        if(sf.getString("type","")!=""){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setVideo(){
        mVideoview = (VideoView)findViewById(R.id.videoview);
        //play video
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.background); //영상 추가.
        Log.d("Uri", String.valueOf(uri));
        //mVideoview.setVideoPath();
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
    }
}
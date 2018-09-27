package com.ensharp.seoul.seoultheplace.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginBackgroundActivity extends AppCompatActivity {
    private String TAG = "LoginBackgroundActivity";
    public VideoView mVideoview;

    LoginFragment loginFragment;
    public DAO dao;

    public void DeleteData(){
        SharedPreferences sf = getSharedPreferences("data",0);
        SharedPreferences.Editor editor = sf.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_background);
        CheckAlreadyLogin();
        dao = new DAO();

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
        fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_bottom,R.anim.anim_slide_out_top,R.anim.anim_slide_in_top,R.anim.anim_slide_out_bottom);
        fragmentTransaction.replace(R.id.main_frame, new OriginSignUpFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void EmailLoginChanger(){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left,R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
        fragmentTransaction.replace(R.id.main_frame,new EmailLoginFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void NextActivity(){
        Log.d(TAG,"convertToMainActivity");
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    //이미 로그인한 적이 있는지 확인.
    public void CheckAlreadyLogin(){
        Log.d(TAG,"CheckAlreadyLogin");
        SharedPreferences sf = getSharedPreferences("data",0);
        if(sf.getString("email","")!=""){
            Log.d(TAG,"Already Login : "+sf.getString("email",""));
            NextActivity();
        }
        else{
            DeleteData(); //없으면 초기화
        }
    }

    private void setVideo(){
        mVideoview = (VideoView)findViewById(R.id.videoview);
        //play video
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.background2); //영상 추가.
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

    //서버에 로그인 정보 저장.
    public void SendData(SharedPreferences signup){
        Log.d(TAG,"SendData signUp");
        String[] memberCategory = new String[]{ signup.getString("email",null),
                signup.getString("password",null),
                signup.getString("name",null),
                signup.getString("phone",null)};
        Log.d(TAG,"SignUp final : " + dao.insertMemberData(memberCategory));
    }

       public boolean CheckEmail(SharedPreferences data){
        JSONObject jsonObject = dao.SNSloginCheck("email",data.getString("email",null));
        if(jsonObject==null){
            Log.d(TAG,"new ID");
            Toast.makeText(this,"회원가입 성공",Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            Log.d(TAG,"old ID");
            Toast.makeText(this,"로그인 성공",Toast.LENGTH_LONG).show();
            return true;
        }
    }
    public boolean CheckDoubleEmail(String email){
        if(dao.checkIDduplicaion(email)){
            Log.d(TAG,"DoubleEmail");
            return true;
        }
        Log.d(TAG,"Not DoubleEmail");
        return false;
    }
    public boolean LoginEmail(String id,String passwd){
        JSONObject jsonObject = dao.loginCheck(id,passwd);
        Log.d(TAG,"Login Email : " + String.valueOf(jsonObject));
        if(jsonObject == null){
            Log.d(TAG,"Login Email : JsonObject Null");
            return false;
        }
        else{
            SharedPreferences.Editor editor = getSharedPreferences("data",0).edit();
            try {
                editor.putString("email", jsonObject.getString("Id"));
                editor.putString("name", jsonObject.getString("Name"));
                editor.apply();
                Log.d(TAG,"Login Success");
                return true;
            } catch (JSONException e) {
                Log.d(TAG,"Login : "+ e );
                return false;
            }
        }
    }
    public void convertToTutorial(){
        Intent intent = new Intent(this,TutorialActivity.class);
        startActivity(intent);
        finish();
    }

    public void SearchPassWDChanger(){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left,R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
        fragmentTransaction.replace(R.id.main_frame,new SearchPasswd());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
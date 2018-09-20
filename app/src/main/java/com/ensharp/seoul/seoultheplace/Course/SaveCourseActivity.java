package com.ensharp.seoul.seoultheplace.Course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.R;

public class SaveCourseActivity extends Activity {

    EditText title;
    EditText description;
    Intent intent;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_save_activity);

        intent = getIntent();
        title = (EditText)findViewById(R.id.savetitle);
        description = (EditText)findViewById(R.id.savedescription);
        btn = (Button)findViewById(R.id.saveDataBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 전달하기
                String mtitle = String.valueOf(title.getText());
                String mDecription = String.valueOf(description.getText());
                Log.e("SaveTest :", mtitle);
                Log.e("SaveTest :", mDecription);
                if (mtitle.length()>0 && mDecription.length()>0) {
                    Intent newIntent = new Intent();
                    newIntent.putExtra("codes", intent.getStringArrayExtra("codes"));
                    newIntent.putExtra("title", mtitle);
                    newIntent.putExtra("description", mDecription);
                    Log.e("SaveTest :",mtitle+"  "+ String.valueOf(title.getText()));
                    Log.e("SaveTest :", mDecription+ " " + String.valueOf(description.getText()));
                    setResult(RESULT_OK, newIntent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
/*
    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }*/
}


package com.ensharp.seoul.seoultheplace.Course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.ensharp.seoul.seoultheplace.R;

public class SaveCourseActivity extends Activity {

    EditText title;
    EditText description;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_save_activity);

        intent = getIntent();
        title = (EditText)findViewById(R.id.savetitle);
        description = (EditText)findViewById(R.id.savedescription);
    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Log.e("SaveTest :", String.valueOf(title.getText()));
        Log.e("SaveTest :", String.valueOf(description.getText()));
        Log.e("SaveTest :", String.valueOf(title.getText()));
        Log.e("SaveTest :", String.valueOf(description.getText()));
        if(title.getText()!=null&&description.getText()!=null) {
            Intent newIntent = new Intent();
            newIntent.putExtra("codes", intent.getStringArrayExtra("codes"));
            newIntent.putExtra("title", String.valueOf(title.getText()));
            newIntent.putExtra("description", String.valueOf(description.getText()));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}


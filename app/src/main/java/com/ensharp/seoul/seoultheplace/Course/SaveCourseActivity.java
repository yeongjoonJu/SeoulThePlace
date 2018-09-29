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

import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.R;

public class SaveCourseActivity extends Activity {

    Toast mToast;
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
                if (CheckTooMuchLongTitle() && CheckTooMuchLongDescription() &&CheckDuplicate(mtitle)) {
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
                    showAToast("모든 항목을 입력해주세요.");
//                    Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean CheckDuplicate(String title){
        DAO dao = new DAO();
        String Id = getSharedPreferences("data", 0).getString("email","");
        if(dao.SaveNameCheck(Id,title))
            return true;
        showAToast("중복된 제목입니다.");
//        Toast.makeText(getApplicationContext(),"중복된 제목입니다.",Toast.LENGTH_LONG).show();
        return false;
    }

    public boolean CheckTooMuchLongTitle(){
        String titles = String.valueOf(title.getText());
        if(titles.length()>0&&titles.length()<20){
            return true;
        }
        showAToast("제목이 너무 길거나 짧습니다.");
//        Toast.makeText(getApplicationContext(),"제목이 너무 길거나 짧습니다.",Toast.LENGTH_LONG).show();
        return false;
    }

    public boolean CheckTooMuchLongDescription(){
        String titles = String.valueOf(description.getText());
        if(titles.length()>0&&titles.length()<150){
            return true;
        }
        showAToast("설명이 너무 길거나 짧습니다.");
//        Toast.makeText(getApplicationContext(),"설명이 너무 길거나 짧습니다.",Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    public void showAToast (String message){ //"Toast toast" is declared in the class
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}


package com.ensharp.seoul.seoultheplace.Login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.R;

public class TutorialActivity extends Activity {

    Button btn;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);

        int[] images = new int[]{R.drawable.tutorial_first,R.drawable.tutorial_second,R.drawable.tutorial_third,R.drawable.tutorial_fourth,R.drawable.tutorial_fifth,R.drawable.tutorial_last};

        btn = (Button)findViewById(R.id.tutorial_Btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count >= images.length){
                    convertToMainActivity();
                }
                else{
                    btn.setBackgroundResource(images[count]);
                }
            }
        });
    }
    public void convertToMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        convertToMainActivity();
    }
}

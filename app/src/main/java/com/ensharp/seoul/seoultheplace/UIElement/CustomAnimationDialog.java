package com.ensharp.seoul.seoultheplace.UIElement;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.ensharp.seoul.seoultheplace.R;

public class CustomAnimationDialog extends ProgressDialog {
    private Context c;
    private ImageView imgLogo;
    public CustomAnimationDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);

        c=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        imgLogo = (ImageView) findViewById(R.id.img_android);
        Animation anim = AnimationUtils.loadAnimation(c, R.anim.loading);
        imgLogo.setAnimation(anim);
    }
    @Override
    public void show() {
        super.show();
    }
    @Override
    public void dismiss() {
        super.dismiss();
    }
}

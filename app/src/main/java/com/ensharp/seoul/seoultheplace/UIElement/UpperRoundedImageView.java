package com.ensharp.seoul.seoultheplace.UIElement;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class UpperRoundedImageView extends android.support.v7.widget.AppCompatImageView {

    private float upperRadius = 18.0f;
    private float bottomRadius = 0;
    private Path path;
    private RectF rect;

    public UpperRoundedImageView(Context context) {
        super(context);
        init();
    }

    public UpperRoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UpperRoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        path.addRoundRect(0, upperRadius, 0, 0, upperRadius, upperRadius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}

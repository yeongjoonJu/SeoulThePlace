package com.ensharp.seoul.seoultheplace.UIElement.SearchBar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public class JJBarWithErrorIconController extends JJBaseController {
    private String mColor = "#00000000";
    private float cx, cy, cr;
    private float sign = 0.707f;
    private float mCircleBig = 10;
    private RectF mRectF, mRectF2;
    private float mCirCleDis = 200;
    private Paint mFontPaint;

    public JJBarWithErrorIconController() {
        mRectF = new RectF();
        mRectF2 = new RectF();
        mFontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFontPaint.setStrokeWidth(1);
        mFontPaint.setColor(Color.parseColor("#00BDFC"));
        mFontPaint.setStyle(Paint.Style.FILL);
        mFontPaint.setTextSize(40);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawColor(Color.parseColor(mColor));
        switch (mState) {
            case STATE_ANIM_NONE:
                drawNormalView(paint, canvas);
                break;
            case STATE_ANIM_START:
                drawStartAnimView(paint, canvas);
                break;
            case STATE_ANIM_STOP:
                drawStopAnimView(paint, canvas);
                break;
        }
    }

    private void drawStopAnimView(Paint paint, Canvas canvas) {
        canvas.save();
        if (mPro <= 0.25) {
            canvas.drawArc(mRectF, 90, -180, false, paint);
            canvas.drawLine(mRectF2.left + cr, mRectF.top, mRectF.right - cr, mRectF.top, paint);
            canvas.drawLine(mRectF2.left + cr, mRectF.bottom, mRectF.right - cr, mRectF.bottom,
                    paint);
            canvas.drawArc(mRectF2, 90, 180, false, paint);
            canvas.drawLine((cx - cr + (cr / 2) + (getWidth() * 0.4f)) + cr * sign, cy + cr * sign,
                    (cx - cr + (cr / 2) + (getWidth() * 0.4f)) + ((cr/2) * sign), cy + ((cr / 2) * sign), paint);
        } else if (mPro > 0.25 && mPro <= 0.5f) {
            canvas.drawArc(mRectF, 90, -180, false, paint);
            canvas.drawLine(mRectF2.left + cr, mRectF.top, mRectF.right - cr, mRectF.top, paint);
            canvas.drawLine(mRectF2.left + cr, mRectF.bottom, mRectF.right - cr, mRectF.bottom,
                    paint);
            canvas.drawArc(mRectF2, 90, 180, false, paint);
            canvas.drawArc(mRectF2, 90, 180, false, paint);
        } else if (mPro > 0.5f && mPro <= 0.75f) {
            canvas.drawCircle(cx, cy, cr, paint);
        } else {
            canvas.drawLine(cx + cr * sign, cy + cr * sign, cx + cr * sign + cr * sign *
                    (mPro - 0.75f) * 4, cy + cr * sign + cr * sign * (mPro - 0.75f) * 4, paint);
            canvas.drawCircle(cx, cy, cr, paint);
        }
        canvas.restore();
    }

    private void drawStartAnimView(Paint paint, Canvas canvas) {
        canvas.save();
        if (mPro <= 0.25) {

        } else if (mPro > 0.25 && mPro <= 0.5f) {
            canvas.drawArc(mRectF, 90, -180, false, paint);
            canvas.drawLine(mRectF2.left + cr, mRectF.top, mRectF.right - cr, mRectF.top, paint);
            canvas.drawLine(mRectF2.left + cr, mRectF.bottom, mRectF.right - cr, mRectF.bottom,
                    paint);
            canvas.drawArc(mRectF2, 90, 180, false, paint);
        } else if (mPro > 0.5f && mPro <= 0.75f) {
            canvas.drawArc(mRectF, 90, -180, false, paint);
            canvas.drawLine(mRectF2.left + cr, mRectF.top, mRectF.right - cr, mRectF.top, paint);
            canvas.drawLine(mRectF2.left + cr, mRectF.bottom, mRectF.right - cr, mRectF.bottom,
                    paint);
            canvas.drawArc(mRectF2, 90, 180, false, paint);
            canvas.drawLine((cx - cr + (cr / 2) + (getWidth() * 0.4f)) + cr * sign, cy + cr * sign,
                    (cx - cr + (cr / 2) + (getWidth() * 0.4f)) + ((cr/2) * sign), cy + ((cr / 2) * sign), paint);
        } else {
            canvas.drawArc(mRectF, 90, -180, false, paint);
            canvas.drawLine(mRectF2.left + cr, mRectF.top, mRectF.right - cr, mRectF.top, paint);
            canvas.drawLine(mRectF2.left + cr, mRectF.bottom, mRectF.right - cr, mRectF.bottom,
                    paint);
            canvas.drawArc(mRectF2, 90, 180, false, paint);
            canvas.drawCircle(cx - cr + (cr / 2) + (getWidth() * 0.4f), cy, cr / 2, paint);
            canvas.drawLine((cx - cr + (cr / 2) + (getWidth() * 0.4f)) + cr * sign, cy + cr * sign,
                    (cx - cr + (cr / 2) + (getWidth() * 0.4f)) + ((cr/2) * sign), cy + ((cr / 2) * sign), paint);
        }

        canvas.restore();
    }

    private void drawNormalView(Paint paint, Canvas canvas) {
        cr = getWidth() / 25;
        cx = getWidth() / 2;
        cy = getHeight() / 2;
        mRectF.top = cy - cr;
        mRectF.bottom = cy + cr;
        mRectF2.top = cy - cr;
        mRectF2.bottom = cy + cr;
        mRectF.left = cx - cr + (getWidth() * 0.4f );
        mRectF.right = cx + cr + (getWidth() * 0.4f );
        mRectF2.left = cx - cr - (getWidth() * 0.4f );
        mRectF2.right = cx + cr - (getWidth() * 0.4f );

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.parseColor("#00BDFC"));
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.STROKE);

        startAnim();
    }

    @Override
    public void startAnim() {
        mState = STATE_ANIM_START;
        startSearchViewAnim();
    }

    @Override
    public void resetAnim() {
        if (mState == STATE_ANIM_STOP) return;
        mState = STATE_ANIM_STOP;
        startSearchViewAnim();
    }

}

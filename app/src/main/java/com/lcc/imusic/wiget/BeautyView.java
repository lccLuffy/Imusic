package com.lcc.imusic.wiget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class BeautyView extends View {

    private static final int COLOR_BACKGROUND = Color.parseColor("#7ECEC9");
    private static final int COLOR_MOUNTAIN_1 = Color.parseColor("#86DAD7");
    private static final int COLOR_MOUNTAIN_2 = Color.parseColor("#3C929C");
    private static final int COLOR_MOUNTAIN_3 = Color.parseColor("#3E5F73");

    private static final int WIDTH = 240;
    private static final int HEIGHT = 180;


    private Paint mMountPaint = new Paint();
    private Paint mTrunkPaint = new Paint();
    private Paint mBranchPaint = new Paint();
    private Paint mBoarderPaint = new Paint();

    private Path mMount1 = new Path();
    private Path mMount2 = new Path();
    private Path mMount3 = new Path();

    private float mScaleX = 5f;
    private float mScaleY = 5f;
    private float mMoveFactor = 0;
    private Matrix mTransMatrix = new Matrix();

    public BeautyView(Context context) {
        super(context);
        init();
    }

    public BeautyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BeautyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BeautyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final float width = getMeasuredWidth();
        final float height = getMeasuredHeight();
        mScaleX = width / WIDTH;
        mScaleY = height / HEIGHT;

        updateMountainPath(mMoveFactor);
    }

    private void init() {
        mMountPaint.setAntiAlias(true);
        mMountPaint.setStyle(Paint.Style.FILL);

        mTrunkPaint.setAntiAlias(true);
        mBranchPaint.setAntiAlias(true);
        mBoarderPaint.setAntiAlias(true);
        mBoarderPaint.setStyle(Paint.Style.STROKE);
        mBoarderPaint.setStrokeWidth(2);
        mBoarderPaint.setStrokeJoin(Paint.Join.ROUND);

        updateMountainPath(mMoveFactor);
    }

    private void updateMountainPath(float factor) {

        mTransMatrix.reset();
        mTransMatrix.setScale(mScaleX, mScaleY);

        int offset1 = (int) (10 * factor);
        mMount1.reset();
        mMount1.moveTo(0, 95 + offset1);
        mMount1.lineTo(55, 74 + offset1);
        mMount1.lineTo(146, 104 + offset1);
        mMount1.lineTo(227, 72 + offset1);
        mMount1.lineTo(WIDTH, 80 + offset1);
        mMount1.lineTo(WIDTH, HEIGHT);
        mMount1.lineTo(0, HEIGHT);
        mMount1.close();
        mMount1.transform(mTransMatrix);

        int offset2 = (int) (20 * factor);
        mMount2.reset();
        mMount2.moveTo(0, 103 + offset2);
        mMount2.lineTo(67, 90 + offset2);
        mMount2.lineTo(165, 115 + offset2);
        mMount2.lineTo(221, 87 + offset2);
        mMount2.lineTo(WIDTH, 100 + offset2);
        mMount2.lineTo(WIDTH, HEIGHT);
        mMount2.lineTo(0, HEIGHT);
        mMount2.close();
        mMount2.transform(mTransMatrix);

        int offset3 = (int) (30 * factor);
        mMount3.reset();
        mMount3.moveTo(0, 114 + offset3);
        mMount3.cubicTo(30, 106 + offset3, 196, 97 + offset3, WIDTH, 104 + offset3);
        mMount3.lineTo(WIDTH, HEIGHT);
        mMount3.lineTo(0, HEIGHT);
        mMount3.close();
        mMount3.transform(mTransMatrix);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(COLOR_BACKGROUND);

        mMountPaint.setColor(COLOR_MOUNTAIN_1);
        canvas.drawPath(mMount1, mMountPaint);

        canvas.save();
        canvas.scale(-1, 1, getWidth() / 2, 0);

        canvas.restore();
        mMountPaint.setColor(COLOR_MOUNTAIN_2);
        canvas.drawPath(mMount2, mMountPaint);


        mMountPaint.setColor(COLOR_MOUNTAIN_3);
        canvas.drawPath(mMount3, mMountPaint);
    }
}

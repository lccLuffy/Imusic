package com.lcc.imusic.wiget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class BeautyView extends View {
    private int width;
    private int height;
    private int xCount = 10;
    private int yCount = 5;
    private int xSize,ySize;
    int i = 0 ,j = 0;
    private Paint paint;
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
    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
    }


    public void setXCount(int xCount)
    {
        this.xCount = xCount;
    }
    public void setYCount(int yCount)
    {
        this.yCount = yCount;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(0, 0, width, height, paint);
        for (i = 0; i < xCount; i++)
        {
            for (j = 0; i < yCount; j++)
            {
                if(i + j % 2 == 0)
                {
                    canvas.drawLine(i * xSize, j * ySize, i * xSize + xSize, j * ySize + ySize, paint);
                }
                else
                {
                    canvas.drawLine(i * xSize + xSize, j * ySize, i * xSize, j * ySize + ySize, paint);
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
        xSize = width / xCount;
        ySize = height / yCount;
    }
}

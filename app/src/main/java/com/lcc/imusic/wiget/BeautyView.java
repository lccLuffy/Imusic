package com.lcc.imusic.wiget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BeautyView extends View {
    private int width;
    private int height;
    private int xCount = 12;
    private int yCount = 7;
    private int xSize,ySize;
    private Paint paint;
    private List<Line> lines;
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
        if(!ready || isInEditMode())
            return;
        for (Line line : lines)
        {
            line.draw(canvas);
        }
    }

    private boolean ready = false;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
        xSize = width / xCount;
        ySize = height / yCount;

        lines = new ArrayList<>();
        for (int i = 0; i < xCount; i++)
        {
            for (int j = 0; j < yCount; j++)
            {
                lines.add(new Line(i * xSize,j*ySize,xSize,ySize));
            }
        }

        ready = true;
    }
    private static Random random = new Random();
    private static Point point = new Point();
    public class Line
    {
        private int xSize;
        private int ySize;
        private int xHalfSize;
        private int yHalfSize;
        private int startX;
        private int startY;
        private int stopX;
        private int stopY;

        private int centerX,centerY;

        public Line(int startX, int startY ,int xSize, int ySize) {
            this.ySize = ySize;
            this.xSize = xSize;
            xHalfSize = xSize / 2;
            yHalfSize = ySize / 2;


            this.startX = startX + xHalfSize;
            this.startY = startY;

            stopX = startX;
            stopY = startY + ySize;

            centerX = (startX + stopX) / 2;
            centerY = (startY + stopY) / 2;

            double degree = Math.atan2(centerY - point.y,centerX - point.x);
            rotate(degree);
        }
        public void draw(Canvas canvas)
        {
            canvas.drawLine(startX,startY,stopX,stopY,paint);
        }
        public void rotate(double degree)
        {
            double sin = Math.sin(degree);
            double cos = Math.cos(degree);
            double xx = (startX - centerX) * cos - (startY - centerY) *sin + centerX;
            double yy = (startX - centerX) * sin + (startY - centerY) *cos + centerY;
            startX = (int) xx;
            startY = (int) yy;
            stopX = (int) (centerX * 2 - xx);
            stopY = (int) (centerY * 2 - yy);
        }
    }

    public static class Point
    {
        public int x;
        public int y;
    }
}

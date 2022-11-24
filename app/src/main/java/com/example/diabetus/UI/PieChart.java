package com.example.diabetus.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.example.diabetus.R;

import java.util.ArrayList;
import java.util.List;

public class PieChart extends View {
    List<Integer> mColors;
    List<Float> mSweepAngle;
    int mBackgroundColor, mTextColor;
    int mWidth;
    int mTotal;
    int mRadius;
    int mTextSize;
    String mText;
    Paint mPaint = new Paint();
    RectF mOval = new RectF(0,0,0,0);

    public PieChart (Context context, AttributeSet attrs) {
        super(context, attrs);
        mSweepAngle = new ArrayList<>();
        mColors = new ArrayList<>();
        mTotal = 0;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PieChart);
        try {
            mWidth = a.getInt(R.styleable.PieChart_Width, 0);
            mText = a.getString(R.styleable.PieChart_Text);
            mTextSize = a.getInt(R.styleable.PieChart_TextSize, 48);
            mTextColor = a.getColor(R.styleable.PieChart_TextColor, 0);
            mBackgroundColor = a.getColor(R.styleable.PieChart_InnerColor, 0);
            mRadius = a.getInt(R.styleable.PieChart_Radius,150);
        } finally {
            a.recycle();
        }
    }

    public void set(List<Integer> values, List<Integer> colors, String text) {
        mColors = colors;
        values.forEach(v -> mTotal += v);
        float accumulatedAngle = 0;
        int lastVisible = values.size();
        for (int i = 0; i < values.size(); i++) {
            float angle = values.get(i) * 360.0f / mTotal;
            if (angle != 0) {
                lastVisible = i;
            }
            accumulatedAngle += angle;
            mSweepAngle.add(angle);
        }

        // Solve accumulated rounding error
        if (lastVisible != values.size()) {
            mSweepAngle.set(lastVisible,
                    (mSweepAngle.get(lastVisible) + 360 - accumulatedAngle));
        }

        mText = text;
        invalidate();
    }

    public void onDraw(Canvas canvas) {
        float startAngle = 0.0f;
        mOval.set((float)getWidth()/2 - mRadius, (float)getHeight()/2 - mRadius,
                (float)getWidth()/2 + mRadius, (float)getHeight()/2 + mRadius);
        for (int i = 0; i < mSweepAngle.size(); i++) {
            mPaint.setColor(mColors.get(i));
            canvas.drawArc(mOval,startAngle,mSweepAngle.get(i),true, mPaint);
            startAngle += mSweepAngle.get(i);
        }

        mPaint.setColor(mBackgroundColor);
        int radius = mRadius - mWidth;
        mOval.set((float)getWidth()/2 - radius, (float)getHeight()/2 - radius,
                (float)getWidth()/2 + radius, (float)getHeight()/2 + radius);
        canvas.drawArc(mOval,0,360,true, mPaint);

        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(mText, (float)getWidth()/2, (float)getHeight()/2 - ((mPaint.descent() + mPaint.ascent()) / 2), mPaint);
    }



}

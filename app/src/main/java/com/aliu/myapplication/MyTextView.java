package com.aliu.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import java.util.ArrayList;
import java.util.List;

/**
 * describe
 *
 * @author ikould on 2017/11/14.
 */

public class MyTextView extends AppCompatTextView {

    private Paint mPaint;

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int color = getTextColors().getColorForState(getDrawableState(), android.R.color.white);
        int textSize = (int) getTextSize();
        mPaint.setColor(color);
        mPaint.setTextSize(textSize);
        CharSequence text = getText();
        int gravity = getGravity();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int remainWidth = width - paddingLeft - paddingRight;
        int remainHeight = height - paddingTop - paddingBottom;
        int maxNumLine = remainWidth / textSize;
        //  Log.d("MyTextView", "onDraw: text = " + text + " maxNumLine = " + maxNumLine + " remainWidth = " + remainWidth);
        List<String> textList = getDrawTextList(maxNumLine, remainWidth, text);
        //  Log.d("MyTextView", "initText: width = " + width + " height = " + height + " maxNumLine = " + maxNumLine + " textList.size = " + (textList == null ? 0 : textList.size()));

        //  Log.d("MyTextView", "onDraw: width = " + width + " height = " + height);
        if (textList == null || textList.size() == 0) {
            return;
        }
        int lines = textList.size();
        int left = 0, top = 0, right = 0, bottom = 0;
        int textAllHeight = lines * textSize;
        int maxTop = paddingTop + (remainHeight - textAllHeight) / 2;
        //gravity = transGravity(gravity);
        for (int i = 0; i < lines; i++) {
            String charSequence = textList.get(i);
            switch (gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
                case Gravity.LEFT:
                case Gravity.START:
                    //  Log.d("MyTextView", "onDraw: LEFT");
                    left = paddingLeft;
                    break;
                case Gravity.RIGHT:
                case Gravity.END:
                    //   Log.d("MyTextView", "onDraw: RIGHT");
                    right = width - paddingRight;
                    left = (int) (right - mPaint.measureText(charSequence));
                    break;
                case Gravity.CENTER_HORIZONTAL:
                    //   Log.d("MyTextView", "onDraw: CENTER_HORIZONTAL");
                    left = (int) ((remainWidth - mPaint.measureText(charSequence)) / 2 + paddingLeft);
                    break;

            }
            switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                case Gravity.BOTTOM:
                    // Log.d("MyTextView", "onDraw: BOTTOM");
                    top = height - paddingBottom - (lines - i) * textSize;
                    break;
                case Gravity.TOP:
                    //  Log.d("MyTextView", "onDraw: TOP");
                    top = i * textSize;
                    break;
                case Gravity.CENTER_VERTICAL:
                    //  Log.d("MyTextView", "onDraw: CENTER_VERTICAL");
                    top = maxTop + i * textSize;
                    break;
            }
            //   Log.d("MyTextView", "onDraw: charSequence = " + charSequence + " left = " + left + " top = " + top);
            bottom = top + textSize;
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            int baseLine = (int) ((top + bottom) / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2);
            canvas.drawText(charSequence, left, baseLine, mPaint);
        }
    }

    /**
     * 获取文本序列
     */
    private List<String> getDrawTextList(int maxNumLine, int remainWidth, CharSequence text) {
        List<String> textList = new ArrayList<>();
        CharSequence[] texts = text.toString().split("\n");
        int[] num = new int[1];
        for (CharSequence charSequence : texts) {
            int l = charSequence.length();
            if (l > maxNumLine) {
                String result = null;
                do {
                    int start = num[0];
                    num[0] += maxNumLine;
                    result = getPerfectString(charSequence.toString(), start, num, remainWidth, mPaint);
                    // Log.d("MyTextView", "getDrawTextList: result = " + result + " num[0] = " + num[0]);
                    textList.add(result);
                } while (num[0] < l);
            } else {
                textList.add(charSequence.toString());
            }
        }
        return textList;
    }

    private String getPerfectString(String str, int start, int[] num, int width, Paint paint) {
     /*   Log.d("MyTextView", "getPerfectString: str = " + str + " subString = " + str.substring(0, str.length()));
        Log.d("MyTextView", "getPerfectString: start = " + start + " end = " + num[0] + " length = " + str.length());*/
        if (num[0] > str.length()) {
            num[0] = str.length();
        }
        if (num[0] < start)
            num[0] = start;
        String resultStr1;
        String resultStr2 = "";
        do {
            resultStr1 = str.substring(start, num[0]);
            if (paint.measureText(resultStr1) <= width) {
                resultStr2 = resultStr1;
            } else {
                break;
            }
            num[0]++;
          /*  Log.d("MyTextView", "getPerfectString: resultStr2 = " + resultStr2 + " num[0] = " + num[0]);
            Log.d("MyTextView", "getPerfectString: paint.measureText(resultStr1) = " + (paint.measureText(resultStr1)));*/
        } while (num[0] <= str.length());
        return resultStr2;
    }
}

package com.scnu.zhou.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhou on 16/10/31.
 */
public class PieView extends View {

    // 默认长宽，在设置wrap_content时使用
    private int mWidth = 400;
    private int mHeight = 400;

    // 背景颜色:白色
    private int backgroundColor = Color.parseColor("#FFFFFF");
    // 文字颜色：黑色
    private int textColor = Color.BLACK;

    // 画笔
    private Paint mPaint;
    private Paint mTextPaint;

    // 饼状图初始绘制角度
    private float mStartAngle = 0;

    // 数据
    private List<PieData> mData = new ArrayList<>();

    // 选中的项
    private int selectPos = 0;

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PieView);
            backgroundColor = a.getColor(R.styleable.PieView_backgroundColor, Color.parseColor("#97CC00"));
            textColor = a.getColor(R.styleable.PieView_textColor, Color.BLACK);
            mStartAngle = a.getFloat(R.styleable.PieView_startAngle, 0);
            a.recycle();
        }

        initPaint();
    }

    public void initPaint(){

        mPaint = new Paint();
        mPaint.setColor(backgroundColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10f);

        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(40);
    }


    // 测量View的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth, mHeight);
        }
        else if (widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth, heightSpecSize);
        }
        else if (heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize, mHeight);
        }
    }


    // 确定View的大小
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }


    // 绘制View
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mWidth/2, mHeight/2, mWidth/2 - 80, mPaint);

        if (mData == null || mData.size() == 0) {
            return;
        }
        float currentAngle = mStartAngle;
        float r = (float) (Math.min(mWidth, mHeight) / 2);     // 饼状图绘制半径
        RectF rect = new RectF(-(r-80), -(r-80), r-80, r-80);;    // 饼状图绘制区域

        canvas.translate(mWidth/2, mHeight/2);   // 将画布坐标原点移动到中心位置

        float selectStartAngle = 0;
        for (int i=0; i<mData.size(); i++){

            PieData pie = mData.get(i);

            if (i == selectPos) {    // 选中
                mPaint.setColor(Color.parseColor("#FFFFFF"));
                selectStartAngle = currentAngle;
            }
            else{
                mPaint.setColor(pie.getColor());
            }

            pie.setStartAngle(currentAngle);   // 记录绘制角度
            canvas.drawArc(rect, currentAngle, pie.getAngle(), true, mPaint);
            currentAngle += pie.getAngle();
        }

        // 计算原点坐标偏移量
        float piangle = (float) (mData.get(selectPos).getAngle() / 2 * Math.PI / 180);
        float spiangle = (float) ((selectStartAngle + mData.get(selectPos).getAngle() / 2) * Math.PI / 180);
        float translationX = (float) (10 / Math.sin(piangle) * Math.cos(spiangle));
        float translationY = (float) (10 / Math.sin(piangle) * Math.sin(spiangle));

        canvas.translate(translationX, translationY);
        mPaint.setColor(mData.get(selectPos).getColor());
        canvas.drawArc(rect, selectStartAngle, mData.get(selectPos).getAngle(), true, mPaint);

        canvas.translate(-mWidth/2 - translationX, -mHeight/2 - translationY);   // 回归最初原点
        mPaint.setColor(backgroundColor);    // 绘制覆盖区
        canvas.drawCircle(mWidth/2, mHeight/2, mWidth/4, mPaint);

        canvas.drawText(mData.get(selectPos).getTitle(), mWidth/2 - mData.get(selectPos)
                .getTitle().length() / 2 * 19, mHeight/2 - 20, mTextPaint);   // 绘制文字

        String percent = mData.get(selectPos).getPercentage() * 100 + "%";
        canvas.drawText(percent, mWidth/2 - percent.length() / 2 * 19,
                mHeight/2 + 40, mTextPaint);   // 绘制文字
    }


    // 设置背景颜色
    public void setBackgroundColor(int color){

        this.backgroundColor = color;
        invalidate();
    }


    // 设置起始角度
    public void setStartAngle(float mStartAngle){

        this.mStartAngle = mStartAngle;
    }


    // 设置数据
    public void setData(List<PieData> mData){

        this.mData = mData;
        initData();
        invalidate();
    }


    // 初始化数据
    private void initData(){

        if (mData == null || mData.size() == 0){   // 数据有问题，直接返回
            return;
        }

        float sumValue = 0;
        for (int i=0; i<mData.size(); i++){

            sumValue += mData.get(i).getValue();
        }


        for (int i=0; i<mData.size(); i++){

            PieData pie = mData.get(i);

            float a = pie.getValue() / sumValue;   // 百分比
            float percentage = (float)(Math.round(a*100))/100;
            float angle = percentage * 360;   // 对应角度

            pie.setPercentage(percentage);
            pie.setAngle(angle);
        }
    }


    // 选择某一项
    public void selectPos(int pos){

        if (selectPos != pos) {
            mData.get(selectPos).setSelected(false);
            mData.get(pos).setSelected(true);
            selectPos = pos;
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                double x = event.getX() - mWidth/2;
                double y = event.getY() - mHeight/2;
                float angle = (float) (Math.atan2(y, x) * 180 / Math.PI);
                Log.e("touch", angle+"");

                if (y < 0){
                    angle += 360;
                }

                int pos = 0;
                for (int i=0; i<mData.size(); i++){
                    if (angle > mData.get(i).getStartAngle() && angle < mData.get(i).getStartAngle()
                            + mData.get(i).getAngle()){
                        pos = i;
                        break;
                    }
                }

                Log.e("select", mData.get(pos).getTitle());
                selectPos(pos);
                break;
        }
        return super.onTouchEvent(event);
    }

    public static class PieData{

        // 用户关心数据
        private String title;   // 属性标题
        private float value;   // 属性数值
        private int color = 0;   // 颜色

        // 非用户关心数据
        private float percentage;   // 百分比
        private float startAngle;  // 绘制开始角度
        private float angle = 0;   // 角度大小
        private boolean isSelected = false;   // 是否选中

        public PieData(String title, float value, int color){

            this.title = title;
            this.value = value;
            this.color = color;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public float getPercentage() {
            return percentage;
        }

        public void setPercentage(float percentage) {
            this.percentage = percentage;
        }

        public float getStartAngle() {
            return startAngle;
        }

        public void setStartAngle(float startAngle) {
            this.startAngle = startAngle;
        }

        public float getAngle() {
            return angle;
        }

        public void setAngle(float angle) {
            this.angle = angle;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}

package com.shihang.kotlin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.shihang.kotlin.R;


public class ProportionImageView extends ImageView {

    private float ratio;
    //private int ratioDirection;
    //private final int WIDTH = 0;
    //private final int HEIGHT = 1;

    public ProportionImageView(Context context) {
        super(context);
    }

    public ProportionImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProportionImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProportionImageView, defStyleAttr, 0);
        ratio = a.getFloat(R.styleable.ProportionImageView_ratio, 0.0f);
        //ratioDirection = a.getInt(R.styleable.ProportionImageView_ratio_direction, HEIGHT);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取宽度的模式和尺寸
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //获取高度的模式和尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //宽确定，高不确定
        if(widthMode == MeasureSpec.EXACTLY && heightMode!=MeasureSpec.EXACTLY&& ratio!=0){
            heightSize = (int) (widthSize*ratio+0.5f);//根据宽度和比例计算高度
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }else if(widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY&& ratio!=0){
            widthSize = (int) (heightSize/ratio+0.5f);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize,MeasureSpec.EXACTLY);
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }
}

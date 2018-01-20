package com.shihang.kotlin.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhy.autolayout.utils.AutoUtils;
import com.shihang.kotlin.R;


public class HomeTabButton extends LinearLayout{


	public interface OnCheckedChangeListener {
		 void onCheckedChanged(HomeTabButton button, boolean isChecked);
	}

	private boolean myCheck;
	private Drawable top_on;
	private Drawable top_off;
	private int textColor_off;
	private int textColor_on;
	private Drawable background_off;
	private Drawable background_on;
	private OnCheckedChangeListener checkListener;


	private ImageView imageView;
	private TextView textView;


	public boolean isChecked(){
		return myCheck;
	}


	public void setCheck(boolean isChecked){
		this.myCheck = isChecked;
		setImage(isChecked);
		if(checkListener != null){
			checkListener.onCheckedChanged(this, isChecked);
		}
	}


	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public HomeTabButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(attrs);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public HomeTabButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}

	public HomeTabButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public HomeTabButton(Context context) {
		super(context);
		init(null);
	}


	public void setOnChangeListener(OnCheckedChangeListener listener){
		this.checkListener = listener;
	}


	private void init(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.HomeTabButton);
		top_on = a.getDrawable(R.styleable.HomeTabButton_top_on);
		top_off = a.getDrawable(R.styleable.HomeTabButton_top_off);
		textColor_on = a.getColor(R.styleable.HomeTabButton_textColor_on, Color.WHITE);
		textColor_off = a.getColor(R.styleable.HomeTabButton_textColor_off,Color.WHITE);
		background_on = a.getDrawable(R.styleable.HomeTabButton_background_on);
		background_off = a.getDrawable(R.styleable.HomeTabButton_background_off);
		myCheck = a.getBoolean(R.styleable.HomeTabButton_checked, false);

		int imgHeith = a.getDimensionPixelSize(R.styleable.HomeTabButton_image_width, 0);
		int imgWidth = a.getDimensionPixelSize(R.styleable.HomeTabButton_image_height, 0);
		int textSize= (int) a.getDimension(R.styleable.HomeTabButton_content_size, 0);
		int text = a.getResourceId(R.styleable.HomeTabButton_content, 0);

		setOrientation(VERTICAL);
		setGravity(Gravity.CENTER);

		imageView = new ImageView(getContext());
		LayoutParams lp;

		if(imgHeith > 0 && imgWidth > 0){
			lp = new LayoutParams(AutoUtils.getPercentWidthSizeBigger(imgWidth), AutoUtils.getPercentHeightSizeBigger(imgHeith));
		}else{
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		addView(imageView, lp);

		textView = new TextView(getContext());
		textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentHeightSizeBigger(textSize));
		if (text != 0) { textView.setText(text); }
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 0, 0, 0);
		addView(textView, lp);
		a.recycle();
		setImage(isChecked());
	}


	public void setImage(boolean check){
		if(check){
			if(background_on != null){setBackgroundDrawable(background_on);}
			if(textColor_on != 0){textView.setTextColor(textColor_on);}
			if(top_on != null){ imageView.setImageDrawable(top_on); }
		}else{
			if(background_off != null){setBackgroundDrawable(background_off);}
			if(textColor_on != 0){textView.setTextColor(textColor_off);}
			if(top_on != null){ imageView.setImageDrawable(top_off); }
		}
	}

}

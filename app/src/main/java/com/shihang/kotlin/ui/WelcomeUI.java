package com.shihang.kotlin.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;

import com.shihang.kotlin.R;
import com.shihang.kotlin.adapter.WelcomeAdapter;
import com.shihang.kotlin.config.Keys;
import com.shihang.kotlin.utils.PreferenceUtils;


public class WelcomeUI extends BaseUI {

    private ViewPager page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_welcome);
        page = (ViewPager) findViewById(R.id.viewPager);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isFirst = PreferenceUtils.INSTANCE.getPrefBoolean(WelcomeUI.this, Keys.IS_FIRST, true);
                if(isFirst){
                    WelcomeAdapter adapter = new WelcomeAdapter(WelcomeUI.this);
                    page.setAdapter(adapter);
                }else{
                    boolean autoLogin = PreferenceUtils.INSTANCE.getPrefBoolean(WelcomeUI.this, Keys.AUTO_LOGIN, false);
                    if(autoLogin){

                    }else{
                        openUI(LoginUI.class);
                        finish();
                    }
                }
            }
        }, 1500);
    }
}

package com.shihang.kotlin.ui.activity;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.shihang.kotlin.R;
import com.shihang.kotlin.databinding.ActivityCodeDemoBinding;
import com.shihang.kotlin.ui.fragment.VerifyCodeFragment;

public class VerifyCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentLayout());
        initViews();
    }

    public int contentLayout(){
        return R.layout.activity_code_demo;
    }

    public void initViews(){
        //在原来R.layout.xxx的基础上获取ViewBinding
        View view = findViewById(R.id.contentRootLayout);
        ActivityCodeDemoBinding binding = ActivityCodeDemoBinding.bind(view);
        binding.appThemeBar.setLeftIconClick(v -> finish());

        VerifyCodeFragment getVerifyCodeFragment = new VerifyCodeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(binding.frameLayout.getId(), getVerifyCodeFragment)
                .commitNowAllowingStateLoss();
    }

}

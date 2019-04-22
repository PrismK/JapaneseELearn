package com.prismk.japaneseelearn.activities;

import android.content.Intent;
import android.os.Bundle;

import com.prismk.japaneseelearn.R;

public class ReleaseClassesActivity extends BaseActivity {

    private boolean isVip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        getIntentValue();
        initView();
    }

    private void initView() {

    }

    private void getIntentValue() {
        Intent intent = getIntent();
        isVip = intent.getBooleanExtra("isVip", false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_classes;
    }
}

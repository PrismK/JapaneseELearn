package com.prismk.japaneseelearn.activities;

import android.os.Bundle;

import com.prismk.japaneseelearn.R;

public class ADContextActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_adcontext;
    }
}

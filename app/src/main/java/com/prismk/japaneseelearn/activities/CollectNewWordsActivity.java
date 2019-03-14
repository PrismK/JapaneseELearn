package com.prismk.japaneseelearn.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.bean.NewWordSEvent;
import com.prismk.japaneseelearn.db.word.bean.NewWordsBean;
import com.prismk.japaneseelearn.managers.DBManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CollectNewWordsActivity extends BaseActivity {

    ViewPager rec_words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        DBManager.getInstance().queryCollectNewsWords();
    }

    private void initView() {
        rec_words = findViewById(R.id.vg_collect_newwords);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collect_new_words;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NewWordSEvent event) {
        for (NewWordsBean bean : event.beans) {
            Log.d("CollectNewWordsActivity", "bean.wordBean:" + bean.wordBean);
        }

    }
}

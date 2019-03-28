package com.prismk.japaneseelearn.activities;

import android.os.Bundle;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.bean.RemeberWordEvent;
import com.prismk.japaneseelearn.db.word.bean.RememberWordsBean;
import com.prismk.japaneseelearn.managers.WordsDBManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class IKnowWordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WordsDBManager.getInstance().queryKnowWords();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_iknow_word;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RemeberWordEvent event) {
        for (RememberWordsBean bean : event.beans) {

        }
    }
}

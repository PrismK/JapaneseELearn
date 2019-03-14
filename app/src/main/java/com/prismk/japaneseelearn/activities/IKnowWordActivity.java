package com.prismk.japaneseelearn.activities;

import android.os.Bundle;
import android.util.Log;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.bean.RemeberWordEvent;
import com.prismk.japaneseelearn.db.word.bean.RememberWordsBean;
import com.prismk.japaneseelearn.managers.DBManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class IKnowWordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBManager.getInstance().queryKnowWords();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_iknow_word;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RemeberWordEvent event) {
        for (RememberWordsBean bean : event.beans) {
            Log.d("IKnowWordActivity", "bean.new_id:" + bean.new_id);
            Log.d("IKnowWordActivity", "bean.new_id:" + bean.wordBean);
            Log.d("IKnowWordActivity", "bean.new_id:" + bean.phone);
        }
    }
}

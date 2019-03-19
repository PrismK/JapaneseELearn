package com.prismk.japaneseelearn.activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.adapters.RemeberWordSAdapter;
import com.prismk.japaneseelearn.bean.RemeberWordEvent;
import com.prismk.japaneseelearn.db.word.bean.RememberWordsBean;
import com.prismk.japaneseelearn.managers.WordsDBManager;
import com.prismk.japaneseelearn.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class KnowActivity extends BaseActivity {

    RecyclerView rec_words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        WordsDBManager.getInstance().queryKnowWords();
    }

    private void initView() {
        rec_words =  findViewById(R.id.rec_know_words);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_know;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RemeberWordEvent event) {
        if (event.beans.size() == 0) {
            ToastUtils.show("暂无数据");
        } else {
            WordsDBManager manager = WordsDBManager.getInstance();
            RemeberWordSAdapter remeberWordSAdapter = new RemeberWordSAdapter(event, this);
            remeberWordSAdapter.setListener(new RemeberWordSAdapter.OnWordStateChangeListener() {
                @Override
                public void unGetIt(RememberWordsBean bean) {
                    WordsDBManager.getInstance().collectNewWords(bean.wordBean);
                    manager.collectNewWords(bean.wordBean);
                    manager.forgetRememberWords(bean);
                }
            });
            rec_words.setAdapter(remeberWordSAdapter);
        }
    }
}

package com.prismk.japaneseelearn.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.adapters.NewWordSAdapter;
import com.prismk.japaneseelearn.bean.NewWordSEvent;
import com.prismk.japaneseelearn.db.word.bean.NewWordsBean;
import com.prismk.japaneseelearn.managers.WordsDBManager;
import com.prismk.japaneseelearn.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class NewWordsActivity extends BaseActivity {

    RecyclerView rec_words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        rec_words.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        WordsDBManager.getInstance().queryNewsWords();
    }

    private void initView() {
        rec_words = findViewById(R.id.rec_new_words);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_words;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NewWordSEvent event) {
        if (event.beans.size() == 0) {
            ToastUtils.show("暂无数据");
        } else {
            NewWordSAdapter newWordSAdapter = new NewWordSAdapter(event, this);
            newWordSAdapter.setListener(new NewWordSAdapter.OnWordStateChangeListener() {
                @Override
                public void unGetIt(NewWordsBean bean) {
                    WordsDBManager.getInstance().collectNewWords(bean.wordBean);
                }

                @Override
                public void getIt(NewWordsBean bean) {
                    WordsDBManager.getInstance().rememberWord(bean.wordBean);
                }
            });
            rec_words.setAdapter(newWordSAdapter);
        }
    }
}

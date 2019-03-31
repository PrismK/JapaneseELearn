package com.prismk.japaneseelearn.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.adapters.WordsAdapter;
import com.prismk.japaneseelearn.bean.RemeberWordEvent;
import com.prismk.japaneseelearn.db.word.bean.RememberWordsBean;
import com.prismk.japaneseelearn.db.word.bean.WordBean;
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
        rec_words = findViewById(R.id.rec_know_words);
        rec_words.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        new LinearSnapHelper().attachToRecyclerView(rec_words);
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
            WordsAdapter remeberWordSAdapter = new WordsAdapter(this, event.getWordBeans());
            remeberWordSAdapter.isKnowWords();
            remeberWordSAdapter.setOnWordsStatueChangeListener(new WordsAdapter.OnWordsStatueChangeListener() {
                @Override
                public void onForget(WordBean bean, int position) {
                    RememberWordsBean currentBeans = event.getCurrentBeans(bean);
                    if (currentBeans != null) {
                        manager.forgetRememberWords(currentBeans);
                    }
                    manager.insertNewWord(bean);
                }

                @Override
                public void onGetIt(WordBean bean, int position) {

                }
            });
            rec_words.setAdapter(remeberWordSAdapter);
        }
    }
}

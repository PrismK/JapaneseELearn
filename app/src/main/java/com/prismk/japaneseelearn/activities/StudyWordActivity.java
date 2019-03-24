package com.prismk.japaneseelearn.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.adapters.WordsAdapter;
import com.prismk.japaneseelearn.bean.AllWordsEvent;
import com.prismk.japaneseelearn.db.word.bean.WordBean;
import com.prismk.japaneseelearn.managers.WordsDBManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class StudyWordActivity extends BaseActivity {

    RecyclerView rec_words;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_study_word;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        WordsDBManager.getInstance().queryWords();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rec_words.setLayoutManager(linearLayoutManager);

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rec_words);
    }

    private void initView() {
        rec_words = findViewById(R.id.words);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AllWordsEvent event) {
        WordsAdapter wordsAdapter = new WordsAdapter(this, event.beans);
        wordsAdapter.setOnWordsStatueChangeListener(new WordsAdapter.OnWordsStatueChangeListener() {
            @Override
            public void onForget(WordBean bean) {

            }

            @Override
            public void onGetit(WordBean bean) {

            }
        });
        rec_words.setAdapter(wordsAdapter);
    }

}


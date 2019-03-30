package com.prismk.japaneseelearn.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.adapters.NewWordSAdapter;
import com.prismk.japaneseelearn.bean.NewWordSEvent;
import com.prismk.japaneseelearn.db.word.bean.NewWordsBean;
import com.prismk.japaneseelearn.managers.WordsDBManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CollectNewWordsActivity extends BaseActivity {

    RecyclerView rec_words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        WordsDBManager.getInstance().queryCollectNewsWords();
    }

    private void initView() {
        rec_words = findViewById(R.id.vg_collect_newwords);
        new LinearSnapHelper().attachToRecyclerView(rec_words);
        rec_words.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collect_new_words;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NewWordSEvent event) {

        NewWordSAdapter newWordSAdapter = new NewWordSAdapter(event, this);
        newWordSAdapter.isCollect();
        newWordSAdapter.setListener(new NewWordSAdapter.OnWordStateChangeListener() {
            @Override
            public void unGetIt(NewWordsBean bean) {
                WordsDBManager.getInstance().rememberWord(bean.wordBean);
//                WordsDBManager.getInstance().collectNewWords(bean.wordBean);
            }

            @Override
            public void getIt(NewWordsBean bean) {

            }
        });
        rec_words.setAdapter(newWordSAdapter);

    }
}

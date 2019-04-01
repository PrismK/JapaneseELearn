package com.prismk.japaneseelearn.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.adapters.WordsAdapter;
import com.prismk.japaneseelearn.bean.AllWordsEvent;
import com.prismk.japaneseelearn.db.word.bean.WordBean;
import com.prismk.japaneseelearn.managers.WordsDBManager;
import com.prismk.japaneseelearn.widgets.Title;

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
        setStatusBarDark();
        initTitle();
        initView();
        WordsDBManager.getInstance().queryWords();
    }

    private void initTitle() {
        Title mTitle = findViewById(R.id.title);
        mTitle.setShowDivider(false);
        mTitle.setTitleNameStr("所有单词");
        mTitle.setTheme(Title.TitleTheme.THEME_LIGHT);
        Title.ButtonInfo buttonInfoLeft = new Title.ButtonInfo(true, Title.BUTTON_LEFT);
        buttonInfoLeft.iconRes = R.drawable.selector_btn_titleback;
        mTitle.setButtonInfo(buttonInfoLeft);
        mTitle.setOnTitleButtonClickListener(onTitleButtonClickListener);
    }

    private Title.OnTitleButtonClickListener onTitleButtonClickListener = new Title.OnTitleButtonClickListener() {
        @Override
        public void onClick(int id, Title.ButtonViewHolder viewHolder) {
            if (id == Title.BUTTON_LEFT) {
                goBack();
            }
        }
    };

    private void initView() {
        rec_words = findViewById(R.id.words);
        rec_words.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AllWordsEvent event) {
        WordsAdapter wordsAdapter = new WordsAdapter(this, event.beans);
        wordsAdapter.setOnWordsStatueChangeListener(new WordsAdapter.OnWordsStatueChangeListener() {
            @Override
            public void onForget(WordBean bean,int u) {
                WordsDBManager.getInstance().insertNewWord(bean);

            }

            @Override
            public void onGetIt(WordBean bean,int u) {
                WordsDBManager.getInstance().collectNewWords(bean);

            }
        });
        rec_words.setAdapter(wordsAdapter);
    }

}


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
import com.prismk.japaneseelearn.utils.ToastUtils;
import com.prismk.japaneseelearn.widgets.Title;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class NewWordsActivity extends BaseActivity {

    RecyclerView rec_words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        initTitle();
        initView();
        WordsDBManager.getInstance().queryNewsWords();
    }

    private void initTitle() {
        Title mTitle = findViewById(R.id.title);
        mTitle.setShowDivider(false);
        mTitle.setTitleNameStr("生词本");
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
        rec_words = findViewById(R.id.rec_new_words);
        new LinearSnapHelper().attachToRecyclerView(rec_words);
        rec_words.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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

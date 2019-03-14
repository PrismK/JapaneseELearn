package com.prismk.japaneseelearn.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.activities.CollectNewWordsActivity;
import com.prismk.japaneseelearn.activities.IKnowWordActivity;
import com.prismk.japaneseelearn.activities.NewWordsActivity;
import com.prismk.japaneseelearn.activities.StudyWordActivity;
import com.prismk.japaneseelearn.widgets.Title;


/**
 * 首页Fragment
 */
public class WordFragment extends BaseFragment {

    private Button btn_all_word;
    private Button btn_unknow;
    private Button btn_know;
    private Button btn_unknow_correct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initTitle();
        View wordLayout = View.inflate(getActivity(), getLayoutId(), null);
        initWidgets();
        initOnClickListener();
        return mRootView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_word;
    }

    private void initOnClickListener() {
        btn_all_word.setOnClickListener(onButtonClickListener);
        btn_unknow.setOnClickListener(onButtonClickListener);
        btn_know.setOnClickListener(onButtonClickListener);
        btn_unknow_correct.setOnClickListener(onButtonClickListener);
    }

    private void initWidgets() {
        btn_all_word = mRootView.findViewById(R.id.btn_all_word);
        btn_unknow = mRootView.findViewById(R.id.btn_unknow);
        btn_know = mRootView.findViewById(R.id.btn_know);
        btn_unknow_correct = mRootView.findViewById(R.id.btn_unknow_correct);
    }

    private View.OnClickListener onButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.btn_all_word:
                    intent = new Intent(getActivity(), StudyWordActivity.class);
                    break;
                case R.id.btn_unknow:
                    intent = new Intent(getActivity(), NewWordsActivity.class);
                    break;
                case R.id.btn_know:
                    intent = new Intent(getActivity(), IKnowWordActivity.class);
                    break;
                case R.id.btn_unknow_correct:
                    intent = new Intent(getActivity(), CollectNewWordsActivity.class);
                    break;
            }
            startActivity(intent);
        }
    };

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        setStatusBarView();
    }

    private void initTitle() {
        Title title = mRootView.findViewById(R.id.title);
        title.setTitleNameStr("单词");
        title.setTheme(Title.TitleTheme.THEME_LIGHT);
        title.setShowDivider(true);
    }
}
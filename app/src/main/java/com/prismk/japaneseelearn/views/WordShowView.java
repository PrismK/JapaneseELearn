package com.prismk.japaneseelearn.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.db.word.bean.WordBean;
import com.prismk.japaneseelearn.managers.DBManager;

import java.util.ArrayList;
import java.util.List;

public class WordShowView extends LinearLayout {

    private List<ViewHolder> currentViews = new ArrayList<>();

    public WordShowView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public WordShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WordShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setData(List<WordBean> beans) {

        for (ViewHolder holder : currentViews) {
            holder.clean();
        }
        for (int i = 0; i < beans.size(); i++) {
            ViewHolder holder;
            if (i >= currentViews.size()) {
                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.item_word_check, null);
                addView(inflate);
                holder = new ViewHolder(inflate);
                currentViews.add(holder);
            } else {
                holder = currentViews.get(i);
            }
            holder.setData(beans.get(i));
            holder.setEvent(beans.get(i));
        }

    }

    private class ViewHolder {
        private View view;
        private TextView id;
        private TextView chi;
        private TextView eng;
        private Button btn_insert_newWords;

        public ViewHolder(View view) {
            this.view = view;
            id = view.findViewById(R.id.tv_id);
            chi = view.findViewById(R.id.tv_chinese);
            eng = view.findViewById(R.id.tv_japanese);
            btn_insert_newWords = view.findViewById(R.id.btn_insert_newWords);
        }

        public void setData(WordBean wordBean) {
            id.setText(String.valueOf(wordBean.id));
            chi.setText(wordBean.chinese);
            eng.setText(wordBean.japanese);
        }

        public void setEvent(WordBean wordBean){
            btn_insert_newWords.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    insertToNewWords(wordBean);
                }
            });
        }

        public void clean() {
            id.setText("");
            chi.setText("");
            eng.setText("");
            btn_insert_newWords.setVisibility(View.GONE);
        }

        public void insertToNewWords(WordBean wordBean){
            DBManager.getInstance().insertNewWord(wordBean);
        }

        public void setGone() {
            view.setVisibility(View.GONE);
        }
    }
}

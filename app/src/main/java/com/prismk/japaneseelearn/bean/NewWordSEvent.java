package com.prismk.japaneseelearn.bean;

import com.prismk.japaneseelearn.db.word.bean.NewWordsBean;

import java.util.List;

public class NewWordSEvent {
    public List<NewWordsBean> beans;

    public NewWordSEvent(List<NewWordsBean> beans) {
        this.beans = beans;
    }
}

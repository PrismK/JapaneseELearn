package com.prismk.japaneseelearn.bean;

import com.prismk.japaneseelearn.db.word.bean.WordBean;

import java.util.List;

public class AllWordsEvent {

    public List<WordBean> beans;

    public AllWordsEvent(List<WordBean> beans) {
        this.beans = beans;
    }
}

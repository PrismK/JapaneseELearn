package com.prismk.japaneseelearn.bean;

import com.prismk.japaneseelearn.db.word.bean.RememberWordsBean;

import java.util.List;

public class RemeberWordEvent {
    public List<RememberWordsBean> beans;

    public RemeberWordEvent(List<RememberWordsBean> beans) {
        this.beans = beans;
    }
}

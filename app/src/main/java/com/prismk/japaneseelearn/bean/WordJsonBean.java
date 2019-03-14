package com.prismk.japaneseelearn.bean;

import com.prismk.japaneseelearn.db.word.bean.WordBean;

import java.util.List;

public class WordJsonBean {


    private List<WordBean> words;

    public List<WordBean> getWords() {
        return words;
    }

    public void setWords(List<WordBean> words) {
        this.words = words;
    }


}

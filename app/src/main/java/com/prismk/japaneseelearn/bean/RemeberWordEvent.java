package com.prismk.japaneseelearn.bean;

import com.prismk.japaneseelearn.db.word.bean.RememberWordsBean;
import com.prismk.japaneseelearn.db.word.bean.WordBean;

import java.util.ArrayList;
import java.util.List;

public class RemeberWordEvent {
    public List<RememberWordsBean> beans;

    public RemeberWordEvent(List<RememberWordsBean> beans) {
        this.beans = beans;
    }

    public List<WordBean> getWordBeans() {
        ArrayList<WordBean> objects = new ArrayList<>();
        for (RememberWordsBean bean : beans) {
            objects.add(bean.wordBean);
        }
        return objects;
    }

    public RememberWordsBean getCurrentBeans(WordBean wordBean) {
        for (RememberWordsBean bean : beans) {
            if (wordBean.equals(bean.wordBean)) {
                return bean;
            }
        }
        return null;
    }
}

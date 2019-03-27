package com.prismk.japaneseelearn.views.WordShow;

public interface WordFunction<T> {
    void setCurrentType(WordShowType type);

    enum WordShowType {
        NEW, COLLECTED
    }

    void BindData(T t);
}

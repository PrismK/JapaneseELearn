package com.prismk.japaneseelearn.bean;

/**
 * create by Nevermore
 * 2019/4/23
 * 你不愿回忆的过去已经死亡，就让黑夜把它永远埋葬
 */
public class ArticleData {
    private String title;
    private String context;

    public ArticleData(String title, String context) {
        this.title = title;
        this.context = context;
    }

    public ArticleData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}

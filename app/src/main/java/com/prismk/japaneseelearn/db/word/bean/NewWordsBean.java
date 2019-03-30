package com.prismk.japaneseelearn.db.word.bean;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.prismk.japaneseelearn.utils.LogicUtils;


@Entity(tableName = "newWords")
public class NewWordsBean {


    @PrimaryKey(autoGenerate = true)
    public long new_id;

    public long phone;
    public boolean collection;

    public NewWordsBean(boolean collection, WordBean wordBean) {
        this.phone = LogicUtils.getInstance().getUserPhone();
        this.collection = collection;
        this.wordBean = wordBean;
    }

    public NewWordsBean() {
    }

    @Embedded
    public WordBean wordBean;

    @Ignore
    public boolean isFont = true;
}

package com.prismk.japaneseelearn.db.word.bean;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.prismk.japaneseelearn.utils.LogicUtils;


@Entity(tableName = "remember")
public class RememberWordsBean {
    @PrimaryKey(autoGenerate = true)
    public long new_id;

    public long phone;
    @Embedded
    public WordBean wordBean;

    public RememberWordsBean(WordBean wordBean) {
        this.phone = LogicUtils.getInstance().getUserPhone();
        this.wordBean = wordBean;
    }
}

package com.prismk.japaneseelearn.db.word.bean;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.prismk.japaneseelearn.utils.LogicUtils;

@Entity(tableName = "words")
public class WordBean {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String japanese;
    public String chinese;
    public String katakana;
    public String example;


}

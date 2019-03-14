package com.prismk.japaneseelearn.db.word.bean;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.prismk.japaneseelearn.db.word.baseDao.NewWordsDao;
import com.prismk.japaneseelearn.db.word.baseDao.RememberWordsDao;
import com.prismk.japaneseelearn.db.word.baseDao.WordDao;

@Database(entities ={WordBean.class,NewWordsBean.class,RememberWordsBean.class} ,version = 1,exportSchema = false)
public abstract class WordDataBase extends RoomDatabase {
    public abstract WordDao getWordDao();
    public abstract NewWordsDao getNewWordsDao();
    public abstract RememberWordsDao getRememberWords();

}

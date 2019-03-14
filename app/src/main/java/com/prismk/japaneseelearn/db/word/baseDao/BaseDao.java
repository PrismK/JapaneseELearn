package com.prismk.japaneseelearn.db.word.baseDao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

@Dao
public interface BaseDao<T> {

    @Insert
    void insertWord(T... beans);

    @Update
    void UpdateWord(T... beans);

    @Delete
    void deleteWords(T... beans);

}

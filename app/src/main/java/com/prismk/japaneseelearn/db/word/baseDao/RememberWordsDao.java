package com.prismk.japaneseelearn.db.word.baseDao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.prismk.japaneseelearn.db.word.bean.RememberWordsBean;

import java.util.List;

@Dao
public interface RememberWordsDao extends BaseDao<RememberWordsBean>{
    @Query("SELECT * from remember where phone=:phone")
    List<RememberWordsBean> getUserRemeberWords(long phone);

    @Query("DELETE  FROM remember where phone=:phone and chinese=:chinese")

    void forgetWord(long phone,String chinese);
}

package com.prismk.japaneseelearn.db.word.baseDao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.prismk.japaneseelearn.db.word.bean.NewWordsBean;

import java.util.List;

@Dao
public interface NewWordsDao extends BaseDao<NewWordsBean> {
    @Query("SELECT * from newWords where phone=:phone")
    List<NewWordsBean> getUserAllNewWords(long phone);

    @Query("SELECT * from newWords where phone=:phone and collection=1")
    List<NewWordsBean> getUserCollectNewWords(long phone);

    @Query("SELECT * from newWords where phone=:phone and collection=0")
    List<NewWordsBean> getUserUnCollectNewWords(long phone);

    //根据名字删除
    @Query("DELETE  FROM newWords where phone=:phone and chinese=:chinese")
    void deleteFormName(long phone, String chinese);

    @Query("UPDATE newWords SET collection = 1 WHERE chinese = :chinese and collection=0 and phone =:phone")
    void upDateCollect(long phone, String chinese);

}

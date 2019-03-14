package com.prismk.japaneseelearn.db.word.baseDao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;


import com.prismk.japaneseelearn.db.word.bean.WordBean;

import java.util.List;
@Dao
public interface WordDao extends BaseDao<WordBean> {

    @Query("SELECT * from words")
    List<WordBean> getAll();

}

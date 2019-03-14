package com.prismk.japaneseelearn.managers;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.google.gson.Gson;
import com.prismk.japaneseelearn.bean.WordJsonBean;
import com.prismk.japaneseelearn.db.word.baseDao.WordDao;
import com.prismk.japaneseelearn.db.word.bean.NewWordsBean;
import com.prismk.japaneseelearn.db.word.bean.RememberWordsBean;
import com.prismk.japaneseelearn.db.word.bean.WordBean;
import com.prismk.japaneseelearn.db.word.bean.WordDataBase;
import com.prismk.japaneseelearn.services.DBservier;
import com.prismk.japaneseelearn.utils.LogicUtils;

import java.util.List;

public class DBManager {
    private static final String JSON_BEAN = "{\"words\":[{\"japanese\":\"風\",\"chinese\":\"风\",\"katakana\":\"かぜ\",\"example\":\"風XXXXXXXXX\"},{\"japanese\":\"吹いて\",\"chinese\":\"吹\",\"katakana\":\"ふいて\",\"example\":\"吹いてXXXXXXXXX\"},{\"japanese\":\"雪\",\"chinese\":\"雪\",\"katakana\":\"ゆき\",\"example\":\"雪XXXXXXXXXX\"},{\"japanese\":\"犬\",\"chinese\":\"狗\",\"katakana\":\"いぬ\",\"example\":\"犬XXXXXXXXX\"},{\"japanese\":\"気\",\"chinese\":\"气\",\"katakana\":\"き\",\"example\":\"気XXXXXXXXX\"},{\"japanese\":\"兄\",\"chinese\":\"哥哥\",\"katakana\":\"あに\",\"example\":\"兄XXXXXXXXX\"},{\"japanese\":\"姉\",\"chinese\":\"姐姐\",\"katakana\":\"あね\",\"example\":\"姉XXXXXXXXX\"},{\"japanese\":\"弟\",\"chinese\":\"弟弟\",\"katakana\":\"おとうと\",\"example\":\"弟XXXXXXXXX\"},{\"japanese\":\"料理\",\"chinese\":\"料理\",\"katakana\":\"りょうり\",\"example\":\"料理XXXXXXXXX\"},{\"japanese\":\"戦士\",\"chinese\":\"战士\",\"katakana\":\"せんし\",\"example\":\"戦士XXXXXXXXX\"},{\"japanese\":\"先生\",\"chinese\":\"老师\",\"katakana\":\"せんせい\",\"example\":\"先生XXXXXXXXX\"},{\"japanese\":\"生徒\",\"chinese\":\"学生\",\"katakana\":\"せいと\",\"example\":\"生徒XXXXXXXXX\"},{\"japanese\":\"上手\",\"chinese\":\"擅长\",\"katakana\":\"じょうず\",\"example\":\"上手XXXXXXXXX\"},{\"japanese\":\"車\",\"chinese\":\"车\",\"katakana\":\"くるま\",\"example\":\"車XXXXXXXXX\"},{\"japanese\":\"一番\",\"chinese\":\"第一\",\"katakana\":\"いちばん\",\"example\":\"一番XXXXXXXXX\"}]}";
    private static WordDataBase word;
    private static DBManager manager;

    public static DBManager getInstance() {
        if (manager == null) {
            manager = new DBManager();
        }
        return manager;
    }

    private DBManager() {
    }

    public static void init(Context context) {
        word = Room.databaseBuilder(context.getApplicationContext(), WordDataBase.class, "words.db").build();
        initSqlData();
    }

    private static void initSqlData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                WordDao wordDao = word.getWordDao();
                if (word.getWordDao().getAll().size() == 0) {
                    List<WordBean> words = new Gson().fromJson(JSON_BEAN, WordJsonBean.class).getWords();
                    if (words != null) {
                        wordDao.insertWord(words.toArray(new WordBean[words.size()]));
                    }
                }
            }
        }.start();
    }


    public void queryWords() {
        DBservier.getInstance().queryAllWords(word.getWordDao());
    }

    public void queryNewsWords() {
        DBservier.getInstance().queryNewWords(word.getNewWordsDao());
    }

    public void queryCollectNewsWords() {
        DBservier.getInstance().queryCollectNewWords(word.getNewWordsDao());
    }

    public void queryKnowWords() {
        DBservier.getInstance().queryRememberWordWords(word.getRememberWords());
    }


    public void collectNewWords(WordBean wordsBean) {
        DBservier.getInstance().async(() -> word.getNewWordsDao().upDateCollect(LogicUtils.getInstance().getUserPhone(), wordsBean.chinese));
    }

    public void rememberWord(WordBean wordsBean) {
        DBservier.getInstance().async(() -> {
            word.getNewWordsDao().deleteFormName(LogicUtils.getInstance().getUserPhone(), wordsBean.chinese);
            word.getRememberWords().insertWord(new RememberWordsBean(wordsBean));
        });
    }

    public void insertNewWord(WordBean wordsBean) {
        NewWordsBean newWordsBean = new NewWordsBean();
        newWordsBean.phone = LogicUtils.getInstance().getUserPhone();
        newWordsBean.collection = false;
        newWordsBean.wordBean = wordsBean;

        DBservier.getInstance().async(() -> {
            word.getNewWordsDao().insertWord(newWordsBean);
            word.getNewWordsDao().UpdateWord(newWordsBean);
        });
    }


    public void forgetRememberWords(RememberWordsBean bean) {

        DBservier.getInstance().async(() -> {
            word.getRememberWords().deleteWords(bean);
        });
    }

}

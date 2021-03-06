package com.prismk.japaneseelearn.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.prismk.japaneseelearn.bean.AllWordsEvent;
import com.prismk.japaneseelearn.bean.NewWordSEvent;
import com.prismk.japaneseelearn.bean.RemeberWordEvent;
import com.prismk.japaneseelearn.db.word.baseDao.NewWordsDao;
import com.prismk.japaneseelearn.db.word.baseDao.RememberWordsDao;
import com.prismk.japaneseelearn.db.word.baseDao.WordDao;
import com.prismk.japaneseelearn.managers.WordsDBManager;
import com.prismk.japaneseelearn.utils.LogicUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WordsDBServer extends Service {

    private static ExecutorService executorService;
    private static WordsDBServer dbServer;

    public static WordsDBServer getInstance() {
        return dbServer;
    }

    public WordsDBServer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new Binder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WordsDBManager.init(getApplicationContext());
        dbServer = this;
        executorService = Executors.newFixedThreadPool(4);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService = null;
        dbServer = null;
    }

    public void async(Runnable runnable) {
        executorService.execute(runnable);
    }

    public void queryAllWords(WordDao dao) {
        executorService.execute(() ->
                EventBus.getDefault().post(new AllWordsEvent(dao.getAll()))

        );
    }

    public void queryNewWords(NewWordsDao dao) {
        executorService.execute(() -> EventBus.getDefault().post(new NewWordSEvent(dao.getUserUnCollectNewWords(LogicUtils.getInstance().getUserPhone()))));
    }

    public void queryCollectNewWords(NewWordsDao dao) {
        executorService.execute(() -> EventBus.getDefault().post(new NewWordSEvent(dao.getUserCollectNewWords(LogicUtils.getInstance().getUserPhone()))));
    }

    public void queryRememberWordWords(RememberWordsDao dao) {
        executorService.execute(() -> EventBus.getDefault().post(new RemeberWordEvent(dao.getUserRemeberWords(LogicUtils.getInstance().getUserPhone()))));
    }

}

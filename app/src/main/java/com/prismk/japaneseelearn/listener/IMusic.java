package com.prismk.japaneseelearn.listener;

/**
 * Created by 棱镜 Prism *
 * 2018/5/24 *
 */
public interface IMusic {
     void play();
     void pause();
     void replay();
     void stop();
     int getPlayState();
}

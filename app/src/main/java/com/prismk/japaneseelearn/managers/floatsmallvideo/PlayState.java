package com.prismk.japaneseelearn.managers.floatsmallvideo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
    public @interface PlayState {
        int PLAY = 1;
        int PAUSE = 2;
        int STOP = 3;
    }
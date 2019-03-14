package com.prismk.japaneseelearn.widgets.elearncamera;

import android.app.Activity;
import android.content.Intent;

import com.prismk.japaneseelearn.R;

/**
 * Create by chn on 2018/4/20.
 */

public class ElearnCameraManager {

    private static ElearnCameraManager elearnCameraManager;
    private TakePictureListener mTakePictureListener;
    private int requestCode;

    public static ElearnCameraManager getInstance() {
        if (elearnCameraManager == null) {
            elearnCameraManager = new ElearnCameraManager();
        }
        return elearnCameraManager;
    }

    private ElearnCameraManager() {

    }

    public void startCamera(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, ElearnCameraActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
        this.requestCode = requestCode;
    }

    public void setTakePhotoListener(TakePictureListener takePictureListener) {
        this.mTakePictureListener = takePictureListener;
    }

    public void removeTakePhotoListener() {
        this.mTakePictureListener = null;
    }

    public void notifyPictureTaken(String path) {
        if (mTakePictureListener != null) {
            mTakePictureListener.onPictureTaken(requestCode, path);
        }
    }

    public interface TakePictureListener {
        void onPictureTaken(int requestCode, String path);
    }
}



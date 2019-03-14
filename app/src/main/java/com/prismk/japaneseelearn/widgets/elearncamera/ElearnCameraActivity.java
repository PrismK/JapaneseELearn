package com.prismk.japaneseelearn.widgets.elearncamera;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.activities.BaseActivity;
import com.prismk.japaneseelearn.services.ELearnApplication;
import com.prismk.japaneseelearn.widgets.elearncamera.state.ErrorListener;
import com.prismk.japaneseelearn.widgets.elearncamera.util.SavePhotoUtil;
import com.prismk.japaneseelearn.widgets.elearncamera.view.JCameraListener;
import com.prismk.japaneseelearn.widgets.elearncamera.view.ElearnCamera;

import java.io.File;

public class ElearnCameraActivity extends BaseActivity {

    private ElearnCamera elearnCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        elearnCamera = (ElearnCamera) findViewById(R.id.jcameraview);
        //设置视频保存路径
        elearnCamera.setSaveVideoPath(Environment
                .getExternalStorageDirectory().getPath() + File.separator +
                "JCamera");
        elearnCamera.setMediaQuality(ElearnCamera.MEDIA_QUALITY_MIDDLE);
        elearnCamera.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //错误监听
                finish();
                goDownAnim();
            }

            @Override
            public void AudioPermissionError() {
                Toast.makeText(ElearnCameraActivity.this,
                        ELearnApplication.getELearnAppContext().getString(R
                                .string.request_record_permission), Toast
                                .LENGTH_SHORT).show();
            }
        });
        //JCameraView监听
        elearnCamera.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap

                String storePath = SavePhotoUtil.saveImage
                        (ElearnCameraActivity.this, bitmap);
                ElearnCameraManager.getInstance().notifyPictureTaken
                        (storePath);
                finish();
                goDownAnim();

            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径

            }
        });

        //disableBack();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_widget_elearn_camere;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        elearnCamera.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        elearnCamera.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        elearnCamera.onDestroy();

    }


}

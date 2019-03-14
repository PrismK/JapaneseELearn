package com.prismk.japaneseelearn.widgets.elearncamera.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.services.ELearnApplication;
import com.prismk.japaneseelearn.widgets.ELearnToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SavePhotoUtil {
    public static String saveImage(Activity activity, Bitmap bmp) {

        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (activity.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    activity.requestPermissions(permissions, REQUEST_CODE_CONTACT);

                }
            }
        }

        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File
                .separator + "/1ELearnFolder/";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            //把文件插入到系统图库
            // MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();

    }

    //保存文件到指定路径
    public static boolean saveImageToAlbum(Activity activity, Bitmap bmp) {

        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (activity.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    activity.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return true;
                }
            }
        }

        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File
                .separator + "1ELearnFolder";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            // MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                ELearnToast.showShort(ELearnApplication.getELearnAppContext().getString(R.string.save_ok));
                return true;

            } else {
                ELearnToast.showShort(ELearnApplication.getELearnAppContext().getString(R.string.save_failed));
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

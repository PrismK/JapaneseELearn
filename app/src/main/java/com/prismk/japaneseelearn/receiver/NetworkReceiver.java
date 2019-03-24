package com.prismk.japaneseelearn.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * ================================================
 * 作    者：prism（棱镜）Github地址：https://github.com/prismk/
 * 本地用户：18340
 * 版    本：1.0
 * 创建日期：2019/3/25
 * 描    述：
 * 修订历史：
 * ================================================
 */

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        //如果无网络连接activeInfo为null

        //也可获取网络的类型
        if (activeInfo != null) {
            //网络连接
            Toast.makeText(context, "测试：网络连接成功", Toast.LENGTH_SHORT).show();
        } else {
            //网络断开
            Toast.makeText(context, "测试：网络断开", Toast.LENGTH_SHORT).show();
        }
    }
}

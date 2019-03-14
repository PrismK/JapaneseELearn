package com.prismk.japaneseelearn.utils.httputils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtils {

    public static void sendOkHttpRequest(final String address, final Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpRequest(final String address, FormBody.Builder param, Callback callback) {

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(address).post(param.build()).build();
        System.out.println(request.toString());
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }
}

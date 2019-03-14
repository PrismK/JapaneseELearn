package com.prismk.japaneseelearn.utils.httputils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpStreamOP{

    public InputStream getInputStream(String path) throws Exception {


        InputStream in = null;

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        int code = conn.getResponseCode();
        if (code == 200) {
            in = conn.getInputStream();
        }
        return in;
    }
}


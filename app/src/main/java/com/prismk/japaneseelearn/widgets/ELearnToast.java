package com.prismk.japaneseelearn.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.services.ELearnApplication;

public class ELearnToast {
    private Toast mToast;
    private ELearnToast(Context context, String text, int duration) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_widget_elearn_toast, null);
        TextView textView = (TextView) v.findViewById(R.id.tv_toastContent);
        textView.setText(text);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(v);
        mToast.show();
    }

    public static void showShort(String text) {
        new ELearnToast(ELearnApplication.getELearnAppContext(), text, Toast.LENGTH_SHORT);
    }

    public static void showLong(String text) {
        new ELearnToast(ELearnApplication.getELearnAppContext(), text, Toast.LENGTH_LONG);
    }
}


package com.prismk.japaneseelearn.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * create by Nevermore
 * 2019/3/23
 * 你不愿回忆的过去已经死亡，就让黑夜把它永远埋葬
 */
public class NoScrollListView extends ListView {
    private int count = 7;//item条数，默认为count+1 = 8

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setListViewHeight();
//            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//                    MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void setListViewHeight() {
        ListAdapter listAdapter = NoScrollListView.this.getAdapter();
        if (listAdapter == null) {
            return;
        }
        View view = listAdapter.getView(0, null, NoScrollListView.this);
        view.measure(0, 0);
        int itemHeight = view.getMeasuredHeight();
        int itemCount = listAdapter.getCount();
        LinearLayout.LayoutParams layoutParams ;
        if (itemCount <= 1) {
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * itemCount);
            NoScrollListView.this.setLayoutParams(layoutParams);
        } else if (itemCount > 2) {
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * (count+1));
            NoScrollListView.this.setLayoutParams(layoutParams);
        }

    }
    //设置数量
    public void setItemCount(int count) {
        this.count = count;
    }
}






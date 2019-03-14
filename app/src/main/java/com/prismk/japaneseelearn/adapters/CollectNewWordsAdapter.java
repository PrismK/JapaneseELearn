package com.prismk.japaneseelearn.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.prismk.japaneseelearn.db.word.bean.NewWordsBean;

import java.util.ArrayList;


class CollectNewWordsAdapter extends PagerAdapter {
    private ArrayList<View> items;

    public void CollectNewWordsAdapter(ArrayList<View> items){
        this.items = items;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View layout = items.get(position);
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View layout = items.get(position);
        container.removeView(layout);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return POSITION_NONE;
    }

    public interface OnWordStateChangeListener {

        void getIt(NewWordsBean bean);
    }
}
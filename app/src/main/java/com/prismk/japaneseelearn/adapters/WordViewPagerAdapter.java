package com.prismk.japaneseelearn.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.prismk.japaneseelearn.db.word.bean.WordBean;
import com.prismk.japaneseelearn.views.WordShowView;

import java.util.ArrayList;
import java.util.List;

public class WordViewPagerAdapter extends PagerAdapter {

    private List<WordBean> wordBeans;
    private Context context;

    public WordViewPagerAdapter(List<WordBean> wordBeans, Context context) {
        this.wordBeans = wordBeans;
        this.context = context;
    }


    @Override
    public int getCount() {
        int size = wordBeans.size() / 5;
        if (wordBeans.size() % 5 != 0) {
            size += 1;
        }
        return size;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        WordShowView wordShowView = new WordShowView(context);
        ArrayList<WordBean> beans = new ArrayList<>();
        int start = position * 5;
        int end = (position + 1) * 5;

        end = end < wordBeans.size() ? end : wordBeans.size();
        for (int i = start; i < end; i++) {
            beans.add(wordBeans.get(i));
        }
        wordShowView.setData(beans);

        container.addView(wordShowView);
        return wordShowView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


    public void removePageData() {
        int end = 5 < wordBeans.size() ? 5 : wordBeans.size();

        for (int current = 0; current < end; current++) {
            wordBeans.remove(current);
        }
        notifyDataSetChanged();
    }

}

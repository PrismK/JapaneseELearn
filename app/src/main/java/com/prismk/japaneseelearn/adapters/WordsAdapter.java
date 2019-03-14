package com.prismk.japaneseelearn.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.prismk.japaneseelearn.db.word.bean.WordBean;
import com.prismk.japaneseelearn.views.WordShowView;

import java.util.ArrayList;
import java.util.List;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder> {

    private Context context;
    private List<WordBean> wordBeans;

    public WordsAdapter(Context context, List<WordBean> wordBeans) {
        this.context = context;
        this.wordBeans = wordBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(new WordShowView(context));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ArrayList<WordBean> beans = new ArrayList<>();
        int start = position * 5;
        int end = (position + 1) * 5;
        end = end < wordBeans.size() ? end : wordBeans.size();
        for (int i = start; i < end; i++) {
            beans.add(wordBeans.get(i));
        }
        viewHolder.bindData(beans);
    }


    @Override
    public int getItemCount() {
        int size = wordBeans.size() / 5;
        if (wordBeans.size() % 5 != 0) {
            size += 1;
        }
        return size;
    }

    public void removePageData() {
        int end = 5 < wordBeans.size() ? 5 : wordBeans.size();

        for (int current = 0; current < end; current++) {
            wordBeans.remove(current);
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private WordShowView view;

        public ViewHolder(WordShowView view) {
            super(view);
            this.view = view;
        }

        public void bindData(List<WordBean> beans) {
            view.setData(beans);
        }
    }
}

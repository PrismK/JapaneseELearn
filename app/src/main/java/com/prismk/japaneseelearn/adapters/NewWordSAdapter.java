package com.prismk.japaneseelearn.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.prismk.japaneseelearn.bean.NewWordSEvent;
import com.prismk.japaneseelearn.db.word.bean.NewWordsBean;
import com.prismk.japaneseelearn.db.word.bean.WordBean;
import com.prismk.japaneseelearn.views.WordShow.WordShowView;

import java.util.List;

public class NewWordSAdapter extends RecyclerView.Adapter<NewWordSAdapter.ViewHolder> {
    private List<NewWordsBean> beans;
    private Context context;

    public NewWordSAdapter(NewWordSEvent event, Context context) {
        beans = event.beans;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(new WordShowView(context));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindData(beans.get(i));
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {


        private WordShowView wordShowView;

        public ViewHolder(WordShowView wordShowView) {
            super(wordShowView);
            this.wordShowView = wordShowView;

        }

        public void bindData(NewWordsBean bean) {
            wordShowView.BindData(bean.wordBean);
            wordShowView.setOnButtonClickListener(new WordShowView.OnButtonClickListener() {
                @Override
                public void OnPostionCLick(@Nullable WordBean wordBean) {
                    if (listener != null) {
                        moveItem();
                        listener.getIt(bean);
                    }
                }

                @Override
                public void OnNagitiveCLick(@Nullable WordBean wordBean) {
                    if (listener != null) {
                        moveItem();
                        listener.unGetIt(bean);
                    }

                }

                private void moveItem() {
                    if (beans.contains(bean)) {
                        beans.remove(bean);
                        notifyDataSetChanged();
                    }
                }
            });
        }

    }

    private OnWordStateChangeListener listener;

    public void setListener(OnWordStateChangeListener listener) {
        this.listener = listener;
    }

    public interface OnWordStateChangeListener {
        void unGetIt(NewWordsBean bean);

        void getIt(NewWordsBean bean);
    }
}

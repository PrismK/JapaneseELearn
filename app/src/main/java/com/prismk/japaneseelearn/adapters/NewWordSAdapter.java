package com.prismk.japaneseelearn.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.bean.NewWordSEvent;
import com.prismk.japaneseelearn.db.word.bean.NewWordsBean;

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
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_new_words, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindData(beans.get(i));
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_china;
        private final TextView tv_english;
        private final TextView tv_katakana;
        private final TextView tv_example;
        private final TextView btn_konw;
        private final TextView btn_un_konw;
        private NewWordsBean bean;

        public ViewHolder(View view) {
            super(view);
            tv_china = view.findViewById(R.id.tv_chinese);
            tv_english = view.findViewById(R.id.tv_japanese);
            tv_katakana = view.findViewById(R.id.tv_katakana);
            tv_example = view.findViewById(R.id.tv_example);
            btn_konw = view.findViewById(R.id.btn_konw);
            btn_un_konw = view.findViewById(R.id.btn_un_konw);
        }

        public void bindData(NewWordsBean beans) {
            this.bean = beans;
            tv_china.setText(beans.wordBean.chinese);
            tv_english.setText(beans.wordBean.japanese);
            tv_katakana.setText(beans.wordBean.katakana);
            tv_example.setText(beans.wordBean.example);
            btn_konw.setOnClickListener(this);
            btn_un_konw.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                if (beans.contains(bean)) {
                    beans.remove(bean);
                    notifyDataSetChanged();
                }
                switch (v.getId()) {
                    case R.id.btn_konw:
                        listener.getIt(bean);
                        break;
                    case R.id.btn_un_konw:
                        listener.unGetIt(bean);
                        break;

                }
            }
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

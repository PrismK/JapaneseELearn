package com.prismk.japaneseelearn.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.bean.RemeberWordEvent;
import com.prismk.japaneseelearn.db.word.bean.RememberWordsBean;

import java.util.List;

public class RemeberWordSAdapter extends RecyclerView.Adapter<RemeberWordSAdapter.ViewHolder> {
    private List<RememberWordsBean> beans;
    private Context context;

    public RemeberWordSAdapter(RemeberWordEvent event, Context context) {
        beans = event.beans;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_remeber_words, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindData(beans.get(i));
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_china;
        private final TextView tv_english;
        private final TextView tv_katakana;
        private final TextView tv_example;
        private final TextView btn_un_konw;

        public ViewHolder(View view) {
            super(view);
            tv_china = view.findViewById(R.id.tv_chinese);
            tv_english = view.findViewById(R.id.tv_japanese);
            tv_katakana = view.findViewById(R.id.tv_katakana);
            tv_example = view.findViewById(R.id.tv_example);
            btn_un_konw = view.findViewById(R.id.btn_un_konw);
        }

        public void bindData(RememberWordsBean beans) {
            tv_china.setText(beans.wordBean.chinese);
            tv_english.setText(beans.wordBean.japanese);
            tv_katakana.setText(beans.wordBean.katakana);
            tv_example.setText(beans.wordBean.example);
            btn_un_konw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.unGetIt(beans);

                }
            });
        }

    }

    private OnWordStateChangeListener listener;

    public void setListener(OnWordStateChangeListener listener) {
        this.listener = listener;
    }

    public interface OnWordStateChangeListener {
        void unGetIt(RememberWordsBean bean);

    }
}

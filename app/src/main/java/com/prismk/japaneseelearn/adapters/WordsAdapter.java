package com.prismk.japaneseelearn.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.db.word.bean.WordBean;
import com.prismk.japaneseelearn.views.EasyFlipView;

import java.util.List;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder> {

    private Context context;
    private List<WordBean> wordBeans;
    private OnWordsStatueChangeListener onWordsStatueChangeListener;
    private boolean isKnowWords;

    public WordsAdapter(Context context, List<WordBean> wordBeans) {
        this.context = context;
        this.wordBeans = wordBeans;
    }

    public void isKnowWords() {
        isKnowWords = true;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_studyword, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bindData(position);
    }


    @Override
    public int getItemCount() {
        return wordBeans.size();
    }

    public void setOnWordsStatueChangeListener(OnWordsStatueChangeListener onWordsStatueChangeListener) {
        this.onWordsStatueChangeListener = onWordsStatueChangeListener;

    }


    public interface OnWordsStatueChangeListener {
        void onForget(WordBean bean, int position);

        void onGetIt(WordBean bean, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button forget;
        private Button get;
        private TextView tv_jap;
        private TextView tv_exa;
        private TextView tv_jia;
        private TextView tv_line;
        private TextView tv_chinese;
        private int position;
        private WordBean wordBean;
        private final EasyFlipView easyFlipView;
        private LinearLayout ll_get_it;

        public ViewHolder(View view) {
            super(view);
            easyFlipView = view.findViewById(R.id.flipView);
            tv_jap = easyFlipView.findViewById(R.id.tv_jap);
            tv_exa = easyFlipView.findViewById(R.id.tv_exa);
            tv_jia = easyFlipView.findViewById(R.id.tv_jia);
            tv_line = easyFlipView.findViewById(R.id.tv_line);
            tv_chinese = easyFlipView.findViewById(R.id.tv_chinese);

            forget = view.findViewById(R.id.btn_add_forget);
            get = view.findViewById(R.id.btn_get_it);
            ll_get_it = view.findViewById(R.id.ll_get_it);
        }

        public void bindData(int position) {
            forget.setOnClickListener(this);
            get.setOnClickListener(this);
            if (!easyFlipView.isFrontSide()) {
                easyFlipView.flipTheView();
            }
            if (isKnowWords) {
                get.setVisibility(View.GONE);
                ll_get_it.setVisibility(View.GONE);
            }
            this.position = position;
            wordBean = wordBeans.get(position);

            tv_jap.setText(wordBean.japanese);
            tv_chinese.setText(wordBean.chinese);
            tv_jia.setText(wordBean.katakana);
            tv_exa.setText(wordBean.example);
            tv_line.setText(wordBean.example);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add_forget:
                    onWordsStatueChangeListener.onForget(wordBean, position);
                    removeItem();
                    break;
                case R.id.btn_get_it:
                    onWordsStatueChangeListener.onGetIt(wordBean, position);
                    removeItem();
                    break;
            }
        }

        private void removeItem() {
            wordBeans.remove(position);
            //notifyDataSetChanged();
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, wordBeans.size());
        }
    }

}

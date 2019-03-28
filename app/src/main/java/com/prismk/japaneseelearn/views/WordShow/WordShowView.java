package com.prismk.japaneseelearn.views.WordShow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.db.word.bean.WordBean;

public class WordShowView extends FrameLayout implements WordFunction<WordBean>, View.OnClickListener {

    private OnButtonClickListener onButtonClickListener;
    private TextView tv_exa_china;
    private TextView tv_exa;
    private TextView tv_chinese;
    private TextView tv_kata;
    private TextView tv_japanese;
    private Button btn_cancle;
    private Button btn_sumbit;
    private WordBean wordBean;


    public WordShowView(Context context) {
        super(context);
        init();
    }


    public WordShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WordShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.item_view_show_new_world, this, true);
        tv_japanese = inflate.findViewById(R.id.tv_japanese);
        tv_kata = inflate.findViewById(R.id.tv_kata);
        tv_chinese = inflate.findViewById(R.id.tv_chinese);
        tv_exa = inflate.findViewById(R.id.tv_exa);
        tv_exa_china = inflate.findViewById(R.id.tv_exa_china);
        btn_cancle = inflate.findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(this);
        btn_sumbit = inflate.findViewById(R.id.btn_sumbit);
        btn_sumbit.setOnClickListener(this);

    }

    @Override
    public void setCurrentType(WordShowType type) {
        if (type == WordShowType.COLLECTED) {
            btn_sumbit.setVisibility(GONE);
        } else {
            btn_cancle.setVisibility(VISIBLE);
            btn_sumbit.setVisibility(VISIBLE);
        }
    }

    @Override
    public void BindData(WordBean wordBean) {
        this.wordBean = wordBean;
        tv_exa_china.setText(wordBean.example);
        tv_exa.setText(wordBean.example);
        tv_japanese.setText(wordBean.japanese);
        tv_kata.setText(wordBean.katakana);
        tv_chinese.setText(wordBean.chinese);

    }


    @Override
    public void onClick(View v) {
        if (onButtonClickListener != null) {
            switch (v.getId()) {
                case R.id.btn_cancle:
                    onButtonClickListener.OnNagitiveCLick(wordBean);
                    break;
                case R.id.btn_sumbit:
                    onButtonClickListener.OnPostionCLick(wordBean);
                    break;
            }
        }
    }


    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public interface OnButtonClickListener {
        void OnPostionCLick(@Nullable WordBean wordBean);

        void OnNagitiveCLick(@Nullable WordBean wordBean);

    }
}

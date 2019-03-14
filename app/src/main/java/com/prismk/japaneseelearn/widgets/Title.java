package com.prismk.japaneseelearn.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prismk.japaneseelearn.R;

public class Title extends RelativeLayout {

    private RelativeLayout titleCustomLayoutAear = null;//自定义View区
    private View titleCustomView = null;//用户设置的自定义View
    private OnFinishCustomLayout onFinishCustomLayout = null;//如果设置自定义View可以使用此监听
    private TextView titleName = null;//标题
    private String titleNameStr = null;
    private boolean isShowTitle = true;
    public static final int GONE = 8;
    public static final int VISIBLE = 0;
    private View titleDivider = null;//分隔线
    private boolean isShowDivider = true;//是否显示分隔线
    private ProgressBar pbTitlePending;

    private ButtonViewHolder buttonHolderLeft = new ButtonViewHolder(false, BUTTON_LEFT);
    private ButtonViewHolder buttonHolderRight1 = new ButtonViewHolder(false, BUTTON_RIGHT1);
    private ButtonViewHolder buttonHolderRight2 = new ButtonViewHolder(false, BUTTON_RIGHT2);

    private OnTitleButtonClickListener onTitleButtonClickListener;

    public Title(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //自定义View区
        titleCustomLayoutAear = (RelativeLayout) findViewById(R.id.titleCustomLayoutAear);
        if(titleCustomView != null){
            titleCustomLayoutAear.setVisibility(View.VISIBLE);
            titleCustomLayoutAear.addView(titleCustomView);
        }
        //标题栏分隔线
        titleDivider = findViewById(R.id.titleDivider);

        //标题
        titleName = (TextView)findViewById(R.id.titleName);
        if(null != titleName && null != titleNameStr){
            titleName.setText(titleNameStr);
        }
        if(isShowTitle){
            titleName.setVisibility(View.VISIBLE);
        }else{
            titleName.setVisibility(View.GONE);
        }

        //左按钮
        buttonHolderLeft.rootView = findViewById(R.id.titleBackLayout);
        buttonHolderLeft.image = (ImageView)findViewById(R.id.titleBackImg);
        buttonHolderLeft.text = (TextView)findViewById(R.id.titleBackName);
        buttonHolderLeft.initOnClick();

        //右一按钮
        buttonHolderRight1.rootView = findViewById(R.id.titleRightLayout);
        buttonHolderRight1.image = (ImageView)findViewById(R.id.titleRightImg) ;
        buttonHolderRight1.text = (TextView)findViewById(R.id.titleRight) ;
        buttonHolderRight1.initOnClick();

        //右二按钮
        buttonHolderRight2.rootView = findViewById(R.id.titleRightLayout2);
        buttonHolderRight2.image = (ImageView)findViewById(R.id.titleRightImg2) ;
        buttonHolderRight2.text = (TextView)findViewById(R.id.titleRight2);
        buttonHolderRight2.initOnClick();

        refreshButtonShow(new ButtonViewHolder[]{buttonHolderLeft, buttonHolderRight1, buttonHolderRight2});

        refreshTheme(TitleTheme.THEME_LIGHT);

        if(null != onFinishCustomLayout){
            onFinishCustomLayout.onFinishCustomLayout(titleCustomLayoutAear, titleCustomView);
        }

        pbTitlePending = findViewById(R.id.pb_title_pending);
    }

    public void setTitleNameRes(int resId){
        this.titleNameStr = getResources().getString(resId);
        if(null != titleName){
            titleName.setText(titleNameStr);
        }
    }

    public void setTitleNameStr(String name){
        this.titleNameStr = name;
        if(null != titleName){
            titleName.setText(titleNameStr);
        }
    }

    public void showTitle(boolean showTitle){
        this.isShowTitle = showTitle;
        if(isShowTitle){
            titleName.setVisibility(View.VISIBLE);
        } else {
            titleName.setVisibility(View.GONE);
        }
    }

    public void setTitlePending(boolean pending) {
        if (pending) {
            pbTitlePending.setVisibility(View.VISIBLE);
        } else {
            pbTitlePending.setVisibility(View.GONE);
        }
    }

    /**
     * 添加自定义View，将会替换前一个
     * @param customView
     */
    public void addCustomView(View customView){
        titleCustomView = customView;
        if(titleCustomLayoutAear != null){
            if(customView == null){
                titleCustomLayoutAear.removeAllViews();
                titleCustomLayoutAear.setVisibility(View.GONE);
            }else{
                titleCustomLayoutAear.setVisibility(View.VISIBLE);
                if(customView != titleCustomLayoutAear.getChildAt(0)){
                    titleCustomLayoutAear.removeAllViews();
                    titleCustomLayoutAear.addView(customView);
                }
            }
        }
    }

    public void setOnFinishCustomLayout(OnFinishCustomLayout onFinishCustomLayout) {
        this.onFinishCustomLayout = onFinishCustomLayout;
    }

    /**
     * 返回指标标识的View
     * @param id
     * @return
     */
    public ButtonViewHolder getViewHolder(int id){
        switch (id){
            case BUTTON_LEFT:
                return buttonHolderLeft;

            case BUTTON_RIGHT1:
                return buttonHolderRight1;

            case BUTTON_RIGHT2:
                return buttonHolderRight2;
        }
        return null;
    }
    public enum TitleTheme {
        THEME_TRANSLATE,//主题_透明白分割线
        THEME_LIGHT,//主题_明亮，背景是浅色
        THEME_DARK,//主题_黑色，背景是深色
        THEME_TRANSLATE_NODIVIDER,//主题_透明无分割线
        THEME_TRANSLATE_GRAYDIVIDER;//主题_灰无分割线
    }


    /**
     * 设置主题
     * @param titleTheme
     */
    public void setTheme(TitleTheme titleTheme){
        refreshTheme(titleTheme);
    }

    private void refreshTheme(TitleTheme titleTheme){
        switch (titleTheme){
            case THEME_TRANSLATE:
                if(titleName != null){
                    titleName.setTextColor(Color.WHITE);
                }
                buttonHolderLeft.text.setTextColor(getResources().getColorStateList(R.color.selector_titletext_themetransport));
                buttonHolderRight1.text.setTextColor(getResources().getColorStateList(R.color.selector_titletext_themetransport));
                buttonHolderRight2.text.setTextColor(getResources().getColorStateList(R.color.selector_titletext_themetransport));

                titleDivider.setBackgroundColor(Color.WHITE);
                this.setBackgroundResource(android.R.color.transparent);
                break;

            case THEME_TRANSLATE_GRAYDIVIDER:
                if(titleName != null){
                    titleName.setTextColor(Color.WHITE);
                }
                buttonHolderLeft.text.setTextColor(getResources().getColorStateList(R.color.selector_titletext_themetransport));
                buttonHolderRight1.text.setTextColor(getResources().getColorStateList(R.color.selector_titletext_themetransport));
                buttonHolderRight2.text.setTextColor(getResources().getColorStateList(R.color.selector_titletext_themetransport));
                titleDivider.setBackgroundResource(R.color.grayDividerDark);

                this.setBackgroundResource(android.R.color.transparent);
                break;
            case THEME_LIGHT:
                if(titleName != null){
                    titleName.setTextColor(getResources().getColor(R.color.transparentAA));
                }
                buttonHolderLeft.text.setTextColor(getResources().getColorStateList(R.color.selector_titletext_themelight));
                buttonHolderRight1.text.setTextColor(getResources().getColorStateList(R.color.selector_titletext_themelight));
                buttonHolderRight2.text.setTextColor(getResources().getColorStateList(R.color.selector_titletext_themelight));
                titleDivider.setBackgroundResource(R.color.grayDividerDark);
                this.setBackgroundResource(R.color.pureWhite);
                break;

            case THEME_DARK:
                if(titleName != null){
                    titleName.setTextColor(Color.WHITE);
                }
                buttonHolderLeft.text.setTextColor(getResources().getColorStateList(R.color.selector_titletext_themetransport));
                buttonHolderRight1.text.setTextColor(getResources().getColorStateList(R.color.selector_titletext_themetransport));
                buttonHolderRight2.text.setTextColor(getResources().getColorStateList(R.color.selector_titletext_themetransport));

                titleDivider.setBackgroundColor(Color.WHITE);
                this.setBackgroundResource(android.R.color.transparent);
                break;
            case THEME_TRANSLATE_NODIVIDER:
                if(titleName != null){
                    titleName.setTextColor(Color.WHITE);
                }
                buttonHolderLeft.text.setTextColor(getResources().getColorStateList(R.color.selector_titletext_themetransport));
                buttonHolderRight1.text.setTextColor(getResources().getColorStateList(R.color.selector_titletext_themetransport));
                buttonHolderRight2.text.setTextColor(getResources().getColorStateList(R.color.selector_titletext_themetransport));

                titleDivider.setBackgroundResource(android.R.color.transparent);
                this.setBackgroundResource(android.R.color.transparent);
                break;
        }

        refreshDividerStatus();
    }

    public void setShowDivider(boolean showDivider) {
        isShowDivider = showDivider;
        refreshDividerStatus();
    }

    private void refreshDividerStatus(){
        if(isShowDivider){
            titleDivider.setVisibility(View.VISIBLE);
        }else{
            titleDivider.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新按钮显示
     * @param viewHolders
     */
    public void refreshButtonShow(ButtonViewHolder[] viewHolders){
        for(ButtonViewHolder holder : viewHolders){
            if(null == holder.rootView){
                break;
            }
            if(holder.buttonInfo.isShow){
                holder.rootView.setVisibility(View.VISIBLE);
                if(holder.buttonInfo.iconRes == 0){
                    holder.image.setVisibility(View.GONE);
                }else{
                    holder.image.setVisibility(View.VISIBLE);
                    holder.image.setImageResource(holder.buttonInfo.iconRes);
                }

                if(holder.buttonInfo.name == null){
                    holder.text.setVisibility(View.GONE);
                }else{
                    holder.text.setVisibility(View.VISIBLE);
                    holder.text.setText(holder.buttonInfo.name);
                }
            }else{
                holder.rootView.setVisibility(View.GONE);
            }
        }
    }

    public void setOnTitleButtonClickListener(OnTitleButtonClickListener onTitleButtonClickListener) {
        this.onTitleButtonClickListener = onTitleButtonClickListener;
    }

    public void setButtonInfo(ButtonInfo buttonInfo){
        if(buttonInfo == null){
            return;
        }
        switch (buttonInfo.id){
            case BUTTON_LEFT:
                buttonHolderLeft.buttonInfo =  buttonInfo;
                break;

            case BUTTON_RIGHT1:
                buttonHolderRight1.buttonInfo =  buttonInfo;
                break;

            case BUTTON_RIGHT2:
                buttonHolderRight2.buttonInfo =  buttonInfo;
                break;
            default:
                return;
        }
        refreshButtonShow(new ButtonViewHolder[]{buttonHolderLeft, buttonHolderRight1, buttonHolderRight2});
    }

    public static final int  BUTTON_LEFT   = 1;
    public static final int  BUTTON_RIGHT1 = 2;
    public static final int  BUTTON_RIGHT2 = 3;

    public static class ButtonInfo{

        public ButtonInfo(boolean isShow, int id){
            this.isShow = isShow;
            this.id = id;
        }

        public ButtonInfo(boolean isShow, int id, int icRes, String name){
            this.isShow = isShow;
            this.id = id;
            this.iconRes = icRes;
            this.name = name;
        }

        public boolean isShow;
        public int id = BUTTON_LEFT;
        public int iconRes = 0;//如果为0表示图标不显示
        public String name = null;//如果为null表示文字不显示
        public boolean isOpen = false;//支持状态：展开或者关闭，用于按钮可能有状的情况
    }

    public class ButtonViewHolder{

        public ButtonViewHolder(boolean isShow, int id){
            buttonInfo = new ButtonInfo(isShow, id);
        }

        public View rootView;
        public TextView text;
        public ImageView image;

        private ButtonInfo buttonInfo;

        public void initOnClick(){
            rootView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onTitleButtonClickListener){
                        onTitleButtonClickListener.onClick(buttonInfo.id, ButtonViewHolder.this);
                    }
                }
            });
        }
    }

    public interface OnTitleButtonClickListener{
        void onClick(int id, ButtonViewHolder viewHolder);
    }


    public interface OnFinishCustomLayout{
        void onFinishCustomLayout(View customViewLayout, View customView);
    }


}

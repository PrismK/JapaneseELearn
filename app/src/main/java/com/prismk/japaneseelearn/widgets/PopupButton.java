package com.prismk.japaneseelearn.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.prismk.japaneseelearn.R;


/*
* private void popMenu(final PostBean post) {
        popupButton = new PopupButton(mActivity, R.style.MyDialogAnimation);
        popupButton.setButtonInfo(new PopupButton.ButtonInfo(PopupButton.ButtonId.Button_First, "保存图片"));
        popupButton.setOnPopupClickListener(new PopupButton.OnPopupClickListener() {
            @Override
            public void onClick(PopupButton.ButtonId id) {
                switch (id) {
                    case Button_First:

                        break;
                    case Button_CANCEL:
                        popupButton.setDismiss();
                        break;
                }
            }
        });
    }
* */

public class PopupButton extends Dialog {
    private Context context;
    private PopupButton popupButton;
    private OnPopupClickListener onPopupClickListener;
    private ButtonHolder ButtonHolderFirst = new ButtonHolder(ButtonId.Button_First, "");
    private ButtonHolder ButtonHolderSecond = new ButtonHolder(ButtonId.Button_Second, "");
    private ButtonHolder ButtonHolderThird = new ButtonHolder(ButtonId.Button_Third, "");
    private ButtonHolder ButtonHolderCancel = new ButtonHolder(ButtonId.Button_CANCEL, "");

    public PopupButton(Context context) {
        super(context);
    }

    public PopupButton(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        openPopupWindow();
    }

    public PopupButton(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setButtonInfo(ButtonInfo buttonInfo) {//对按钮的选择
        if (buttonInfo == null) {
            return;
        }
        switch (buttonInfo.id) {
            case Button_First:
                ButtonHolderFirst.btnpopupwindow.setVisibility(View.VISIBLE);
                ButtonHolderFirst.btnpopupwindow.setText(buttonInfo.name);
                break;

            case Button_Second:
                ButtonHolderSecond.btnpopupwindow.setVisibility(View.VISIBLE);
                ButtonHolderSecond.btnpopupwindow.setText(buttonInfo.name);
                break;

            case Button_Third:
                ButtonHolderThird.btnpopupwindow.setVisibility(View.VISIBLE);
                ButtonHolderThird.btnpopupwindow.setText(buttonInfo.name);
                break;
            case Button_CANCEL:
                ButtonHolderCancel.btnpopupwindow.setText(buttonInfo.name);
            default:
                return;
        }

    }

    //按钮的选择初始化
//    public static final int Button_First = 1;
//    public static final int Button_Second = 2;
//    public static final int Button_Third = 3;
//    public static final int Button_CANCEL = 4;

    public enum ButtonId {
        Button_First,
        Button_Second,
        Button_Third,
        Button_CANCEL
    }

    public static class ButtonInfo {
        public ButtonId id;
        public String name;

        public ButtonInfo(ButtonId id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public class ButtonHolder {

        public Button btnpopupwindow;
        private ButtonInfo buttonInfo;

        public ButtonHolder(ButtonId id, String name) {
            buttonInfo = new ButtonInfo(id, name);
        }

        public void initOnClick() {
            btnpopupwindow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onPopupClickListener) {
                        onPopupClickListener.onClick(buttonInfo.id);
                    }
                }
            });
        }
    }


    public void setOnPopupClickListener(OnPopupClickListener onPopupClickListener) {
        this.onPopupClickListener = onPopupClickListener;
    }

    public interface OnPopupClickListener {
        void onClick(ButtonId id);

    }

    public boolean isOpenPopup(){
           return popupButton.isShowing();
    }


    public void setDismiss() {
        popupButton.dismiss();
    }


    private void openPopupWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_widget_popupbutton, null);
        popupButton = new PopupButton(context);
        popupButton.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ButtonHolderFirst.btnpopupwindow = (Button) view.findViewById(R.id.popupbtn_first);
        ButtonHolderFirst.initOnClick();

        ButtonHolderSecond.btnpopupwindow = (Button) view.findViewById(R.id.popupbtn_second);
        ButtonHolderSecond.initOnClick();

        ButtonHolderThird.btnpopupwindow = (Button) view.findViewById(R.id.popupbtn_third);
        ButtonHolderThird.initOnClick();

        ButtonHolderCancel.btnpopupwindow = (Button) view.findViewById(R.id.popupbtn_cancel);
        ButtonHolderCancel.initOnClick();

        popupButton.setContentView(view);
        view.findViewById(R.id.ll_popupbutton_none).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDismiss();
            }
        });
        popupButton.setCancelable(true);
        int screenHeight = getScreenHeight();
//       int screeWidth=getScreenWidth();
        int statusBarHeight = getStatusBarHeight();
        int dialogHeight = screenHeight - statusBarHeight;
        popupButton.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setWindowAnimations(R.style.MyDialogAnimation);
        getWindow().setWindowAnimations(R.style.MyDialogAnimation);
        popupButton.getWindow().setBackgroundDrawableResource(R.color.transparent);

        popupButton.getWindow().setGravity(Gravity.BOTTOM);
        popupButton.getWindow().setWindowAnimations(R.style.MyDialogAnimation);

        popupButton.show();
    }

    private int getScreenHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
//    private static int getScreenWidth(){
//        WindowManager wm = (WindowManager) context
//                .getSystemService(Context.WINDOW_SERVICE);
//        int width = wm.getDefaultDisplay().getWidth();
//        return width;
//    }
}

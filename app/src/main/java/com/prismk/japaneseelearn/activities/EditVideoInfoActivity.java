package com.prismk.japaneseelearn.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.widgets.Title;

import java.util.Timer;
import java.util.TimerTask;

public class EditVideoInfoActivity extends BaseActivity {

    private EditText edit;
    private Intent intent;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setStatusBarDark();
        initTitle();
        initView();
    }

    private void initView() {
        edit = findViewById(R.id.et_modify);
        edit.requestFocus();
        edit.setFocusable(true);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager = (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(edit, 0);
                           }
                       },
                100);
    }

    private void initTitle() {
        Title title = findViewById(R.id.title);
        title.setTitleNameStr("编辑信息");
        title.setTheme(Title.TitleTheme.THEME_LIGHT);
        title.setShowDivider(true);
        Title.ButtonInfo backbutton = new Title.ButtonInfo(true, Title.BUTTON_LEFT, R.mipmap.navigationbar_back_gray, null);
        Title.ButtonInfo savebutton = new Title.ButtonInfo(true, Title.BUTTON_RIGHT1, 0, "保存");
        title.setButtonInfo(backbutton);
        title.setButtonInfo(savebutton);
        title.setOnTitleButtonClickListener(new Title.OnTitleButtonClickListener() {
            @Override
            public void onClick(int id, Title.ButtonViewHolder viewHolder) {
                switch (id) {
                    case Title.BUTTON_LEFT:
                        finish();
                        goPreAnim();
                        break;
                    case Title.BUTTON_RIGHT1:
                        Intent intent = new Intent();
                        String edit = EditVideoInfoActivity.this.edit.getText().toString().trim();
                        intent.putExtra("edit", edit);
                        setResult(RESULT_OK, intent);
                        finish();
                        goPreAnim();
                        break;
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_personal_nickname;
    }
}


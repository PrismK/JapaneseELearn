package com.prismk.japaneseelearn.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.utils.CleanMessageUtil;
import com.prismk.japaneseelearn.widgets.Title;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_changepw;
    private RelativeLayout rl_aboutour;
    private RelativeLayout rl_cache;
    private TextView tv_cachesize;
    private Button bt_unlogin;

    private SharedPreferences login_sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setStatusBarDark();
        initTitle();
        initView();
        setOnClick();
        initSP();
    }

    private void initSP() {
        login_sp = getSharedPreferences("userInfo", 0);
    }

    private void initView() {
        rl_changepw = (RelativeLayout) findViewById(R.id.rl_changepw);
        rl_aboutour = (RelativeLayout) findViewById(R.id.rl_aboutour);
        rl_cache = (RelativeLayout) findViewById(R.id.rl_cache);

        tv_cachesize = (TextView) findViewById(R.id.tv_cachesize);
        bt_unlogin = (Button) findViewById(R.id.bt_unlogin);
        try {
            String size = CleanMessageUtil.getTotalCacheSize(this);
            tv_cachesize.setText(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTitle() {
        Title mTitle = findViewById(R.id.title);
        mTitle.setShowDivider(false);
        mTitle.setTitleNameStr("");
        mTitle.setTheme(Title.TitleTheme.THEME_LIGHT);
        Title.ButtonInfo buttonInfoLeft = new Title.ButtonInfo(true, Title.BUTTON_LEFT);
        buttonInfoLeft.iconRes = R.drawable.selector_btn_titleback;
        mTitle.setButtonInfo(buttonInfoLeft);
        mTitle.setOnTitleButtonClickListener(onTitleButtonClickListener);
    }

    private Title.OnTitleButtonClickListener onTitleButtonClickListener = new Title.OnTitleButtonClickListener() {
        @Override
        public void onClick(int id, Title.ButtonViewHolder viewHolder) {
            if (id == Title.BUTTON_LEFT) {
                goBack();
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    private void setOnClick() {
        rl_changepw.setOnClickListener(this);
        rl_aboutour.setOnClickListener(this);
        rl_cache.setOnClickListener(this);
        bt_unlogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_changepw:
                Intent intent_Login_to_reset = new Intent(SettingActivity.this, ResetPWDActivity.class);    //切换Login Activity至User Activity
                startActivity(intent_Login_to_reset);
                goNextAnim();
                break;
            case R.id.rl_aboutour:
                Uri uri = Uri.parse("https://www.thethreestooges.cn");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.rl_cache:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("警告！");
                builder.setMessage("确定清除缓存吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CleanMessageUtil.clearAllCache(SettingActivity.this);
                        tv_cachesize.setText("0KB");
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
                break;
            case R.id.bt_unlogin:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("警告！");
                builder2.setMessage("确定注销登录吗？");
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = login_sp.edit();
                        editor.clear();
                        editor.commit();
                        Intent unlogin = new Intent(SettingActivity.this, LoginActivity.class);
                        startActivity(unlogin);
                        finish();
                    }
                });
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder2.show();
        }
    }
}

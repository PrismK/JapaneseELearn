package com.prismk.japaneseelearn.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.bean.UserData;
import com.prismk.japaneseelearn.managers.UserDBManager;
import com.prismk.japaneseelearn.utils.CleanMessageUtil;
import com.prismk.japaneseelearn.widgets.Title;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_changepw;
    private RelativeLayout rl_aboutour;
    private RelativeLayout rl_cache;
    private TextView tv_cachesize;
    private Button bt_unlogin;

    private SharedPreferences login_sp;
    private LinearLayout ll_userinfo;
    private RelativeLayout rl_share;
    private CircleImageView imv_userhead;
    private TextView nickname;
    private TextView tv_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setStatusBarDark();
        initTitle();
        initView();
        initData();
        setOnClick();
        initSP();
    }

    private void initData() {
        UserDBManager userDBManager = new UserDBManager(SettingActivity.this);
        List<UserData> userDataListFromUserDB = userDBManager.getUserDataListFromUserDB();
        Glide.with(SettingActivity.this).load(userDataListFromUserDB.get(userDBManager.getLoginUesrID() - 1).getHeadImgUrlString()).into(imv_userhead);
        nickname.setText(userDataListFromUserDB.get(userDBManager.getLoginUesrID() - 1).getNickName());
        tv_username.setText(userDataListFromUserDB.get(userDBManager.getLoginUesrID() - 1).getSign());
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

        ll_userinfo = (LinearLayout) findViewById(R.id.ll_userinfo);
        rl_share = (RelativeLayout) findViewById(R.id.rl_share);

        imv_userhead = (CircleImageView) findViewById(R.id.imv_userhead);
        nickname = (TextView) findViewById(R.id.nickname);
        tv_username = (TextView) findViewById(R.id.tv_username);
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
        ll_userinfo.setOnClickListener(this);
        rl_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_changepw:
                Intent intent_Login_to_reset = new Intent(SettingActivity.this, ResetPWDActivity.class);
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
                break;
            case R.id.ll_userinfo:
                Intent intent3 = new Intent(SettingActivity.this, PersonalInfoActivity.class);
                startActivity(intent3);
                goNextAnim();
                break;
            case R.id.rl_share:
                sms();
                break;
        }
    }

    public void sms(){
        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
        builder3.setTitle("注意！");
        builder3.setMessage("该应用想要发送一条短信，允许吗？");
        builder3.setPositiveButton("允许", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + ""));
                intent .putExtra("sms_body",
                        "我正在使用日语易学通App学习日语,非常好用哦，快来和我一起学习吧!");
                startActivity(intent);
            }
        });
        builder3.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder3.show();
    }
}

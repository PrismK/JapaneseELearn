package com.prismk.japaneseelearn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.bean.UserData;
import com.prismk.japaneseelearn.managers.UserDBManager;
import com.prismk.japaneseelearn.properties.ELearnAppProperties;
import com.prismk.japaneseelearn.widgets.Title;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInfoActivity extends BaseActivity {


    private CircleImageView userAvator;
    private TextView userName;
    private TextView userSign;
    private TextView userTel;

    public static final int USERNICKNAME = 0;
    public static final int USERSIGN = 1;
    public static final int USERTEL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setStatusBarDark();
        initTitle();
        initView();
        initData();
    }

    private void initData() {
        UserDBManager userDBManager = new UserDBManager(PersonalInfoActivity.this);
        List<UserData> userDataListFromUserDB = userDBManager.getUserDataListFromUserDB();
        Glide.with(PersonalInfoActivity.this).load(userDataListFromUserDB.get(userDBManager.getLoginUesrID()-1).getHeadImgUrlString()).into(userAvator);
        userName.setText(userDataListFromUserDB.get(userDBManager.getLoginUesrID()-1).getNickName());
        userSign.setText(userDataListFromUserDB.get(userDBManager.getLoginUesrID()-1).getSign());
        userTel.setText(userDataListFromUserDB.get(userDBManager.getLoginUesrID()-1).getUserName());
    }

    private void initView() {
        RelativeLayout rl_userAvator = findViewById(R.id.rl_useravator);
        RelativeLayout rl_userName = findViewById(R.id.rl_usernickname);
        RelativeLayout rl_userSign = findViewById(R.id.rl_usersign);
        RelativeLayout rl_userTel = findViewById(R.id.rl_usertel);
        userAvator = findViewById(R.id.civ_useravator);
        userName = findViewById(R.id.tv_usernickname);
        userSign = findViewById(R.id.tv_usersign);
        userTel = findViewById(R.id.tv_usertel);
        rl_userAvator.setOnClickListener(new onPersonalInfoClickListener());
        rl_userName.setOnClickListener(new onPersonalInfoClickListener());
        rl_userSign.setOnClickListener(new onPersonalInfoClickListener());
        rl_userTel.setOnClickListener(new onPersonalInfoClickListener());
    }

    private void initTitle() {
        Title title = findViewById(R.id.title);
        title.setTitleNameStr("个人信息");
        title.setTheme(Title.TitleTheme.THEME_LIGHT);
        title.setShowDivider(true);
        Title.ButtonInfo backbutton = new Title.ButtonInfo(true,Title.BUTTON_LEFT,R.mipmap.camera_back,null);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == Title.BUTTON_LEFT){
                    finish();
                    goPreAnim();
                }
            }
        });
        title.setButtonInfo(backbutton);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_info;
    }

     private class onPersonalInfoClickListener implements View.OnClickListener {

         @Override
         public void onClick(View v) {
             Intent intent = new Intent(PersonalInfoActivity.this,ModifyPersonalInfoActivity.class);
             switch (v.getId()){
                 case R.id.rl_useravator:

                     break;
                 case R.id.rl_usernickname:
                     intent.putExtra(ELearnAppProperties.USER_INFO,USERNICKNAME);
                     startActivity(intent);
                     goNextAnim();
                     break;
                 case R.id.rl_usersign:
                     intent.putExtra(ELearnAppProperties.USER_INFO,USERSIGN);
                     startActivity(intent);
                     goNextAnim();
                     break;
                 case R.id.rl_usertel:
                     intent.putExtra(ELearnAppProperties.USER_INFO,USERTEL);
                     startActivity(intent);
                     goNextAnim();
                     break;
             }
         }
     }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }
}

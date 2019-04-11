package com.prismk.japaneseelearn.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.bean.UserData;
import com.prismk.japaneseelearn.managers.UserDBManager;
import com.prismk.japaneseelearn.properties.ELearnAppProperties;
import com.prismk.japaneseelearn.widgets.Title;

import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;

public class ModifyPersonalInfoActivity extends BaseActivity {

    private static final String MODIFYNICKNAME = "更改名字";
    private static final String MODIFYSIGN = "更改签名";
    private static final String MODIFYTEL = "更改电话";
    private EditText modifyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setStatusBarDark();
        initTitle();
        initView();
    }

    private void initView() {
        modifyInfo = findViewById(R.id.et_modify);
    }


    private void initTitle() {
        Title title = findViewById(R.id.title);
        switch (getIntent().getIntExtra(ELearnAppProperties.USER_INFO, -1)) {
            case PersonalInfoActivity.USERNICKNAME:
                title.setTitleNameStr(MODIFYNICKNAME);
                break;
            case PersonalInfoActivity.USERSIGN:
                title.setTitleNameStr(MODIFYSIGN);
                break;
            case PersonalInfoActivity.USERTEL:
                title.setTitleNameStr(MODIFYTEL);
                break;
        }
        title.setTheme(Title.TitleTheme.THEME_LIGHT);
        title.setShowDivider(true);
        Title.ButtonInfo backbutton = new Title.ButtonInfo(true, Title.BUTTON_LEFT, R.mipmap.camera_back, null);
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
                       modify(title.getTitleNameStr());
                       break;
               }
           }
       });
    }
        private void modify (String titleStr){
            UserDBManager userDBManager = new UserDBManager(ModifyPersonalInfoActivity.this);
            int loginuserid = userDBManager.getLoginUesrID();
            String updateInfo = modifyInfo.getText().toString().trim();
            switch (titleStr) {
                case MODIFYNICKNAME:
                    userDBManager.updateUserDataById(UserDBManager.USER_NICKNAME,loginuserid,updateInfo);
                    finish();
                    goPreAnim();
                    break;
                case MODIFYSIGN:
                    userDBManager.updateUserDataById(UserDBManager.USER_SIGN,loginuserid,updateInfo);
                    finish();
                    goPreAnim();
                    break;
                case MODIFYTEL:
                    userDBManager.updateUserDataById(UserDBManager.USER_NAME,loginuserid,updateInfo);
                    SharedPreferences sp = getSharedPreferences("userInfo", 0);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("USER_NAME",updateInfo);
                    edit.commit();
                    finish();
                    goPreAnim();
                    break;
            }
        }

        @Override
        protected int getLayoutId () {
            return R.layout.activity_modify_personal_nickname;
        }
    }


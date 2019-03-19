package com.prismk.japaneseelearn.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.bean.UserData;
import com.prismk.japaneseelearn.managers.UserDBManager;
import com.prismk.japaneseelearn.widgets.Title;

/**
 * 修改密码界面
 */
public class ResetPWDActivity extends BaseActivity {

    private EditText edt_reset_username;
    private EditText edt_reset_oldpw;
    private EditText edt_reset_newpw;
    private EditText edt_reset_renewpw;
    private Button btn_reset_ok;
    private UserDBManager mUserDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setStatusBarDark();
        initTitle();
        initView();
        initClickListener();
        initUserDataManager();
    }

    private void initTitle() {
        Title mTitle = findViewById(R.id.title);
        mTitle.setShowDivider(false);
        mTitle.setTitleNameStr("修改密码");
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
        return R.layout.activity_resetpwd;
    }

    private void initUserDataManager() {
        if (mUserDBManager == null) {
            mUserDBManager = new UserDBManager(this);
            mUserDBManager.openDataBase();
        }
    }

    private void initClickListener() {
        btn_reset_ok.setOnClickListener(m_resetpwd_Listener);
    }

    private void initView() {
        edt_reset_username = (EditText) findViewById(R.id.edt_reset_username);
        edt_reset_oldpw = (EditText) findViewById(R.id.edt_reset_oldpw);
        edt_reset_newpw = (EditText) findViewById(R.id.edt_reset_newpw);
        edt_reset_renewpw = (EditText) findViewById(R.id.edt_reset_renewpw);
        btn_reset_ok = (Button) findViewById(R.id.btn_reset_ok);
    }

    View.OnClickListener m_resetpwd_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_reset_ok:
                    resetpwd_check();
                    break;
            }
        }
    };
    public void resetpwd_check() {
        if (isUserNameAndPwdValid()) {
            String userName = edt_reset_username.getText().toString().trim();
            String userPwd_old = edt_reset_oldpw.getText().toString().trim();
            String userPwd_new = edt_reset_newpw.getText().toString().trim();
            String userPwdCheck = edt_reset_renewpw.getText().toString().trim();
            int result= mUserDBManager.findUserByNameAndPwd(userName, userPwd_old);
            if(result==1){//返回1说明用户名和密码均正确,继续后续操作
                if(userPwd_new.equals(userPwdCheck)==false){//两次密码输入不一样
                    Toast.makeText(this, getString(R.string.pwd_not_the_same),Toast.LENGTH_SHORT).show();
                    return ;
                } else {
                    UserData mUser = new UserData(userName, userPwd_new);
                    mUserDBManager.openDataBase();
                    boolean flag = mUserDBManager.updateUserPW(mUser);
                    if (flag == false) {
                        Toast.makeText(this, getString(R.string.resetpwd_fail),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, getString(R.string.resetpwd_success),Toast.LENGTH_SHORT).show();
                        mUser.pwdresetFlag=1;
                        finish();
                    }
                }
            }else if(result==0){//返回0说明用户名和密码不匹配，重新输入
                Toast.makeText(this, getString(R.string.pwd_not_fit_user),Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }
    public boolean isUserNameAndPwdValid() {
        String userName = edt_reset_username.getText().toString().trim();
        //检查用户是否存在
        int count= mUserDBManager.findUserByName(userName);
        //用户不存在时返回，给出提示文字
        if(count<=0){
            Toast.makeText(this, getString(R.string.name_not_exist, userName),Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edt_reset_username.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),Toast.LENGTH_SHORT).show();
            return false;
        } else if (edt_reset_oldpw.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty),Toast.LENGTH_SHORT).show();
            return false;
        } else if (edt_reset_newpw.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_new_empty),Toast.LENGTH_SHORT).show();
            return false;
        }else if(edt_reset_renewpw.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_check_empty),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}


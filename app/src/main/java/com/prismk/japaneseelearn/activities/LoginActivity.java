package com.prismk.japaneseelearn.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.managers.UserDataManager;

public class LoginActivity extends BaseActivity {

    private EditText edt_username;
    private EditText edt_pw;
    private TextView tv_register;
    private Button btn_login;
    private Button btn_cancel;
    private SharedPreferences login_sp;
    private TextView tv_changepw;
    private UserDataManager mUserDataManager;
    private LinearLayout ll_login_rootview;
    private RelativeLayout rl_logobg;
    private ImageView imv_logo;
    private ImageButton imbtn_delete_username;
    private ImageButton imbtn_delete_pw;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initSP();
        initClickListener();
        initUserDataManager();
    }

    private void initUserDataManager() {
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
    }

    private void initClickListener() {
        tv_register.setOnClickListener(mListener);
        btn_login.setOnClickListener(mListener);
        btn_cancel.setOnClickListener(mListener);
        tv_changepw.setOnClickListener(mListener);
        imbtn_delete_username.setOnClickListener(mListener);
        imbtn_delete_pw.setOnClickListener(mListener);
    }

    private void initSP() {
        login_sp = getSharedPreferences("userInfo", 0);
        String name = login_sp.getString("USER_NAME", "");
        String pwd = login_sp.getString("PASSWORD", "");
        edt_username.setText(name);
        edt_pw.setText(pwd);
    }

    private void initView() {
        edt_username = findViewById(R.id.edt_username);
        edt_pw = findViewById(R.id.edt_reginster_pw);
        tv_register = findViewById(R.id.tv_register);
        btn_login = findViewById(R.id.btn_login);
        btn_cancel = findViewById(R.id.btn_cancel);
        tv_changepw = findViewById(R.id.tv_changepw);
        ll_login_rootview = findViewById(R.id.ll_login_rootview);
        rl_logobg = findViewById(R.id.rl_logobg);
        imv_logo = findViewById(R.id.imv_logo);
        imbtn_delete_username = findViewById(R.id.imbtn_delete_username);
        imbtn_delete_pw = findViewById(R.id.imbtn_delete_pw);
    }

    OnClickListener mListener = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_register:
                    Intent intent_Login_to_Register = new Intent(LoginActivity.this, RegisterActivity.class);    //切换Login Activity至User Activity
                    startActivity(intent_Login_to_Register);
                    goNextAnim();
                    break;
                case R.id.btn_login:
                    login();
                    break;
                case R.id.btn_cancel:
                    cancel();
                    break;
                case R.id.tv_changepw:
                    Intent intent_Login_to_reset = new Intent(LoginActivity.this, ResetPWDActivity.class);    //切换Login Activity至User Activity
                    startActivity(intent_Login_to_reset);
                    goNextAnim();
                    break;
                case R.id.imbtn_delete_username:
                    edt_username.getText().clear();
                    break;
                case R.id.imbtn_delete_pw:
                    edt_pw.getText().clear();
                    break;
            }
        }
    };

    public void login() {
        if (isUserNameAndPwdValid()) {
            String userName = edt_username.getText().toString().trim();
            String userPwd = edt_pw.getText().toString().trim();
            SharedPreferences.Editor editor = login_sp.edit();
            int result = mUserDataManager.findUserByNameAndPwd(userName, userPwd);
            if (result == 1) {//返回1说明用户名和密码均正确
                editor.putString("USER_NAME", userName);
                editor.putString("PASSWORD", userPwd);
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);    //切换Login Activity至User Activity
                startActivity(intent);
                finish();
                goNextAnim();
                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
            } else if (result == 0) {
                Toast.makeText(this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void cancel() {
        if (isUserNameAndPwdValid()) {
            String userName = edt_username.getText().toString().trim();
            String userPwd = edt_pw.getText().toString().trim();
            int result = mUserDataManager.findUserByNameAndPwd(userName, userPwd);
            if (result == 1) {//返回1说明用户名和密码均正确
                Toast.makeText(this, getString(R.string.cancel_success), Toast.LENGTH_SHORT).show();//登录成功提示
                edt_pw.setText("");
                edt_username.setText("");
                mUserDataManager.deleteUserDatabyname(userName);
            } else if (result == 0) {
                Toast.makeText(this, getString(R.string.cancel_fail), Toast.LENGTH_SHORT).show();  //登录失败提示
            }
        }

    }

    public boolean isUserNameAndPwdValid() {
        if (edt_username.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty), Toast.LENGTH_SHORT).show();
            return false;
        } else if (edt_pw.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addOnGlobalLayoutListener() {
        ll_login_rootview.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    private void removeOnGlobalLayoutListener() {
        ll_login_rootview.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    private TextWatcher mTextWacher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!edt_username.getText().toString().isEmpty()) {
                imbtn_delete_username.setVisibility(View.VISIBLE);
            } else {
                imbtn_delete_username.setVisibility(View.INVISIBLE);
            }
            if (!edt_pw.getText().toString().isEmpty()) {
                imbtn_delete_pw.setVisibility(View.VISIBLE);
            } else {
                imbtn_delete_pw.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            final int screenHeight = metrics.heightPixels;
            Rect r = new Rect();
            ll_login_rootview.getWindowVisibleDisplayFrame(r);
            int deltaHeight = screenHeight - r.bottom;
            if (deltaHeight > 150) {
                rl_logobg.setVisibility(View.GONE);
                imv_logo.setVisibility(View.GONE);
            } else {
                rl_logobg.setVisibility(View.VISIBLE);
                imv_logo.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onResume() {
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
        addOnGlobalLayoutListener();
        edt_username.addTextChangedListener(mTextWacher);
        edt_pw.addTextChangedListener(mTextWacher);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onPause() {
        if (mUserDataManager != null) {
            mUserDataManager.closeDataBase();
            mUserDataManager = null;
        }
        removeOnGlobalLayoutListener();
        edt_username.removeTextChangedListener(mTextWacher);
        edt_pw.removeTextChangedListener(mTextWacher);
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

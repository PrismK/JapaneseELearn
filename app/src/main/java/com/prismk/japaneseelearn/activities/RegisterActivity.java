package com.prismk.japaneseelearn.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.bean.UserData;
import com.prismk.japaneseelearn.managers.UserDataManager;
import com.prismk.japaneseelearn.widgets.Title;

public class RegisterActivity extends BaseActivity {
    private EditText edt_register_username;
    private EditText edt_register_pw;
    private EditText edt_register_repw;
    private UserDataManager mUserDataManager;
    private Button btn_register_ok;
    private ImageButton imbtn_register_delete_username;
    private ImageButton imbtn_register_delete_pw;
    private ImageButton imbtn_register_delete_repw;
    private CheckBox cb_agree;
    private TextView tv_agree;
    private SharedPreferences login_sp;
    private SharedPreferences.Editor editor;
    private TextView tv_alreadyhave;
    private LinearLayout ll_student;
    private LinearLayout ll_teacher;
    private RadioButton rb_student;
    private RadioButton rb_teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setStatusBarDark();
        initTitle();
        initView();
        initClickListener();
        initSP();
        initUserDataManager();
    }

    private void initSP() {
        login_sp = getSharedPreferences("userInfo", 0);
        editor = login_sp.edit();
    }

    private void initUserDataManager() {
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
    }

    private void initClickListener() {
        btn_register_ok.setOnClickListener(m_register_Listener);
        tv_agree.setOnClickListener(m_register_Listener);
        tv_alreadyhave.setOnClickListener(m_register_Listener);
        ll_student.setOnClickListener(m_register_Listener);
        ll_teacher.setOnClickListener(m_register_Listener);
    }

    private void initView() {
        edt_register_username = findViewById(R.id.edt_register_username);
        edt_register_pw = findViewById(R.id.edt_register_pw);
        edt_register_repw = findViewById(R.id.edt_register_repw);
        btn_register_ok = findViewById(R.id.btn_register_ok);
        imbtn_register_delete_username = findViewById(R.id.imbtn_register_delete_username);
        imbtn_register_delete_pw = findViewById(R.id.imbtn_register_delete_pw);
        imbtn_register_delete_repw = findViewById(R.id.imbtn_register_delete_repw);
        cb_agree = findViewById(R.id.cb_agree);
        tv_agree = findViewById(R.id.tv_agree);
        tv_alreadyhave = findViewById(R.id.tv_alreadyhave);
        ll_student = findViewById(R.id.ll_student);
        ll_teacher = findViewById(R.id.ll_teacher);
        rb_teacher = findViewById(R.id.rb_teacher);
        rb_student = findViewById(R.id.rb_student);
    }

    private void initTitle() {
        Title mTitle = findViewById(R.id.title);
        mTitle.setShowDivider(false);
        mTitle.setTitleNameStr("账号注册");
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
            } else if (id == Title.BUTTON_RIGHT1) {

            }
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!edt_register_username.getText().toString().isEmpty()) {
                imbtn_register_delete_username.setVisibility(View.VISIBLE);
            } else {
                imbtn_register_delete_username.setVisibility(View.INVISIBLE);
            }
            if (!edt_register_pw.getText().toString().isEmpty()) {
                imbtn_register_delete_pw.setVisibility(View.VISIBLE);
            } else {
                imbtn_register_delete_pw.setVisibility(View.INVISIBLE);
            }
            if (!edt_register_repw.getText().toString().isEmpty()) {
                imbtn_register_delete_repw.setVisibility(View.VISIBLE);
            } else {
                imbtn_register_delete_repw.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    View.OnClickListener m_register_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_register_ok:
                    register_check();
                    break;
                case R.id.tv_agree:
                    if (cb_agree.isChecked()) {
                        cb_agree.setChecked(false);
                    } else {
                        cb_agree.setChecked(true);
                    }
                    break;
                case R.id.tv_alreadyhave:
                    goBack();
                    break;
                case R.id.ll_student:
                    rb_student.setChecked(true);
                    rb_teacher.setChecked(false);
                    break;
                case R.id.ll_teacher:
                    rb_student.setChecked(false);
                    rb_teacher.setChecked(true);
                    break;
            }
        }
    };

    public void register_check() {                                //确认按钮的监听事件
        if (isUserNameAndPwdValid()) {
            String userName = edt_register_username.getText().toString().trim();
            String userPwd = edt_register_pw.getText().toString().trim();
            String userPwdCheck = edt_register_repw.getText().toString().trim();
            if (!cb_agree.isChecked()) {
                Toast.makeText(this, "请阅读条款", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!rb_student.isChecked() && !rb_teacher.isChecked()) {
                Toast.makeText(this, "请选择您的角色", Toast.LENGTH_SHORT).show();
            }
            //检查用户是否存在
            int count = mUserDataManager.findUserByName(userName);
            //用户已经存在时返回，给出提示文字
            if (count > 0) {
                Toast.makeText(this, getString(R.string.name_already_exist, userName), Toast.LENGTH_SHORT).show();
                return;
            }
            if (userPwd.equals(userPwdCheck) == false) {     //两次密码输入不一样
                Toast.makeText(this, getString(R.string.pwd_not_the_same), Toast.LENGTH_SHORT).show();
                return;
            } else {
                boolean teacherChecked = rb_teacher.isChecked();
                UserData mUser = new UserData(userName, userPwd,teacherChecked,false,null,null,null);
                mUserDataManager.openDataBase();
                long flag = mUserDataManager.insertUserData(mUser); //新建用户信息
                if (flag == -1) {
                    Toast.makeText(this, getString(R.string.register_fail), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                    editor.putString("USER_NAME", userName);
                    editor.putString("PASSWORD", userPwd);
                    editor.putBoolean("IS_TEACHER",teacherChecked);
                    editor.commit();
                    Intent intent_Register_to_Login = new Intent(RegisterActivity.this, MainActivity.class);    //切换User Activity至Login Activity
                    startActivity(intent_Register_to_Login);
                    finish();
                    goNextAnim();
                }
            }
        }
    }

    public boolean isUserNameAndPwdValid() {
        if (edt_register_username.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty), Toast.LENGTH_SHORT).show();
            return false;
        } else if (edt_register_pw.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty), Toast.LENGTH_SHORT).show();
            return false;
        } else if (edt_register_repw.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_check_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*edt_register_username.addTextChangedListener(mTextWatcher);
        edt_register_pw.addTextChangedListener(mTextWatcher);
        edt_register_repw.addTextChangedListener(mTextWatcher);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*edt_register_username.removeTextChangedListener(mTextWatcher);
        edt_register_pw.removeTextChangedListener(mTextWatcher);
        edt_register_repw.removeTextChangedListener(mTextWatcher);*/
    }
}
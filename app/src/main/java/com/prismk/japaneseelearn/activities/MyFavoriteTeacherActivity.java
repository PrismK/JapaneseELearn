package com.prismk.japaneseelearn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.adapters.TeacherListAdapter;
import com.prismk.japaneseelearn.bean.TeacherFollowedData;
import com.prismk.japaneseelearn.bean.UserData;
import com.prismk.japaneseelearn.managers.TeacherFollowedDBManager;
import com.prismk.japaneseelearn.managers.UserDBManager;
import com.prismk.japaneseelearn.properties.ELearnAppProperties;
import com.prismk.japaneseelearn.widgets.Title;

import java.util.ArrayList;
import java.util.List;

public class MyFavoriteTeacherActivity extends BaseActivity {

    private ListView lv_myfavoriteteacher;
    private TeacherListAdapter teacherListAdapter;
    private UserDBManager userDBManager;
    private TeacherFollowedDBManager teacherFollowedDBManager;

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
        userDBManager = new UserDBManager(MyFavoriteTeacherActivity.this);
        teacherFollowedDBManager = new TeacherFollowedDBManager(MyFavoriteTeacherActivity.this);
        teacherListAdapter = new TeacherListAdapter(MyFavoriteTeacherActivity.this, getFavoriteTeacherList());
        lv_myfavoriteteacher.setAdapter(teacherListAdapter);
    }

    private void initView() {
        lv_myfavoriteteacher = findViewById(R.id.lv_myfavoriteteacher);
        lv_myfavoriteteacher.setOnItemClickListener(new OnItemClickListener());
    }

    private void initTitle() {
        Title title = findViewById(R.id.title);
        title.setTitleNameStr("我关注的教师");
        title.setTheme(Title.TitleTheme.THEME_LIGHT);
        title.setShowDivider(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_favorite_teacher;
    }

    private List<UserData> getFavoriteTeacherList() {
        List<UserData> userDataListFromUserDB = userDBManager.getUserDataListFromUserDB();
        List<Integer> favoriteTeacherId = teacherFollowedDBManager.getFavoriteTeacherId(userDBManager.getLoginUesrID());
        List<UserData> favoriteTeacherList = new ArrayList<>();
        for (UserData userData : userDataListFromUserDB) {
            for (int i : favoriteTeacherId) {
                if (userData.getUserId() == i) {
                    UserData data = new UserData();
                    i -= 1;
                    data.setUserId(i);
                    data.setVIP(userDataListFromUserDB.get(i).isVIP());
                    data.setNickName(userDataListFromUserDB.get(i).getNickName());
                    data.setHeadImgUrlString(userDataListFromUserDB.get(i).getHeadImgUrlString());
                    data.setSign(userDataListFromUserDB.get(i).getSign());
                    data.setTeacherUser(userDataListFromUserDB.get(i).isTeacherUser());
                    favoriteTeacherList.add(data);
                }
            }
        }
        return favoriteTeacherList;
    }

    private class OnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MyFavoriteTeacherActivity.this, TeacherInfoActivity.class);
            intent.putExtra(ELearnAppProperties.INTENT_TEACHERID, getFavoriteTeacherList().get(position).getUserId() - 1);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        //为了在推荐视频中关注老师时返回的时候列表可以刷新
        initData();
        super.onResume();
    }
}

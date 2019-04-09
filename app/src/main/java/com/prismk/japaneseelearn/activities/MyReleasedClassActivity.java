package com.prismk.japaneseelearn.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.adapters.VideoAdapter;
import com.prismk.japaneseelearn.bean.VideoData;
import com.prismk.japaneseelearn.managers.UserDBManager;
import com.prismk.japaneseelearn.managers.VideoDBManager;
import com.prismk.japaneseelearn.widgets.Title;

import java.util.List;

public class MyReleasedClassActivity extends BaseActivity {

    private ListView myReleasedClass;
    private VideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setStatusBarDark();
        initTitle();
        initData();
        initView();
    }

    private void initData() {
        VideoDBManager videoDBManager = new VideoDBManager(MyReleasedClassActivity.this);
        UserDBManager userDBManager = new UserDBManager(MyReleasedClassActivity.this);
        List<VideoData> videoDataListFromTeacherID = videoDBManager.getVideoDataListFromTeacherID(userDBManager.getLoginUesrID());
        videoAdapter = new VideoAdapter(MyReleasedClassActivity.this,videoDataListFromTeacherID);
    }

    private void initView() {
        myReleasedClass = findViewById(R.id.lv_classes_release);
        myReleasedClass.setAdapter(videoAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_released_class;
    }
    private void initTitle(){
        Title title = findViewById(R.id.title);
        title.setTitleNameStr("发布的课程");
        title.setTheme(Title.TitleTheme.THEME_LIGHT);
        title.setShowDivider(true);
    }
}

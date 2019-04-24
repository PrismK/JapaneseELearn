package com.prismk.japaneseelearn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.adapters.VideoAdapter;
import com.prismk.japaneseelearn.bean.VideoData;
import com.prismk.japaneseelearn.managers.UserDBManager;
import com.prismk.japaneseelearn.managers.VideoDBManager;
import com.prismk.japaneseelearn.properties.ELearnAppProperties;
import com.prismk.japaneseelearn.widgets.Title;

import java.util.List;

public class MyReleasedClassActivity extends BaseActivity {

    private ListView myReleasedClass;
    private VideoAdapter videoAdapter;
    private List<VideoData> videoDataListFromTeacherID;

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
        videoDataListFromTeacherID = videoDBManager.getVideoDataListFromTeacherID(userDBManager.getLoginUesrID());
        videoAdapter = new VideoAdapter(MyReleasedClassActivity.this, videoDataListFromTeacherID);
    }

    private void initView() {
        myReleasedClass = findViewById(R.id.lv_classes_release);
        myReleasedClass.setAdapter(videoAdapter);
        setOnItemClick();
    }

    private void setOnItemClick() {
        myReleasedClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyReleasedClassActivity.this, VideoPlayerActivity.class);
                intent.putExtra(ELearnAppProperties.INTENT_VIDEO_POSITION, videoDataListFromTeacherID.get(position).getVideoId() - 1);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_released_class;
    }

    private void initTitle() {
        Title title = findViewById(R.id.title);
        title.setTitleNameStr("我发布的课程");
        title.setTheme(Title.TitleTheme.THEME_LIGHT);
        title.setShowDivider(true);
        Title.ButtonInfo backbutton = new Title.ButtonInfo(true, Title.BUTTON_LEFT, R.mipmap.navigationbar_back_gray, null);
        title.setButtonInfo(backbutton);
        title.setOnTitleButtonClickListener(new Title.OnTitleButtonClickListener() {
            @Override
            public void onClick(int id, Title.ButtonViewHolder viewHolder) {
                switch (id) {
                    case Title.BUTTON_LEFT:
                        finish();
                        goPreAnim();
                        break;
                }
            }
        });
    }

}

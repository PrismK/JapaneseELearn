package com.prismk.japaneseelearn.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.adapters.VideoAdapter;
import com.prismk.japaneseelearn.bean.UserData;
import com.prismk.japaneseelearn.bean.VideoData;
import com.prismk.japaneseelearn.managers.TeacherFollowedDBManager;
import com.prismk.japaneseelearn.managers.UserDBManager;
import com.prismk.japaneseelearn.managers.VideoDBManager;
import com.prismk.japaneseelearn.properties.ELearnAppProperties;
import com.prismk.japaneseelearn.views.NoScrollListView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherInfoActivity extends BaseActivity {

    private CircleImageView teacherAvatar;
    private TextView teacherName;
    private TextView teacherFans;
    private TextView teacherRelease;
    private TextView teacherSign;
    private NoScrollListView classesRelease;
    private UserDBManager userDBManager;
    private VideoDBManager videoDBManager;
    private TeacherFollowedDBManager teacherFollowedDBManager;
    private VideoAdapter videoAdapter;
    private List<VideoData> videoDataListFromTeacherID;
    private int position;
    private int drawposition;
    private int teacherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initData();
    }

    private void initData() {
        position = getIntent().getIntExtra(ELearnAppProperties.INTENT_TEACHERINFO_POSITION, -1);
        drawposition = getIntent().getIntExtra(ELearnAppProperties.INTENT_TEACHERLIST_POSITION,-1);
        userDBManager = new UserDBManager(TeacherInfoActivity.this);
        videoDBManager = new VideoDBManager(TeacherInfoActivity.this);
        teacherFollowedDBManager = new TeacherFollowedDBManager(TeacherInfoActivity.this);
        List<UserData> userDataList = userDBManager.getUserDataListFromUserDB();
        List<UserData> teacherDataList = userDBManager.getTeacherListFromUserDB();
        List<VideoData> videoDataList = videoDBManager.getVideoDataListFromVideoDB();
        teacherId = 0;
        if (position!=-1){
            teacherId = videoDataList.get(position).getUploadTeacherId();
        } else if (drawposition!=-1){
            teacherId = teacherDataList.get(drawposition).getUserId();
        }else {
            int favoriteTeacherID = getIntent().getIntExtra(ELearnAppProperties.INTENT_TEACHERID,-1);
            teacherId = favoriteTeacherID+2;
        }

        videoDataListFromTeacherID = videoDBManager.getVideoDataListFromTeacherID(teacherId);
        Glide.with(TeacherInfoActivity.this).load(userDataList.get(teacherId -1).getHeadImgUrlString()).into(teacherAvatar);
        teacherName.setText(userDataList.get(teacherId - 1).getNickName());
        teacherSign.setText(userDataList.get(teacherId - 1).getSign());

        classesRelease.setItemCount(videoDataListFromTeacherID.size());
        initAdapter();
//        for (VideoData data:videoDataListFromTeacherID) {
//            if ()
//        }
    }

    private void initAdapter() {
        videoAdapter = null;
        videoAdapter = new VideoAdapter(TeacherInfoActivity.this, videoDataListFromTeacherID);
        classesRelease.setAdapter(videoAdapter);
    }
    private void initView() {
        teacherAvatar = findViewById(R.id.civ_teacher_avatar);
        teacherName = findViewById(R.id.tv_teacher_name);
        teacherFans = findViewById(R.id.tv_fans);
        teacherRelease = findViewById(R.id.tv_teacher_release);
        teacherSign = findViewById(R.id.tv_teacher_sign);
        classesRelease = findViewById(R.id.lv_classes_release);
        classesRelease.setOnItemClickListener(new onNoScrollListViewItemClickListener());
        classesRelease.setPadding(0,20,0,0);
    }
    private class onNoScrollListViewItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(TeacherInfoActivity.this,VideoPlayerActivity.class);
            intent.putExtra(ELearnAppProperties.INTENT_VIDEO_POSITION,videoDataListFromTeacherID.get(position).getVideoId()-1);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        teacherFans.setText(String.valueOf(teacherFollowedDBManager.getFansCount(teacherId)));
        teacherRelease.setText(String.valueOf(videoDataListFromTeacherID.size()));
        super.onResume();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_teacher_info;
    }
}

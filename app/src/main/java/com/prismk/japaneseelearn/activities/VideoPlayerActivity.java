package com.prismk.japaneseelearn.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.adapters.VideoAdapter;
import com.prismk.japaneseelearn.bean.UserData;
import com.prismk.japaneseelearn.bean.VideoData;
import com.prismk.japaneseelearn.managers.UserDBManager;
import com.prismk.japaneseelearn.managers.VideoDBManager;
import com.prismk.japaneseelearn.managers.floatsmallvideo.FloatVideoController;
import com.prismk.japaneseelearn.properties.ELearnAppProperties;
import com.prismk.japaneseelearn.views.NoScrollListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCUserActionStandard;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoPlayerActivity extends BaseActivity {

    JCVideoPlayerStandard mJcVideoPlayerStandard;

    private FloatVideoController mFloatVideoController;

    private int REQUEST_CODE = 1000;

    private boolean isTeacherFavorite = false;
    private boolean isVideoFavorite = false;
    private TextView teacherFavorite;
    private ImageView videoFavorite;
    private CircleImageView teacherAvatar;
    private TextView teacherName;
    private TextView teacherSign;
    private TextView videoTitle;
    private ImageView isVip;
    private TextView videoDescription;
    private NoScrollListView videoRecommend;
    private VideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setStatusBarLight();
        initView();
        initTitle();
        initVideoPlayer();
        initData();
    }


    private void askPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        List<String> mPermissionList = new ArrayList<>();
        mPermissionList.clear();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);
            }
        }
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_player;
    }

    private void initView() {
        mJcVideoPlayerStandard = findViewById(R.id.jc_video);
        mFloatVideoController = FloatVideoController.getInstance();
        teacherFavorite = (TextView) findViewById(R.id.tv_teacher_favorite);
        videoFavorite = findViewById(R.id.iv_classes_favorite);

        //头像
        teacherAvatar = findViewById(R.id.civ_teacher_avatar);
        //名字
        teacherName = findViewById(R.id.tv_teacher_name);
        //个性签名
        teacherSign = findViewById(R.id.tv_teacher_sign);
        //视频标题
        videoTitle = findViewById(R.id.tv_classes_title);
        //VIP
        isVip = findViewById(R.id.iv_vip);
        //视频描述
        videoDescription = findViewById(R.id.tv_classes_description);
        videoRecommend = findViewById(R.id.lv_classes);

    }

    private void initData() {
        List<UserData> userDataList = new ArrayList<>();
        UserDBManager userDBManager = new UserDBManager(VideoPlayerActivity.this);
        userDataList = userDBManager.getUserDataListFromUserDB();
        int teacherId = getIntent().getBundleExtra(ELearnAppProperties.INTENT_BUNDLE).getInt(ELearnAppProperties.INTENT_TEACHER_ID);
        Glide.with(VideoPlayerActivity.this).load(userDataList.get(teacherId).getHeadImgUrlString()).into(teacherAvatar);
        teacherName.setText(userDataList.get(teacherId).getNickName());
        teacherSign.setText(userDataList.get(teacherId).getSign());
        String vt = getIntent().getBundleExtra(ELearnAppProperties.INTENT_BUNDLE).getString(ELearnAppProperties.INTENT_VIDEO_TITLE);
        videoTitle.setText(vt);
        String vd = getIntent().getBundleExtra(ELearnAppProperties.INTENT_BUNDLE).getString(ELearnAppProperties.INTENT_VIDEO_DESCRIPTION);
        videoDescription.setText(vd);
        boolean Vip = getIntent().getBundleExtra(ELearnAppProperties.INTENT_BUNDLE).getBoolean(ELearnAppProperties.INTENT_IS_VIP);
        if (Vip)
            isVip.setVisibility(View.VISIBLE);
        else
            isVip.setVisibility(View.INVISIBLE);

        initAdapter();
    }

    private void initAdapter() {
        videoAdapter = null;
        VideoDBManager videoDBManager = new VideoDBManager(VideoPlayerActivity.this);
        List<VideoData>list = videoDBManager.getVideoDataListFromVideoDB();
        videoAdapter = new VideoAdapter(VideoPlayerActivity.this, list);
        videoRecommend.setAdapter(videoAdapter);

    }

    private void initTitle() {

    }

    private void initVideoPlayer() {
        mJcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.jc_video);
        mJcVideoPlayerStandard.setUp("http://video.jiecao.fm/11/23/xin/%E5%81%87%E4%BA%BA.mp4"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "棱镜测试视频");
        Picasso.with(this)
                .load("http://img4.jiecaojingxuan.com/2016/11/23/00b026e7-b830-4994-bc87-38f4033806a6.jpg@!640_360")
                .into(mJcVideoPlayerStandard.thumbImageView);

        JCVideoPlayer.setJcUserAction(new MyUserActionStandard());
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_tiny_window:
                //mJcVideoPlayerStandard.startWindowTiny();
                doOpenSmallWindow();
                finish();
                break;
            //在这里处理关注和收藏视频的逻辑
            case R.id.tv_teacher_favorite://关注
                //TODO 关注和取消关注
                setTeacherFavoriteClick();
                break;
            case R.id.ll_classes_favorite:
                //TODO 收藏和取消收藏
                setVideoFavoriteClick();
        }
    }

    private void setVideoFavoriteClick() {
        if (isVideoFavorite) {
            videoFavorite.setImageResource(R.mipmap.collection_notselect);
            isVideoFavorite = false;
        } else {
            videoFavorite.setImageResource(R.mipmap.collection_select);
            isVideoFavorite = true;
        }
    }

    private void setTeacherFavoriteClick() {
        if (isTeacherFavorite) {
            teacherFavorite.setBackgroundColor(getResources().getColor(R.color.gray1));
            isTeacherFavorite = false;
        } else {
            teacherFavorite.setBackgroundColor(getResources().getColor(R.color.teacher_favorite));
            isTeacherFavorite = true;
        }
    }

    private void doOpenSmallWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                mFloatVideoController.requestPermission(this);
                return;
            } else {
                mFloatVideoController.doOverlayPlay();
            }
        } else {
            mFloatVideoController.doOverlayPlay();
        }
    }

    class MyUserActionStandard implements JCUserActionStandard {

        @Override
        public void onEvent(int type, String url, int screen, Object... objects) {
            switch (type) {
                case JCUserAction.ON_CLICK_START_ICON:
                    Log.i("USER_EVENT", "ON_CLICK_START_ICON" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_START_ERROR:
                    Log.i("USER_EVENT", "ON_CLICK_START_ERROR" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_START_AUTO_COMPLETE:
                    Log.i("USER_EVENT", "ON_CLICK_START_AUTO_COMPLETE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_PAUSE:
                    Log.i("USER_EVENT", "ON_CLICK_PAUSE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_RESUME:
                    Log.i("USER_EVENT", "ON_CLICK_RESUME" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_SEEK_POSITION:
                    Log.i("USER_EVENT", "ON_SEEK_POSITION" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_AUTO_COMPLETE:
                    Log.i("USER_EVENT", "ON_AUTO_COMPLETE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_ENTER_FULLSCREEN:
                    Log.i("USER_EVENT", "ON_ENTER_FULLSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_QUIT_FULLSCREEN:
                    Log.i("USER_EVENT", "ON_QUIT_FULLSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_ENTER_TINYSCREEN:
                    Log.i("USER_EVENT", "ON_ENTER_TINYSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_QUIT_TINYSCREEN:
                    Log.i("USER_EVENT", "ON_QUIT_TINYSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_TOUCH_SCREEN_SEEK_VOLUME:
                    Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_VOLUME" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_TOUCH_SCREEN_SEEK_POSITION:
                    Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_POSITION" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserActionStandard.ON_CLICK_START_THUMB:
                    Log.i("USER_EVENT", "ON_CLICK_START_THUMB" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserActionStandard.ON_CLICK_BLANK:
                    Log.i("USER_EVENT", "ON_CLICK_BLANK" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                default:
                    Log.i("USER_EVENT", "unknow");
                    break;
            }
        }
    }
}

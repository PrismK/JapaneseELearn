package com.prismk.japaneseelearn.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.adapters.VideoAdapter;
import com.prismk.japaneseelearn.bean.TeacherFollowedData;
import com.prismk.japaneseelearn.bean.UserData;
import com.prismk.japaneseelearn.bean.VideoCollectionData;
import com.prismk.japaneseelearn.bean.VideoData;
import com.prismk.japaneseelearn.managers.TeacherFollowedDBManager;
import com.prismk.japaneseelearn.managers.UserDBManager;
import com.prismk.japaneseelearn.managers.VideoCollectionDBManager;
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
    private int position;
    private int teacherId;
    private int loginUesrID;
    private List<TeacherFollowedData> teacherFollowedDataList;
    private TeacherFollowedDBManager teacherFollowedDBManager;
    private UserDBManager userDBManager;
    private VideoDBManager videoDBManager;
    private VideoCollectionDBManager videoCollectionDBManager;
    private int videoId;
    private LinearLayout checkUserIsVip;
    private TextView buyVip;
    private boolean isClassVip;
    private boolean isUserVip;
    private List<UserData> userDataList;
    private List<VideoData> videoDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setStatusBarDark();
        initView();
        initTitle();
        initData();
        initVideoPlayer();
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
        isVip = findViewById(R.id.imv_vip);
        //视频描述
        videoDescription = findViewById(R.id.tv_classes_description);
        videoRecommend = findViewById(R.id.lv_classes);
        videoRecommend.setOnItemClickListener(new onNoScrollListViewItemClickListener());
        checkUserIsVip = findViewById(R.id.ll_buy_vip);
        buyVip = findViewById(R.id.tv_buy_vipclass);
    }

    private void initData() {
        userDBManager = new UserDBManager(VideoPlayerActivity.this);
        videoDBManager = new VideoDBManager(VideoPlayerActivity.this);
        teacherFollowedDBManager = new TeacherFollowedDBManager(VideoPlayerActivity.this);
        videoCollectionDBManager = new VideoCollectionDBManager(VideoPlayerActivity.this);
        loginUesrID = userDBManager.getLoginUesrID();
        userDataList = userDBManager.getUserDataListFromUserDB();
        videoDataList = videoDBManager.getVideoDataListFromVideoDB();
        position = getIntent().getIntExtra(ELearnAppProperties.INTENT_VIDEO_POSITION, -1);
        teacherId = videoDataList.get(position).getUploadTeacherId();
        videoId = videoDataList.get(position).getVideoId();
        Glide.with(VideoPlayerActivity.this).load(userDataList.get(teacherId - 1).getHeadImgUrlString()).into(teacherAvatar);
        teacherName.setText(userDataList.get(teacherId - 1).getNickName());
        teacherSign.setText(userDataList.get(teacherId - 1).getSign());
        videoTitle.setText(videoDataList.get(videoId - 1).getVideoTitle());
        videoDescription.setText(videoDataList.get(videoId - 1).getVideoContext());
        videoRecommend.setItemCount(7);
        isClassVip = videoDataList.get(videoId - 1).isVipVideo();
        isUserVip = userDataList.get(loginUesrID - 1).isVIP();
        checkUserAndClassVipState();
        if (checkTeacherFavorite()) {
            teacherFavorite.setBackgroundColor(getResources().getColor(R.color.gray1));
            teacherFavorite.setText("✓已关注");
        } else {
            teacherFavorite.setBackgroundColor(getResources().getColor(R.color.teacher_favorite));
            teacherFavorite.setText("+加关注");
        }
        if (checkVideoFavorite())
            videoFavorite.setImageResource(R.mipmap.collection_select);
        else
            videoFavorite.setImageResource(R.mipmap.collection_notselect);
        initAdapter();
    }

    private void checkUserAndClassVipState() {
        if (isClassVip) {
            isVip.setVisibility(View.VISIBLE);
            if (isUserVip) {
                checkUserIsVip.setVisibility(View.GONE);
                mJcVideoPlayerStandard.setVisibility(View.VISIBLE);
            } else {
                checkUserIsVip.setVisibility(View.VISIBLE);
                mJcVideoPlayerStandard.setVisibility(View.GONE);
            }
        } else {
            isVip.setVisibility(View.INVISIBLE);
            checkUserIsVip.setVisibility(View.GONE);
        }
    }

    private boolean checkTeacherFavorite() {
        teacherFollowedDataList = teacherFollowedDBManager.getTeacherFlollowedDataFromDB();
        List<Integer> teacherIDlist = teacherFollowedDBManager.getFavoriteTeacherId(loginUesrID);
        for (TeacherFollowedData data : teacherFollowedDataList) {
            if (data.getStudentId() == loginUesrID) {
                for (int i : teacherIDlist) {
                    if (teacherId == i) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkVideoFavorite() {
        List<VideoCollectionData> videoCollectionDataList = videoCollectionDBManager.getVideoFlollowedDataFromDB();
        List<Integer> videoIDlist = videoCollectionDBManager.getFavoriteVideoId(loginUesrID);
        for (VideoCollectionData data : videoCollectionDataList) {
            if (data.getStudentId() == loginUesrID) {
                for (int i : videoIDlist) {
                    if (videoId == i) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void initAdapter() {
        videoAdapter = null;
        videoAdapter = new VideoAdapter(VideoPlayerActivity.this, videoDataList, 7);
        videoRecommend.setAdapter(videoAdapter);
    }

    private void initTitle() {

    }

    private void initVideoPlayer() {
        mJcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.jc_video);
        mJcVideoPlayerStandard.setUp(videoDataList.get(videoId - 1).getVideoUrlString(),
                JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, videoDataList.get(videoId - 1).getVideoTitle());
        Picasso.with(this)
                .load(videoDataList.get(videoId - 1).getVideoImgUrlString())
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
            /*case R.id.btn_tiny_window:
                //mJcVideoPlayerStandard.startWindowTiny();
                doOpenSmallWindow();
                break;*/
            //在这里处理关注和收藏视频的逻辑
            case R.id.tv_teacher_favorite://关注
                setTeacherFavoriteClick();
                break;
            case R.id.ll_classes_favorite:
                setVideoFavoriteClick();
                break;
            case R.id.ll_teacher_info:
                Intent intent = new Intent(this, TeacherInfoActivity.class);
                intent.putExtra(ELearnAppProperties.INTENT_TEACHERINFO_POSITION, position);
                startActivity(intent);
                break;
            case R.id.tv_buy_vipclass:
                //展示购买成功效果
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("购买VIP");
                builder.setMessage("确认购买VIP?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userDBManager.updateUserVipState();
                        userDataList = userDBManager.getUserDataListFromUserDB();
                        isUserVip = userDataList.get(loginUesrID - 1).isVIP();
                        checkUserAndClassVipState();

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(VideoPlayerActivity.this);
                        builder1.setTitle("购买成功");
                        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                        dialog.dismiss();
                    }
                }).show();




                break;
        }
    }

    private void setVideoFavoriteClick() {
        isVideoFavorite = checkVideoFavorite();
        VideoCollectionData data = new VideoCollectionData(loginUesrID, videoId);
        if (isVideoFavorite) {
            videoFavorite.setImageResource(R.mipmap.collection_notselect);
            videoCollectionDBManager.deleteFavoriteVideo(data);
            isVideoFavorite = checkVideoFavorite();
        } else {
            videoFavorite.setImageResource(R.mipmap.collection_select);
            videoCollectionDBManager.insertFavoriteVideo(data);
            isVideoFavorite = checkVideoFavorite();
        }
    }

    private void setTeacherFavoriteClick() {
        isTeacherFavorite = checkTeacherFavorite();
        TeacherFollowedData data = new TeacherFollowedData(loginUesrID, teacherId);
        if (isTeacherFavorite) {
            teacherFavorite.setBackgroundColor(getResources().getColor(R.color.teacher_favorite));
            teacherFavorite.setText("+加关注");
            teacherFollowedDBManager.deleteFavoriteTeacher(data);
            isTeacherFavorite = checkTeacherFavorite();
        } else {
            teacherFavorite.setBackgroundColor(getResources().getColor(R.color.gray1));
            teacherFavorite.setText("✓已关注");
            teacherFollowedDBManager.insertFavoriteTeacher(data);
            isTeacherFavorite = checkTeacherFavorite();
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
    private class onNoScrollListViewItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(VideoPlayerActivity.this,VideoPlayerActivity.class);
            intent.putExtra(ELearnAppProperties.INTENT_VIDEO_POSITION,position);
            startActivity(intent);
        }
    }
}

package com.prismk.japaneseelearn.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.activities.AllCollectionClassesActivity;
import com.prismk.japaneseelearn.activities.MyFavoriteTeacherActivity;
import com.prismk.japaneseelearn.activities.MyReleasedClassActivity;
import com.prismk.japaneseelearn.activities.SettingActivity;
import com.prismk.japaneseelearn.bean.UserData;
import com.prismk.japaneseelearn.bean.VideoData;
import com.prismk.japaneseelearn.managers.TeacherFollowedDBManager;
import com.prismk.japaneseelearn.managers.UserDBManager;
import com.prismk.japaneseelearn.managers.VideoCollectionDBManager;
import com.prismk.japaneseelearn.managers.VideoDBManager;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 首页Fragment
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    private View mMeFragment;
    private RelativeLayout rl_classescollection;
    private LinearLayout ll_classes_all;
    private LinearLayout ll_classes_notvip;
    private LinearLayout ll_classes_vip;
    private TextView tv_classes_all;
    private TextView tv_classes_notvip;
    private TextView tv_classes_vip;
    private ImageView imv_classes_all;
    private ImageView imv_classes_notvip;
    private ImageView iv_classes_vip;
    private CircleImageView imageHeader;
    private UserDBManager userDBManager;
    private TextView userName;
    private TextView userSign;
    private TextView userCollectionCount;
    private TextView userFavoriteCount;
    private RelativeLayout myFavoriteTeacher;
    private RelativeLayout rl_myvip;
    private RelativeLayout rl_setting;
    private RelativeLayout myReleasedClass;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mMeFragment = View.inflate(getActivity(), getLayoutId(), null);
        initView();
        initData();
        setOnClickListener();
        return mRootView;
    }

    private void setOnClickListener() {
        rl_classescollection.setOnClickListener(this);
        ll_classes_all.setOnClickListener(this);
        ll_classes_notvip.setOnClickListener(this);
        ll_classes_vip.setOnClickListener(this);
        myFavoriteTeacher.setOnClickListener(this);
        rl_myvip.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        myReleasedClass.setOnClickListener(this);
    }

    private void initView() {
        rl_classescollection = mRootView.findViewById(R.id.rl_classescollection);

        ll_classes_all = mRootView.findViewById(R.id.ll_classes_all);
        ll_classes_notvip = mRootView.findViewById(R.id.ll_classes_notvip);
        ll_classes_vip = mRootView.findViewById(R.id.ll_classes_vip);

        tv_classes_all = mRootView.findViewById(R.id.tv_classes_all);
        tv_classes_notvip = mRootView.findViewById(R.id.tv_classes_notvip);
        tv_classes_vip = mRootView.findViewById(R.id.tv_classes_vip);

        imv_classes_all = mRootView.findViewById(R.id.imv_classes_all);
        imv_classes_notvip = mRootView.findViewById(R.id.imv_classes_notvip);
        iv_classes_vip = mRootView.findViewById(R.id.iv_classes_vip);


        imageHeader = mRootView.findViewById(R.id.imageHeader);
        userName = mRootView.findViewById(R.id.tv_userInfo);
        userSign = mRootView.findViewById(R.id.tv_usersign);
        userCollectionCount = mRootView.findViewById(R.id.tv_collection_count);
        userFavoriteCount = mRootView.findViewById(R.id.tv_favorite_count);
        myFavoriteTeacher = mRootView.findViewById(R.id.rl_followedteacher);
        myReleasedClass = mRootView.findViewById(R.id.rl_releasedclass);
        rl_myvip = mRootView.findViewById(R.id.rl_myvip);
        rl_setting = mRootView.findViewById(R.id.rl_setting);
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        setStatusBarLight();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), AllCollectionClassesActivity.class);
        switch (v.getId()) {
            case R.id.rl_classescollection:
            case R.id.ll_classes_all:
                intent.putExtra("position", 0);
                startActivity(intent);
                break;
            case R.id.ll_classes_notvip:
                intent.putExtra("position", 1);
                startActivity(intent);
                break;
            case R.id.ll_classes_vip:
                intent.putExtra("position", 2);
                startActivity(intent);
                break;
            case R.id.rl_followedteacher:
                Intent intent1 = new Intent(getContext(), MyFavoriteTeacherActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.rl_myvip:

                break;
            case R.id.rl_releasedclass:
                Intent intent2 = new Intent(getContext(), MyReleasedClassActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private void initData() {
        userDBManager = new UserDBManager(getContext());
        List<UserData> userDataList = userDBManager.getUserDataListFromUserDB();

        Glide.with(getContext()).load(userDataList.get(userDBManager.getLoginUesrID() - 1).getHeadImgUrlString()).into(imageHeader);
        userName.setText(userDataList.get(userDBManager.getLoginUesrID() -1).getNickName().trim());
        userSign.setText(userDataList.get(userDBManager.getLoginUesrID() -1).getSign().trim());

        //判断是不是老师
        if (userDataList.get(userDBManager.getLoginUesrID() - 1).isTeacherUser())
            myReleasedClass.setVisibility(View.VISIBLE);
    }

    private int getAllCollectionVideoCount() {
        VideoDBManager videoDBManager = new VideoDBManager(getContext());
        VideoCollectionDBManager videoCollectionDBManager = new VideoCollectionDBManager(getContext());
        List<VideoData> videoDataList = videoDBManager.getVideoDataListFromVideoDB();
        List<VideoData> collectionVideoList = new ArrayList<>();
        List<Integer> favoriteVideoId = videoCollectionDBManager.getFavoriteVideoId(userDBManager.getLoginUesrID());
        for (VideoData videoData : videoDataList) {
            for (int i : favoriteVideoId) {
                if (videoData.getVideoId() == i) {
                    VideoData data = new VideoData();
                    i -= 1;
                    data.setVideoId(i);
                    data.setUploadTeacherId(videoDataList.get(i).getUploadTeacherId());
                    data.setUploadTime(videoDataList.get(i).getUploadTime());
                    data.setVipVideo(videoDataList.get(i).isVipVideo());
                    data.setVideoIntroduction(videoDataList.get(i).getVideoIntroduction());
                    data.setVideoTitle(videoDataList.get(i).getVideoTitle());
                    data.setVideoImgUrlString(videoDataList.get(i).getVideoImgUrlString());
                    data.setVideoUrlString(videoDataList.get(i).getVideoUrlString());
                    collectionVideoList.add(data);
                }
            }
        }
        return collectionVideoList.size();
    }

    private int getFavoriteTeacherCount() {
        TeacherFollowedDBManager teacherFollowedDBManager = new TeacherFollowedDBManager(getContext());
        List<Integer> favoriteTeacherIdList = teacherFollowedDBManager.getFavoriteTeacherId(userDBManager.getLoginUesrID());
        return favoriteTeacherIdList.size();
    }

    @Override
    public void onResume() {
        userCollectionCount.setText(String.valueOf(getAllCollectionVideoCount()));
        userFavoriteCount.setText(String.valueOf(getFavoriteTeacherCount()));
        super.onResume();
    }
}
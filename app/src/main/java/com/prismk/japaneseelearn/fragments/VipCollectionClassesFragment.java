package com.prismk.japaneseelearn.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.adapters.VideoAdapter;
import com.prismk.japaneseelearn.bean.VideoData;
import com.prismk.japaneseelearn.managers.UserDBManager;
import com.prismk.japaneseelearn.managers.VideoCollectionDBManager;
import com.prismk.japaneseelearn.managers.VideoDBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：prism（棱镜）Github地址：https://github.com/prismk/
 * 本地用户：18340
 * 版    本：1.0
 * 创建日期：2019/3/31
 * 描    述：
 * 修订历史：
 * ================================================
 */

public class VipCollectionClassesFragment extends BaseFragment {

    private ListView vipCollectionClass;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vip_classes;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = View.inflate(getActivity(), getLayoutId(), null);
        initView();
        initData();
        return mRootView;
    }

    private void initData() {
        List<VideoData> collectionVideoList = getVipCollectionVideo();
        VideoAdapter videoAdapter = new VideoAdapter(getContext(), collectionVideoList);
        vipCollectionClass.setAdapter(videoAdapter);

    }

    private void initView() {
        vipCollectionClass = mRootView.findViewById(R.id.lv_vip_collection);
    }

    private List<VideoData> getVipCollectionVideo() {
        VideoDBManager videoDBManager = new VideoDBManager(getContext());
        VideoCollectionDBManager videoCollectionDBManager = new VideoCollectionDBManager(getContext());
        UserDBManager userDBManager = new UserDBManager(getContext());
        List<VideoData> videoDataList = videoDBManager.getVideoDataListFromVideoDB();
        List<VideoData> collectionVideoList = new ArrayList<>();
        List<Integer> favoriteVideoId = videoCollectionDBManager.getFavoriteVideoId(userDBManager.getLoginUesrID());
        for (VideoData videoData : videoDataList) {
            for (int i : favoriteVideoId) {
                if (videoData.getVideoId() == i) {
                    VideoData data = new VideoData();
                    i -= 1;
                    if (videoDataList.get(i).isVipVideo()) {
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
        }
        return collectionVideoList;
    }
}

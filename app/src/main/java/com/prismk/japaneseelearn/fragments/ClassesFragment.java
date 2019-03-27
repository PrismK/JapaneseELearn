package com.prismk.japaneseelearn.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.activities.VideoPlayerActivity;
import com.prismk.japaneseelearn.adapters.VideoAdapter;
import com.prismk.japaneseelearn.bean.VideoData;
import com.prismk.japaneseelearn.managers.VideoDBManager;
import com.prismk.japaneseelearn.properties.ELearnAppProperties;
import com.prismk.japaneseelearn.widgets.Title;

import java.util.List;

/**
 * 首页Fragment
 */
public class ClassesFragment extends BaseFragment {

    private VideoAdapter videoAdapter;
    private List<VideoData> list;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classes;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initTitle();
        mRootView = View.inflate(getActivity(), getLayoutId(), null);
        initData();
        initView();
        return mRootView;
    }

    private void initTitle() {
        Title title = mRootView.findViewById(R.id.title);
        title.setTitleNameStr("课程");
        title.setTheme(Title.TitleTheme.THEME_LIGHT);
        title.setShowDivider(true);
        /*Title.ButtonInfo buttonInfoRight1 = new Title.ButtonInfo(true, Title.BUTTON_RIGHT1);
        buttonInfoRight1.iconRes = R.drawable.selector_btn_addcontact;
        Title.ButtonInfo bottonRight2 = new Title.ButtonInfo(true, Title
                .BUTTON_RIGHT2, R.mipmap.ic_search_btn, null);
        title.setOnTitleButtonClickListener(new Title.OnTitleButtonClickListener() {
            @Override
            public void onClick(int id, Title.ButtonViewHolder viewHolder) {
                switch (id) {
                    case Title.BUTTON_RIGHT1:
                        SearchRemoteContactActivity.open(getActivity(), SearchRemoteContactActivity.REQUEST_CODE_FROM_CONTACT);
                        break;
                    case Title.BUTTON_RIGHT2:
                        SearchLocalActivity.open(getActivity());
                        break;
                }
            }
        });
        title.setButtonInfo(bottonRight2);
        title.setButtonInfo(buttonInfoRight1);*/
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        setStatusBarView();
    }
    private void initView(){
        ListView videoListView = mRootView.findViewById(R.id.lv_classes);
        videoListView.setAdapter(videoAdapter);
        videoListView.setOnItemClickListener(new OnItemClickListener());
    }
    private void initData(){
        videoAdapter = null;
        VideoDBManager videoDBManager = new VideoDBManager(getContext());
        list = videoDBManager.getVideoDataListFromVideoDB();
        videoAdapter = new VideoAdapter(getContext(), list);
    }

    private class OnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
            Bundle bundle = new Bundle();
            //老师ID
            bundle.putInt(ELearnAppProperties.INTENT_TEACHER_ID,list.get(position).getUploadTeacherId());
            //视频标题
            bundle.putString(ELearnAppProperties.INTENT_VIDEO_TITLE,list.get(position).getVideoTitle());
            //视频描述
            bundle.putString(ELearnAppProperties.INTENT_VIDEO_DESCRIPTION,list.get(position).getVideoIntroduction());
            //vip
            bundle.putBoolean(ELearnAppProperties.INTENT_IS_VIP,list.get(position).isVipVideo());
            intent.putExtra(ELearnAppProperties.INTENT_BUNDLE,bundle);
            startActivity(intent);
        }
    }
}
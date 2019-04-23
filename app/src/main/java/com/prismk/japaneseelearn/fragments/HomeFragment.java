package com.prismk.japaneseelearn.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.activities.TeacherInfoActivity;
import com.prismk.japaneseelearn.activities.VideoPlayerActivity;
import com.prismk.japaneseelearn.adapters.ArticleAdapter;
import com.prismk.japaneseelearn.adapters.TeacherListAdapter;
import com.prismk.japaneseelearn.adapters.VideoAdapter;
import com.prismk.japaneseelearn.bean.UserData;
import com.prismk.japaneseelearn.bean.VideoData;
import com.prismk.japaneseelearn.managers.UpdateEverydayManager;
import com.prismk.japaneseelearn.managers.UserDBManager;
import com.prismk.japaneseelearn.managers.VideoDBManager;
import com.prismk.japaneseelearn.managers.mLunBoTuManager;
import com.prismk.japaneseelearn.properties.ELearnAppProperties;
import com.prismk.japaneseelearn.utils.ArticleXmlParser;
import com.prismk.japaneseelearn.views.NoScrollListView;
import com.prismk.japaneseelearn.widgets.Title;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * 首页Fragment
 */
public class HomeFragment extends BaseFragment {

    private UserDBManager userDBManager;
    private VideoAdapter videoAdapter;
    private List<VideoData> videoDataList;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NoScrollListView drawHeaderView;
    private TeacherListAdapter teacherListAdapter;
    private List<UserData> teacherDataList;
    private boolean teacherUser;

    @Override
    protected int getLayoutId() {
        return showLayout();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = View.inflate(getActivity(), getLayoutId(), null);
        initData();
        initTitle();
        initView();
        return mRootView;
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        setStatusBarView();
    }

    private void initTitle() {
        Title title = mRootView.findViewById(R.id.title);
        title.setTitleNameStr("首页");
        title.setTheme(Title.TitleTheme.THEME_LIGHT);
        title.setShowDivider(true);
        if (teacherUser) {
            Title.ButtonInfo drawer = new Title.ButtonInfo(true, Title.BUTTON_LEFT, 0, "教师列表");
            title.setButtonInfoNamePadding(Title.BUTTON_LEFT, 15, 0, 0, 0);
            title.setButtonInfo(drawer);
            title.setOnTitleButtonClickListener(new Title.OnTitleButtonClickListener() {
                @Override
                public void onClick(int id, Title.ButtonViewHolder viewHolder) {
                    switch (id) {
                        case Title.BUTTON_LEFT:
                            if (drawerLayout.isDrawerOpen(navigationView))
                                drawerLayout.closeDrawer(navigationView);
                            else
                                drawerLayout.openDrawer(navigationView);
                            break;
                    }
                }
            });
        }

    }

    private void initData() {
        VideoDBManager videoDBManager = new VideoDBManager(getContext());
        videoDataList = videoDBManager.getVideoDataListFromVideoDB();
        if (getLayoutId() == R.layout.fragment_classes) {
            userDBManager = new UserDBManager(getContext());
            videoAdapter = new VideoAdapter(getContext(), videoDataList);
            teacherDataList = userDBManager.getTeacherListFromUserDB();
            teacherListAdapter = new TeacherListAdapter(getContext(), teacherDataList);
        } else if (getLayoutId() == R.layout.fragment_home) {
            videoAdapter = new VideoAdapter(getContext(), getVipVideoList(),6);
        }
    }

    private List<VideoData> getVipVideoList() {
        List<VideoData> VipVideoList = new ArrayList<>();
        for (VideoData videoData : videoDataList) {
            if (videoData.isVipVideo()) {
                VipVideoList.add(videoData);
            }
        }
        return VipVideoList;
    }

    private void initView() {
        if (getLayoutId() == R.layout.fragment_classes) {
            ListView videoListView = mRootView.findViewById(R.id.lv_classes);
            videoListView.setAdapter(videoAdapter);
            videoListView.setOnItemClickListener(new OnItemClickListener());
            drawerLayout = mRootView.findViewById(R.id.dl_classfragment);
            navigationView = mRootView.findViewById(R.id.nv_teacherlist);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势滑动
            drawHeaderView = (NoScrollListView) navigationView.getHeaderView(0);
            drawHeaderView.setItemCount(teacherDataList.size());
            drawHeaderView.setAdapter(teacherListAdapter);
            drawHeaderView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getContext(), TeacherInfoActivity.class);
                    intent.putExtra(ELearnAppProperties.INTENT_TEACHERLIST_POSITION, position);
                    startActivity(intent);
                }
            });
        } else if (getLayoutId() == R.layout.fragment_home) {
            initmLunBoTu();
            initUpdateEveryDay();
            initArticle();
            initNetClass();
        }

    }

    private void initNetClass() {
        NoScrollListView netclass = mRootView.findViewById(R.id.nlv_net_class);
        netclass.setItemCount(6);
        netclass.setAdapter(videoAdapter);
        netclass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),VideoPlayerActivity.class);
                intent.putExtra(ELearnAppProperties.INTENT_VIDEO_POSITION,getVipVideoList().get(position).getVideoId()-1);
                startActivity(intent);
            }
        });
    }

    private void initArticle() {
        try {
            InputStream inputStream = getContext().getAssets().open("article.xml");
            ListView article = mRootView.findViewById(R.id.nlv_article);
//            article.setItemCount(2);
            article.setAdapter(new ArticleAdapter(ArticleXmlParser.parser(inputStream),getContext()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initUpdateEveryDay() {
        TextView tv = mRootView.findViewById(R.id.tv_update_everyday);
        UpdateEverydayManager manager = new UpdateEverydayManager(tv);
        manager.checkSystemTime();
    }

    private void initmLunBoTu() {
        ViewPager mlunBoTu = mRootView.findViewById(R.id.vp_lunbotu);
        mLunBoTuManager manager = new mLunBoTuManager(getContext(), mlunBoTu, mRootView);
        manager.setLunBoImages(new int[]{R.drawable.a, R.drawable.b, R.drawable.c});
        //点击事件
//            manager.setOnPageClickListener(new mLunBoTuManager.OnPageClickListener() {
//                @Override
//                public void OnPageClick(int id) {
//                    switch (id){
//                        case IMAGE1:
//                            break;
//                        case IMAGE2:
//                            break;
//                        case IMAGE3:
//                            break;
//                    }
//                }
//            });
        manager.create();
    }

    private int showLayout() {
        if (userDBManager == null) {
            userDBManager = new UserDBManager(getContext());
        }
        List<UserData> userDataListFromUserDB = userDBManager.getUserDataListFromUserDB();
        teacherUser = userDataListFromUserDB.get(userDBManager.getLoginUesrID() - 1).isTeacherUser();
        if (teacherUser)
            return R.layout.fragment_classes;
        else
            return R.layout.fragment_home;
    }

    private class OnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
            intent.putExtra(ELearnAppProperties.INTENT_VIDEO_POSITION, videoDataList.get(position).getVideoId() - 1);
            startActivity(intent);
        }
    }
}

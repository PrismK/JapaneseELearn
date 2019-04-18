package com.prismk.japaneseelearn.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.activities.TeacherInfoActivity;
import com.prismk.japaneseelearn.activities.VideoPlayerActivity;
import com.prismk.japaneseelearn.adapters.TeacherListAdapter;
import com.prismk.japaneseelearn.adapters.VideoAdapter;
import com.prismk.japaneseelearn.bean.UserData;
import com.prismk.japaneseelearn.bean.VideoData;
import com.prismk.japaneseelearn.managers.UserDBManager;
import com.prismk.japaneseelearn.managers.VideoDBManager;
import com.prismk.japaneseelearn.properties.ELearnAppProperties;
import com.prismk.japaneseelearn.views.NoScrollListView;
import com.prismk.japaneseelearn.widgets.Title;

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
        //TODO:加判断是否教师用户
        if (teacherUser){
            Title.ButtonInfo drawer = new Title.ButtonInfo(true,Title.BUTTON_LEFT,0,"教师列表");
            title.setButtonInfoNamePadding(Title.BUTTON_LEFT,15,0,0,0);
            title.setButtonInfo(drawer);
            title.setOnTitleButtonClickListener(new Title.OnTitleButtonClickListener() {
                @Override
                public void onClick(int id, Title.ButtonViewHolder viewHolder) {
                    switch (id){
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

    private void initData(){
        if (getLayoutId()==R.layout.fragment_classes){
            userDBManager = new UserDBManager(getContext());
            VideoDBManager videoDBManager = new VideoDBManager(getContext());
            videoDataList = videoDBManager.getVideoDataListFromVideoDB();
            videoAdapter = new VideoAdapter(getContext(), videoDataList);
            teacherDataList = userDBManager.getTeacherListFromUserDB();
            teacherListAdapter = new TeacherListAdapter(getContext(), teacherDataList);
        }
    }

    private void initView(){
        if (getLayoutId()==R.layout.fragment_classes){
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
                    intent.putExtra(ELearnAppProperties.INTENT_TEACHERLIST_POSITION,position);
                    startActivity(intent);
                }
            });


        }

    }
    private int showLayout(){
        if (userDBManager==null){
          userDBManager = new UserDBManager(getContext());
        }
        List<UserData> userDataListFromUserDB = userDBManager.getUserDataListFromUserDB();
        teacherUser = userDataListFromUserDB.get(userDBManager.getLoginUesrID() - 1).isTeacherUser();
        if (teacherUser)
            return R.layout.fragment_classes;
        else
            return R.layout.fragment_home;
    }
    private class OnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
            intent.putExtra(ELearnAppProperties.INTENT_VIDEO_POSITION, videoDataList.get(position).getVideoId()-1);
            startActivity(intent);
        }
    }
}

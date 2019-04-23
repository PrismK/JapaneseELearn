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
public class ClassesFragment extends BaseFragment {

    private VideoAdapter videoAdapter;
    private List<VideoData> videoDataList;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NoScrollListView drawHeaderView;
    private TeacherListAdapter teacherListAdapter;
    private List<UserData> teacherDataList;
    private VideoDBManager videoDBManager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classes;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = View.inflate(getActivity(), getLayoutId(), null);
        initTitle();
        initData();
        initView();
        return mRootView;
    }

    private void initTitle() {
        Title title = mRootView.findViewById(R.id.title);
        title.setTitleNameStr("课程");
        title.setTheme(Title.TitleTheme.THEME_LIGHT);
        title.setShowDivider(true);
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
        drawerLayout = mRootView.findViewById(R.id.dl_classfragment);
        navigationView = mRootView.findViewById(R.id.nv_teacherlist);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势滑动
        drawHeaderView = (NoScrollListView) navigationView.getHeaderView(0);
        drawHeaderView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), TeacherInfoActivity.class);
                intent.putExtra(ELearnAppProperties.INTENT_TEACHERLIST_POSITION,position);
                startActivity(intent);
            }
        });
        drawHeaderView.setItemCount(teacherDataList.size());
        drawHeaderView.setAdapter(teacherListAdapter);
    }
    private void initData(){
        videoAdapter = null;
        videoDBManager = new VideoDBManager(getContext());
        videoDataList = videoDBManager.getVideoDataListFromVideoDB();
        videoAdapter = new VideoAdapter(getContext(), videoDataList);
        UserDBManager userDBManager = new UserDBManager(getContext());
        teacherDataList = userDBManager.getTeacherListFromUserDB();
        teacherListAdapter = new TeacherListAdapter(getContext(), teacherDataList);
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
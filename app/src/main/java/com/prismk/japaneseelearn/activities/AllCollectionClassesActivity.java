package com.prismk.japaneseelearn.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.fragments.AllCollectionClassesFragment;
import com.prismk.japaneseelearn.fragments.BaseFragment;
import com.prismk.japaneseelearn.fragments.NotVipCollectionClassesFragment;
import com.prismk.japaneseelearn.fragments.VipCollectionClassesFragment;
import com.prismk.japaneseelearn.widgets.Title;

import java.util.ArrayList;
import java.util.List;

public class AllCollectionClassesActivity extends BaseActivity implements View.OnClickListener {

    private int posint;
    private List<BaseFragment> mFragmentList = new ArrayList<BaseFragment>();
    private AllCollectionClassesFragment allCollectionClassesFragment;
    private NotVipCollectionClassesFragment notVipCollectionClassesFragment;
    private VipCollectionClassesFragment vipCollectionClassesFragment;
    private ViewPager vp_content;
    private LinearLayout item_all_classes;
    private TextView tv_all_classes;
    private LinearLayout item_notvip;
    private TextView tv_notvip;
    private LinearLayout item_vip;
    private TextView tv_vip;
    private FragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setStatusBarDark();
        initTitle();
        posint = getIntent().getIntExtra("position",0);
        initView();
        setFragments();
        setViewPagerListener();
    }

    private void initTitle() {
        Title title = findViewById(R.id.title);
        title.setTitleNameStr("收藏的视频");
        title.setTheme(Title.TitleTheme.THEME_LIGHT);
        title.setShowDivider(true);
        Title.ButtonInfo drawer = new Title.ButtonInfo(true,Title.BUTTON_LEFT,R.mipmap.navigationbar_back,"");
        title.setButtonInfoNamePadding(Title.BUTTON_LEFT,15,0,0,0);
        title.setButtonInfo(drawer);
        title.setOnTitleButtonClickListener(new Title.OnTitleButtonClickListener() {
            @Override
            public void onClick(int id, Title.ButtonViewHolder viewHolder) {
                switch (id){
                    case Title.BUTTON_LEFT:
                        goBack();
                        break;
                }
            }
        });
    }

    private void setViewPagerListener() {
        //ViewPager的监听事件
        vp_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*此方法在页面被选中时调用*/
                changeViewColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
                arg0 ==1的时辰默示正在滑动，
                arg0==2的时辰默示滑动完毕了，
                arg0==0的时辰默示什么都没做。*/
            }
        });
    }

    private void initView() {
        vp_content = (ViewPager) findViewById(R.id.vp_content);

        item_all_classes = (LinearLayout) findViewById(R.id.item_all_classes);
        item_notvip = (LinearLayout) findViewById(R.id.item_notvip);
        item_vip = (LinearLayout) findViewById(R.id.item_vip);

        tv_all_classes = (TextView) findViewById(R.id.tv_all_classes);
        tv_notvip = (TextView) findViewById(R.id.tv_notvip);
        tv_vip = (TextView) findViewById(R.id.tv_vip);

        allCollectionClassesFragment = new AllCollectionClassesFragment();
        notVipCollectionClassesFragment = new NotVipCollectionClassesFragment();
        vipCollectionClassesFragment = new VipCollectionClassesFragment();

        mFragmentList.add(allCollectionClassesFragment);
        mFragmentList.add(notVipCollectionClassesFragment);
        mFragmentList.add(vipCollectionClassesFragment);

        item_all_classes.setOnClickListener(this);
        item_notvip.setOnClickListener(this);
        item_vip.setOnClickListener(this);
    }

    /*
     *由ViewPager的滑动修改底部导航Text的颜色
     */
    private void changeViewColor(int position) {
        if (position == 0) {
            tv_all_classes.setTextColor(Color.parseColor("#ffffff"));
            tv_notvip.setTextColor(Color.parseColor("#FF888888"));
            tv_vip.setTextColor(Color.parseColor("#FF888888"));

            item_all_classes.setBackgroundColor(getResources().getColor(R.color.blueMain));
            item_notvip.setBackgroundColor(getResources().getColor(R.color.pureWhite));
            item_vip.setBackgroundColor(getResources().getColor(R.color.pureWhite));
        } else if (position == 1) {
            tv_all_classes.setTextColor(Color.parseColor("#FF888888"));
            tv_notvip.setTextColor(Color.parseColor("#ffffff"));
            tv_vip.setTextColor(Color.parseColor("#FF888888"));

            item_all_classes.setBackgroundColor(getResources().getColor(R.color.pureWhite));
            item_notvip.setBackgroundColor(getResources().getColor(R.color.blueMain));
            item_vip.setBackgroundColor(getResources().getColor(R.color.pureWhite));
        } else if (position == 2) {
            tv_all_classes.setTextColor(Color.parseColor("#FF888888"));
            tv_notvip.setTextColor(Color.parseColor("#FF888888"));
            tv_vip.setTextColor(Color.parseColor("#ffffff"));

            item_all_classes.setBackgroundColor(getResources().getColor(R.color.pureWhite));
            item_notvip.setBackgroundColor(getResources().getColor(R.color.pureWhite));
            item_vip.setBackgroundColor(getResources().getColor(R.color.blueMain));
        }
    }

    private void setFragments() {
        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(),mFragmentList);
        vp_content.setOffscreenPageLimit(3);//ViewPager的缓存为3帧
        vp_content.setAdapter(mFragmentAdapter);

        if (posint == 0) {
            tv_all_classes.setTextColor(Color.parseColor("#ffffff"));
            tv_notvip.setTextColor(Color.parseColor("#FF888888"));
            tv_vip.setTextColor(Color.parseColor("#FF888888"));

            item_all_classes.setBackgroundColor(getResources().getColor(R.color.blueMain));
            item_notvip.setBackgroundColor(getResources().getColor(R.color.pureWhite));
            item_vip.setBackgroundColor(getResources().getColor(R.color.pureWhite));
            vp_content.setCurrentItem(0);//初始设置ViewPager选中第一帧
        }else if (posint == 1) {
            tv_all_classes.setTextColor(Color.parseColor("#FF888888"));
            tv_notvip.setTextColor(Color.parseColor("#ffffff"));
            tv_vip.setTextColor(Color.parseColor("#FF888888"));

            item_all_classes.setBackgroundColor(getResources().getColor(R.color.pureWhite));
            item_notvip.setBackgroundColor(getResources().getColor(R.color.blueMain));
            item_vip.setBackgroundColor(getResources().getColor(R.color.pureWhite));
            vp_content.setCurrentItem(1);
        }else if (posint == 2) {
            tv_all_classes.setTextColor(Color.parseColor("#FF888888"));
            tv_notvip.setTextColor(Color.parseColor("#FF888888"));
            tv_vip.setTextColor(Color.parseColor("#ffffff"));

            item_all_classes.setBackgroundColor(getResources().getColor(R.color.pureWhite));
            item_notvip.setBackgroundColor(getResources().getColor(R.color.pureWhite));
            item_vip.setBackgroundColor(getResources().getColor(R.color.blueMain));
            vp_content.setCurrentItem(2);
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_all_collection_classes;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_all_classes:
                vp_content.setCurrentItem(0, true);
                break;
            case R.id.item_notvip:
                vp_content.setCurrentItem(1, true);
                break;
            case R.id.item_vip:
                vp_content.setCurrentItem(2, true);
                break;
        }
    }

    public class FragmentAdapter extends FragmentPagerAdapter {

        List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();

        public FragmentAdapter(android.support.v4.app.FragmentManager fm, List<BaseFragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

}

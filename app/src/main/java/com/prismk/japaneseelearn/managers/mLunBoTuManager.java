package com.prismk.japaneseelearn.managers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.adapters.mViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



/**
 * create by Nevermore
 * 2019/4/16
 * 你不愿回忆的过去已经死亡，就让黑夜把它永远埋葬
 */
public class mLunBoTuManager {
    private ViewPager mViewPager;
    private Context context;
    private List<ImageView> mImageList;//轮播的图片集合
    private int previousPosition = 0;//前一个被选中的position
    private List<View> mDots;//小点
    private long delayAndPeriodTime = 5500;
    // 在values文件假下创建了pager_image_ids.xml文件，并定义了4张轮播图对应的id，用于点击事件
    private int[] image_ids = new int[]{R.id.pager_image1, R.id.pager_image2, R.id.pager_image3};
    //图片数组
    private int[] images = new int[3];
    private Timer timer;
    private final int START = 1;
    private final int STOP = 2;
    private int defalutDotRound = 16;
    private int selectDotRound = 24;
    private OnPageClickListener onPageClickListener;



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START:
                    autoPlayView();
                    break;
                case STOP:
                    cancel();
                    break;
            }
        }
    };

    public mLunBoTuManager(Context context, ViewPager viewPager) {
        this.context = context;
        this.mViewPager = viewPager;

    }

    private void initData() {
        addImageToImageList();
        addDotToLinearLayout();
    }

    private void addDotToLinearLayout() {
        //添加轮播点
        LinearLayout linearLayoutDots = (LinearLayout) ((Activity) context).findViewById(R.id.lineLayout_dot);
        mDots = addDots(linearLayoutDots, fromResToDrawable(R.drawable.vector_lunbotu), mImageList.size());
        mDots.get(0).post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                dotParams.width = selectDotRound;
                dotParams.height = selectDotRound;
                dotParams.setMargins(5, 0, 5, 5);
                mDots.get(0).setLayoutParams(dotParams);
            }
        });
    }

    private void addImageToImageList() {
        int[] imageRess = images;
        if (imageRess.length != 0) {
            //添加图片到图片列表里
            mImageList = new ArrayList<>();
            ImageView iv;
            for (int i = 0; i < 3; i++) {
                iv = new ImageView(context);
                iv.setBackgroundResource(imageRess[i]);//设置图片
                iv.setId(image_ids[i]);//顺便给图片设置id
                iv.setOnClickListener(new pagerImageOnClick());//设置图片点击事件
                mImageList.add(iv);
            }
        }
    }

    private void initView() {
        mViewPagerAdapter viewPagerAdapter = new mViewPagerAdapter(mImageList, mViewPager);
        mViewPager.setAdapter(viewPagerAdapter);
        setOnTouchListener();
        setOnPageChangeListener();
        setFirstLocation();

    }

    private void setOnPageChangeListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //伪无限循环，滑到最后一张图片又从新进入第一张图片
                int newPosition = position % mImageList.size();


                // 把当前选中的点给切换了, 还有描述信息也切换
                //设置轮播点
                LinearLayout.LayoutParams newDotParams = (LinearLayout.LayoutParams) mDots.get(newPosition).getLayoutParams();
                newDotParams.width = selectDotRound;
                newDotParams.height = selectDotRound;

                LinearLayout.LayoutParams oldDotParams = (LinearLayout.LayoutParams) mDots.get(previousPosition).getLayoutParams();
                oldDotParams.width = defalutDotRound;
                oldDotParams.height = defalutDotRound;

                // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
                mDots.get(newPosition).setLayoutParams(newDotParams);

                previousPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setOnTouchListener() {
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        stopPlayView();
                        break;
                    case MotionEvent.ACTION_UP:
                        startPlayView();
                        break;
                }
                return false;
            }
        });
    }


    public interface OnPageClickListener {
        int IMAGE1 = R.id.pager_image1;
        int IMAGE2 = R.id.pager_image2;
        int IMAGE3 = R.id.pager_image3;
        void OnPageClick(int id);
    }

    public void setOnPageClickListener(OnPageClickListener onPageClickListener) {
        this.onPageClickListener = onPageClickListener;
    }

    private class pagerImageOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
                    if (onPageClickListener!=null)
                    onPageClickListener.OnPageClick(v.getId());
            }
        }





    /**
     * 资源图片转Drawable
     *
     * @param resId
     * @return
     */
    private Drawable fromResToDrawable(int resId) {
        return context.getResources().getDrawable(resId);
    }

    /**
     * 添加多个轮播小点到横向线性布局
     *
     * @param linearLayout
     * @param backgount
     * @param number
     * @return
     */
    private List<View> addDots(final LinearLayout linearLayout, Drawable backgount, int number) {
        List<View> dots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int dotId = addDot(linearLayout, backgount);
            dots.add(((Activity) context).findViewById(dotId));
        }
        return dots;
    }

    /**
     * 动态添加一个点
     *
     * @param linearLayout 添加到LinearLayout布局
     * @param backgount    设置
     * @return
     */
    @SuppressLint("NewApi")
    private int addDot(final LinearLayout linearLayout, Drawable backgount) {
        final View dot = new View(context);
        LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        dotParams.width = defalutDotRound;
        dotParams.height = defalutDotRound;
        dotParams.setMargins(5, 0, 5, 5);
        dot.setLayoutParams(dotParams);
        dot.setBackground(backgount);
        dot.setId(View.generateViewId());
        linearLayout.addView(dot);
        return dot.getId();
    }


    /**
     * 设置刚打开app时显示的图片和文字
     */
    private void setFirstLocation() {
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % mImageList.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        mViewPager.setCurrentItem(currentPosition);
    }

    private synchronized void autoPlayView() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    }
                });
            }
        };
        timer.schedule(timerTask, delayAndPeriodTime, delayAndPeriodTime);

    }

    public void stopPlayView() {
        handler.sendEmptyMessage(STOP);
    }

    private void startPlayView() {
        handler.sendEmptyMessage(START);
    }



    public void setDelayAndPeriodTime(long periodTime) {
        this.delayAndPeriodTime = periodTime;
    }

    public void setDefalutDotRound(int defalutDotRound) {
        this.defalutDotRound = defalutDotRound;
    }

    public void setSelectDotRound(int selectDotRound) {
        this.selectDotRound = selectDotRound;
    }

    private void cancel() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void create() {
        initData();
        initView();
        startPlayView();
    }

    public void setLunBoImages(int[] resIds) {
        this.images = resIds;
    }
}

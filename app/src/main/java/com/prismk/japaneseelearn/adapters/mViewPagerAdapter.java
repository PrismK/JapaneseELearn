package com.prismk.japaneseelearn.adapters;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * create by Nevermore
 * 2019/4/16
 * 你不愿回忆的过去已经死亡，就让黑夜把它永远埋葬
 */
public class mViewPagerAdapter extends PagerAdapter {

    private List<ImageView> images;
    private ViewPager viewPager;

    public mViewPagerAdapter(List<ImageView> images, ViewPager viewPager) {
        this.images = images;
        this.viewPager = viewPager;
    }

    @Override
    public int getCount() {
        //返回一个无限大的值实现无限循环
        return Integer.MAX_VALUE;
    }

    /**
     * 判断是否使用缓存, 如果返回的是true, 使用缓存. 不去调用instantiateItem方法创建一个新的对象
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // 把position对应位置的ImageView添加到ViewPager中
        ImageView iv = images.get(position % images.size());
        container.removeView(iv);
        viewPager.addView(iv);
        // 把当前添加ImageView返回回去.
        return iv;
    }

    /**
     * 销毁一个条目
     * position 就是当前需要被销毁的条目的索引
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 把ImageView从ViewPager中移除掉,下面方法不屏蔽会有空白，有bug再改
//        viewPager.removeView(images.get(position % images.size()));
    }
}

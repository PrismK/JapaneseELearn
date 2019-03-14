package com.prismk.japaneseelearn.libs.banner;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.donkingliang.banner.CustomBanner;
import com.prismk.japaneseelearn.R;

import java.util.ArrayList;

/**
 * ================================================
 * 作    者：prism（棱镜）Github地址：https://github.com/prismk/
 * 本地用户：18340
 * 版    本：1.0
 * 创建日期：2018/12/13
 * 描    述：
 * 修订历史：
 * ================================================
 */

public class BannerActivity extends AppCompatActivity {

    private CustomBanner<String> mBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_banner_activity_main);

        mBanner = (CustomBanner) findViewById(R.id.banner);

        ArrayList<String> images = new ArrayList<>();
        images.add("https://i0.hdslb.com/bfs/article/4b8e48058a2de8afa30e3e7be697579f5c8c1b73.jpg@1320w_954h.webp");
        images.add("http://img.mp.itc.cn/upload/20170304/ee023ee695974eedbcaeb94af2e8cdb3_th.jpg");
        images.add("https://ws1.sinaimg.cn/mw600/c524f7d4jw1ej551xbiorj20yu0oeqga.jpg");
        images.add("https://ws1.sinaimg.cn/large/c524f7d4jw1ej551n8g4wj218g0p0tnz.jpg");
        images.add("https://static.acg12.com/uploads/2018/07/9455a35b6de473661832d2ab9308f5ba-800x500.jpg");
        images.add("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2122017879,3985476468&fm=175&s=D87DA844EFCFB72EE48B279D0300908E&w=640&h=252&img.JPEG");
        images.add("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=178900788,1761672991&fm=175&s=3C41A94C1B23937E1CC92D15030050C0&w=639&h=338&img.JPEG");

        setBean(images);
    }

    //设置普通指示器
    private void setBean(final ArrayList<String> beans) {
        mBanner.setPages(new CustomBanner.ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String entity) {
                Glide.with(context).load(entity).into((ImageView) view);
            }
        }, beans)
//                //设置指示器为普通指示器
//                .setIndicatorStyle(CustomBanner.IndicatorStyle.ORDINARY)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setIndicatorRes(R.drawable.shape_point_select, R.drawable.shape_point_unselect)
//                //设置指示器的方向
//                .setIndicatorGravity(CustomBanner.IndicatorGravity.CENTER)
//                //设置指示器的指示点间隔
//                .setIndicatorInterval(20)
                //设置自动翻页
                .startTurning(4000);
    }

//    //设置普通指示器
//    private void setBean(final ArrayList beans) {
//        mBanner.setPages(new CustomBanner.ViewCreator<String>() {
//            @Override
//            public View createView(Context context, int position) {
//                ImageView imageView = new ImageView(context);
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                return imageView;
//            }
//
//            @Override
//            public void updateUI(Context context, View view, int position, String entity) {
//                Glide.with(context).load(entity).into((ImageView) view);
//            }
//        }, beans)
//                //设置指示器为普通指示器
//                .setIndicatorStyle(CustomBanner.IndicatorStyle.NUMBER)
//                //设置指示器的方向
//                .setIndicatorGravity(CustomBanner.IndicatorGravity.RIGHT)
//                //设置自动翻页
//                .startTurning(5000);
//    }

//    //设置没有指示器
//    private void setBean(final ArrayList beans) {
//        mBanner.setPages(new CustomBanner.ViewCreator<String>() {
//            @Override
//            public View createView(Context context, int position) {
//                ImageView imageView = new ImageView(context);
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                return imageView;
//            }
//
//            @Override
//            public void updateUI(Context context, View view, int position, String entity) {
//                Glide.with(context).load(entity).into((ImageView) view);
//            }
//        }, beans)
//                //设置没有指示器
//                .setIndicatorStyle(CustomBanner.IndicatorStyle.NONE)
//                //设置自动翻页
//                .startTurning(5000);
//    }
}

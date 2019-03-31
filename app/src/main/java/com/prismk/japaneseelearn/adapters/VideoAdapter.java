package com.prismk.japaneseelearn.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.bean.VideoData;

import java.util.List;

/**
 * create by Nevermore
 * 2019/3/25
 * 你不愿回忆的过去已经死亡，就让黑夜把它永远埋葬
 */
public class VideoAdapter extends BaseAdapter {
    private Context context;
    private List<VideoData> list;
    private int size ;
    public VideoAdapter(Context context, List<VideoData> list) {
        this.context = context;
        this.list = list;
    }
    public VideoAdapter(Context context, List<VideoData> list, int size) {
        this.context = context;
        this.list = list;
        this.size = size;
    }

    @Override
    public int getCount() {
        if (size!=0){
            return size;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_classes, null);
            viewHolder = new ViewHolder();
            viewHolder.videoTitle = convertView.findViewById(R.id.tv_classes_title);
            viewHolder.videoImg = convertView.findViewById(R.id.iv_class_des);
            viewHolder.videoIntroduction = convertView.findViewById(R.id.tv_classes_description);
            viewHolder.uploadTime = convertView.findViewById(R.id.tv_pubdate);
            viewHolder.isVipVideo = convertView.findViewById(R.id.imv_vip);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.videoTitle.setText(list.get(position).getVideoTitle());
        Glide.with(context).load(list.get(position).getVideoImgUrlString()).into(viewHolder.videoImg);
        viewHolder.videoIntroduction.setText(list.get(position).getVideoIntroduction());
        viewHolder.uploadTime.setText(list.get(position).getUploadTime());
        if (list.get(position).isVipVideo()) {
            viewHolder.isVipVideo.setVisibility(View.VISIBLE);
        } else {
            viewHolder.isVipVideo.setVisibility(View.INVISIBLE);
        }

        return convertView;

    }

   private static class ViewHolder {
        ImageView videoImg;
        TextView videoTitle;
        TextView videoIntroduction;
        ImageView isVipVideo;
        TextView uploadTime;
    }
}

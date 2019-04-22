package com.prismk.japaneseelearn.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.bean.UserData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class TeacherListAdapter extends BaseAdapter {
    private Context context;
    private List<UserData> list;

    public TeacherListAdapter(Context context, List<UserData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_teacherlist, null);
            viewHolder = new ViewHolder();
            viewHolder.avatar = convertView.findViewById(R.id.civ_teacher_avatar);
            viewHolder.teacherName = convertView.findViewById(R.id.tv_teacher_name);
            viewHolder.teacehrSign = convertView.findViewById(R.id.tv_teacher_sign);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(list.get(position).getHeadImgUrlString()).into(viewHolder.avatar);
        viewHolder.teacherName.setText(list.get(position).getNickName().trim());
        viewHolder.teacehrSign.setText(list.get(position).getSign().trim());
        return convertView;
    }

    private static class ViewHolder {
        CircleImageView avatar;
        TextView teacherName;
        TextView teacehrSign;
    }
}

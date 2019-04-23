package com.prismk.japaneseelearn.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.bean.ArticleData;

import org.w3c.dom.Text;

import java.util.List;

/**
 * create by Nevermore
 * 2019/4/23
 * 你不愿回忆的过去已经死亡，就让黑夜把它永远埋葬
 */
public class ArticleAdapter extends BaseAdapter {
    private List<ArticleData> list;
    private Context context;

    public ArticleAdapter(List<ArticleData> list, Context context) {
        this.list = list;
        this.context = context;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_article, null);
            viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.tv_article_title);
            viewHolder.content = convertView.findViewById(R.id.tv_article_content);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(list.get(position).getTitle());
        viewHolder.content.setText(list.get(position).getContext());
        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        TextView content;
    }
}

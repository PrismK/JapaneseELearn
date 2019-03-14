package com.prismk.japaneseelearn.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.prismk.japaneseelearn.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * ================================================
 * 作    者：prism（棱镜）Github地址：https://github.com/prismk/
 * 本地用户：18340
 * 版    本：1.0
 * 创建日期：2018/12/16
 * 描    述：
 * 修订历史：
 * ================================================
 */

public class BaseRecyclerAdapter extends RecyclerView.Adapter<BaseRecyclerAdapter.MyViewHolder> {
    private static final String TAG = "MyRecyclerAdapter";

    private List<String> mData;
    private Context mContext;
    private LayoutInflater inflater;

    public BaseRecyclerAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.mData = data;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onViewRecycled(MyViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //先设置图片占位符
        holder.imageView.setImageDrawable(mContext.getDrawable(R.mipmap.ic_launcher));
        final String url = mData.get(position);
        //为imageView设置Tag,内容是该imageView等待加载的图片url
        holder.imageView.setTag(url);
        @SuppressLint("StaticFieldLeak")
        AsyncTask asyncTask = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                try {
                    URL url = new URL(mData.get(position));
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                    return bitmap;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                //加载完毕后判断该imageView等待的图片url是不是加载完毕的这张
                //如果是则为imageView设置图片,否则说明imageView已经被重用到其他item
                if(url.equals(holder.imageView.getTag())) {
                    holder.imageView.setImageBitmap(bitmap);
                }
            }
        }.execute();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_baserecycler, parent, false);
        return new MyViewHolder(view);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imv_image);
        }

    }
}

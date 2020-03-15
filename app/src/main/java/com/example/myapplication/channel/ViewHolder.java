package com.example.myapplication.channel;


import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    public ViewHolder(View itemView) {
        super(itemView);
        mViews=new SparseArray();
    }

    /**
     * 获取View方法
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T getView(@IdRes int id){
        View view= mViews.get(id);

        if(view==null) {
            view =itemView.findViewById(id);
            mViews.put(id,view);
        }

        return (T) view;
    }

    /**
     * 设置文本
     */
    public ViewHolder setText(@IdRes int id, CharSequence text){
        TextView textView = getView(id);
        if(TextUtils.isEmpty(text)){
            textView.setText("");
        }else{
            textView.setText(text);
        }

        return this;
    }



    /**
     * 设置图片
     */
    public ViewHolder setImageResource(@IdRes int id, @DrawableRes int resId){
        ImageView view = getView(id);
        view.setImageResource(resId);
        return this;
    }

    /**
     * 设置网络图片
     */

    public ViewHolder setImage(@IdRes int id, ImageLoader loader){
        ImageView view = getView(id);
        loader.loadImage(view,loader.mPath);
        return this;
    }

    /**
     * 解耦图片处理
     */
    public abstract static class ImageLoader{
        String mPath;
        public ImageLoader(String path){
            this.mPath=path;
        }

        protected abstract void loadImage(ImageView imageView, String path);
    }
}

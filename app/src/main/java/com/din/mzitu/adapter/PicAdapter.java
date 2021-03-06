package com.din.mzitu.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.din.mzitu.R;
import com.din.mzitu.base.BaseAdapter;
import com.din.mzitu.bean.PicBean;
import com.din.mzitu.utill.GlideApp;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PicAdapter extends BaseAdapter {

    public PicAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    protected int layoutContentID() {
        return R.layout.rv_post_single;
    }

    @Override
    protected void setBindViewHolderData(ViewHolder viewHolder, int position) {
        PicBean picBean = (PicBean) beans.get(position);
        TextView title = viewHolder.get(R.id.text);
        final ImageView imageView = viewHolder.get(R.id.image);
        title.setText(picBean.getTitle());
        String header = "";
        if (picBean.getType() == TYPE_MZITU) {
            header = HEADER_MZITU;
        } else if (picBean.getType() == TYPE_LIGUI) {
            header = HEADER_LIGUI;
        }

        String tag = (String) imageView.getTag(R.id.image_url);
        String url = picBean.getImage();

        // 防盗链，图片会加载失败，需要更换请求头
        GlideUrl glideUrl = new GlideUrl(url == tag ? tag : url, header(header));
        if (tag == url) {
            GlideApp.with(fragment)
                    .load(glideUrl)
                    .thumbnail(0.1f)
                    .centerCrop()
                    .skipMemoryCache(false)
                    .transition(withCrossFade())
                    .into(imageView);
        } else {
            GlideApp.with(fragment)
                    .asBitmap()
                    .load(glideUrl)
                    .centerCrop()
                    .skipMemoryCache(false)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder_error)
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageView.setImageBitmap(resource);
                        }
                    });
            imageView.setTag(R.id.image_url, url);
        }
    }
}
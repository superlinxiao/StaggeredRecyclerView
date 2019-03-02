package com.wuxiaolong.pullloadmorerecyclerviewsample;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.List;

/**
 * Created by WuXiaolong on 2015/9/14.
 */
public class StaggeredRecycleViewAdapter extends RecyclerView.Adapter<StaggeredRecycleViewAdapter.ViewHolder> {

    private Context mContext;
    private List<String> dataList;
    private final float screenWidth;

    public StaggeredRecycleViewAdapter(Context context, List<String> dataList) {
        this.dataList = dataList;
        mContext = context;
        screenWidth = DeviceUtil.getScreenWidth(mContext) / 2;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SimpleDraweeView simpleDraweeView =
                (SimpleDraweeView) LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        return new ViewHolder(simpleDraweeView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String String = dataList.get(position);
        Uri uri = Uri.parse(String);
        final SimpleDraweeView baseProgressView = (SimpleDraweeView) holder.itemView;
        baseProgressView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        //重置view的大小，防止recyclerView计算出错
        ViewGroup.LayoutParams layoutParams = baseProgressView.getLayoutParams();
        layoutParams.width = (int) screenWidth;
        layoutParams.height = (int) screenWidth;
        baseProgressView.setLayoutParams(layoutParams);
        baseProgressView.requestLayout();

        ControllerListener<ImageInfo> controllerListener =
                new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(
                            String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                        if (imageInfo == null) {
                            return;
                        }
                        //设置图片真实大小
                        ViewGroup.LayoutParams layoutParams = baseProgressView.getLayoutParams();
                        int width = imageInfo.getWidth();
                        int height = imageInfo.getHeight();
                        layoutParams.width = (int) screenWidth;
                        layoutParams.height = (int) (height * screenWidth / width);
                        baseProgressView.setLayoutParams(layoutParams);
                        baseProgressView.requestLayout();
                    }

                    @Override
                    public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        //图片12是加载不出来的
                        Log.i("onFailure:", throwable == null ? "null" : throwable.toString());
                    }
                };

        DraweeController draweeController =
                Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setControllerListener(controllerListener)
                        .setAutoPlayAnimations(false) // 设置加载图片完成后是否直接进行播放
                        .build();

        baseProgressView.setController(draweeController);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

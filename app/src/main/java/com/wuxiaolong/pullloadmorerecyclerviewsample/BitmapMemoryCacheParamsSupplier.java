package com.wuxiaolong.pullloadmorerecyclerviewsample;

import android.app.ActivityManager;
import android.os.Build;

import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.imagepipeline.cache.MemoryCacheParams;

/**
 * 内存缓存配置 参考:https://github.com/facebook/fresco/issues/738 Description Author lizheng Create Data
 * 2018\7\9 0009
 */
public class BitmapMemoryCacheParamsSupplier implements Supplier<MemoryCacheParams> {

  private final ActivityManager mActivityManager;

  BitmapMemoryCacheParamsSupplier(ActivityManager activityManager) {
    mActivityManager = activityManager;
  }

  @Override
  public MemoryCacheParams get() {
    int maxCacheSize = getMaxCacheSize();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      return new MemoryCacheParams(
          maxCacheSize, // 内存缓存中总图片的最大大小,以字节为单位。
          56, // 内存缓存中图片的最大数量。
          maxCacheSize, // 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
          Integer.MAX_VALUE, // 内存缓存中准备清除的总图片的最大数量。
          Integer.MAX_VALUE); // 内存缓存中单个图片的最大大小。
    } else {
      return new MemoryCacheParams(
          maxCacheSize, 256, maxCacheSize, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
  }

  private int getMaxCacheSize() {
    final int maxMemory =
        Math.min(mActivityManager.getMemoryClass() * ByteConstants.MB, Integer.MAX_VALUE);
    if (maxMemory < 32 * ByteConstants.MB) {
      return 4 * ByteConstants.MB;
    } else if (maxMemory < 64 * ByteConstants.MB) {
      return 6 * ByteConstants.MB;
    } else {
      return maxMemory / 4;
    }
  }
}

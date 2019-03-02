package com.wuxiaolong.pullloadmorerecyclerviewsample;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.util.ByteConstants;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;

import java.io.File;

/**
 * Description fresco config Author lizheng 参考：https://www.jianshu.com/p/8ff81be83101 Create Data
 * 2018\7\9 0009
 */
class ImageLoaderConfig {

  private static final String IMAGE_PIPELINE_CACHE_DIR = "image_cache";

  private static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "image_small_cache";

  private static final int MAX_DISK_SMALL_CACHE_SIZE = 10 * ByteConstants.MB;

  private static final int MAX_DISK_SMALL_LOW_SPACE_CACHE_SIZE = 5 * ByteConstants.MB;

  private ImagePipelineConfig sImagePipelineConfig;

  /** Creates config using android http stack as network backend. */
  ImagePipelineConfig getImagePipelineConfig(final Context context) {
    if (sImagePipelineConfig == null) {
      File fileCacheDir = context.getApplicationContext().getCacheDir();
      DiskCacheConfig mainDiskCacheConfig =
          DiskCacheConfig.newBuilder(context)
              .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
              .setBaseDirectoryPath(fileCacheDir)
              .build();

      DiskCacheConfig smallDiskCacheConfig =
          DiskCacheConfig.newBuilder(context)
              .setBaseDirectoryPath(fileCacheDir)
              .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)
              .setMaxCacheSize(MAX_DISK_SMALL_CACHE_SIZE)
              .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_SMALL_LOW_SPACE_CACHE_SIZE)
              .build();
      // 当内存紧张时采取的措施
      sImagePipelineConfig =
          ImagePipelineConfig.newBuilder(context)
              .setBitmapsConfig(Bitmap.Config.RGB_565) // 若不是要求忒高清显示应用，就用使用RGB_565吧（默认是ARGB_8888)
              .setDownsampleEnabled(true) // 在解码时改变图片的大小，支持PNG、JPG以及WEBP格式的图片，与ResizeOptions配合使
              // 设置Jpeg格式的图片支持渐进式显示
              .setProgressiveJpegConfig(
                  new ProgressiveJpegConfig() {
                    @Override
                    public int getNextScanNumberToDecode(int scanNumber) {
                      return scanNumber + 2;
                    }

                    public QualityInfo getQualityInfo(int scanNumber) {
                      boolean isGoodEnough = (scanNumber >= 5);
                      return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
                    }
                  })
              // 设置内存配置
              .setBitmapMemoryCacheParamsSupplier(
                  new BitmapMemoryCacheParamsSupplier(
                      (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)))
              .setMainDiskCacheConfig(mainDiskCacheConfig) // 设置主磁盘配置
              .setSmallImageDiskCacheConfig(smallDiskCacheConfig) // 设置小图的磁盘配置
              .setResizeAndRotateEnabledForNetwork(true) // 调整和旋转是否支持网络图片,可以对网络图片进行resize处理，减少内存消耗
              .build();
    }
    return sImagePipelineConfig;
  }
}

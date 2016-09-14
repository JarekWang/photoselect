package com.jarek.imageselect;

import android.app.Application;
import android.content.Context;

import com.jarek.imageselect.core.SDCardStoragePath;
import com.jarek.imageselect.utils.SDCardUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;
import java.io.IOException;

/**
 * 　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　 ████━████     ┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　 　 ┗━━━┓
 * 　　　　　　　　　┃ 神兽保佑　　 ┣┓
 * 　　　　　　　　　┃ 代码无BUG   ┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
 * Created by 王健(Jarek) on 2016/9/12.
 */
public class ImageSelectApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(this);
    }

    /**
     * <br>
     * This configuration tuning is custom. You can tune every option, you may
     * tune some of them, or you can create default configuration by
     * ImageLoaderConfiguration.createDefault(this); method. </br>
     *
     * @param context Context
     */
    private void initImageLoader(Context context) {
        try {
            SDCardUtils.createFolder(SDCardStoragePath.DEFAULT_IMAGE_CACHE_PATH);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCache(new UnlimitedDiskCache(new File(SDCardStoragePath.DEFAULT_IMAGE_CACHE_PATH)))
//				.discCache(DiscCacheFactory.create(context, SDCardStoragePath.DEFAULT_IMAGE_CACHE_PATH))
                .memoryCacheSizePercentage(8)
                .memoryCacheExtraOptions(480, 800)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .imageDownloader(new BaseImageDownloader(context))
                .writeDebugLogs()
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}

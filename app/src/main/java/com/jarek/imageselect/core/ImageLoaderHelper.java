package com.jarek.imageselect.core;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nostra13.universalimageloader.core.DisplayImageOptions;


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
 *
 * 图片加载ImageLoader 的option配置
 * Created by 王健(Jarek) on 2016/9/12.
 */
public class ImageLoaderHelper {

    
    /**
     * <li> 默认option配置 </li>,可设置默认显示图片
     */
    public static DisplayImageOptions buildDisplayImageOptionsDefault(int drawable) {
    	BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1024*2;
        return new DisplayImageOptions.Builder()
        		.showImageOnLoading(drawable)
        		.showImageForEmptyUri(drawable)
        		.showImageOnFail(drawable)
        		.decodingOptions(options)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

	/**
	 * <li> 默认option配置 </li>
	 */
	public static DisplayImageOptions buildDisplayImageOptionsPager() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		return new DisplayImageOptions.Builder()
				.decodingOptions(options)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	}

}

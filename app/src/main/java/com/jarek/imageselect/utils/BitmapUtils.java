
package com.jarek.imageselect.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jarek.imageselect.core.SDCardStoragePath;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 *  　　　　　　　　┏┓　　　┏┓
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
 * 图片压缩类
 * Created by 王健(Jarek) on 2016/9/12.
 */
public class BitmapUtils {

    /**
     * <li> 压缩图片 </li>
     */
    public static File commpressImage(String filePath) {
        try {
            Bitmap bitmap = getSmallBitmap(filePath);
            File file = new File(SDCardStoragePath.DEFAULT_IMAGE_CACHE_PATH + "/" + UUID.randomUUID().toString()
                    + ".jpeg");
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out)) {
                out.flush();
                out.close();
            }
            file.length();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * <li> 计算图片的缩放值 </li>
     * @param options options
     * @param reqWidth 宽
     * @param reqHeight 高
     * @return inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * <li> 根据路径获得图片并压缩，返回bitmap </li>
     * @param filePath filePath
     * @return Bitmap
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 200, 200);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }
}

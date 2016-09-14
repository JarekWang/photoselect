package com.jarek.imageselect.utils;
/**
 * sdcard工具类
 */
import android.os.Environment;

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
 *
 * 文件工具类
 * Created by 王健(Jarek) on 2016/9/12.
 */
public class SDCardUtils {
	/**
	 * 创建文件夹
	 * @param folderPath folderPath
	 * @throws IOException
	 */
	public static boolean createFolder(String folderPath) throws IOException {
		if (!isSDCardMounted()) {
            return false;
        }
		File folder = new File(folderPath);
		if(!folder.exists()) {
			 return folder.mkdirs();
		}
		return true;
	}

	/**
	 * sdcard是否挂载
	 */
	public static boolean isSDCardMounted() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
}

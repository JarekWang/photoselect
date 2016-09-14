package com.jarek.imageselect.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarek.imageselect.R;
import com.jarek.imageselect.bean.ImageFolderBean;
import com.jarek.imageselect.core.ImageSelectObservable;
import com.jarek.library.TitleView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

import java.util.ArrayList;
import java.util.List;



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
 * 预览图片
 * Created by 王健(Jarek) on 2016/9/12.
 */
public class PreviewImageActivity extends Activity implements OnClickListener {
	/** 显示/隐藏 过程持续时间 */
	private static final int SHOW_HIDE_CONTROL_ANIMATION_TIME = 500;
	private ViewPager mPhotoPager;

	/**标题栏*/
	private TitleView mTitleView;
	/**选择按钮*/
	private TextView mCheckedTv;
	/**控制显示、隐藏顶部标题栏*/
	private boolean isHeadViewShow = true;
	private View mFooterView;

	/**需要预览的所有图片*/
	private List<ImageFolderBean> mAllImage;
	/**x选择的所有图片*/
	private List<ImageFolderBean> mSelectImage;

	/**
	 * 预览文件夹下所有图片
	 * @param activity Activity
	 * @param position position 当前显示位置
	 * @param requestCode requestCode
     */
	public static void startPreviewPhotoActivityForResult (Activity activity, int position, int requestCode) {
		Intent intent = new Intent(activity, PreviewImageActivity.class);
		intent.putExtra("position", position);
		activity.startActivityForResult(intent, requestCode);
		activity.overridePendingTransition(R.anim.common_scale_small_to_large, 0);
	}

	/**
	 * 预览选择的图片
	 * @param activity Activity
	 * @param requestCode requestCode
     */
	public static void startPreviewActivity (Activity activity, int requestCode) {
		Intent intent = new Intent(activity, PreviewImageActivity.class);
		intent.putExtra("preview", true);
		activity.startActivityForResult(intent, requestCode);
		activity.overridePendingTransition(R.anim.common_scale_small_to_large, 0);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preview_image_activity);

		initImages();

		initView();

		initAdapter();

		/*全屏*/
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}

	/**
	 * 初始化图片数组
	 */
	private void initImages () {
		mAllImage = new ArrayList<>();
		mSelectImage = ImageSelectObservable.getInstance().getSelectImages();

		if (getIntent().getBooleanExtra("preview", false)) {
			mAllImage.addAll(ImageSelectObservable.getInstance().getSelectImages());
		} else {
			mAllImage.addAll(ImageSelectObservable.getInstance().getFolderAllImages());
		}
	}

	/**初始化控件*/
	private void initView() {
		/*标题栏*/
		mTitleView = (TitleView) findViewById(R.id.tv_large_image);
		mTitleView.getLeftBackImageTv().setOnClickListener(this);
		String title = getIntent().getIntExtra("position", 1) + "/" + mAllImage.size();
		mTitleView.getTitleTv().setText(title);
		mTitleView.getRightTextTv().setOnClickListener(this);
		mTitleView.getLeftBackImageTv().setOnClickListener(this);

		/*底部菜单栏*/
		mFooterView = findViewById(R.id.rl_check);
		mCheckedTv = (TextView) findViewById(R.id.ctv_check);
		mCheckedTv.setEnabled(mAllImage.get(getIntent().getIntExtra("position", 0)).selectPosition > 0);
		mFooterView.setOnClickListener(this);

		mPhotoPager = (ViewPager) findViewById(R.id.vp_preview);
	}

	/**
	 * 更新选择的顺序
	 */
	private void subSelectPosition () {
		for (int index = 0, len = mSelectImage.size(); index < len; index ++) {
			ImageFolderBean folderBean = mSelectImage.get(index);
			folderBean.selectPosition = index + 1;
		}
	}

	/**
	 * adapter的初始化
	 */
	private void initAdapter() {
		mPhotoPager = (ViewPager) findViewById(R.id.vp_preview);
		PreviewAdapter  previewAdapter = new PreviewAdapter(mAllImage);
		mPhotoPager.setAdapter(previewAdapter);
		mPhotoPager.setPageMargin(5);
		mPhotoPager.setCurrentItem(getIntent().getIntExtra("position", 0));

		mPhotoPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				String text = (arg0 + 1) + "/" + mAllImage.size();
				mTitleView.getTitleTv().setText(text);
				mCheckedTv.setEnabled(mSelectImage.contains(mAllImage.get(arg0)));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}


	/**
	 * 简单的适配器
	 */
	class PreviewAdapter extends PagerAdapter {
		private List<ImageFolderBean> photos;

		public PreviewAdapter(List<ImageFolderBean> photoList) {
			super();
			this.photos = photoList;
		}

		@Override
		public int getCount() {
			return photos.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			LayoutInflater inflater = LayoutInflater.from(PreviewImageActivity.this);
			View view = inflater.inflate(R.layout.preview_image_item, container, false);
			ImageView bigPhotoIv = (ImageView) view.findViewById(R.id.iv_image_item);
			bigPhotoIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isHeadViewShow) {
						hideControls();
					} else {
						showControls();
					}
				}
			});

			ImageLoader.getInstance().displayImage(Scheme.FILE.wrap(photos.get(position).path), bigPhotoIv);
			container.addView(view);
			return view;
		}

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right_text:
			onBackPressed();

			break;
			case R.id.rl_check:
				addOrRemoveImage();
				break;
			case R.id.iv_left_image:
				onBackPressed();
				break;
		}
	}

	/**
	 * 添加或者删除当前操作的图片
	 */
	private void addOrRemoveImage () {
		ImageFolderBean imageBean = mAllImage.get(mPhotoPager.getCurrentItem());

		if (mSelectImage.contains(imageBean)) {
			mSelectImage.remove(imageBean);
			subSelectPosition();
			mCheckedTv.setEnabled(false);
		} else {
			mSelectImage.add(imageBean);
			imageBean.selectPosition = mSelectImage.size();
			mCheckedTv.setEnabled(true);
		}
	}

	/**
	 * <br>显示顶部，底部view动画 </br>
	 */
	private void showControls() {
		AlphaAnimation animation = new AlphaAnimation(0f, 1f);
		animation.setFillAfter(true);
		animation.setDuration(SHOW_HIDE_CONTROL_ANIMATION_TIME);
		isHeadViewShow = true;
		mTitleView.startAnimation(animation);
		mTitleView.setVisibility(View.VISIBLE);
		mFooterView.startAnimation(animation);
		mFooterView.setVisibility(View.VISIBLE);
	}

	/**
	 * <br> 隐藏顶部，底部view 动画</br>
	 */
	private void hideControls() {
		AlphaAnimation animation = new AlphaAnimation(1f, 0f);
		animation.setFillAfter(true);
		animation.setDuration(SHOW_HIDE_CONTROL_ANIMATION_TIME);
		isHeadViewShow = false;
		mTitleView.startAnimation(animation);
		mTitleView.setVisibility(View.GONE);

		mFooterView.startAnimation(animation);
		mFooterView.setVisibility(View.GONE);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		ImageSelectObservable.getInstance().updateImageSelectChanged();
		overridePendingTransition(0, R.anim.common_scale_large_to_small);
	}
}

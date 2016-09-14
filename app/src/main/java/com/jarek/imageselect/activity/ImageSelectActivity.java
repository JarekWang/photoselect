package com.jarek.imageselect.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.jarek.imageselect.R;
import com.jarek.imageselect.adapter.ImageGridApter;
import com.jarek.imageselect.bean.ImageFolderBean;
import com.jarek.imageselect.core.ImageSelectObservable;
import com.jarek.imageselect.listener.OnRecyclerViewClickListener;
import com.jarek.imageselect.utils.ImageUtils;
import com.jarek.library.TitleView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;


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
 * 系统相册选择
 * Created by 王健(Jarek) on 2016/9/12.
 */
public class ImageSelectActivity extends Activity implements Callback, OnClickListener, OnRecyclerViewClickListener, Observer {

    public static void startPhotoSelectGridActivity(Activity activity, String folder, boolean singleSelect, int maxCount, int requestCode) {
        Intent intent = new Intent(activity, ImageSelectActivity.class);
        intent.putExtra("data", folder);
        intent.putExtra("single", singleSelect);
        intent.putExtra("maxCount", maxCount);
        activity.startActivityForResult(intent, requestCode);
    }

    private final int REQUEST_PREVIEW_PHOTO = 10;

    /**
     * 返回消息what
     */
    private final int MSG_PHOTO = 11;

    /**
     * 图片选择适配器
     */
    private ImageGridApter mAdapter;

    private Handler mHandler;

    /**
     * 确定
     */
    private TextView mOkTv;

    private boolean mIsSelectSingleImge;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.photo_gridview_main);
        ImageSelectObservable.getInstance().addObserver(this);
        mHandler = new Handler(this);
        mIsSelectSingleImge = getIntent().getBooleanExtra("single", false);
        initView();
        initData();
        if (mIsSelectSingleImge) {
            findViewById(R.id.ll_photo_operation).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        ImageLoader.getInstance().clearMemoryCache();
        super.onDestroy();
    }

    /**
     * <li>初始化数据</li>
     */
    private void initData() {
        ImageUtils.queryGalleryPicture(this, getIntent().getStringExtra("data"), mHandler, MSG_PHOTO);

    }

    /**
     * <li>初始化view</li>
     */
    private void initView() {
        TitleView titleView = (TitleView) findViewById(R.id.tv_photo_title);
        titleView.getLeftBackImageTv().setOnClickListener(this);

        /*这里直接设置表格布局，三列*/
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lv_photo_folder);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        mAdapter = new ImageGridApter(this, ImageSelectObservable.getInstance().getFolderAllImages(), mIsSelectSingleImge, getIntent().getIntExtra("maxCount", 9));
        mAdapter.setOnClickListener(this);
        recyclerView.setAdapter(mAdapter);

        mOkTv = (TextView) findViewById(R.id.tv_photo_ok);
        mOkTv.setText(String.format(getResources().getString(R.string.photo_ok), mAdapter.getSelectlist().size()));
        findViewById(R.id.tv_photo_scan).setOnClickListener(this);
        mOkTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_photo_scan:
                if (mAdapter.getSelectlist().size() <= 0) {
                    Toast.makeText(this, R.string.photo_no_select, Toast.LENGTH_SHORT).show();
                } else {
                    PreviewImageActivity.startPreviewActivity(this, REQUEST_PREVIEW_PHOTO);
                }
                break;
            case R.id.tv_photo_ok:
                setResult(RESULT_OK);
                this.finish();

                break;
            case R.id.iv_left_image:
                this.finish();
                break;
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PREVIEW_PHOTO) {
                mAdapter.notifyDataSetChanged();
                mOkTv.setText(getResources().getString(R.string.photo_ok, mAdapter.getSelectlist().size()));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_PHOTO:
                ImageSelectObservable.getInstance().addFolderImagesAndClearBefore((Collection<? extends ImageFolderBean>) msg.obj);
                mAdapter.notifyDataSetChanged();
                break;
        }
        return false;
    }


    @Override
    public void onItemClick(View v, int position) {
        if (mIsSelectSingleImge) {
            setResult(RESULT_OK);
            this.finish();
            return;
        }

        if (position >= 0) {
            PreviewImageActivity.startPreviewPhotoActivityForResult(this, position, REQUEST_PREVIEW_PHOTO);
        }
        mOkTv.setText(getResources().getString(R.string.photo_ok, mAdapter.getSelectlist().size()));

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void update(Observable o, Object arg) {
        mAdapter.notifyDataSetChanged();
        mOkTv.setText(getResources().getString(R.string.photo_ok, mAdapter.getSelectlist().size()));
    }
}

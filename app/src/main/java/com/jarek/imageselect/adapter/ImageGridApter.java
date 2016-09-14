 package com.jarek.imageselect.adapter;

 import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
 import android.widget.Toast;

 import com.jarek.imageselect.R;
import com.jarek.imageselect.bean.ImageFolderBean;
import com.jarek.imageselect.core.AnimateFirstDisplayListener;
import com.jarek.imageselect.core.ImageLoaderHelper;
import com.jarek.imageselect.core.ImageSelectObservable;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

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
  * 图片选择适配器
  * Created by 王健(Jarek) on 2016/9/12.
  */
 public class ImageGridApter extends BaseRecyclerAdapter<ImageFolderBean, RecyclerView.ViewHolder> {

     /**
      * 标注是否是单选图片模式
      */
     private boolean mIsSelectSingleImge;

     /**
      * 已选图片列表
      */
     private List<ImageFolderBean> mSelectlist;

     private int maxCount;


     public ImageGridApter(Context context, List<ImageFolderBean> list, boolean isSelectSingleImge, int maxCount){
         super(context, list);
         this.mIsSelectSingleImge = isSelectSingleImge;
         this.maxCount = maxCount;

         displayListener = new AnimateFirstDisplayListener();

         mSelectlist = ImageSelectObservable.getInstance().getSelectImages();
     }

     @Override
     public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View view = mInflater.inflate(R.layout.photo_grid_item, parent, false);
         return new GridViewHolder(view);
     }

     @Override
     public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
         final GridViewHolder viewHolder = (GridViewHolder) holder;
         ImageFolderBean imageBean = list.get(position);
         imageBean.position = viewHolder.getAdapterPosition();

         notifyImageChanged(viewHolder.picIv, imageBean);
         nitifyCheckChanged(viewHolder, imageBean);

         /*点击监听*/
         setSelectOnClickListener(viewHolder.selectView, imageBean, viewHolder.getAdapterPosition());
         setOnItemClickListener(viewHolder.mCardView, viewHolder.getAdapterPosition());

     }

     /**
      * 图片加载
      * @param imageView ImageView
      * @param imageBean ImageFolderBean
      */
     private void notifyImageChanged (ImageView imageView, ImageFolderBean imageBean) {
         imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
         ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(imageBean.path), imageView, ImageLoaderHelper.buildDisplayImageOptionsDefault(R.drawable.defaultpic), displayListener);
     }

     /**
      * 选择按钮更新
      * @param viewHolder GridViewHolder
      * @param imageBean ImageFolderBean
      */
     private void nitifyCheckChanged (GridViewHolder viewHolder, ImageFolderBean imageBean) {
         if (mIsSelectSingleImge) { //单选模式，不显示选择按钮
             viewHolder.checked.setVisibility(View.INVISIBLE);
         } else {
             if (mSelectlist.contains(imageBean)) {  //当已选列表里包括当前item时，选择状态为已选，并显示在选择列表里的位置
                 viewHolder.checked.setEnabled(true);
                 viewHolder.checked.setText(String.valueOf(imageBean.selectPosition));
                 viewHolder.forgroundIv.setVisibility(View.VISIBLE);
             } else {
                 viewHolder.checked.setEnabled(false);
                 viewHolder.checked.setText("");
                 viewHolder.forgroundIv.setVisibility(View.GONE);
             }
         }
     }


     /**
      * 选择按钮点击监听
      * @param view 点击view
      * @param imageBean 对应的实体类
      * @param position 点击位置
      */
     private void setSelectOnClickListener(View view, final ImageFolderBean imageBean, final int position) {

         View.OnClickListener listener = new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (mSelectlist.contains(imageBean)) { //点击的item为已选过的图片时，删除
                     mSelectlist.remove(imageBean);
                     subSelectPosition();
                 } else { //不在选择列表里，添加
                     if (mSelectlist.size() >= maxCount) {
                         Toast.makeText(mContext, mContext.getResources().getString(R.string.publish_select_photo_max, maxCount), Toast.LENGTH_SHORT).show();
                         return;
                     }
                     mSelectlist.add(imageBean);
                     imageBean.selectPosition = mSelectlist.size();
                 }

                 //通知点击项发生了改变
                 notifyItemChanged(position);

                 if (mOnClickListener != null) { //回调，页面需要展示选择的图片张数
                     mOnClickListener.onItemClick(v, -1);
                 }
             }
         };

         view.setOnClickListener(listener);

     }

     /**
      * item点击监听，多选时查看大图，单选时返回选择图片
      * @param view 点击view
      * @param position 点击位置
      */
     private void setOnItemClickListener (View view, final int position) {
         View.OnClickListener listener = new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (mOnClickListener != null) {
                     if (mIsSelectSingleImge) {
                         mSelectlist.add(list.get(position));
                     }
                     mOnClickListener.onItemClick(v, position);
                 }
             }
         };
         view.setOnClickListener(listener);
     }


     /**
      * 更新选择的顺序
      */
     private void subSelectPosition () {
         for (int index = 0, len = mSelectlist.size(); index < len; index ++) {
             ImageFolderBean folderBean = mSelectlist.get(index);
             folderBean.selectPosition = index + 1;
             notifyItemChanged(folderBean.position);
         }

     }

     /**
      * 所有选择的图片
      * @return List<ImageFolderBean>
      */
     public List<ImageFolderBean> getSelectlist() {
         return mSelectlist;
     }

     private class GridViewHolder extends RecyclerView.ViewHolder{
         public View containerView;
         public ImageView picIv;
         public ImageView forgroundIv;
         public TextView checked;
         public TextView selectView;
         public CardView mCardView;

         public GridViewHolder(View convertView) {
             super(convertView);

             containerView = convertView.findViewById(R.id.main_frame_layout);
             picIv = (ImageView) convertView.findViewById(R.id.iv_pic);
             checked = (TextView) convertView.findViewById(R.id.tv_select);
             forgroundIv = (ImageView) convertView.findViewById(R.id.iv_forgound);
             mCardView = (CardView) convertView.findViewById(R.id.card_view);
             selectView = (TextView) convertView.findViewById(R.id.tv_select_v);
         }
     }
 }

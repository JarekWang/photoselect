# 图片选择器<br>

 仿QQ本地图片选择，包括单选，多选时图片顺序标注，效果图如下：<br><br>


![image](https://github.com/JarekWang/photoselect/blob/master/app/screenshot/GIF2.gif)


```java
    /**多选*/
    public void onMultiClick(View view) {
        /*参数对应context, 回调code, 传入的图片List, 可选的最大张数*/
        FolderListActivity.startFolderListActivity(this, 1, null, 9);
    }

    /**单选*/
    public void onSingleClick(View view) {
        /*单选，参数对应的是context, 回调*/
        FolderListActivity.startSelectSingleImgActivity(this, 2);
    }
```

<br>

在Activity的onActivityResult中接收返回的图片数据：
```java
  List<ImageFolderBean> list = (List<ImageFolderBean>) data.getSerializableExtra("list");
```

<br>

有反应内存溢出的，一直没时间来改，有的朋友已经讲了，在onDestory里需要把注入的观察者对象移除就Ok了
```java
  ImageSelectObservable.getInstance().deleteObserver(this);
```

<br>
    另外项目里没有加权限申请，更新到23以后SDK需要自己加上权限申请。
<br>有朋友讲ImageLoader初始化有bug,需要context,这个已经在Application里初始化过，已经传入context,应该 是不需要再次传入context的
<br><br>
[我的博客](http://www.cnblogs.com/jarek/)
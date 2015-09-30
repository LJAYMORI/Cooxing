package com.ljaymori.cooxing.common;

import android.app.Application;
import android.content.Context;

public class CooxingApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
//        initImageLoader(this);
    }

    public static Context getContext() {
        return mContext;
    }

//    private void initImageLoader(Context context) {
//        // This configuration tuning is custom. You can tune every option, you may tune some of them,
//        // or you can create default configuration by
//        //  ImageLoaderConfiguration.createDefault(this);
//        // method.
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.image_placeholder)
//                .showImageForEmptyUri(R.drawable.image_placeholder)
//                .showImageOnFail(R.drawable.image_placeholder)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .build();
//
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                .threadPriority(Thread.NORM_PRIORITY - 1)
//                .denyCacheImageMultipleSizesInMemory()
//                .discCacheFileNameGenerator(new Md5FileNameGenerator())
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
////                .writeDebugLogs() // Remove for release app
//                .defaultDisplayImageOptions(options)
//                .imageDownloader(new HttpClientImageDownloader(context, NetworkManager.getInstance().getHttpClient()))
//                .build();
//        ImageLoader.getInstance().init(config);
//        ImageLoader.getInstance().setDefaultLoadingListener(new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                if (failReason.getType() == FailReason.FailType.OUT_OF_MEMORY && view != null) {
//                    System.gc();
//                    ImageLoader.getInstance().displayImage(imageUri, (ImageView) view);
//                }
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//
//            }
//        });
//    }

}

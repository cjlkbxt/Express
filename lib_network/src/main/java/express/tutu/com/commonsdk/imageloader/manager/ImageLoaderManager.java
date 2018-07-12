package express.tutu.com.commonsdk.imageloader.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


/**
 * Created by cjlkbxt on 2018/7/8/008.
 */

public class ImageLoaderManager {

    private static final int THREAD_COUNT = 4;
    private static final int PRIORITY = 2;
    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024;
    private static final int CONNECTION_TIME_OUT = 5 * 1000;
    private static final int READ_TIME_OUT = 30 * 1000;

    private static Context mContext;
    private static ImageLoader mImageLoader = null;

    private ImageLoaderManager(Context context){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPoolSize(THREAD_COUNT)
                .threadPriority(Thread.NORM_PRIORITY - PRIORITY)
                .denyCacheImageMultipleSizesInMemory()
                //.memoryCache(new UsingFreqLimitedMemoryCache(MEMORY_CACHE_SIZE))
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(DISK_CACHE_SIZE)
                //将保存的时候的URI名称用MD5 加密
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(getDefaultOptions())
                .imageDownloader(new BaseImageDownloader(context, CONNECTION_TIME_OUT, READ_TIME_OUT))
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);
        mImageLoader = ImageLoader.getInstance();
    }

    public static ImageLoaderManager getInstance(Context context){
        mContext = context;
        return ImageLoaderManager.SingletonHolder.sInstance;
    }
    //静态内部类
    private static class SingletonHolder{
        private static final ImageLoaderManager sInstance = new ImageLoaderManager(mContext);
    }

    private DisplayImageOptions getDefaultOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.drawable.ic_launcher_background)
//                .showImageOnFail(R.drawable.ic_launcher_background)
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中, 重要，否则图片不会缓存到内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中, 重要，否则图片不会缓存到硬盘中
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .decodingOptions(new BitmapFactory.Options())//设置图片的解码配置
//                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
//                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .build();
        return options;
    }

    //load the image
    public void displayImage(ImageView imageView, String url, DisplayImageOptions options, ImageLoadingListener listener) {
        if (mImageLoader != null) {
            mImageLoader.displayImage(url, imageView, options, listener);
        }
    }

    //load the image
    public void displayImage(ImageView imageView, String url, ImageLoadingListener listener) {
        displayImage(imageView, url ,null, listener);
    }

    public void displayImage(ImageView imageView, String url) {
        displayImage(imageView, url ,null, null);
    }
}

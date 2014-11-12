package com.sb.framework.http.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.sb.framework.SB;

public class ImageShowerUseVolly implements ImageMgmr.ImageDownloaderAndShower{

	static RequestQueue queue;
	public ImageShowerUseVolly(){
		if(queue == null) queue = Volley.newRequestQueue(SB.context);
	}
	@Override
	public void showImage(ImageView imageview, final String url, int loadingImage,
			int errorImage, OnImageDownloadCallback callback, int w, int h) 
	{
//		if(useOldFile){
//			String path = ImageFileUtils.getImagePath(url);
//			if(ImageFileUtils.isValid(path)){
//				//图片存在本地
//				///这里的代码一般运行不起来，因为这里是直接通过Volly下载图片，Volly下载完图片存在哪儿 ，我也不知道
//				//File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
//				//Volly的缓存在：context.getCacheDir()下的 private static final String DEFAULT_CACHE_DIR = "volley"目录
//				Bitmap bm = ImageDecoder.decodeBitmap(path, w, h, useMemoryCache);
//				imageview.setImageBitmap(bm);
//				return;
//			}
//		}
		//--- 下面采用Volly
		//RequestQueue mQueue = Volley.newRequestQueue(context);//, stack);  //这个不行，因为RequestQueue的创建是重量级的，没必要每次都new一个
		RequestQueue mQueue = queue;
		ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
		ImageListener listener = ImageLoader.getImageListener(imageview, loadingImage,errorImage); 
		imageLoader.get(url, listener, w, h);
		/**
		 * 实际上Volly还有一种方式加载网络图片：
		 * networkImageView.setDefaultImageResId(R.drawable.default_image);  
			networkImageView.setErrorImageResId(R.drawable.failed_image);  
			networkImageView.setImageUrl("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg",  
                imageLoader); 
                
              <com.android.volley.toolbox.NetworkImageView   
		        android:id="@+id/network_image_view"  
		        android:layout_width="200dp"  
		        android:layout_height="200dp"  
		        android:layout_gravity="center_horizontal"  
		        />
		         * 
		NetworkImageView中完全没有提供设置最大宽度和高度的方法，那么是不是使用NetworkImageView来加载的图片都不会进行压缩呢？
其实并不是这样的，NetworkImageView并不需要提供任何设置最大宽高的方法也能够对加载的图片进行压缩。这是由于NetworkImageView是一个控件，在加载图片的时候它会自动获取自身的宽高，然后对比网络图片的宽度，再决定是否需要对图片进行压缩。也就是说，压缩过程是在内部完全自动化的，并不需要我们关心，NetworkImageView会始终呈现给我们一张大小刚刚好的网络图片，不会多占用任何一点内存，这也是NetworkImageView最简单好用的一点吧。
当然了，如果你不想对图片进行压缩的话，其实也很简单，只需要在布局文件中把NetworkImageView的layout_width和layout_height都设置成wrap_content就可以了，这样NetworkImageView就会将该图片的原始大小展示出来，不会进行任何压缩。
		 */
		
	}
	
	public class BitmapCache implements ImageCache {  
		  
	    private LruCache<String, Bitmap> mCache;  
	  
	    public BitmapCache() {  
	        int maxSize = 10 * 1024 * 1024;  
	        mCache = new LruCache<String, Bitmap>(maxSize) {  
	            @Override  
	            protected int sizeOf(String key, Bitmap bitmap) {  
	                return bitmap.getRowBytes() * bitmap.getHeight();  
	            }  
	        };  
	    }  
	  
	    @Override  
	    public Bitmap getBitmap(String url) {  
	        return mCache.get(url);  
	    }  
	  
	    @Override  
	    public void putBitmap(String url, Bitmap bitmap) {  
	        mCache.put(url, bitmap);  
	    }  
	  
	}  

}

package com.sb.framework.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.sb.framework.R;
import com.sb.framework.http.image.ImageMgmr;
import com.sb.framework.http.image.ImageMgmr.ImageDownloaderAndShower;
import com.sb.framework.http.image.ImageShowerUseVolly;
import com.sb.framework.view.SBQuery;

public class ImageViewActivity extends Activity {

	public static void startTestImageViewOnNet(Context context, String url, boolean useCache, String useWhichThirdJar){
		Intent intent = new Intent(context, ImageViewActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("useCache", useCache);
		intent.putExtra("useWhichHttpComponent", useWhichThirdJar);
		context.startActivity(intent);
	}

	private String url;
	private boolean useCache;
	private String useWhichThirdJar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		
		url = getIntent().getStringExtra("url");
		useCache = getIntent().getBooleanExtra("useCache", false);
		useWhichThirdJar = getIntent().getStringExtra("useWhichHttpComponent");
		
		//--------------------------必须的，但是都有默认值--------------------------//
		ImageMgmr.configLocalCachePath(""); //默认：sb_image_utils目录
		ImageMgmr.configImageThreadCount(3); //默认2
		ImageMgmr.configImageNameGenerator(null); //默认使用服务器的文件名作为本地文件名
		ImageDownloaderAndShower d = null;
		if(useWhichThirdJar.equals("volly")){
			d = new ImageShowerUseVolly();
		}
		ImageMgmr.configDownloaderAndShower(d); //默认使用URLConnection的实验性实现
		//------------------------------------------------------------------------//
		
		RequestQueue mQueue = Volley.newRequestQueue(this);
		SBQuery q = new SBQuery(this);
		ImageView iv1 = (ImageView) q.id(R.id.iv1).getView();
		//final ImageView iv2 = (ImageView) q.id(R.id.iv2).getView();
		ImageMgmr.showImage(iv1, 
				url, 
				R.drawable.ic_launcher, 
				R.drawable.chat_edit_face_normal, 
				null, 
				0, 
				0, 
				useCache, //是否使用sd卡缓存
				true//是否使用内存缓存
				);
		
//		OnImageDownloadCallback callback = new OnImageDownloadCallback() {
//			
//			@Override
//			public void onStart() {
//				// TODO Auto-generated method stub
//				System.out.println("开始下载。。。");
//			}
//			
//			@Override
//			public void onOK(String path) {
//				// TODO Auto-generated method stub
//				Bitmap bm = ImageDecoder.decodeBitmap(path, 720, 600, true);
//				iv2.setImageBitmap(bm);
//			}
//			
//			@Override
//			public void onLoading(int progress, int total) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onFuck(String errorInfo) {
//				// TODO Auto-generated method stub
//				
//			}
//		};
//		ImageMgmr.download(url2, callback, true);
		
		q.id(R.id.btn_clean).clicked(this, "clean");
	}

	public void clean(){
		ImageMgmr.cleanCacheInSD();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_image_view, menu);
		return true;
	}

}

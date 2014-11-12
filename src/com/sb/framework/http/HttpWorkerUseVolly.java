package com.sb.framework.http;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sb.framework.SB;
import com.sb.framework.http.HttpHelper;
import com.sb.framework.http.SBHttpWorker;
import com.sb.framework.http.SBRequest;
import com.sb.framework.http.SBRequest.NetAccessListener;
import com.sb.framework.utils.SBLog;

/**
 * ndroid开发团队也是意识到了有必要将HTTP的通信操作再进行简单化，
 * 于是在2013年Google I/O大会上推出了一个新的网络通信框架——Volley。
 * Volley可是说是把AsyncHttpClient和Universal-Image-Loader的优点集于了一身，
 * 既可以像AsyncHttpClient一样非常简单地进行HTTP通信，也可以像Universal-Image-Loader一样轻松加载网络上的图片。
 * 除了简单易用之外，Volley在性能方面也进行了大幅度的调整，
 * 它的设计目标就是非常适合去进行数据量不大，但通信频繁的网络操作，
 * 
 * 而对于大数据量的网络操作，比如说下载文件等，Volley的表现就会非常糟糕。
 * @author seven
 *
 */
public class HttpWorkerUseVolly implements SBHttpWorker{

	//Context context;
	public HttpWorkerUseVolly(){
		if(queue == null) queue = Volley.newRequestQueue(SB.context);
	}
	
	//private static Map<String, RequestQueue> queueCache = new HashMap<String, RequestQueue>();
	private static RequestQueue queue;
	
	@Override
	public void sendRequest(final SBRequest request, final NetAccessListener listender) {
		
		SBLog.debug("--------------------");
		SBLog.debug("请求参数：");
		HttpHelper.printMap(request.params);
		SBLog.debug("请求头：");
		HttpHelper.printMap(request.headers);
		
		
		if(request.useCache && request.method.equalsIgnoreCase("get")){
			String data = request.cacheHandler.get(HttpHelper.makeURL(request.url, request.params));
			if(!TextUtils.isEmpty(data)){
				listender.onRequestFinished(true, data, "", request.flag);
				return;
			}
		}
		
		RequestQueue mQueue = queue; //(RequestQueue)thirdPartyNeed;
		String url = request.url;
		int m = request.method.equalsIgnoreCase("get") ? Method.GET : Method.POST;
		SBLog.debug("请求URL：");
		if(request.method.equals("get")){
			url = HttpHelper.makeURL(request.url, request.params);
			SBLog.debug(url);
		}else{
			SBLog.debug(url);
		}
		SBLog.debug("--------------------");
		StringRequest stringRequest = new StringRequest(m, url,  
                new Response.Listener<String>() {  
                    @Override  
                    public void onResponse(String response) {  
                    	listender.onRequestFinished(true, response, "", request.flag);
                    }  
                }, new Response.ErrorListener() {  
                    @Override  
                    public void onErrorResponse(VolleyError error) {  
                        //Log.e("TAG", error.getMessage(), error);  
                    	listender.onRequestFinished(false, "", error.getMessage(), request.flag);
                    }  
                })
		{
			/**
			 * 当method是post时，StringRequest并没有直接听过设置请求参数的接口，但是Volly会试着从
			 * StringRequest的基类的函数getParams获取post参数
			 */
			@Override  
		    protected Map<String, String> getParams() throws AuthFailureError {  
				if(request.method.equals("post")) return request.params;
				return new HashMap<String, String>();
		          
		    }  
			
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				return request.headers;
			}
		}; 
		stringRequest.setTag(request.flag);
		stringRequest.setShouldCache(request.useCache);
//		stringRequest.setCacheEntry(entry);
//		stringRequest.setRequestQueue(requestQueue);
//		stringRequest.setRetryPolicy(retryPolicy);
//		stringRequest.setSequence(sequence);
		mQueue.add(stringRequest);
	}

	@Override
	public void sendRequestSync(SBRequest request, NetAccessListener listender) {
		throw new RuntimeException("没实现！！！！");
	}

}

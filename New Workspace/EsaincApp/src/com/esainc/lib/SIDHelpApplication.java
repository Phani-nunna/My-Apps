package com.esainc.lib;

import android.app.Application;
import android.content.Context;

import com.esainc.lib.uc.Logger;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class SIDHelpApplication extends Application {
	private boolean mTablet;
	public static Context mContext;
	public static Logger mLogger;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mContext = getApplicationContext();
		mTablet = getResources().getBoolean(R.bool.tablet);
		mLogger = Logger.getInstance();
		
		// Default options
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		    .showImageOnLoading(R.drawable.noimage)
			.showImageForEmptyUri(R.drawable.noimage)
			.showImageOnFail(R.drawable.noimage)
			.cacheInMemory(true)
			.build();
		
		// Create global configuration and initialize ImageLoader with this configuration
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
			.memoryCacheExtraOptions(getResources().getInteger(R.integer.NewsImageWidth), getResources().getInteger(R.integer.NewsImageHeight))
			.memoryCache(new UsingFreqLimitedMemoryCache(2000000))
			.defaultDisplayImageOptions(options)
			.threadPoolSize(20)
			.threadPriority(Thread.MAX_PRIORITY)
			.tasksProcessingOrder(QueueProcessingType.LIFO)
			.build();
		
		ImageLoader.getInstance().init(config);
	}
	
	public boolean isTablet() {
		return mTablet;
	}
}

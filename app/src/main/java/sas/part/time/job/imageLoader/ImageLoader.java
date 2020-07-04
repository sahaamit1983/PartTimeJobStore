package sas.part.time.job.imageLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImageLoader {

	MemoryCache memoryCache=new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;
	Handler handler=new Handler();//handler to display images in UI thread
	private Activity mActivity;
	
	public ImageLoader(Context context,int image_id){
		stub_id = image_id;
		fileCache=new FileCache(context);
		executorService=Executors.newFixedThreadPool(5);
		mActivity = (Activity)context;
	}

	int stub_id = 0;
	public void DisplayImage(String url, ImageView imageView, ProgressBar bar)
	{
		imageViews.put(imageView, url);
		Bitmap bitmap=memoryCache.get(url);
		if(bitmap!=null) {
			imageView.setBackgroundResource(android.R.color.transparent);
			imageView.setImageBitmap(bitmap);	
			bar.setVisibility(View.GONE);
		}
		else
		{
			queuePhoto(url, imageView, bar);
			imageView.setBackgroundResource(android.R.color.transparent);
			imageView.setImageResource(stub_id);
		}
	}

	private void queuePhoto(String url, ImageView imageView, ProgressBar bar)
	{
		PhotoToLoad p=new PhotoToLoad(url, imageView, bar);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) 
	{
		File f = fileCache.getFile(url);

		//from SD cache
		Bitmap b = decodeScaledBitmapFromSdCard(f);
		if(b!=null)
			return b;

		//from web
		try {
			Bitmap bitmap=null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is=conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			conn.disconnect();
			bitmap = decodeScaledBitmapFromSdCard(f);
			return bitmap;
		} catch (Throwable ex){
			ex.printStackTrace();
			if(ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}
	}

	//Task for the queue
	private class PhotoToLoad
	{
		public ImageView imageView;
		public ProgressBar Pbar;
		public String Url;
		
		public PhotoToLoad(String url, ImageView i, ProgressBar bar){
			imageView=i;
			Pbar = bar;
			Url = url;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;
		PhotosLoader(PhotoToLoad photoToLoad){
			this.photoToLoad=photoToLoad;
		}

		@Override
		public void run() {
			try{
				if(imageViewReused(photoToLoad))
					return;
				Bitmap bmp=getBitmap(photoToLoad.Url);
				memoryCache.put(photoToLoad.Url, bmp);
				if(imageViewReused(photoToLoad))
					return;
				BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
				handler.post(bd);
			}catch(Throwable th){
				th.printStackTrace();
			}
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad){
		String tag=imageViews.get(photoToLoad.imageView);
		if(tag==null || !tag.equals(photoToLoad.Url))
			return true;
		return false;
	}

	//Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable
	{
		Bitmap bitmap;
		PhotoToLoad photoToLoad;
		public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
		public void run()
		{
			if(imageViewReused(photoToLoad))
				return;
			if(bitmap!=null) {
				photoToLoad.imageView.setBackgroundResource(android.R.color.transparent);
				photoToLoad.imageView.setImageBitmap(bitmap);
				photoToLoad.Pbar.setVisibility(View.GONE);
			}
			else {
				photoToLoad.imageView.setBackgroundResource(android.R.color.transparent);
				photoToLoad.imageView.setImageResource(stub_id);
				photoToLoad.Pbar.setVisibility(View.GONE);
			}
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	private Bitmap decodeScaledBitmapFromSdCard(File f) {

		try {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			FileInputStream stream1=new FileInputStream(f);
			BitmapFactory.decodeStream(stream1,null,options);
			stream1.close();

			DisplayMetrics dm = new DisplayMetrics();
			mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			int w = dm.widthPixels;
			int h = dm.heightPixels;

			int srcWidth = options.outWidth;
			int srcHeight = options.outHeight;

			int[] newWH =  new int[2];
			newWH[0] = w/2;
			newWH[1] = h/2;

			while(srcWidth > newWH[0]) {
				srcWidth /= 2;
			}
			while(srcHeight > newWH[1]) {
				srcHeight /= 2;
			}

			options.inSampleSize = calculateInSampleSize(options, srcWidth, srcHeight);
			options.inJustDecodeBounds = false;
			FileInputStream stream2=new FileInputStream(f);
			Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, options);
			stream2.close();

			return bitmap;

		} catch (FileNotFoundException e) {
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {

			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
}

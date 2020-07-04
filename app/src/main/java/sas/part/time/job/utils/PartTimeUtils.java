package sas.part.time.job.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

import com.google.android.gms.ads.AdRequest;

import sas.part.time.job.R;
import sas.part.time.job.database.DatabaseInfo;
import sas.part.time.job.pojo.Country;
import sas.part.time.job.pojo.JobData;
import sas.part.time.job.pojo.Zone;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.DisplayMetrics;

@SuppressLint("SimpleDateFormat")
public class PartTimeUtils {

	private static 	PartTimeUtils 		appConfig 				= null;
	private final 	String 				FILTER 					= "subscription"; 
	private final 	String 				UNSEND 					= "0"; 
	private final 	String 				SEND 					= "1"; 
	private final 	SimpleDateFormat	FORMATTER  				= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final 	SimpleDateFormat	FORMATTER2  			= new SimpleDateFormat("dd/MMM/yyyy");

	private final 	int 				JOB_INACTIVE 			= 0; 
	private final 	int 				JOB_ACTIVE 				= 1; 
	private final 	int 				SUBSCRIBED 				= 0; 
	private final 	int 				UNSUBSCRIBED 			= 1; 
	private static 	AdRequest 			adRequest				= null;

	private PartTimeUtils() {}

	public static PartTimeUtils getSingleInstance() {

		if(appConfig == null) {
			appConfig = new PartTimeUtils();
		}
		return appConfig;
	}

	public boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	public boolean isNetworkOnline(Context context) {

		boolean status = false;
		try{
			ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(cm!=null){

				NetworkInfo[] netInfo = cm.getAllNetworkInfo();
				if(netInfo!=null) {
					for (int i = 0; i < netInfo.length; i++) 
						if (netInfo[i].getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
				}
			}
		} catch(Exception e){
			e.printStackTrace();  
			status = false;
		}
		return status;
	}

	public void setUTCFormat() {
		FORMATTER.setTimeZone(TimeZone.getDefault()); 
		FORMATTER2.setTimeZone(TimeZone.getDefault()); //TimeZone.getTimeZone("UTC")
	}

	public static AdRequest getAdRequest() {
		
		if(adRequest==null) {
			adRequest = new AdRequest.Builder()
			//.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
			//.addTestDevice("733B35E050031D04CB62668318DD46ED") 
			.build();
		}
		return adRequest;
	}

	public Date convertStringToDate(String date) {
		try {
			return FORMATTER.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	public String convertDateToString(Date parseDate) {

		try {
			return FORMATTER2.format(parseDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public class JobDataSortByDate implements Comparator<JobData> {

		@Override
		public int compare(JobData arg0, JobData arg1) {

			if(arg0.getPostedDate().after(arg1.getPostedDate())){
				return -1;  
			}
			return 1;
		}
	}

	public boolean getAutoDeleteDate(long date) {

		try {
			Date today = new Date();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(date);
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 7 );

			if(today.after(c.getTime())) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	} 

	public boolean isExpire(Date mDate1, Date mDate2) {

		if(mDate1.after(mDate2)) {
			return false;
		}
		return true;
	}

	public String getDirectory(Context context) {

		String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Part Time Job/"; 
		File newdir=new File(dir);

		if(!newdir.exists()) {
			newdir.mkdirs();
		}

		return dir;
	}

	public ArrayList<Country> getCountryData(Context c) {

		ArrayList<Country> List = new ArrayList<Country>();
		try {
			SQLiteDatabase myDB = c.openOrCreateDatabase(DatabaseInfo.COUNTRY_DB, Context.MODE_PRIVATE, null);

			Cursor cur = myDB.rawQuery(DatabaseInfo.COUNTRY_QUERY,null);

			int Column1Index = cur.getColumnIndex(DatabaseInfo.COUNTRY_COLUMN1);
			int Column2Index = cur.getColumnIndex(DatabaseInfo.COUNTRY_COLUMN2);
			int Column3Index = cur.getColumnIndex(DatabaseInfo.COUNTRY_COLUMN3);

			cur.moveToFirst();

			while (cur.isAfterLast() == false) {		
				Country con = new Country();
				con.setId(cur.getString(Column1Index));
				con.setName(cur.getString(Column2Index));
				con.setCurrency(cur.getString(Column3Index));
				List.add(con);
				cur.moveToNext();
			}
			if(cur!=null) {
				cur.close();
			}
			if(myDB!=null) {
				myDB.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return List;
	}

	public ArrayList<Zone> getStateData(Context c, String countryId) { 

		ArrayList<Zone> zoneName = new ArrayList<Zone>();
		try {
			SQLiteDatabase myDB = c.openOrCreateDatabase(DatabaseInfo.COUNTRY_DB, Context.MODE_PRIVATE, null);

			Cursor cur = myDB.rawQuery(DatabaseInfo.ZONE_QUERY,new String[]{countryId});

			int Column1Index = cur.getColumnIndex(DatabaseInfo.ZONE_COLUMN1);
			int Column2Index = cur.getColumnIndex(DatabaseInfo.ZONE_COLUMN2);
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {		

				Zone data = new Zone();

				data.setId(cur.getString(Column1Index));
				data.setName(cur.getString(Column2Index));

				zoneName.add(data);

				cur.moveToNext();
			}
			if(cur!=null) {
				cur.close();
			}
			if(myDB!=null) {
				myDB.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zoneName;
	}

	public ArrayList<String> getCityData(Context c, String zoneId) {

		ArrayList<String> cityName = new ArrayList<String>();
		try {
			SQLiteDatabase myDB = c.openOrCreateDatabase(DatabaseInfo.COUNTRY_DB, Context.MODE_PRIVATE, null);

			Cursor cur = myDB.rawQuery(DatabaseInfo.CITY_QUERY,new String[]{zoneId});

			//int Column1Index = cur.getColumnIndex(DatabaseInfo.ZONE_COLUMN1);
			int Column2Index = cur.getColumnIndex(DatabaseInfo.CITY_COLUMN2);
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {		

				cityName.add(cur.getString(Column2Index));

				cur.moveToNext();
			}
			if(cur!=null) {
				cur.close();
			}
			if(myDB!=null) {
				myDB.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cityName;
	}

	public boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				deleteDir(new File(dir, children[i]));
			}
		}
		// The directory is now empty so delete it
		return dir.delete();
	}

	public String getDirectory() {

		String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Part Time Job/"; 
		File newdir = new File(dir); 
		if(!newdir.exists()) {
			newdir.mkdirs();
		}

		return dir;
	}

	public Bitmap makeScaling(Activity activity, String originalpath) {

		/*String dir = getDirectory(); 
		File file = new File(originalpath);
		String filepath = dir + file.getName();
		File newfile = new File(filepath);

		if(!newfile.exists()) {
			try {
				newfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}*/


		return decodeFile(activity, originalpath);
	}

	private Bitmap decodeFile(Activity activity, String filePath) {

		File f = new File(filePath);
		try {

			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			int w = dm.widthPixels;
			int h = dm.heightPixels;

			//decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1=new FileInputStream(f);
			BitmapFactory.decodeStream(stream1,null,o);
			stream1.close();

			//Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;

			int scale=1;
			while(width_tmp > w) {
				width_tmp /= 2;
				scale*=2;
			}
			while(height_tmp > h) {
				height_tmp /= 2;
			}

			//decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize=scale;
			FileInputStream stream2=new FileInputStream(f);
			Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return BitmapFactory.decodeResource(activity.getResources(), R.drawable.reg_image_default);
	}

	public String getSend() {
		return SEND;
	}

	public String getUnSend() {
		return UNSEND;
	}

	/*public SimpleDateFormat getFormatter() {
		return FORMATTER;
	}*/

	public int getActiveJob() {
		return JOB_ACTIVE;
	}

	public int getInActiveJob() {
		return JOB_INACTIVE;
	}

	public int getSubscribed() {
		return SUBSCRIBED;
	}

	public int getUnSubscribed() {
		return UNSUBSCRIBED;
	}

	public String getIntentFilterName() {
		return FILTER;
	}
}

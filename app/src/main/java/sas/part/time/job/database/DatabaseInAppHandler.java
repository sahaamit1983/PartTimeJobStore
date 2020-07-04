package sas.part.time.job.database;

import java.util.ArrayList;

import sas.part.time.job.pojo.InAppData;
import sas.part.time.job.utils.PartTimeUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseInAppHandler extends SQLiteOpenHelper {

	private static 	final 	String 				TAG 				= "DatabaseInAppHandler";
	private static 	final 	String 				DATABASE_NAME 		= "SAS";
	private static 	final 	int 				DATABASE_VERSION 	= 1;
	private 				SQLiteDatabase 		DB	  				= null;  

	// for CHAT DATA
	private final String INAPP_TABLE 			= "inapp_table";
	private final String USER_ID 				= "user_id";
	private final String ORDER_ID 		 		= "order_id";
	private final String PRODUCT_ID 		 	= "product_id";
	private final String PACKAGE_NAME 			= "package_name";
	private final String PURCHASE_TIME 	 		= "purchase_time";
	private final String PURCHASE_STATE 		= "purchase_state";
	private final String DEVELOPER_PAYLOAD 		= "developer_payload";
	private final String PURCHASE_TOKEN 		= "purchase_token";
	private final String KEY_ID 				= "id";
	private final String POST_FLAG 				= "post_flag";


	private final PartTimeUtils	appConfig = PartTimeUtils.getSingleInstance();

	public DatabaseInAppHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table if not exists " + INAPP_TABLE 
				+ " ( " + USER_ID + " VARCHAR," + ORDER_ID + " VARCHAR," 
				+ PRODUCT_ID + " VARCHAR," + PURCHASE_STATE + " VARCHAR," 
				+ DEVELOPER_PAYLOAD + " VARCHAR," + PACKAGE_NAME + " VARCHAR," 
				+ PURCHASE_TIME + " VARCHAR," + PURCHASE_TOKEN + " integer, " 
				+ POST_FLAG + " integer default 0,  "
				+ KEY_ID + " integer primary key autoincrement)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

	public synchronized void AddUnSendInAppData(String userId, 
			InAppData appData) {

		try {
			DB = this.getWritableDatabase();

			ContentValues values = new ContentValues();

			values.put(USER_ID, userId);
			values.put(ORDER_ID, appData.getORDER_ID());
			values.put(PRODUCT_ID, appData.getPRODUCT_ID());
			values.put(PACKAGE_NAME, appData.getPACKAGE_NAME());
			values.put(PURCHASE_TIME, appData.getPURCHASE_TIME());
			values.put(PURCHASE_STATE, appData.getPURCHASE_STATE());
			values.put(DEVELOPER_PAYLOAD, appData.getDEVELOPER_PAYLOAD());
			values.put(PURCHASE_TOKEN, appData.getPURCHASE_TOKEN());
			values.put(POST_FLAG, appData.getFLAG_SENT_UNSENT());

			DB.insert(INAPP_TABLE, null, values);

		} catch (SQLiteException lSqlEx) {
			Log.e(TAG, "Could not open database");
			Log.e(TAG, "Exception:" + lSqlEx.getMessage());

		} catch (SQLException lEx) {
			Log.e(TAG, "Could not fetch trip data");
			Log.e(TAG, "Exception:" + lEx.getMessage());
		} finally {
			if (DB != null)
				DB.close();
		}
	}

	public synchronized void UpdateInAppSentFlag(String userId, String orderId, 
			String developerPayload) {

		try {
			DB = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(POST_FLAG,  appConfig.getSend());

			String arg[] = new String[]{userId, orderId, developerPayload};

			DB.update(INAPP_TABLE, values, USER_ID+"=? and "+ORDER_ID+"=? and "
			+DEVELOPER_PAYLOAD+"=?", arg);

		} catch (SQLiteException lSqlEx) {
			Log.e(TAG, "Could not open database");
			Log.e(TAG, "Exception:" + lSqlEx.getMessage());

		} catch (SQLException lEx) {
			Log.e(TAG, "Could not fetch trip data");
			Log.e(TAG, "Exception:" + lEx.getMessage());
		} finally {
			if (DB != null)
				DB.close();
		}
	}

	public synchronized ArrayList<InAppData> getUnSendInAppData(String userId) {

		Cursor cursor = null;

		ArrayList<InAppData> InAppDataList = new ArrayList<InAppData>();
		

		try {

			DB = this.getReadableDatabase();

			final String MY_QUERY = "SELECT * FROM " + INAPP_TABLE 
					+ " where "+POST_FLAG+" = "+appConfig.getUnSend()
					+" and "+USER_ID+" = "+userId+";";

			cursor = DB.rawQuery(MY_QUERY, null);

			int ORDER_ID_INDEX 			= cursor.getColumnIndex(ORDER_ID);
			int PRODUCT_ID_INDEX 		= cursor.getColumnIndex(PRODUCT_ID);
			int PACKAGE_NAME_INDEX 		= cursor.getColumnIndex(PACKAGE_NAME);
			int PURCHASE_TIME_INDEX 	= cursor.getColumnIndex(PURCHASE_TIME);
			int PURCHASE_STATE_INDEX 	= cursor.getColumnIndex(PURCHASE_STATE);
			int DEVELOPER_PAYLOAD_INDEX = cursor.getColumnIndex(DEVELOPER_PAYLOAD);
			int PURCHASE_TOKEN_INDEX 	= cursor.getColumnIndex(PURCHASE_TOKEN);

			if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {

				while(!cursor.isAfterLast()) {
					
					InAppData appData = new InAppData();

					appData.setORDER_ID(cursor.getString(ORDER_ID_INDEX));
					appData.setPRODUCT_ID(cursor.getString(PRODUCT_ID_INDEX));
					appData.setPACKAGE_NAME(cursor.getString(PACKAGE_NAME_INDEX));
					appData.setPURCHASE_TIME(cursor.getString(PURCHASE_TIME_INDEX));
					appData.setPURCHASE_STATE(cursor.getString(PURCHASE_STATE_INDEX));
					appData.setDEVELOPER_PAYLOAD(cursor.getString(DEVELOPER_PAYLOAD_INDEX));
					appData.setPURCHASE_TOKEN(cursor.getString(PURCHASE_TOKEN_INDEX));
				
					InAppDataList.add(appData);
					
					cursor.moveToNext();
				}
			}

		} catch (SQLiteException lSqlEx) {
			Log.e(TAG, "Could not open database");
			Log.e(TAG, "Exception:" + lSqlEx.getMessage());

		} catch (SQLException lEx) {
			Log.e(TAG, "Could not fetch trip data");
			Log.e(TAG, "Exception:" + lEx.getMessage());

		} finally {
			if (DB != null)
				DB.close();
			if (cursor != null)
				cursor.close();
		}
		return InAppDataList;
	}

	public int getUnSendInAppDataCount(String userId) {

		Cursor cursor = null;
		int count = 0;
		try {

			DB = this.getReadableDatabase();

			final String MY_QUERY = "SELECT "+DEVELOPER_PAYLOAD+" FROM " 
			+ INAPP_TABLE + " where " + POST_FLAG+" = "+ appConfig.getUnSend()
			+" and "+USER_ID+" = "+userId+";";

			cursor = DB.rawQuery(MY_QUERY, null);
			count = cursor.getCount();

		} catch (SQLiteException lSqlEx) {
			Log.e(TAG, "Could not open database");
			Log.e(TAG, "Exception:" + lSqlEx.getMessage());

		} catch (SQLException lEx) {
			Log.e(TAG, "Could not fetch trip data");
			Log.e(TAG, "Exception:" + lEx.getMessage());
		} finally {
			if (DB != null)
				DB.close();
			if (cursor != null)
				cursor.close();
		}
		return count;
	}
}

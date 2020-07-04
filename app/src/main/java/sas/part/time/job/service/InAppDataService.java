package sas.part.time.job.service;

import sas.part.time.job.database.DatabaseInAppHandler;
import sas.part.time.job.preference.UserInfo;
import sas.part.time.job.server.PurchasedServer;
import sas.part.time.job.userInterface.IPurchaseData;
import sas.part.time.job.utils.PartTimeUtils;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

public class InAppDataService extends Service implements IPurchaseData {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		helper = new DatabaseInAppHandler(getBaseContext());
		mHandler.post(mRunnable);
	
		return super.onStartCommand(intent, flags, startId);
	}
	
	private PartTimeUtils appConfig = PartTimeUtils.getSingleInstance();
	private String mUserId;
	private DatabaseInAppHandler 	helper;
	private UserInfo info = new UserInfo();
	private Handler mHandler = new Handler();
	private boolean mUpdateUI = false;
	private Runnable mRunnable = new Runnable() {
		
		@Override
		public void run() {
			
			mUserId = info.getUserId(getBaseContext()); 
			if(!TextUtils.isEmpty(mUserId) && helper.getUnSendInAppDataCount(mUserId)>0 && 
					appConfig.isNetworkOnline(getBaseContext())) {
				
				PurchasedServer t = new PurchasedServer(getBaseContext(), 
						InAppDataService.this);
		        t.start();
			} else {
				
				if(mUpdateUI) {
					Intent intent = new Intent(appConfig.getIntentFilterName());
					LocalBroadcastManager.getInstance(getBaseContext()).
					sendBroadcast(intent);
				}
				
				stopSelf();
			}
		}
	};
	
	@Override
	public void onSend(boolean updateUI) {
		mUpdateUI = updateUI;
		mHandler.postDelayed(mRunnable,1000);
	}
}

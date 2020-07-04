package sas.part.time.job.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import sas.part.time.job.R;
import sas.part.time.job.database.DatabaseInAppHandler;
import sas.part.time.job.json.JSONParser;
import sas.part.time.job.pojo.InAppData;
import sas.part.time.job.pojo.SubscriptionMsg;
import sas.part.time.job.preference.UserInfo;
import sas.part.time.job.userInterface.IPurchaseData;
import sas.part.time.job.utils.PartTimeMessage;
import android.content.Context;

public class PurchasedServer extends Thread {	

	private Context 				mContext;
	private DatabaseInAppHandler 	helper;
	private String 					mUserId ;
	private boolean 				updateUI;
	private IPurchaseData 			mListener;
	private PartTimeMessage 		appMessage 	= PartTimeMessage.getSingleInstance();
	
	public PurchasedServer(Context c, IPurchaseData listener) {
		mContext 	= c;
		helper = new DatabaseInAppHandler(mContext);
		UserInfo info = new UserInfo();
		mUserId = info.getUserId(mContext);
		mListener = listener;
		updateUI = false;
	}
	
	@Override
	public void run() {
		super.run();
	
		dbToServer();
		mListener.onSend(updateUI);
	}
	
	private void dbToServer() {
		
		ArrayList<InAppData> InAppDataList = helper.getUnSendInAppData(mUserId);
		for(int i=0;i<InAppDataList.size();i++) {
			
			InAppData inAppData = InAppDataList.get(i);
			SubscriptionMsg data = doInBackground(inAppData); 
			if(!data.isException() && data.isResult()) {
				
				helper.UpdateInAppSentFlag(mUserId, inAppData.getORDER_ID(), 
						inAppData.getDEVELOPER_PAYLOAD());
				updateUI = true;
			}
		}
	}

	private SubscriptionMsg doInBackground(InAppData pData) {

		String URL = mContext.getResources().getString(R.string.order_purchase_url);

		SubscriptionMsg data = new SubscriptionMsg();
		UserInfo info = new UserInfo();
		String mUserId = info.getUserId(mContext);

		try { 
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("user_id", mUserId));
			nameValuePairs.add(new BasicNameValuePair("orderId", pData.getORDER_ID()));
			nameValuePairs.add(new BasicNameValuePair("productId", pData.getPRODUCT_ID()));
			nameValuePairs.add(new BasicNameValuePair("packageName", pData.getPACKAGE_NAME()));
			nameValuePairs.add(new BasicNameValuePair("purchaseTime", pData.getPURCHASE_TIME()));
			nameValuePairs.add(new BasicNameValuePair("purchaseState", pData.getPURCHASE_STATE()));
			nameValuePairs.add(new BasicNameValuePair("purchaseToken", pData.getPURCHASE_TOKEN()));
			nameValuePairs.add(new BasicNameValuePair("developerPayload", pData.getDEVELOPER_PAYLOAD()));
			
			UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(nameValuePairs);

			JSONParser jParser = new JSONParser();
			String jsonStr = jParser.getJSONFromUrl(URL,requestEntity);

			JSONObject json = new JSONObject(jsonStr);

			data.setResult(json.getBoolean("result"));
			
		} catch (Exception e) {
			data.setMessage(appMessage.getErrorMessage());
			data.setException(true);
			data.setResult(false);
			e.printStackTrace();
		}
		
		return data;
	} 
}

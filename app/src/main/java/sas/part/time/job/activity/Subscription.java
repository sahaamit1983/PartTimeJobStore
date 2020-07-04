package sas.part.time.job.activity;

import sas.part.time.job.R;
import sas.part.time.job.database.DatabaseInAppHandler;
import sas.part.time.job.dialog.UserAlertDialog;
import sas.part.time.job.inapppurchase.IabBroadcastReceiver;
import sas.part.time.job.inapppurchase.IabBroadcastReceiver.IabBroadcastListener;
import sas.part.time.job.inapppurchase.IabHelper;
import sas.part.time.job.inapppurchase.IabHelper.IabAsyncInProgressException;
import sas.part.time.job.inapppurchase.IabResult;
import sas.part.time.job.inapppurchase.Inventory;
import sas.part.time.job.inapppurchase.Purchase;
import sas.part.time.job.inapppurchase.RandomString;
import sas.part.time.job.pojo.InAppData;
import sas.part.time.job.pojo.SubscriptionMsg;
import sas.part.time.job.preference.UserInfo;
import sas.part.time.job.server.SubscriptionServer;
import sas.part.time.job.service.InAppDataService;
import sas.part.time.job.userInterface.ISubscription;
import sas.part.time.job.utils.PartTimeMessage;
import sas.part.time.job.utils.PartTimeUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Subscription extends Activity implements IabBroadcastListener, 
ISubscription {

	private Button 					Sub_7,Sub_1, Back;
	private TextView 				mesage, wait;
	private DatabaseInAppHandler 	dbHelper = null;
	private final PartTimeUtils		appConfig = PartTimeUtils.getSingleInstance();
	private PartTimeMessage 		appMessage = PartTimeMessage.getSingleInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subscription);

		dbHelper = new DatabaseInAppHandler(Subscription.this);
		initView();
		listener();

		if(appConfig.isNetworkOnline(Subscription.this)) {
			SubscriptionServer server = new SubscriptionServer(Subscription.this, Subscription.this);
			server.execute();
		} else {
			UserAlertDialog dialog = new UserAlertDialog(Subscription.this);
			dialog.showDialogOk(getString(android.R.string.dialog_alert_title), 
					appMessage.getNetworkFailure());
		}
		updateUI();
	}

	private void initView() {

		Sub_7 	= (Button)findViewById(R.id.sub_7_days);
		Sub_1	= (Button)findViewById(R.id.sub_1_month);
		Back	= (Button)findViewById(R.id.subscribe_back);
		mesage	= (TextView)findViewById(R.id.subscribe_message);
		wait 	= (TextView)findViewById(R.id.subscribe_wait_message);
	}

	private void listener() {

		/*Sub_7.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if(appConfig.isNetworkOnline(Subscription.this)) {
					Upgrade_7_days();
				} else {
					UserAlertDialog dialog = new UserAlertDialog(Subscription.this);
					dialog.showDialogOk(getString(android.R.string.dialog_alert_title), 
							appMessage.getNetworkFailure());
				}
			}
		});*/

		Sub_1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if(appConfig.isNetworkOnline(Subscription.this)) {
					Upgrade_1_month();
				} else {
					UserAlertDialog dialog = new UserAlertDialog(Subscription.this);
					dialog.showDialogOk(getString(android.R.string.dialog_alert_title), 
							appMessage.getNetworkFailure());
				}
			}
		});
		Back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private void updateUI() {

		UserInfo info = new UserInfo();
		String mUserId = info.getUserId(getBaseContext()); 
		if(dbHelper.getUnSendInAppDataCount(mUserId)>0) {
			Sub_7.setVisibility(View.GONE);
			Sub_1.setVisibility(View.GONE);
			wait.setVisibility(View.VISIBLE);
		} else {
			Sub_7.setVisibility(View.VISIBLE);
			Sub_1.setVisibility(View.VISIBLE);
			wait.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(message);
	}

	@Override
	protected void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this).
		registerReceiver(message, new IntentFilter(appConfig.getIntentFilterName()));
	}

	private BroadcastReceiver message = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if(action.equals(appConfig.getIntentFilterName())){

				updateUI();
				SubscriptionServer server = new SubscriptionServer(Subscription.this, 
						Subscription.this);
				server.execute();
			}
		}
	}; 

	@Override
	public void onSubscription(SubscriptionMsg data) {

		if(!data.isException()) {

			if(data.isResult()) {

				if(!appConfig.isExpire(data.getLastDate(), data.getSystemDate())) {
					String date = appConfig.convertDateToString(data.getLastDate());
					mesage.setText("Your subscription will expire on \n"+date);
				} else {
					mesage.setText(getString(R.string.sub_msg));
				}
			} else {
				mesage.setText(getString(R.string.sub_msg));
			}
		} else {
			UserAlertDialog dialog = new UserAlertDialog(Subscription.this);
			dialog.showDialogOk(getString(android.R.string.dialog_alert_title), data.getMessage());
		}
		setInApp();
	}

	// Debug tag, for logging
	//private static final String TAG = "SAS";

	// SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
	private static final String SKU_MONTH_1 = "month_1";
	//private static final String SKU_DAY_7 = "day_7";
	//private static final String SKU_DAY_7 = "android.test.purchased";
	//private final String DAY_7 = "7";
	private final String DAY_30 = "366";

	// (arbitrary) request code for the purchase flow
	private static final int RC_REQUEST = 10001;

	// The helper object
	private IabHelper mHelper;

	// Provides purchase notification while this app is running
	private IabBroadcastReceiver mBroadcastReceiver;

	//private static String DEVELOPER_PAYLOAD = "";

	private void setInApp() {

		String base64EncodedPublicKey = getString(R.string.public_key); 

		// Create the helper, passing it our context and the public key to verify signatures with
		showLog("Creating IAB helper.");
		mHelper = new IabHelper(Subscription.this, base64EncodedPublicKey);

		// enable debug logging (for a production application, you should set this to false).
		mHelper.enableDebugLogging(false);

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		showLog("Starting setup.");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				showLog("Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					complain("Problem setting up in-app billing: " + result);
					return;
				}

				// Have we been disposed of in the meantime? If so, quit.
				if (mHelper == null) return;

				// Important: Dynamically register for broadcast messages about updated purchases.
				// We register the receiver here instead of as a <receiver> in the Manifest
				// because we always call getPurchases() at startup, so therefore we can ignore
				// any broadcasts sent while the app isn't running.
				// Note: registering this listener in an Activity is a bad idea, but is done here
				// because this is a SAMPLE. Regardless, the receiver must be registered after
				// IabHelper is setup, but before first call to getPurchases().
				mBroadcastReceiver = new IabBroadcastReceiver(Subscription.this);
				IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
				registerReceiver(mBroadcastReceiver, broadcastFilter);

				// IAB is fully set up. Now, let's get an inventory of stuff we own.
				showLog("Setup successful. Querying inventory.");
				try {
					mHelper.queryInventoryAsync(mGotInventoryListener);
				} catch (IabAsyncInProgressException e) {
					complain("Error querying inventory. Another async operation in progress.");
				}
			}
		});
	}

	// Listener that's called when we finish querying the items and subscriptions we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			showLog("Query inventory finished.");
			// Have we been disposed of in the meantime? If so, quit.
			if (mHelper == null) return;

			// Is it a failure?
			if (result.isFailure()) {
				complain("Failed to query inventory: " + result);
				return;
			}

			showLog("Query inventory was successful.");

			/*
			 * Check for items we own. Notice that for each purchase, we check
			 * the developer payload to see if it's correct! See
			 * verifyDeveloperPayload().
			 */

			// Check for gas delivery -- if we own gas, we should fill up the tank immediately

			Purchase gasPurchase = inventory.getPurchase(SKU_MONTH_1);
			if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
				showLog("We have item. Consuming it.");
				try {
					mHelper.consumeAsync(inventory.getPurchase(SKU_MONTH_1), mConsumeFinishedListener);
				} catch (IabAsyncInProgressException e) {
					complain("Error consuming. Another async operation in progress.");
				}
				return;
			}
			/*gasPurchase = inventory.getPurchase(SKU_DAY_7);
			if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
				showLog("We have item. Consuming it.");
				try {
					mHelper.consumeAsync(inventory.getPurchase(SKU_DAY_7), mConsumeFinishedListener);
				} catch (IabAsyncInProgressException e) {
					complain("Error consuming. Another async operation in progress.");
				}
				return;
			}*/

			showLog("Initial inventory query finished; enabling main UI.");
		}
	};

	@Override
	public void receivedBroadcast() {
		// Received a broadcast notification that the inventory of items has changed
		showLog("Received broadcast notification. Querying inventory.");
		try {
			mHelper.queryInventoryAsync(mGotInventoryListener);
		} catch (IabAsyncInProgressException e) {
			complain("Error querying inventory. Another async operation in progress.");
		}
	}

	// User clicked the "7_days" button
	/*public void Upgrade_7_days() {

		RandomString randomString = new RandomString(30);
		DEVELOPER_PAYLOAD = randomString.nextString();

		try {
			mHelper.launchSubscriptionPurchaseFlow(this, SKU_DAY_7, RC_REQUEST,
					mPurchaseFinishedListener, DEVELOPER_PAYLOAD);
		} catch (IabAsyncInProgressException e) {
			complain("Error launching purchase flow. Another async operation in progress.");
		}
	}*/

	// User clicked the "1_month" button.
	public void Upgrade_1_month() {

		RandomString randomString = new RandomString(30);
		String DEVELOPER_PAYLOAD = randomString.nextString();
		setDeveloperPayload(Subscription.this, DEVELOPER_PAYLOAD);
		try {
			mHelper.launchSubscriptionPurchaseFlow(this, SKU_MONTH_1, RC_REQUEST,
					mPurchaseFinishedListener, DEVELOPER_PAYLOAD);
		} catch (IabAsyncInProgressException e) {
			complain("Error launching purchase flow. Another async operation in progress.");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		showLog("onActivityResult(" + requestCode + "," + resultCode + "," + data);
		if (mHelper == null) return;

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
		}
		else {
			showLog("onActivityResult handled by IABUtil.");
		}
	}


	/** Verifies the developer payload of a purchase. */
	private boolean verifyDeveloperPayload(Purchase p) {

		String payload = p.getDeveloperPayload();

		if(getDeveloperPayload(Subscription.this).equals(payload))
			return true; 

		return false;
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			showLog("Purchase finished: " + result + ", purchase: " + purchase);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null || purchase == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                return;
            }

			showLog("Purchase successful.");
			storeData(purchase);

			updateUI();

			Intent i = new Intent(Subscription.this, InAppDataService.class);
			startService(i);

			/*if (purchase.getSku().equals(SKU_DAY_7)) {
				// bought 1/4 tank of gas. So consume it.
				showLog("Purchase is gas. Starting gas consumption.");
				try {
					mHelper.consumeAsync(purchase, mConsumeFinishedListener);
				} catch (IabAsyncInProgressException e) {
					complain("Error consuming gas. Another async operation in progress.");
					return;
				}
			} else*/ if (purchase.getSku().equals(SKU_MONTH_1)) {
				// bought 1/4 tank of gas. So consume it.
				showLog("Purchase is gas. Starting gas consumption.");
				try {
					mHelper.consumeAsync(purchase, mConsumeFinishedListener);
				} catch (IabAsyncInProgressException e) {
					complain("Error consuming gas. Another async operation in progress.");
					return;
				}
			}
		}
	};

	// Called when consumption is complete
	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			showLog("Consumption finished. Purchase: " + purchase + ", result: " + result);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null) return;

			// We know this is the "gas" sku because it's the only one we consume,
			// so we don't check which sku was consumed. If you have more than one
			// sku, you probably should check...
			if (result.isSuccess()) {
				// successfully consumed, so we apply the effects of the item in our
				// game world's logic, which in our case means filling the gas tank a bit
				showLog("Consumption successful. Provisioning.");
			}
			else {
				complain("Error while consuming: " + result);
			}
			showLog("End consumption flow.");
		}
	};

	// We're being destroyed. It's important to dispose of the helper here!
	@Override
	public void onDestroy() {
		super.onDestroy();

		// very important:
		if (mBroadcastReceiver != null) {
			unregisterReceiver(mBroadcastReceiver);
		}

		// very important:
		showLog("Destroying helper.");
		if (mHelper != null) {
			mHelper.disposeWhenFinished();
			mHelper = null;
		}
		setDeveloperPayload(Subscription.this, "");
	}

	private void complain(String message) {
		showLog("**** SAS Error: "+message);
		alert("Error: " + message);
	}

	private void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(this);
		bld.setMessage(message);
		bld.setNeutralButton("OK", null);
		showLog("Showing alert dialog: "+message);
		bld.create().show();
	}

	private void showLog(String message) {
		//Log.e(TAG, message);
	}

	private void storeData(Purchase purchase) {

		InAppData appData = new InAppData();
		appData.setDEVELOPER_PAYLOAD(purchase.getDeveloperPayload());
		appData.setORDER_ID(purchase.getOrderId());
		appData.setPACKAGE_NAME(purchase.getPackageName());
		appData.setPRODUCT_ID(purchase.getSku());
		appData.setPURCHASE_TOKEN(purchase.getToken());
		appData.setPURCHASE_STATE(String.valueOf(purchase.getPurchaseState()));
		appData.setFLAG_SENT_UNSENT(appConfig.getUnSend());

		if(purchase.getSku().equals(SKU_MONTH_1))
			appData.setPURCHASE_TIME(DAY_30);
		/*else 
			appData.setPURCHASE_TIME(DAY_7);*/


		UserInfo info = new UserInfo();
		String mUserId = info.getUserId(Subscription.this);

		dbHelper.AddUnSendInAppData(mUserId, appData);
	}
	
	private String DeveloperPayload 	= "DeveloperPayload";
	
	public void setDeveloperPayload(Context ctx, String purchase) {
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(DeveloperPayload, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(DeveloperPayload, purchase); 
		e.commit();
	}
	
	public String getDeveloperPayload(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(DeveloperPayload, Context.MODE_PRIVATE);
		return sharedPrefs.getString(DeveloperPayload, ""); 
	}
}

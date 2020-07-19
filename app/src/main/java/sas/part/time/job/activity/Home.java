package sas.part.time.job.activity;

import sas.part.time.job.R;
import sas.part.time.job.pojo.HomeEnum;
import sas.part.time.job.preference.UserInfo;
import sas.part.time.job.server.AutoDeleteServer;
import sas.part.time.job.service.InAppDataService;
import sas.part.time.job.userInterface.IHomeCallBack;
import sas.part.time.job.userPreference.AutoDeletePref;
import sas.part.time.job.utils.PartTimeUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

//import com.actionbarsherlock.app.SherlockFragmentActivity;
//import com.google.android.gms.ads.AdView;
//import com.slidingmenu.lib.SlidingMenu;

public class Home extends Activity implements IHomeCallBack {

	private ScrollView SC;
	private Button SubmitJob, SearchJob;
	private Button History, EditJob, Slider;
	//private SlidingMenu menu;
	private PartTimeUtils affConfig = PartTimeUtils.getSingleInstance();
	private final int REQUEST_CODE = 102;
	//private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		initView();
		listener();

		SlidingMethod();

		SC.setVerticalScrollBarEnabled(false);

		Intent i = new Intent(Home.this, InAppDataService.class);
		startService(i);
		
		//doAutoDelete();
	}

	private void initView() {

		SC 				= (ScrollView)findViewById(R.id.home_search_scroll);
		SubmitJob 		= (Button)findViewById(R.id.home_submit_job);
		SearchJob 		= (Button)findViewById(R.id.home_search_job);
		History 		= (Button)findViewById(R.id.home_history);
		EditJob 		= (Button)findViewById(R.id.home_edit_job);
		Slider			= (Button)findViewById(R.id.home_slider);
		/*mAdView 		= (AdView) this.findViewById(R.id.home_adView);
		mAdView.loadAd(PartTimeUtils.getAdRequest());*/
	}

	private void listener() {

		SubmitJob.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent i = new Intent(Home.this, SubmitJob.class);
				startActivity(i);
			}
		});
		SearchJob.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent i = new Intent(Home.this, SearchJob.class);
				startActivity(i);
			}
		});
		History.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent i = new Intent(Home.this, History.class);
				startActivity(i);
			}
		});
		EditJob.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent i = new Intent(Home.this, EditList.class);
				startActivity(i);
			}
		});
		/*Slider.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				menu.toggle();
			}
		});*/
	}

	private void SlidingMethod() {		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		width = (int)((width *15)/100); // 15 percent
		/*menu = new SlidingMenu(Home.this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffset(width);
		menu.setFadeDegree(0.35f); 
		menu.attachToActivity(Home.this,SlidingMenu.SLIDING_CONTENT); 
		menu.setMenu(R.layout.fragment_home_menu);
		menu.setSlidingEnabled(true);*/
	}

	@Override
	public void onItemClick(HomeEnum type) {

		Intent i = null;

		switch (type) {

		case PROFILE: 

			i = new Intent(Home.this, EditProfile.class);
			startActivityForResult(i, REQUEST_CODE);

			break;

		case SUBSCRIPTION: 

			/*i = new Intent(Home.this, Subscription.class);
			startActivity(i);*/

			break;

		case ABOUT: 

			i = new Intent(Home.this, WarningAbout.class);
			i.putExtra("Warning", false);
			startActivity(i);

			break;

		case WARNING: 

			i = new Intent(Home.this, WarningAbout.class);
			i.putExtra("Warning", true);
			startActivity(i);

			break;

		case LOGOUT: 

			Logout();

			break;
		default:
			break;
		}
		h.postDelayed(r, 100);
	}

	private Handler h = new Handler();
	private Runnable r= new Runnable() {

		@Override
		public void run() {
			//menu.toggle();
		}
	};

	private void Logout() {

		UserInfo info = new UserInfo();
		info.setEmail(Home.this, "");
		info.setUserId(Home.this, "");

		info.setFirstName(Home.this, "");
		info.setLastName(Home.this, "");
		info.setDateOfBirth(Home.this, "");
		info.setGender(Home.this, "");
		info.setPhoneNumber(Home.this, "");
		info.setImage(Home.this, "");
		info.setAddress(Home.this, "");
		info.setCity(Home.this, "");
		info.setState(Home.this, "");
		info.setCountry(Home.this, "");
		info.setPin(Home.this, "");

		finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		Logout();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	
		if(requestCode == REQUEST_CODE && resultCode == RESULT_OK
				&& resultCode!= RESULT_CANCELED) {
			
			Logout();
		}
	}

	@SuppressWarnings("unused")
	private void doAutoDelete() {
		
		AutoDeletePref pref = new AutoDeletePref();
		Long dateTime = pref.getAutoDelete(Home.this);
		if(dateTime == 0L) {
			
			pref.setAutoDelete(Home.this, System.currentTimeMillis());
		
		} else if(affConfig.getAutoDeleteDate(dateTime)) {
				
			AutoDeleteServer server = new AutoDeleteServer(Home.this);
			server.start();
			
			pref.setAutoDelete(Home.this, System.currentTimeMillis());
		}
	}
	
	/*@Override
	public void onPause() {
		if (mAdView != null) {
			mAdView.pause();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mAdView != null) {
			mAdView.resume();
		}
	}

	@Override
	public void onDestroy() {
		if (mAdView != null) {
			mAdView.destroy();
		}
		super.onDestroy();
	}*/
}

package sas.part.time.job.activity;

import java.io.IOException;

import sas.part.time.job.R;
import sas.part.time.job.database.DataBaseHelper;
import sas.part.time.job.utils.PartTimeUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.ads.AdView;

public class SplashScreen extends Activity {

	private final int SPLASH_DURATION = 2*1000; // 1000 is 1 second
	private AdView mAdView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		DataBaseHelper dbHelper = new DataBaseHelper(this);
		try {
			dbHelper.createDataBase();
			dbHelper.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		PartTimeUtils.getSingleInstance().setUTCFormat();
		mAdView = (AdView) this.findViewById(R.id.splash_adView);
		mAdView.loadAd(PartTimeUtils.getAdRequest());

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(SplashScreen.this, Login.class);
				SplashScreen.this.startActivity(intent);
				finish();
			}

		},SPLASH_DURATION);
	}

	@Override
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
	}
}

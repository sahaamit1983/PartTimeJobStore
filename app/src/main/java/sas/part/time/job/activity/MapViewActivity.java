package sas.part.time.job.activity;

import java.util.ArrayList;
import java.util.HashMap;

import sas.part.time.job.R;
import sas.part.time.job.dialog.UserAlertDialog;
import sas.part.time.job.pojo.JobData;
import sas.part.time.job.utils.PartTimeMessage;
import sas.part.time.job.utils.PartTimeUtils;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapViewActivity extends FragmentActivity {

	private GoogleMap googleMap;
	private LocationManager 			locationManager;
	private Button 						Back;
	private ArrayList<JobData> 			List = new ArrayList<>();
	private HashMap<LatLng, JobData> 	mapList = new HashMap<>();
	private PartTimeMessage 			appMessage = PartTimeMessage.getSingleInstance();
	private String 						Subscription = "";
	//private AdView 						mAdView;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);

		initView();
		listener();
		Subscription = getIntent().getStringExtra("Subscription");
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (locationManager != null) {
			/*Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if(location==null) {
				location = locationManager.getLastKnownLocation(
						LocationManager.GPS_PROVIDER);
			}*/

			if(true) { //location!=null

				List = (ArrayList<JobData>)getIntent().getSerializableExtra("Map");
				for(int i=0;i<List.size();i++) {

					double lat = 0.0, lng = 0.0;
					try {
						lat = Double.parseDouble(List.get(i).getLatitude());
						lng = Double.parseDouble(List.get(i).getLongitude());
					}catch(Exception e) {
						e.printStackTrace();
					}
					LatLng latLng = new LatLng(lat, lng);
					googleMap.addMarker(new MarkerOptions().position(latLng)
							.icon(BitmapDescriptorFactory.
									fromResource(R.drawable.job_pin)));

					mapList.put(latLng, List.get(i));
					if(i==0)
						googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
				}
				googleMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
			} else {
				UserAlertDialog dialog = new UserAlertDialog(MapViewActivity.this);
				dialog.showDialogClose(getString(android.R.string.dialog_alert_title), 
						appMessage.getNetworkFailure());
			}
		} else {
			UserAlertDialog dialog = new UserAlertDialog(MapViewActivity.this);
			dialog.showDialogClose(getString(android.R.string.dialog_alert_title), 
					appMessage.getNetworkFailure());
		}
	}


	private void initView() {

		Back = (Button)findViewById(R.id.map_back);
		/*mAdView = (AdView) this.findViewById(R.id.map_adView);
		mAdView.loadAd(PartTimeUtils.getAdRequest());*/
		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().
				findFragmentById(R.id.map_show);
		fm.getMapAsync(null);
	}

	private void listener() {

		Back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				finish();
			}
		});
		googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker arg0) {
				if(!getIntent().getBooleanExtra("isDetailsvisible", false)) {
					Intent i = new Intent(MapViewActivity.this, JobDetails.class);
					i.putExtra("Details", mapList.get(arg0.getPosition()));
					i.putExtra("Subscription", Subscription);
					startActivity(i);
				}
				finish();
			}
		});
		googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

			// Use default InfoWindow frame
			@Override
			public View getInfoWindow(Marker arg0) {
				return null;
			}

			// Defines the contents of the InfoWindow
			@Override
			public View getInfoContents(Marker arg0) {

				View v = getLayoutInflater().inflate(R.layout.windowlayout, null);
				TextView Title = (TextView) v.findViewById(R.id.title);
				TextView Address = (TextView) v.findViewById(R.id.location);
				TextView Amount = (TextView) v.findViewById(R.id.amount);

				String address = "";

				if(!TextUtils.isEmpty(mapList.get(arg0.getPosition()).
						getAddress().trim())){ 
					address = mapList.get(arg0.getPosition()).getAddress().trim() +" ";
				}
				if(!TextUtils.isEmpty(mapList.get(arg0.getPosition()).
						getCity().trim())){ 
					address = address + 
							mapList.get(arg0.getPosition()).getCity().trim() +" ";
				}
				if(!TextUtils.isEmpty(mapList.get(arg0.getPosition()).
						getState().trim())){ 
					address = address + 
							mapList.get(arg0.getPosition()).getState().trim() +" ";
				}
				if(!TextUtils.isEmpty(mapList.get(arg0.getPosition()).
						getCountry().trim())){ 
					address = address + 
							mapList.get(arg0.getPosition()).getCountry().trim() +" ";
				}
				if(!TextUtils.isEmpty(mapList.get(arg0.getPosition()).
						getPin().trim())){ 
					address = address + 
							mapList.get(arg0.getPosition()).getPin().trim();
				}
				Address.setText(address);

				Title.setText(mapList.get(arg0.getPosition()).getJobType());
				Amount.setText(mapList.get(arg0.getPosition()).getCurrency()
						+" "+mapList.get(arg0.getPosition()).getAmount());

				return v;

			}
		});
	}

	@Override
	protected void onDestroy() {
		/*if (mAdView != null) {
			mAdView.destroy();
		}*/
		super.onDestroy();
		mapList.clear();
		List.clear();
		try {
			FragmentManager fm = getSupportFragmentManager();
			Fragment fragment = (fm.findFragmentById(R.id.map_show));
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(fragment);
			ft.commit();
		} catch (Exception e) {
			e.printStackTrace();
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
	}*/
}

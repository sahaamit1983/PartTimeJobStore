package sas.part.time.job.fragment;

import sas.part.time.job.R;
import sas.part.time.job.imageLoader.ImageLoader;
import sas.part.time.job.pojo.HomeEnum;
import sas.part.time.job.preference.UserInfo;
import sas.part.time.job.userInterface.IHomeCallBack;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class HomeFragment extends Fragment {

	private IHomeCallBack CallBack;
	private TextView Name, PhoneNumber, Email, Address;
	private TextView Subscription, About, Warning, Logout;
	private Button Profile;
	private ImageView Pic;
	private ImageLoader imageLoader;
	private ProgressBar	Pb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home_slide, container, false);
		
		initView(rootView);
		listener(rootView);
		
		//CallBack = (IHomeCallBack)getSherlockActivity();
	
		rootView.setOnTouchListener(new View.OnTouchListener() {
	        @SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {
	            return true;
	        }
	    });
		About.setVisibility(View.GONE);
		setData();
		return rootView;
	}
	
	private void initView(View v) {
		
		Name = (TextView)v.findViewById(R.id.home_fragment_profile_name);
		PhoneNumber= (TextView)v.findViewById(R.id.home_fragment_phone);
		Email= (TextView)v.findViewById(R.id.home_fragment_email);
		Address = (TextView)v.findViewById(R.id.home_fragment_location);
		Subscription = (TextView)v.findViewById(R.id.home_fragment_subscription);
		About = (TextView)v.findViewById(R.id.home_fragment_about);
		Warning = (TextView)v.findViewById(R.id.home_fragment_warning);
		Logout = (TextView)v.findViewById(R.id.home_fragment_logout);
		Profile = (Button)v.findViewById(R.id.home_fragment_edit_profile);
		Pic = (ImageView)v.findViewById(R.id.home_fragment_profile_pic);
		Pb	= (ProgressBar)v.findViewById(R.id.home_fragment_profile_pic_loader);
	}
	private void listener(View v) {

		Profile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CallBack.onItemClick(HomeEnum.PROFILE);
			}
		});
		Subscription.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CallBack.onItemClick(HomeEnum.SUBSCRIPTION);
			}
		});
		About.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CallBack.onItemClick(HomeEnum.ABOUT);
			}
		});
		Warning.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CallBack.onItemClick(HomeEnum.WARNING);
			}
		});
		Logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CallBack.onItemClick(HomeEnum.LOGOUT);
			}
		});
	}
	
	private void setData() {
		
		imageLoader = new ImageLoader(getActivity(),R.drawable.profile_pic);
		UserInfo info = new UserInfo();
		Name.setText(info.getFirstName(getActivity())+" "+info.getLastName(getActivity()));
		
		Pb.setVisibility(View.VISIBLE);
		imageLoader.DisplayImage(info.getImage(getActivity()), Pic, Pb);
		
		PhoneNumber.setText(info.getPhoneNumber(getActivity())); 
		Email.setText(info.getEmail(getActivity())); 
		Address.setText(info.getAddress(getActivity()) +", "
		+ info.getCity(getActivity())+", "
		+ info.getState(getActivity())+", "
		+ info.getCountry(getActivity())); 
	}
}
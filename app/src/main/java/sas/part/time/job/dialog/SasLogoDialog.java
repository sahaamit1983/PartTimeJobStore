package sas.part.time.job.dialog;

import sas.part.time.job.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class SasLogoDialog extends Dialog {

	private ImageView imageFlip,imageCricle;
	private Handler	AnimHandler1 = new Handler();
	private final int Time = 500;
	private final int Time2 = 2000;
	private Context mContext;
	
	public SasLogoDialog(Context context) {     
		super(context);
		setCancelable(false);
		mContext = context;
		getWindow().setBackgroundDrawable(new ColorDrawable(android.R.drawable.screen_background_dark_transparent));
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_sas);
		
		initialize();
		
		Animation a = AnimationUtils.loadAnimation((Activity)mContext, R.anim.progress_anim);
		a.setDuration(Time2);
		imageCricle.startAnimation(a);

		a.setInterpolator(new Interpolator()
		{
			private final int frameCount = 50;

			@Override
			public float getInterpolation(float input)
			{
				return (float)Math.floor(input*frameCount)/frameCount;
			}
		});
		AnimHandler1.post(new Runnable1());
	}
	
	private void initialize() {
		
		imageCricle = (ImageView) findViewById(R.id.image_circle);
		imageFlip 	= (ImageView) findViewById(R.id.image_flipper);
	}
	
	private class Runnable1 implements Runnable {

		@Override
		public void run() {
			Anime1();
			AnimHandler1.postDelayed(new Runnable2(),Time);
		}
	}
	
	private class Runnable2 implements Runnable {

		@Override
		public void run() {
			Anime2();
			AnimHandler1.postDelayed(new Runnable1(),Time);
		}
	}
	
	private void Anime1() {
		
		Animation b = AnimationUtils.loadAnimation((Activity)mContext, R.anim.progress_inside_anim1);
		b.setDuration(Time);
		imageFlip.startAnimation(b);
		
		b.setInterpolator(new Interpolator()
		{
			private final int frameCount = 50;

			@Override
			public float getInterpolation(float input)
			{
				return (float)Math.floor(input*frameCount)/frameCount;
			}
		});
		
	}
	private void Anime2() {
		
		Animation c = AnimationUtils.loadAnimation((Activity)mContext, R.anim.progress_inside_anim2);
		c.setDuration(Time);
		imageFlip.startAnimation(c);
		
		c.setInterpolator(new Interpolator()
		{
			private final int frameCount = 50;

			@Override
			public float getInterpolation(float input)
			{
				return (float)Math.floor(input*frameCount)/frameCount;
			}
		});
	}
}

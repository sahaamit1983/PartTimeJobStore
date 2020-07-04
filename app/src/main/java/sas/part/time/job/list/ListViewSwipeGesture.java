package sas.part.time.job.list;

import sas.part.time.job.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class ListViewSwipeGesture implements View.OnTouchListener {

	private Activity activity;

	// Cached ViewConfiguration and system-wide constant values
	private int mSlop;
	private int mMinFlingVelocity;
	private int mMaxFlingVelocity;

	// Fixed properties
	private ListView mListView;

	//private DismissCallbacks mCallbacks;
	private int mViewWidth = 1; // 1 and not 0 to prevent dividing by zero
	private int largewidth = 1;
	private int textwidth = 1;
	private int textheight = 1;

	// Transient properties
	private int mDismissAnimationRefCount = 0;
	private float mDownX;
	private boolean mSwiping;
	private VelocityTracker mVelocityTracker;
	private int temp_position,opened_position,stagged_position;
	private ViewGroup mDownView,old_mDownView;
	private ViewGroup mDownView_parent;
	private TextView Delete;
	private boolean mPaused;
	public 	boolean moptionsDisplay = false;
	public 	static TouchCallbacks tcallbacks;

	public ListViewSwipeGesture(ListView listView, TouchCallbacks Callbacks, Activity ac){

		ViewConfiguration vc    =   ViewConfiguration.get(listView.getContext());
		mSlop                   =   vc.getScaledTouchSlop();
		mMinFlingVelocity       =   vc.getScaledMinimumFlingVelocity() * 16;
		mMaxFlingVelocity       =   vc.getScaledMaximumFlingVelocity();
		mListView               =   listView;
		activity                =   ac;
		tcallbacks              =   Callbacks;

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) { //Invokes OnClick Functionality
				if(!moptionsDisplay){
					tcallbacks.OnClickListView(temp_position);
				}
			}
		});
	}

	public interface TouchCallbacks {       //Callback functions
		void DeleteTaskListView(int position);
		void OnClickListView(int position);
	}

	@SuppressLint({ "ResourceAsColor", "ClickableViewAccessibility" })
	@Override
	public boolean onTouch(final View view, MotionEvent event) {

		if (mViewWidth < 2) {
			mViewWidth  	=   mListView.getWidth();
			int tenWidth	=   mViewWidth / 10; // leave 10% for list
			mViewWidth		= 	mViewWidth - tenWidth;
			textwidth		=	mViewWidth/3;
			largewidth		=	textwidth ; // Add textwidth + textwidth if need to add
		}									//  more button in swipe

		int tempwidth = textwidth;

		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN: {

			if (mPaused) {
				return false;
			}
			Rect rect               =   new Rect();
			int childCount          =   mListView.getChildCount();
			int[] listViewCoords    = new int[2];
			mListView.getLocationOnScreen(listViewCoords);
			int x                   = (int) event.getRawX() - listViewCoords[0];
			int y                   = (int) event.getRawY() - listViewCoords[1];
			ViewGroup child;
			for (int i = 0; i < childCount; i++) {
				child = (ViewGroup) mListView.getChildAt(i);
				child.getHitRect(rect);
				if (rect.contains(x, y)) {
					mDownView_parent    =   child;
					mDownView           =   (ViewGroup) child.findViewById(R.id.list_display_view_container);

					if(mDownView_parent.getChildCount()==1){
						textheight	=	mDownView_parent.getHeight();
						SetBackGroundforList(i);
					}

					if(old_mDownView!=null && mDownView!=old_mDownView){
						ResetListItem(old_mDownView);
						old_mDownView=null;
						return false;
					}
					break;
				}
			}

			if (mDownView != null) {
				mDownX              =   event.getRawX();
				mVelocityTracker    =   VelocityTracker.obtain();
				mVelocityTracker.addMovement(event);
			} else {
				mDownView               =   null;
			}

			//mSwipeDetected              =   false;
			temp_position               =   mListView.pointToPosition((int) event.getX(), (int) event.getY());
			view.onTouchEvent(event);
			return true;
		}

		case MotionEvent.ACTION_UP: {

			if (mVelocityTracker == null) {
				break;
			}
			float deltaX                =   event.getRawX() - mDownX;
			mVelocityTracker.addMovement(event);
			mVelocityTracker.computeCurrentVelocity(1000); // 1000 by defaut but
			float velocityX             =   mVelocityTracker.getXVelocity();											// it was too much
			float absVelocityX          =   Math.abs(velocityX);
			float absVelocityY          =   Math.abs(mVelocityTracker.getYVelocity());
			boolean swipe               =   false;
			boolean swipeRight          =   false;

			if (Math.abs(deltaX) > tempwidth) {
				swipe               =   true;
				swipeRight          =   deltaX > 0;

			}else if (mMinFlingVelocity <= absVelocityX && absVelocityX <= mMaxFlingVelocity && absVelocityY < absVelocityX) {
				// dismiss only if flinging in the same direction as dragging
				swipe   = (velocityX < 0) == (deltaX < 0);
				swipeRight          = mVelocityTracker.getXVelocity() > 0;
			}

			if (deltaX < 0 && swipe) {
				mListView.setDrawSelectorOnTop(false);

				if (swipe && !swipeRight && deltaX <= -tempwidth) {
					FullSwipeTrigger();
				} else if (deltaX >= -textwidth) {
					ResetListItem(mDownView);
				}else {
					ResetListItem(mDownView);
				}
			} else {
				ResetListItem(mDownView);
			}

			mVelocityTracker.recycle();
			mVelocityTracker    =   null;
			mDownX              =   0;
			mDownView           =   null;
			mSwiping            =   false;
			break;
		}

		case MotionEvent.ACTION_MOVE: {

			float deltaX = event.getRawX() - mDownX;
			if (mVelocityTracker == null || mPaused ||deltaX>0) {
				break;
			}

			mVelocityTracker.addMovement(event);

			if (Math.abs(deltaX) > mSlop) {
				mSwiping = true;
				mListView.requestDisallowInterceptTouchEvent(true);

				// Cancel ListView's touch (un-highlighting the item)
				MotionEvent cancelEvent     =   MotionEvent.obtain(event);
				cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
						(event.getActionIndex()
								<< MotionEvent.ACTION_POINTER_INDEX_SHIFT));
				mListView.onTouchEvent(cancelEvent);
				cancelEvent.recycle();
			}
			if (mSwiping && deltaX < 0 ) {

				int width	=	largewidth;

				if (-deltaX<width){
					mDownView.setTranslationX(deltaX);
					return false;
				}
				return false;
			}else if(mSwiping){
				ResetListItem(mDownView);
			}
			break;
		}
		}
		return false;
	}

	@SuppressLint("InflateParams")
	private void SetBackGroundforList(int position) {
		// TODO Auto-generated method stub
		Delete =	new TextView(activity.getApplicationContext());
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp1.addRule(RelativeLayout.CENTER_VERTICAL);
		Delete.setId(111111);
		Delete.setLayoutParams(lp1);
		Delete.setGravity(Gravity.CENTER_HORIZONTAL);
		//Delete.setText(activity.getResources().getString(R.string.delete));
		Delete.setWidth(textwidth);
		Delete.setPadding(0, textheight/4, 0, 0);
		Delete.setHeight(textheight);
		Delete.setTextColor(Color.parseColor("#FFFFFF"));
		Delete.setCompoundDrawablesWithIntrinsicBounds(null , activity.getResources().getDrawable(R.drawable.delete_icon), null, null );
		Delete.setBackgroundColor(Color.parseColor("#FF0000"));
		mDownView_parent.addView(Delete, 0);
	}

	private void ResetListItem(View tempView) { 
		Log.d("Shortlist reset call","Works");
		tempView.animate().translationX(0).alpha(1f).setListener(new AnimatorListenerAdapter(){
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				int count=mDownView_parent.getChildCount()-1;
				for(int i=0;i<count;i++) {
					//View V=	mDownView_parent.getChildAt(i);
					//Log.d("removing child class",""+V.getClass());
					mDownView_parent.removeViewAt(0);
				}
				moptionsDisplay	= false;
			}
		});
		stagged_position=-1;
		opened_position=-1;
	}

	private void FullSwipeTrigger(){
		Log.d("FUll Swipe trigger call","Works**********************"+mDismissAnimationRefCount);
		old_mDownView	=	mDownView;
		int width	=	largewidth;

		mDownView.animate().translationX(-width).setDuration(300)
		.setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);

				moptionsDisplay     =   true;
				stagged_position	=	temp_position;
				Delete.setOnTouchListener(new touchClass());
			}

			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
			}
		});
	}

	private class touchClass implements OnTouchListener {

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			opened_position     =   mListView.getPositionForView((View) v.getParent());
			switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN: {
				if(opened_position==stagged_position && moptionsDisplay){
					try {
						ResetListItem(old_mDownView);
					} catch (Exception e) {
						e.printStackTrace();
					}
					switch (v.getId()) {
					case 111111:
						tcallbacks.DeleteTaskListView(temp_position);
						return true;
					}
				}
			}
			return false;
			}
			return false;
		}
	}
}
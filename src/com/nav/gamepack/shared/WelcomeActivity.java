/**
 * 
 */
package com.nav.gamepack.shared;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.nav.gamepack.R;
import android.view.animation.Animation;

;

/**
 * @author naveed
 * 
 */
public class WelcomeActivity extends Activity {
	ImageView imageViewCloudMediumAnimated,imageViewCloudSmallAnimated;
	Animation slideLeftSlowAnimation,slideLeftFastAnimation;
	AnimationDrawable myJigsawAnim;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		imageViewCloudMediumAnimated = (ImageView) findViewById(R.id.imageViewCloudMediumAnimated);
		imageViewCloudSmallAnimated=(ImageView)findViewById(R.id.imageViewCloudSmallAnimated);
		
		slideLeftSlowAnimation = AnimationUtils.loadAnimation(this,
				R.anim.slide_left_slow);
		slideLeftFastAnimation = AnimationUtils.loadAnimation(this,
				R.anim.slide_left_fast);
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onWindowFocusChanged(boolean)
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	if(hasFocus){
		imageViewCloudMediumAnimated.startAnimation(slideLeftSlowAnimation);
		imageViewCloudSmallAnimated.startAnimation(slideLeftFastAnimation);
		ImageView im=(ImageView)findViewById(R.id.imageViewMyJigsaw);
		im.setBackgroundResource(R.drawable.jigsaw_walking);
		myJigsawAnim = (AnimationDrawable) im.getBackground();
		myJigsawAnim.start();
		
	}
	}
}

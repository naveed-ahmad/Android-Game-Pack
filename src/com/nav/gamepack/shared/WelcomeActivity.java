/**
 * 
 */
package com.nav.gamepack.shared;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.nav.gamepack.R;
import com.nav.gamepack.puzzle.jigsaw.ImageChooserActivity;
import com.nav.gamepack.puzzle.jigsaw.JigsawActivity;

import android.view.animation.Animation;

/**
 * @author naveed
 * 
 */
public class WelcomeActivity extends Activity {
	private final int CHOOSE_IMAGE_CODE = 36;
	private final String TAG = "WelcomeActivity";
	private ImageView slidingDrawerHandle, cloudMedium1, cloudMedium2, cloudSmall1;
	private SlidingDrawer slidingDrawerMenu;
	private BitmapDrawable imgDrawableHandelClose, imgDrawableHandelOpen;
	private Animation lft2RgtSlow, lft2RgtNormal, lft2RgtFast;
	private View.OnClickListener cloudClickListener, btnClickListner;
	private Animation cloudFallAnimation;
	private RelativeLayout.LayoutParams waterDroplayoutParams;
	private Button btnStartGame, btnAboutUs, btnHelp;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		slidingDrawerHandle = (ImageView) findViewById(R.id.slidingDrawerHandle);
		slidingDrawerMenu = (SlidingDrawer) findViewById(R.id.slidingDrawerGameMenu);
		btnStartGame = (Button) findViewById(R.id.btnStartGame);
		btnAboutUs = (Button) findViewById(R.id.btnAboutUs);

		// slidingDrawerMenu.setOnDrawerOpenListener(this);
		imgDrawableHandelClose = new BitmapDrawable(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.sliding_drawer_handler_close)));
		imgDrawableHandelOpen = new BitmapDrawable(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.sliding_drawer_handler_open)));
		lft2RgtSlow = AnimationUtils.loadAnimation(this, R.anim.slide_left_slow);
		lft2RgtNormal = AnimationUtils.loadAnimation(this, R.anim.slide_left_normal);
		lft2RgtFast = AnimationUtils.loadAnimation(this, R.anim.slide_left_fast);
		cloudFallAnimation = AnimationUtils.loadAnimation(this, R.anim.cloud_fall_down);
		waterDroplayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		cloudMedium1 = (ImageView) findViewById(R.id.imgViewCloudMedium1);
		cloudMedium2 = (ImageView) findViewById(R.id.imgViewCloudMedium2);
		cloudSmall1 = (ImageView) findViewById(R.id.imgViewCloudSmall);
		hookupListener();

	}

	/**
	 * 
	 */
	private void hookupListener() {
		cloudClickListener = new OnClickListener() {
			public void onClick(View v) {
				playWaterDropFallAnimation((ImageView) v);

			}
		};
		cloudMedium1.setOnClickListener(cloudClickListener);
		cloudMedium2.setOnClickListener(cloudClickListener);
		cloudSmall1.setOnClickListener(cloudClickListener);
		btnClickListner = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == btnStartGame.getId()) {
					Intent intentStartGame = new Intent();
					intentStartGame.setClass(WelcomeActivity.this, JigsawActivity.class);
					startActivity(intentStartGame);
				}
			}
		};
		btnStartGame.setOnClickListener(btnClickListner);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onWindowFocusChanged(boolean)
	 */
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		cloudMedium1.startAnimation(lft2RgtSlow);
		cloudMedium2.startAnimation(lft2RgtNormal);
		cloudSmall1.startAnimation(lft2RgtFast);
		cloudMedium1.setVisibility(View.VISIBLE);
		cloudMedium2.setVisibility(View.VISIBLE);
		cloudSmall1.setVisibility(View.VISIBLE);
Toast.makeText(this, "Start", Toast.LENGTH_LONG);
		ImageView img = (ImageView) findViewById(R.id.animImgViewTree1);
		img.setBackgroundResource(R.drawable.tree_anim);
        img.setVisibility(View.VISIBLE);
		// Get the background, which has been compiled to an
		// AnimationDrawable object.
		AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

		// Start the animation (looped playback by default).
		frameAnimation.start();
	}

	private void playWaterDropFallAnimation(ImageView cloud) {
		Log.i(TAG, "Start animation for  water fall");
		int[] position = new int[2];
		cloud.getLocationOnScreen(position);

		Log.i(TAG, "Left=" + position[0] + " Top=" + position[1] + "Layout");

		Animation tl = AnimationUtils.loadAnimation(this, R.anim.cloud_fall_down);// =new
																					// TranslateAnimation(position[0],position[0]+6,position[1],
																					// position[1]+400);
		// tl.setInterpolator(new LinearInterpolator());
		// tl.setDuration(2000);
		ImageView waterDropFirst = new ImageView(this);
		waterDropFirst.setBackgroundResource(R.drawable.water_drop);

		this.addContentView(waterDropFirst, waterDroplayoutParams);

		waterDropFirst.startAnimation(tl);
	}

}

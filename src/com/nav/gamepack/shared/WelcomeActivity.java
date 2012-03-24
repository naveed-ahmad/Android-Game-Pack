/**
 * 
 */
package com.nav.gamepack.shared;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.nav.gamepack.R;
import com.nav.gamepack.R.id;
import com.nav.gamepack.puzzle.jigsaw.ImageChooserActivity;
import com.nav.gamepack.puzzle.jigsaw.JigsawActivity;
import com.nav.gamepack.puzzle.jigsaw.JigsawSettingActivity;

import android.view.animation.Animation;

/**
 * @author naveed
 * 
 */
public class WelcomeActivity extends Activity implements OnTouchListener, AnimationListener {
	private final int GET_JIGSAW_SETTING_REQUEST = 1032;
	private final String TAG = "WelcomeActivity";
	private ImageView imgViewCloudMediumSlow, imgViewCloudMediumNormal, imgViewCloudSmallFast, imgViewAnimTree, imgViewJigsawWalking;
	private Animation jigsawJumpAnimation, cloudMediumAnimationNormal, cloudMediumAnimationSlow, cloudSmallAnimationFast, jigsawWalkingAnimation;
	private View.OnClickListener btnClickListner;
	AnimationDrawable jigsawWalkingFrameAnimation;
	private RelativeLayout.LayoutParams waterDroplayoutParams;
	private Button btnStartGame, btnAboutUs, btnHelp;
	private AnimationDrawable treeAnimation;
	TranslateAnimation trnsAnimCloudSlow;
	private RelativeLayout rltvLayoutBoundry;

	Boolean isAnimationRunning, isJigsawJumpAnimEnabled;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// slidingDrawerHandle = (ImageView)
		// findViewById(R.id.slidingDrawerHandle);
		// slidingDrawerMenu = (SlidingDrawer)
		// findViewById(R.id.slidingDrawerGameMenu);
		btnStartGame = (Button) findViewById(R.id.btnStartGame);
		btnAboutUs = (Button) findViewById(R.id.btnAboutUs);
		btnHelp = (Button) findViewById(R.id.btnHelp);
		imgViewJigsawWalking = (ImageView) findViewById(R.id.imgViewJigsawWalking);

		// BitmapDrawable(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.sliding_drawer_handler_close)));
		// imgDrawableHandelOpen = new
		// BitmapDrawable(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.sliding_drawer_handler_open)));
		imgViewAnimTree = (ImageView) findViewById(R.id.animImgViewTree);
		rltvLayoutBoundry = (RelativeLayout) findViewById(R.id.rltvLayoutBoundryWelcome);
		// treeAnimation = (AnimationDrawable) imgViewAnimTree.getBackground();
		cloudMediumAnimationSlow = AnimationUtils.loadAnimation(this, R.anim.slide_left_slow_cloud);
		cloudMediumAnimationNormal = AnimationUtils.loadAnimation(this, R.anim.slide_left_normal_cloud);
		cloudSmallAnimationFast = AnimationUtils.loadAnimation(this, R.anim.slide_left_fast_cloud);
		jigsawJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.jigsaw_jump);
		// waterDropFallAnimation = AnimationUtils.loadAnimation(this,
		// R.anim.water_drop_fall_down);
		jigsawWalkingAnimation = AnimationUtils.loadAnimation(this, R.anim.jigsaw_walk);
		waterDroplayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		imgViewCloudMediumSlow = (ImageView) findViewById(R.id.imgViewCloudMediumSlow);
		imgViewCloudMediumNormal = (ImageView) findViewById(R.id.imgViewCloudMediumNormal);
		imgViewCloudSmallFast = (ImageView) findViewById(R.id.imgViewCloudSmallFast);
		jigsawWalkingFrameAnimation = (AnimationDrawable) imgViewJigsawWalking.getBackground();

		hookupListener();
		// Display display = getWindowManager().getDefaultDisplay();
		//
		// trnsAnimCloudSlow = new TranslateAnimation(display.getWidth(), 10,
		// cloudMedium1.getTop(), cloudMedium1.getTop());
		// trnsAnimCloudSlow.setDuration(5000);
		// trnsAnimCloudSlow.setFillAfter(true);
		isAnimationRunning = true;
		isJigsawJumpAnimEnabled = false;
	}

	/**
	 * 
	 */
	private void hookupListener() {
		btnClickListner = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getId() == btnStartGame.getId()) {
					Intent intentGetJigsawSetting = new Intent();
					intentGetJigsawSetting.setClass(WelcomeActivity.this, JigsawSettingActivity.class);
					startActivityForResult(intentGetJigsawSetting, GET_JIGSAW_SETTING_REQUEST);
				} else if (v.getId() == btnHelp.getId())
					showHelpDialog();
				else if (v.getId() == btnAboutUs.getId())
					showAboutUsDialog();
				else if (v.getId() == imgViewJigsawWalking.getId()) {
					if (isJigsawJumpAnimEnabled)
						playJigsawJumpAnimation();
				}

			}
		};
		jigsawWalkingAnimation.setAnimationListener(this);
		imgViewJigsawWalking.setOnClickListener(btnClickListner);
		btnAboutUs.setOnClickListener(btnClickListner);
		btnHelp.setOnClickListener(btnClickListner);
		btnStartGame.setOnClickListener(btnClickListner);
		rltvLayoutBoundry.setOnTouchListener(this);
		jigsawWalkingAnimation.setAnimationListener(this);
		imgViewAnimTree.setOnTouchListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onWindowFocusChanged(boolean)
	 */
	// @Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		startAnimations();
	}

	/**
	 * 
	 */
	private void startAnimations() {
		if (!isAnimationRunning)
			stopAnimations();
		imgViewCloudMediumSlow.startAnimation(cloudMediumAnimationSlow);
		imgViewCloudMediumNormal.startAnimation(cloudMediumAnimationNormal);
		imgViewCloudSmallFast.startAnimation(cloudSmallAnimationFast);
		imgViewCloudMediumSlow.setVisibility(View.VISIBLE);
		imgViewCloudMediumNormal.setVisibility(View.VISIBLE);
		imgViewCloudSmallFast.setVisibility(View.VISIBLE);
		imgViewAnimTree.setVisibility(View.VISIBLE);

		imgViewAnimTree.post(new Runnable() {
			public void run() {
				AnimationDrawable frameAnimation = (AnimationDrawable) imgViewAnimTree.getBackground();
				frameAnimation.start();
			}
		});
		if (!isJigsawJumpAnimEnabled) {
			imgViewJigsawWalking.post(new Runnable() {
				public void run() {
					jigsawWalkingFrameAnimation.start();
				}
			});
			imgViewJigsawWalking.startAnimation(jigsawWalkingAnimation);
		}
		isAnimationRunning = true;
	}

	private void stopAnimations() {
		imgViewAnimTree.clearAnimation();
		imgViewCloudSmallFast.clearAnimation();
		imgViewCloudMediumNormal.clearAnimation();
		imgViewCloudMediumSlow.clearAnimation();
		jigsawWalkingFrameAnimation.stop();
	}

	private void playWaterDropFallAnimation(int x, int y, int animLenght, int delayOffSet) {
		playWaterDropFallAnimation(x, x, y, animLenght, delayOffSet);
	}

	private void playWaterDropFallAnimation(int startX, int endX, int startY, int animVertLength, int delayOffSet) {
		Log.i(TAG, "Start animation for  water fall X=" + startX + ", y=" + startY);
		final ImageView imgViewWaterDrop = new ImageView(this);
		imgViewWaterDrop.setBackgroundResource(R.drawable.water_drop);

		// rltvLayoutBoundry.addView(imgViewWaterDrop, waterDroplayoutParams);
		this.addContentView(imgViewWaterDrop, waterDroplayoutParams);
		Animation waterDropAnim = new TranslateAnimation(startX, endX, startY, startY + animVertLength);
		waterDropAnim.setDuration(2000);
		waterDropAnim.setInterpolator(new AccelerateInterpolator(1.0f));
		// waterDropAnim.setAnimationListener(new AnimationListener() {
		// public void onAnimationStart(Animation arg0) {
		// }
		//
		// public void onAnimationRepeat(Animation arg0) {
		// }
		//
		// public void onAnimationEnd(Animation arg0) {
		// try {
		// rltvLayoutBoundry.removeView(imgViewWaterDrop);
		// } catch (Exception e) {
		// Log.e(TAG, "Error while removing waterDrop", e);
		// }
		// }
		// });
		waterDropAnim.setStartOffset(delayOffSet);
		imgViewWaterDrop.startAnimation(waterDropAnim);
	}

	private void playSeedAnimation(int x, int y, int animLength) {
		Log.i(TAG, "Start animation for seed fall X=" + x + ", y=" + y);
		final ImageView imgViewSeed = new ImageView(this);
		Random r = new Random();
		final RelativeLayout.LayoutParams seedLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (r.nextInt(3) == 1)
			imgViewSeed.setBackgroundResource(R.drawable.seed);
		else
			imgViewSeed.setBackgroundResource(R.drawable.seed_orange);

		this.addContentView(imgViewSeed, seedLayoutParams);
		imgViewSeed.setKeepScreenOn(false);

		final Animation seedDropAnim = new TranslateAnimation(x, x, y, y + animLength);

		seedDropAnim.setDuration(1000);
		seedDropAnim.setInterpolator(new AccelerateInterpolator(1.0f));
		seedDropAnim.setFillAfter(true);

		final Animation fadeOutAnim = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		fadeOutAnim.setFillAfter(true);
		//
		// seedDropAnim.setAnimationListener(new AnimationListener() {
		// @Override
		// public void onAnimationStart(Animation anim) {
		//
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animation anim) {
		//

		// }
		//
		// @Override
		// public void onAnimationEnd(Animation anim) {
		// // seedLayoutParams.setMargins(-100, 0, 0, 0);
		// // imgViewSeed.startAnimation(fadeOutAnim);
		//
		// }
		// });

		// waterDropAnim.setAnimationListener(new AnimationListener() {
		// public void onAnimationStart(Animation arg0) {
		// }
		//
		// public void onAnimationRepeat(Animation arg0) {
		// }
		//
		// public void onAnimationEnd(Animation arg0) {
		// try {
		// rltvLayoutBoundry.removeView(imgViewWaterDrop);
		// } catch (Exception e) {
		// Log.e(TAG, "Error while removing waterDrop", e);
		// }
		// }
		// });

		imgViewSeed.startAnimation(seedDropAnim);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View view, MotionEvent touchArgs) {
		if (touchArgs.getAction() == MotionEvent.ACTION_DOWN)
			return true;
		boolean clickHandled = false;
		int touchPositionX = (int) touchArgs.getX();
		int toucPositionY = (int) touchArgs.getY();
		int animStartX;

		Transformation transformation = new Transformation();
		float[] matrix = new float[9];

		int clickOffsetLeft;
		int clickOffsetRight;
		int imgTop;
		int imgBottom;
		int viewId = view.getId();
		if (viewId == imgViewAnimTree.getId()) {
			// playSeedAnimation(touchPositionX, toucPositionY,
			// imgViewAnimTree.getHeight()); //having issue in fadeout seed so
			// lets delay it
			clickHandled = true;
		} else if (viewId == rltvLayoutBoundry.getId()) {
			// check if click on imgViewCloudMediumNormal
			cloudMediumAnimationNormal.getTransformation(AnimationUtils.currentAnimationTimeMillis(), transformation);
			transformation.getMatrix().getValues(matrix);
			clickOffsetLeft = (int) matrix[Matrix.MTRANS_X] + imgViewCloudMediumNormal.getLeft();
			clickOffsetRight = clickOffsetLeft + imgViewCloudMediumNormal.getWidth();
			imgTop = imgViewCloudMediumNormal.getTop();
			imgBottom = imgViewCloudMediumNormal.getBottom();

			if (clickOffsetLeft < touchPositionX && clickOffsetRight > touchPositionX && toucPositionY >= imgTop && toucPositionY <= imgBottom) {
				Log.i(TAG, "Playing waterdrop animation for cloudMediumAnimationNormal");
				clickHandled = true;
				animStartX = (clickOffsetRight + clickOffsetLeft) / 2;
				playWaterDropFallAnimation(animStartX, animStartX + 15, toucPositionY, 400, 0);
				playWaterDropFallAnimation(animStartX + 10, animStartX + 30, toucPositionY, 410, 100);
				playWaterDropFallAnimation(animStartX - 15, animStartX - 40, toucPositionY, 405, 250);
			}

			// check if click on imgViewCloudMediumSlow
			cloudMediumAnimationSlow.getTransformation(AnimationUtils.currentAnimationTimeMillis(), transformation);
			transformation.getMatrix().getValues(matrix);
			imgTop = imgViewCloudMediumSlow.getTop();
			imgBottom = imgViewCloudMediumSlow.getBottom();

			clickOffsetLeft = (int) matrix[Matrix.MTRANS_X] + imgViewCloudMediumSlow.getLeft();
			clickOffsetRight = clickOffsetLeft + imgViewCloudMediumSlow.getWidth();

			if (clickOffsetLeft < touchPositionX && clickOffsetRight > touchPositionX && toucPositionY >= imgTop && toucPositionY <= imgBottom && toucPositionY >= imgTop && toucPositionY <= imgBottom) {
				Log.i(TAG, "Playing waterdrop animation for cloudMediumAnimationSlow");
				clickHandled = true;
				animStartX = (clickOffsetRight + clickOffsetLeft) / 2;
				playWaterDropFallAnimation(animStartX, animStartX + 15, toucPositionY, 400, 0);
				playWaterDropFallAnimation(animStartX + 10, animStartX + 30, toucPositionY, 410, 100);
				playWaterDropFallAnimation(animStartX - 15, animStartX - 40, toucPositionY, 405, 250);
			}

			// check if click on imgViewCloudSmallFast
			cloudSmallAnimationFast.getTransformation(AnimationUtils.currentAnimationTimeMillis(), transformation);
			transformation.getMatrix().getValues(matrix);
			imgTop = imgViewCloudSmallFast.getTop();
			imgBottom = imgViewCloudSmallFast.getBottom();
			clickOffsetLeft = (int) matrix[Matrix.MTRANS_X] + imgViewCloudSmallFast.getLeft();
			clickOffsetRight = clickOffsetLeft + imgViewCloudSmallFast.getWidth();

			if (clickOffsetLeft < touchPositionX && clickOffsetRight > touchPositionX && toucPositionY >= imgTop && toucPositionY <= imgBottom) {
				Log.i(TAG, "Playing waterdrop animation for imgViewCloudSmallFast");
				clickHandled = true;
				animStartX = (clickOffsetRight + clickOffsetLeft) / 2;
				playWaterDropFallAnimation(animStartX, animStartX + 15, toucPositionY, 400, 0);
				playWaterDropFallAnimation(animStartX + 10, animStartX + 30, toucPositionY, 410, 100);
				playWaterDropFallAnimation(animStartX - 15, animStartX - 40, toucPositionY, 405, 250);
			}
		}
		return clickHandled;
	}

	private void playJigsawJumpAnimation() {
		imgViewJigsawWalking.startAnimation(jigsawJumpAnimation);
	}

	private void showAboutUsDialog() {
		LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View signinView = layoutInflater.inflate(R.layout.about_us, null);
		AlertDialog settingDialog = new AlertDialog.Builder(this).setView(signinView).setTitle("About Us").setPositiveButton("Back", null).create();
		settingDialog.show();
	}

	private void showHelpDialog() {

		LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View signinView = layoutInflater.inflate(R.layout.help, null);
		AlertDialog settingDialog = new AlertDialog.Builder(this).setView(signinView).setTitle("Jigsaw Help").setPositiveButton("Back", null).create();
		settingDialog.show();

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==GET_JIGSAW_SETTING_REQUEST && resultCode == RESULT_OK){
			Log.i(TAG,"Got jigsaw setting result, starting jigsaw activity with results");
			data.setClass(WelcomeActivity.this, JigsawActivity.class);
			startActivity(data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.animation.Animation.AnimationListener#onAnimationEnd(android
	 * .view.animation.Animation)
	 */
	@Override
	public void onAnimationEnd(Animation anim) {
		Log.i(TAG, "any animation is end");
		if (anim.equals(jigsawWalkingAnimation)) {
			jigsawWalkingFrameAnimation.stop();
			Log.i(TAG, "jigsaw playing animation end");
			// Transformation transformation = new Transformation();
			// float[] matrix = new float[9];
			// jigsawWalkingAnimation.getTransformation(AnimationUtils.currentAnimationTimeMillis(),
			// transformation);
			// transformation.getMatrix().getValues(matrix);

			isJigsawJumpAnimEnabled = true;
			imgViewJigsawWalking.setBackgroundResource(R.drawable.jigsaw1);

			((RelativeLayout.LayoutParams) imgViewJigsawWalking.getLayoutParams()).bottomMargin = 20;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.animation.Animation.AnimationListener#onAnimationRepeat(
	 * android.view.animation.Animation)
	 */
	@Override
	public void onAnimationRepeat(Animation arg0) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.animation.Animation.AnimationListener#onAnimationStart(android
	 * .view.animation.Animation)
	 */
	@Override
	public void onAnimationStart(Animation arg0) {

	}
}

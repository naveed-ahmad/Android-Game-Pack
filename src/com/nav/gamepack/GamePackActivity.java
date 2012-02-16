package com.nav.gamepack;

import java.util.ArrayList;

import com.nav.gamepack.puzzle.jigsaw.ImageChooserActivity;
import com.nav.gamepack.puzzle.jigsaw.JigsawActivity;
import com.nav.gamepack.puzzle.jigsaw.JigsawBoardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

public class GamePackActivity extends Activity {
	private final int CHOOSE_IMAGE_CODE = 36;
	private final String TAG = "GamePackActivity";
	JigsawBoardView jbv;
	ImageView slidingDrawerHandle, cloudMedium1, cloudMedium2, cloudSmall1;
	SlidingDrawer slidingDrawerMenu;
	BitmapDrawable imgDrawableHandelClose, imgDrawableHandelOpen;
	Animation lft2RgtSlow, lft2RgtNormal, lft2RgtFast;
	View.OnClickListener cloudClickListener;
	Animation cloudFallAnimation;
	RelativeLayout.LayoutParams waterDroplayoutParams;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Intent getImageIntent = new Intent(GamePackActivity.this, ImageChooserActivity.class);
		startActivityForResult(getImageIntent, CHOOSE_IMAGE_CODE);
		slidingDrawerHandle = (ImageView) findViewById(R.id.slidingDrawerHandle);
		slidingDrawerMenu = (SlidingDrawer) findViewById(R.id.slidingDrawerGameMenu);
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
		cloudClickListener = new OnClickListener() {
			public void onClick(View v) {
				playWaterDropFallAnimation((ImageView) v);

			}
		};
		cloudMedium1.setOnClickListener(cloudClickListener);
		cloudMedium2.setOnClickListener(cloudClickListener);
		cloudSmall1.setOnClickListener(cloudClickListener);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "response from google");
		if (requestCode == CHOOSE_IMAGE_CODE && resultCode == RESULT_OK) {
			// Yeah we have an image lets get it and play jigsaw :)
			Bitmap img = (Bitmap) data.getParcelableExtra("image");

			jbv.setImage(img);
			jbv.prepareJigsawBoardImage();
			jbv.shuffleCells();
			// addContentView(jbv, new
			// LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		} else
			new AlertDialog.Builder(this).setMessage("NO").show();
		super.onActivityResult(requestCode, resultCode, data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onWindowFocusChanged(boolean)
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {

			cloudMedium1.startAnimation(lft2RgtSlow);
			cloudMedium2.startAnimation(lft2RgtNormal);
			cloudSmall1.startAnimation(lft2RgtFast);
			cloudMedium1.setVisibility(View.VISIBLE);
			cloudMedium2.setVisibility(View.VISIBLE);
			cloudSmall1.setVisibility(View.VISIBLE);

			ImageView img = (ImageView) findViewById(R.id.animImgViewTree1);
			img.setBackgroundResource(R.drawable.tree_anim);

			// Get the background, which has been compiled to an
			// AnimationDrawable object.
			AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

			// Start the animation (looped playback by default).
			frameAnimation.start();

		}
	}

	private void playWaterDropFallAnimation(ImageView cloud) {
		Log.i(TAG, "Start animation for  water fall");
		int[] position = new int[2];
		cloud.getLocationOnScreen(position);

		Log.i(TAG, "Left=" + position[0] + " Top=" + position[1] + "Layout");

		Animation tl= AnimationUtils.loadAnimation(this, R.anim.cloud_fall_down);//=new TranslateAnimation(position[0],position[0]+6,position[1], position[1]+400);
		//tl.setInterpolator(new LinearInterpolator());
		//tl.setDuration(2000);
		ImageView waterDropFirst = new ImageView(this);
		waterDropFirst.setBackgroundResource(R.drawable.water_drop);

		this.addContentView(waterDropFirst, waterDroplayoutParams);

		waterDropFirst.startAnimation(tl);
	}

}
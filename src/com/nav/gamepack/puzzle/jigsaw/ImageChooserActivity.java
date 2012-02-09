/**
 * 
 */
package com.nav.gamepack.puzzle.jigsaw;

import com.nav.gamepack.R;

import android.content.Context;
import android.widget.Gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * @author naveed
 * 
 */
public class ImageChooserActivity extends Activity {
	private final String TAG = "ImageChooserView";
	private static final int CAMERA_REQUEST = 1888;
	private Button btnCamera, btnGallery, btnSearchImage, btnDeleteImage, btnSelectImage;
	private ImageView imgViewSelectedImage;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init(getApplicationContext());
	}

	private void init(Context context) {
		Log.i(TAG, "initializing MovieView");
		setContentView(R.layout.image_chooser);
		btnCamera = (Button) findViewById(R.id.btnCamera);
		btnGallery = (Button) findViewById(R.id.btnGallery);
		btnSearchImage = (Button) findViewById(R.id.btnSearchImage);
		btnDeleteImage = (Button) findViewById(R.id.btnDeleteImage);
		btnSelectImage = (Button) findViewById(R.id.btnSearchImage);
		imgViewSelectedImage = (ImageView) findViewById(R.id.imgViewSelectedImage);
		btnCamera.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					Log.i(TAG, "Showing camera ");
					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(cameraIntent, CAMERA_REQUEST);

				} catch (Exception e) {
					Log.e(TAG, "Exception while taking image from camera ", e);
				}
			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			imgViewSelectedImage.setImageBitmap(photo);
		}
	}
}

// public class MyCameraActivity extends Activity {
// private static final int CAMERA_REQUEST = 1888;
// private ImageView imageView;
//
// @Override
// public void onCreate(Bundle savedInstanceState) {
// super.onCreate(savedInstanceState);
// setContentView(R.layout.main);
// this.imageView = (ImageView)this.findViewById(R.id.imageView1);
// Button photoButton = (Button) this.findViewById(R.id.button1);
// photoButton.setOnClickListener(new View.OnClickListener() {
//
// @Override
// public void onClick(View v) {
// Intent cameraIntent = new
// Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
// startActivityForResult(cameraIntent, CAMERA_REQUEST);
// }
// });
// }
//
// protected void onActivityResult(int requestCode, int resultCode, Intent data)
// {
// if (requestCode == CAMERA_REQUEST) {
// Bitmap photo = (Bitmap) data.getExtras().get("data");
// imageView.setImageBitmap(photo);
// }
// }
// }

package com.nav.gamepack.puzzle.jigsaw;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.app.*;

public class ImageChooserActivity extends Activity {

	private Canvas canvas;
	private static int TAKE_PICTURE = 1;
	private Uri outputFileUri;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPictureFromCamera();
	}

	public Bitmap getImage() {

		return null;
	}

	private void openCamera() {
		// Camera camera = new Camera();
		// camera.applyToCanvas(canvas);
		// startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),
		// TAKE_PICTURE);

	}

	private void getPictureFromCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory(),
				"image.jpg");
		outputFileUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//		startActivityForResult(intent, TAKE_PICTURE);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TAKE_PICTURE) {
			Uri imageUri = null;
			// Check if the result includes a thumbnail Bitmap
			if (data != null) {
				if (data.hasExtra("data")) {
					Bitmap thumbnail = data.getParcelableExtra("data");
					// TODO Do something with the thumbnail
				}
			} else {
				// TODO Do something with the full image stored
				// in outputFileUri
			}
		}
	}

}

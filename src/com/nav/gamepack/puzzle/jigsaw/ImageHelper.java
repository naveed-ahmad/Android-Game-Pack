package com.nav.gamepack.puzzle.jigsaw;

import android.graphics.Bitmap;

public class ImageHelper {
	public static Bitmap getScaleImage(Bitmap origionalImage, int mWidth, int mHeight) {
		return Bitmap.createScaledBitmap(origionalImage, mWidth, mHeight, true);
	}

}

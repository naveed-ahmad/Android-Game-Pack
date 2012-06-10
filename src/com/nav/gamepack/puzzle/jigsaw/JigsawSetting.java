/**
 * 
 */
package com.nav.gamepack.puzzle.jigsaw;

import android.graphics.Bitmap;

/**
 * @author naveed
 * 
 */
public class JigsawSetting {
	Boolean enableKeyPad;
	Boolean enableAccelerometer, accelerometerSupported;
	Boolean enableTouch;
	Bitmap jigsawImage;
	private static JigsawSetting setting;
	int rowCount, columnCount;

	/**
 * 
 */
	private JigsawSetting() {
		rowCount = columnCount = 4;
		enableTouch = enableKeyPad = false;
		accelerometerSupported = enableAccelerometer = false;
	}

	/**
	 * @return
	 */
	public static JigsawSetting getSetting() {
		if (setting == null)
			setting = new JigsawSetting();
		return setting;
	}
}

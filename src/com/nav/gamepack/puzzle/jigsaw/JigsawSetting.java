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
	Boolean shuffleCells, cropImage;
	public int[] cellImageMapping;

	private static JigsawSetting setting=null;
	int rowCount, columnCount;

	///public String toString() {
	//	return "shuffle=" + shuffleCells.toString() + " crop=" + cropImage.toString() + " mapping =" + cellImageMapping.toString();
	//}

	/**
 * 
 */
	private JigsawSetting() {
		rowCount = columnCount = 4;
		enableTouch = enableKeyPad = true;
		accelerometerSupported = enableAccelerometer = false;
		shuffleCells = false;
		cropImage = true;
	}

	/**
	 * @return
	 */
	public static JigsawSetting getSetting() {
		if (setting == null)
			setting = new JigsawSetting();
		return setting;
	}

	/**
	 * @param setting2
	 *            TODO: remove all static hell
	 */

	public static void setSetting(JigsawSetting setting2) {
		setting = setting2;
	}
}

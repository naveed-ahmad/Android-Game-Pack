package com.nav.gamepack.puzzle.jigsaw;

import android.content.Context;
import android.view.View;
import java.util.Random;

import javax.security.auth.PrivateCredentialPermission;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.net.NetworkInfo.State;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Naveed Ahmad
 *
 */


public class JigsawCell extends View {
  
	private int cellCurrentPosition;//current position of this cell in board
	private int cellImageIndex;//final position in board
	
	public JigsawCell(Context context) {
		super(context);
		
	}

	/**
	 * @param cellImageIndex the cellImageIndex to set
	 */
	public void setJigsawImageIndex(int jigsawImageIndex) {
		this.cellImageIndex = jigsawImageIndex;
	}

	/**
	 * @return the cellImageIndex
	 */
	public int getJigsawImageIndex() {
		return cellImageIndex;
	}

	/**
	 * @param cellCurrentPosition the cellCurrentPosition to set
	 */
	public void setCellCurrentPosition(int cellCurrentPosition) {
		this.cellCurrentPosition = cellCurrentPosition;
	}

	/**
	 * @return the cellCurrentPosition
	 */
	public int getCurrentPosition() {
		return cellCurrentPosition;
	}
	

}

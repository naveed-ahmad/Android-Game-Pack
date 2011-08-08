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
	private int cellFinalPosition;//final position in board
	
	public JigsawCell(Context context) {
		super(context);
		
	}
	

}

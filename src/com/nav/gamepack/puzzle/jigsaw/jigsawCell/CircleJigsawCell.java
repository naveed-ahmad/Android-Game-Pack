/**
 * 
 */
package com.nav.gamepack.puzzle.jigsaw.jigsawCell;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import com.nav.gamepack.R;

/**
 * @author naveed
 *
 */
public class CircleJigsawCell extends JigsawCells{
	final String TAG="DIamondJigsawCell";

	/**
	 * @param context
	 */
	public CircleJigsawCell(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CircleJigsawCell(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CircleJigsawCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}

	@Override
	public void generateBoundryPath() {
		//a circular shape
	//	setBackgroundBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.home_background)));
		mBoundryPath.addCircle(getBoundaryMiddleX(),getBoundaryMiddleY(), 50, Direction.CW);
		Log.i(TAG,"sBoundry Info left:"+mBoundryRect.left+" right:"+mBoundryRect.right+ "top="+ mBoundryRect.top+ "bottom="+mBoundryRect.bottom+ "middleX="+getBoundaryMiddleX()+" middleY="+getBoundaryMiddleY());
	}

}

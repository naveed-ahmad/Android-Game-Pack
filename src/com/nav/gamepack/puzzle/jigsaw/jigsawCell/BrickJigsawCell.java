/**
 * 
 */
package com.nav.gamepack.puzzle.jigsaw.jigsawCell;

import com.nav.gamepack.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @author naveed
 * 
 */
public class BrickJigsawCell extends JigsawCells {
	final String TAG="DiamondJigsawCell";

	/**
	 * @param context
	 */
	public BrickJigsawCell(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public BrickJigsawCell(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public BrickJigsawCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		generateBoundryPath();
	}

	@Override
	protected void generateBoundryPath() {
		//...|**************|
		//...|..............|
		//...|..............|
		//...|______________|
		
		//setBackgroundBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.home_background)));
		mBoundryPath.addRect(new RectF(mBoundryRect), Direction.CCW);
		Log.i(TAG,"sBoundry Info left:"+mBoundryRect.left+" right:"+mBoundryRect.right+ "top="+ mBoundryRect.top+ "bottom="+mBoundryRect.bottom+ "middleX="+getBoundaryMiddleX()+" middleY="+getBoundaryMiddleY());
	}
}

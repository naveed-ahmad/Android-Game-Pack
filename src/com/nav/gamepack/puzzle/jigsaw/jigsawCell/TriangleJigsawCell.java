/**
 * 
 */
package com.nav.gamepack.puzzle.jigsaw.jigsawCell;

import com.nav.gamepack.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Path.Direction;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @author naveed
 * 
 */
public class TriangleJigsawCell extends JigsawCells {
	final String TAG = "DiamondJigsawCell";
	enum ShapeType{BACKWORD_DIAGONAL,FORWard_DIAGONAL};
	enum DIAGONALType{FULL_DIAGONAL,HALF_DIAGONAL};
	enum PieceCount{TWO,THREE,FOUR};
	enum CircleSide{HYPOTENUSE,BASE,PERPENDICULAR};
	enum CircleType{CONCAVE,CONVEX};
	
	/**
	 * @param context
	 */
	public TriangleJigsawCell(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public TriangleJigsawCell(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public TriangleJigsawCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		generateBoundryPath();
	}

	@Override
	protected void generateBoundryPath() {
		Rect r = new Rect(140, 140, 250, 250);
		setBoundaryRect(r);

	//	setBackgroundBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.home_background)));
		//mBoundryPath.moveTo(getBoundaryMiddleX(), mBoundryRect.top);// 0
		mBoundryPath.lineTo(mBoundryRect.left, mBoundryRect.bottom);// 1
		mBoundryPath.lineTo(mBoundryRect.right, mBoundryRect.bottom);// 2
		mBoundryPath.lineTo(mBoundryRect.left, mBoundryRect.top);// 3
	
		Log.i(TAG, "sBoundry Info left:" + mBoundryRect.left + " right:" + mBoundryRect.right + "top=" + mBoundryRect.top + "bottom=" + mBoundryRect.bottom + "middleX=" + getBoundaryMiddleX()
				+ " middleY=" + getBoundaryMiddleY());
	}
}

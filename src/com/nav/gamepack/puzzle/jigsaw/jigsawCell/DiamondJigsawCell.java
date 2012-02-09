/**
 * 
 */
package com.nav.gamepack.puzzle.jigsaw.jigsawCell;

import com.nav.gamepack.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @author naveed
 * 
 */
public class DiamondJigsawCell extends JigsawCells {
	final String TAG="DiamondJigsawCell";

	/**
	 * @param context
	 */
	public DiamondJigsawCell(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public DiamondJigsawCell(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public DiamondJigsawCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		generateBoundryPath();
	}

	@Override
	protected void generateBoundryPath() {
		//.....0
		// ...1/\4
		// .../..\
		// ..2\../
		// ....\/3
		Rect r = new Rect(140, 140, 250, 250);
		setBoundaryRect(r);

		setBackgroundBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.home_background)));
		mBoundryPath.moveTo(getBoundaryMiddleX(), mBoundryRect.top);//0
		mBoundryPath.lineTo(mBoundryRect.left, getBoundaryMiddleY());//1
		mBoundryPath.lineTo(getBoundaryMiddleX(), mBoundryRect.bottom);//2
		mBoundryPath.lineTo(mBoundryRect.right, getBoundaryMiddleY());//3
		mBoundryPath.lineTo(getBoundaryMiddleX(),mBoundryRect.top);//4
		Log.i(TAG,"sBoundry Info left:"+mBoundryRect.left+" right:"+mBoundryRect.right+ "top="+ mBoundryRect.top+ "bottom="+mBoundryRect.bottom+ "middleX="+getBoundaryMiddleX()+" middleY="+getBoundaryMiddleY());
	}
}

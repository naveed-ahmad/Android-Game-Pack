/**
 * 
 */
package com.nav.gamepack.puzzle.jigsaw.jigsawCell;


import com.nav.gamepack.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author naveed
 * 
 */
public abstract class JigsawCells extends View {
	protected Context mContext;
	protected Path mBoundryPath;
	protected Rect mBoundryRect;
	protected Bitmap mBackgroundBitmap;
	protected RectF mDestRect;
	protected Paint mPaint;
	protected float mBoundaryMiddleX,mBoundaryMiddleY;
	/**
	 * @param context
	 */
	public JigsawCells(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public JigsawCells(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public JigsawCells(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	private void init(Context context, AttributeSet attrs, int defStyle) {
		mContext = context;
		mBoundryPath = new Path();
		mPaint=new Paint();
		mPaint.setStyle(Style.STROKE);
		mPaint.setColor(Color.RED);
	}
	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//if(mBoundryRect!=null)
		//	setMeasuredDimension(mBoundryRect.right-mBoundryRect.left,mBoundryRect.bottom-mBoundryRect.top);
	}

	public void setBoundaryRect(Rect boundry){
		mBoundryRect=boundry;
		mDestRect=new RectF(mBoundryRect);
	}
	public float getBoundaryMiddleX(){
		if(mBoundaryMiddleX==0)
			mBoundaryMiddleX=(mBoundryRect.left+mBoundryRect.right)/2;
		return mBoundaryMiddleX;
			
	}
	public float getBoundaryMiddleY(){
		if(mBoundaryMiddleY==0)
			mBoundaryMiddleY=(mBoundryRect.top+mBoundryRect.bottom)/2;
		return mBoundaryMiddleY;
	}
	
	public void setBackgroundBitmap(Bitmap back){
		mBackgroundBitmap=back;
	}
	/**
	 * initialize boundary  path
	 */
	protected  abstract void generateBoundryPath();
		
	 
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.clipPath(mBoundryPath,Op.REPLACE);
		canvas.drawBitmap(mBackgroundBitmap, mBoundryRect, mDestRect,mPaint);
		
		canvas.drawPath(mBoundryPath, mPaint);
		
	}

}

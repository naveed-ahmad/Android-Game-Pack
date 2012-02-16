/**
 * 
 */
package com.nav.gamepack.puzzle.jigsaw;

import java.util.ArrayList;
import java.util.Random;

import com.nav.gamepack.R;
import com.nav.gamepack.puzzle.jigsaw.jigsawCell.CircleJigsawCell;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author naveed
 * 
 */
public class DNDJigsawBoardView extends View {
	private final String TAG = "DNDJigsawBoard";
	ArrayList cells;

	/**
	 * @param context
	 */
	public DNDJigsawBoardView(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public DNDJigsawBoardView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public DNDJigsawBoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		cells = new ArrayList();
		generateJigsawCells();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int mSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
		setMeasuredDimension(mSize, mSize );
	}
	
	private void generateJigsawCells() {
		//Bitmap jigsawOrigionalImage = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.image)), 500, 500, true);

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				CircleJigsawCell cell = new CircleJigsawCell(getContext());
				cell.setBoundaryRect(new Rect(i, j, (i + 1) * 100, (j + 1) * 100));
				cell.generateBoundryPath();
				Random r=new Random();
				int x=i*r.nextInt(60),y=i*r.nextInt(60);
				cell.layout(x,y,x+100, y+100);
			//	cell.setBackgroundBitmap(jigsawOrigionalImage);
				cells.add(cell);
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < cells.size(); i++)
			try {
				((CircleJigsawCell) cells.get(i)).draw(canvas);
			} catch (Exception e) {
				Log.e(TAG, "Error while rendering " + i, e);
			}
	}
}

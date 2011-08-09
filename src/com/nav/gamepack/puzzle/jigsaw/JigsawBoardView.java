package com.nav.gamepack.puzzle.jigsaw;

import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.text.style.BulletSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Naveed Ahmad
 * 
 */

public class JigsawBoardView extends View {
	private JigsawSetting setting;
	private Bitmap[] jigsawCellImages;
	private boolean isBoardInitialized;
	private JigsawCell[] cells;
	private Context context;
	View.OnClickListener jigsawCellClickListener;
	MotionEvent jigsawCellTouchListener;

	public JigsawBoardView(Context context) {
		super(context);
		init(context);
	}

	public JigsawBoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

	private void init(final Context context) {
		this.context = context;
		isBoardInitialized = false;
		jigsawCellClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(context).setMessage(
						((JigsawCell) v).getCellCurrentPosition() + "").show();
			}
		};

	}

	public JigsawBoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
		// TODO Auto-generated constructor stub
	}

	/*
	 * 
	 */
	private void prepareJigsawBoardImage() {
		Bitmap jigsawImage = setting.getJigwasImage();
		setting.prepareJigwasCellDimension();
		int width = setting.getJigsawCellWidth();
		int height = setting.getJigsawCellHeight();
		int rows = setting.getBoardRowCount();
		int columns = setting.getBoardColumnCount();
		int imageStartX = 0, imageStartY = 5;
		int currentCell = 0;
		for (int column = 0; column < columns; column++) {
			for (int row = 0; row < rows; row++) {
				jigsawCellImages[currentCell] = Bitmap.createBitmap(
						jigsawImage, imageStartX, imageStartY - 5, width,
						height);
				cells[currentCell] = new JigsawCell(context);
				cells[currentCell].layout(imageStartX, imageStartY, imageStartX
						+ width, imageStartY + height);
				cells[currentCell].setCellImageIndex(currentCell);
				cells[currentCell].setClickable(true);
				cells[currentCell].setOnClickListener(jigsawCellClickListener);
				// cells[currentCell].onTouchEvent(jigsawCellTouchListener);
				imageStartX += width;
				currentCell++;
			}
			imageStartX = 0;
			imageStartY += height;
		}
		cells[currentCell] = new JigsawCell(context);
		cells[currentCell].layout((columns - 1) * width, imageStartY, columns
				* width, imageStartY + height);
		cells[currentCell].setCellImageIndex(currentCell);

		// new
		// AlertDialog.Builder(context).setMessage("w="+height+"\nh="+width+"\nrows"+rows).setNegativeButton("cancle",
		// null).setTitle("Setting").show();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!isBoardInitialized)
			initBoard(false);

		Paint p1 = new Paint();
		p1.setStyle(Style.STROKE);
		p1.setColor(Color.RED);
		// Bitmap img;
		// img = setting.getJigwasImage();

		// if (img != null)
		// canvas.drawBitmap(img, 0, 0, p1);
		// else
		// canvas.drawRect(10, 10, getRight() - 10, getBottom() - 10, p1);
		// int i=0;
		for (int i = 0; i < cells.length; i++) {

			// canvas.drawBitmap(jigsawCellImages[i], cells[i].getLeft(),
			// cells[i].getTop(), p1);
			canvas.drawRect(cells[i].getLeft(), cells[i].getTop(), cells[i]
					.getRight(), cells[i].getBottom(), p1);

		}
		// i=5;
		// canvas.drawBitmap(jigsawCellImages[i], cells[i].getLeft(),
		// cells[i].getTop(), p1);
		// i=2;
		// canvas.drawBitmap(jigsawCellImages[i], cells[i].getLeft(),
		// cells[i].getTop(), p1);
		// i=3;
		// canvas.drawBitmap(jigsawCellImages[i], cells[i].getLeft(),
		// cells[i].getTop(), p1);

	}

	/*
	 * initialize game..crop jigsaw image.prepar cells.and draw cells
	 */
	public void initBoard(boolean shuffleCells) {

		// new
		// AlertDialog.Builder(context).setMessage("w="+getWidth()+"\nh="+getHeight()).
		// setTitle("This Size").show();
		// new
		// AlertDialog.Builder(context).setMessage("w="+setting.getJigsawBoardHeight()+"\nh="+setting.getJigsawBoardWidth()).setNegativeButton("cancle",
		// null).setTitle("Setting").show();

		if (isBoardInitialized)
			return;// Board is already initialized
		setting = new JigsawSetting(context, getWidth() - 5, getWidth() - 5);
		int cellCount = setting.getBoardColumnCount()
				* setting.getBoardRowCount() + 1;
		cells = new JigsawCell[cellCount];
		jigsawCellImages = new Bitmap[cellCount];
		prepareJigsawBoardImage();
		if (shuffleCells)
			shuffleCells();
		isBoardInitialized = true;
		// drawBoard();
	}

	/*
	 * Shuffle cells of jigsaw board
	 */
	public void shuffleCells(int shuffleCount) {
		Random r = new Random();
		for (int i = 0; i < shuffleCount; i++) {
			int num1 = r.nextInt(cells.length);
			int num2 = r.nextInt(cells.length);
			JigsawCell c = cells[num1];
			cells[num1] = cells[num2];
			cells[num2] = c;
		}
	}

	public void shuffleCells() {
		shuffleCells(setting.getShuffleCount());
	}

	public boolean isGameOver() {
		return false;
	}

	private int findClickedCellIndex(final float x, final float y) {
		for (int i = 0; i < cells.length; i++)
			if (cells[i].getLeft() < x && cells[i].getRight() > x
					&& cells[i].getTop() < y && cells[i].getBottom() > y)
				return i;
		return -1;
	}

	public boolean onTouchEvent(MotionEvent event) {
		int cellIndex=findClickedCellIndex(event.getX(), event.getY());
		if (cellIndex>0)
		new AlertDialog.Builder(context).setMessage(
				cells[cellIndex].getLeft() + " R "+cells[cellIndex].getRight()+" T: "+cells[cellIndex].getTop()+" D "+cells[cellIndex].getBottom()).show();
		else
			new AlertDialog.Builder(context).setMessage("zero").show();
						
		return true;
	}

}

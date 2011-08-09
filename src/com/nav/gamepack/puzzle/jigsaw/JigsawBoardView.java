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
	private int emptyCellIndex;
	View.OnClickListener jigsawCellClickListener;

	public JigsawBoardView(Context context) {
		super(context);
		init(context);
	}

	public JigsawBoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(final Context context) {
		this.context = context;
		isBoardInitialized = false;
		jigsawCellClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(context).setMessage(
						((JigsawCell) v).getCurrentPosition() + "").show();
			}
		};

	}

	public JigsawBoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
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
				cells[currentCell].setJigsawImageIndex(currentCell);
				cells[currentCell].setClickable(true);
				cells[currentCell].setOnClickListener(jigsawCellClickListener);
				imageStartX += width;
				currentCell++;
			}
			imageStartX = 0;
			imageStartY += height;
		}
		cells[currentCell] = new JigsawCell(context);
		cells[currentCell].layout((columns - 1) * width, imageStartY, columns
				* width, imageStartY + height);
		cells[currentCell].setJigsawImageIndex(currentCell);
		jigsawCellImages[currentCell] = Bitmap.createScaledBitmap(jigsawImage,
				width, height, true);
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
			initBoard(true);

		Paint p1 = new Paint();
		p1.setStyle(Style.STROKE);
		p1.setColor(Color.RED);
		for (int i = 0; i < cells.length; i++) {
			canvas.drawBitmap(jigsawCellImages[cells[i].getJigsawImageIndex()],
					cells[i].getLeft(), cells[i].getTop(), p1);
			canvas.drawRect(cells[i].getLeft(), cells[i].getTop(), cells[i]
					.getRight(), cells[i].getBottom(), p1);
		}
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
	}

	/*
	 * Shuffle cells of jigsaw board
	 */
	public void shuffleCells(int shuffleCount) {
		Random r = new Random();
		for (int i = 0; i < shuffleCount; i++) {
			int num1 = r.nextInt(cells.length - 2);
			int num2 = r.nextInt(cells.length - 2);
			swapCellsImageIndex(num1,num2);
			
			//int tempImageIndex = cells[num1].getJigsawImageIndex();
			//cells[num1].setJigsawImageIndex(cells[num2].getJigsawImageIndex());
			//cells[num2].setJigsawImageIndex(tempImageIndex);
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
		int cellIndex = findClickedCellIndex(event.getX(), event.getY());
		if (cellIndex > 0)
		{	// new AlertDialog.Builder(context).setMessage(
			// cells[cellIndex].getLeft() +
			// " R "+cells[cellIndex].getRight()+" T: "+cells[cellIndex].getTop()+" D "+cells[cellIndex].getBottom()).show();
			handelClick(cellIndex);

			new AlertDialog.Builder(context).setMessage(
					cells[cellIndex].getJigsawImageIndex() + " P "
							+ cells[cellIndex].getCurrentPosition()).show();
		}

		else
			new AlertDialog.Builder(context).setMessage("zero").show();

		return true;
	}
	
	public void swapCellsImageIndex(int  cell1,int cell2)
	{
		int tempImageIndex = cells[cell1].getJigsawImageIndex();
		cells[cell1].setJigsawImageIndex(cells[cell2].getJigsawImageIndex());
		cells[cell2].setJigsawImageIndex(tempImageIndex);
	}
	
	public void handelClick(int  p)
	{emptyCellIndex=8;
		try{//Check if Up Cell is free
			if(cells[p-setting.getBoardRowCount()].getJigsawImageIndex()==emptyCellIndex)
			{
				new AlertDialog.Builder(context).setMessage("up").show();

				swapCellsImageIndex(p, p-setting.getBoardRowCount());
				//cells[p].swapCell(cells[p-puzzleColumn]);
				
			}
		}catch(Exception e){ }
		try{//Check if Down Cell is free
			new AlertDialog.Builder(context).setMessage("down").show();

			if(cells[p+setting.getBoardRowCount()].getJigsawImageIndex()==emptyCellIndex)
			{
				new AlertDialog.Builder(context).setMessage("left").show();

				//cells[p].swapCell(cells[p+puzzleColumn]);
				swapCellsImageIndex(p, p+setting.getBoardRowCount());
			}
		}catch(Exception e){ }
		try{//Check if Left Cell is free
			if(cells[p+1].getJigsawImageIndex()==emptyCellIndex)
			{
				new AlertDialog.Builder(context).setMessage("rigt").show();

			//	cells[p].swapCell(cells[p+1]);
				swapCellsImageIndex(p,p+1);
			}
		}catch(Exception e){ }
		try{//Check if Left Cell is free
			if(cells[p-1].getJigsawImageIndex()==emptyCellIndex)
			{
				//cells[p].swapCell(cells[p-1]);
				swapCellsImageIndex(p, p-1);
			}
		}catch(Exception e){ }
		
	}

}

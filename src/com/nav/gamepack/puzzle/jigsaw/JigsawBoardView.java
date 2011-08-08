package com.nav.gamepack.puzzle.jigsaw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
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

	public JigsawBoardView(Context context) {
		super(context);
		init(context);
	}

	public JigsawBoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

	private void init(Context context) {
		this.context = context;
		isBoardInitialized = false;
		setting = new JigsawSetting(context, getWidth(),getWidth());

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
		int imageStartX = 0, imageStartY = 0;
		int currentCell = 0;
		for (int column = 0; column < columns; column++) {
			for (int row = 0; row < rows; row++) {
				jigsawCellImages[currentCell] = Bitmap.createBitmap(
						jigsawImage, imageStartX, imageStartY, width, height);
				cells[currentCell] = new JigsawCell(context);
				cells[currentCell].layout(imageStartX, imageStartY, imageStartX
						+ width, imageStartY + height);
				imageStartX += width;
				currentCell++;
			}
			imageStartX = 0;
			imageStartY += height;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!isBoardInitialized)
			initGame(false);

		Paint p1 = new Paint();
		p1.setStyle(Style.STROKE);
		p1.setColor(Color.RED);
		//Bitmap img;
		//img = setting.getJigwasImage();
		
		//if (img != null)
			//canvas.drawBitmap(img, 0, 0, p1);
		//else
			canvas.drawRect(10, 10, getRight() - 10, getBottom() - 10, p1);
		int i=0;
			//for (int i = 0; i < cells.length - 2; i++)
			//canvas.drawRect(cells[i].getLeft(), cells[i].getTop(), cells[i]
				//	.getRight(), cells[i].getBottom(), p1);
	        //if(i%5==1)
			 // canvas.drawBitmap(jigsawCellImages[i], cells[i].getLeft(), cells[i].getTop(), p1);
			  i=5;
			  canvas.drawBitmap(jigsawCellImages[i], cells[i].getLeft(), cells[i].getTop(), p1);
			 // i=2;
			  //canvas.drawBitmap(jigsawCellImages[i], cells[i].getLeft(), cells[i].getTop(), p1);
			  //i=3;
			 // canvas.drawBitmap(jigsawCellImages[i], cells[i].getLeft(), cells[i].getTop(), p1);
	
	}

	/*
	 * initialize game..crop jigsaw image.prepar cells.and draw cells
	 */
	public void initGame(boolean shuffleCells) {
		if (isBoardInitialized)
			return;// Board is already initialized
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
		// TODO: Implementation pending

	}

	public void shuffleCells() {
		shuffleCells(setting.getShuffleCount());
	}

	public boolean isGameOver() {
		return false;
	}

}

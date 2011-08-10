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
				// new AlertDialog.Builder(context).setMessage(
				// ((JigsawCell) v).getCurrentPosition() + "").show();
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
		emptyCellIndex = currentCell;
		jigsawCellImages[currentCell] = Bitmap.createScaledBitmap(jigsawImage,
				width, height, true);
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
			int num1 = r.nextInt(cells.length - 3);
			int num2 = r.nextInt(cells.length - 3);
			swapCellsImageIndex(num1, num2);
		}
	}

	/**
     * 
     */
	public void shuffleCells() {
		shuffleCells(setting.getShuffleCount());
	}

	/**
	 * Check if Game is End
	 * 
	 * @return true if game over false otherwise
	 */
	public boolean isGameOver() {
		for (int i = 0; i < jigsawCellImages.length; i++)
			if (cells[i].getCurrentPosition() != cells[i].getJigsawImageIndex())
				return false;
		return true;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return Position(index) of cell user clicked
	 */
	private int findClickedCellIndex(final float x, final float y) {
		for (int i = 0; i < cells.length; i++)
			if (cells[i].getLeft() < x && cells[i].getRight() > x
					&& cells[i].getTop() < y && cells[i].getBottom() > y)
				return i;
		return -1;
	}

	/**
     * 
     */
	public boolean onTouchEvent(MotionEvent event) {
		int cellIndex = findClickedCellIndex(event.getX(), event.getY());
		if (cellIndex >= 0)
			handelClick(cellIndex);
		return false;
	}

	/**
	 * 
	 * @param cell1
	 * @param cell2
	 */
	public void swapCellsImageIndex(int cell1, int cell2) {
		int tempImageIndex = cells[cell1].getJigsawImageIndex();
		// if (cells[cell1].getJigsawImageIndex() == emptyCellIndex)
		// emptyCellIndex = cells[cell2].getJigsawImageIndex();
		// if (cells[cell2].getJigsawImageIndex() == emptyCellIndex)
		// emptyCellIndex = cells[cell1].getJigsawImageIndex();

		cells[cell1].setJigsawImageIndex(cells[cell2].getJigsawImageIndex());
		cells[cell2].setJigsawImageIndex(tempImageIndex);
	}

	/**
	 * 
	 * @param clickedCellIndex
	 */
	public void handelClick(int clickedCellIndex) {
		//new AlertDialog.Builder(context).setMessage("CC="+clickedCellIndex+"\n EC="+emptyCellIndex).show();
		int columns = setting.getBoardColumnCount();
		int rows = setting.getBoardRowCount();
		if(cells[clickedCellIndex].getJigsawImageIndex()==emptyCellIndex)
			return;
		if (clickedCellIndex == getLastCellIndex()) {
			// last cell is clicked.Only Empty cell side can be Top
			if (get2ndLastCellIndex() == emptyCellIndex) {
				swapCellsImageIndex(getLastCellIndex(), get2ndLastCellIndex());
			}
		} else if (clickedCellIndex < columns) {// Clicked in first row
			if (clickedCellIndex == 0) {
				// First Cell of First Row Clicked.
				// Possible Empty cell side can be Right or Down
				checkEmptyCellSide(clickedCellIndex, "RD");
			} else if (clickedCellIndex == columns - 1) {
				// Last Cell of First Row Clicked.
				// Possible Empty cell side can be Left or Down
				checkEmptyCellSide(clickedCellIndex, "LD");
			} else {
				// Any other Cell is Clicked other than first and last
				// Possible Empty Cell side can be Left,Right,or Down
				checkEmptyCellSide(clickedCellIndex, "LRD");
			}
		}// First Row Clicked END
		else if ((clickedCellIndex + 1) % columns == 0) {// Clicked in Last
			new AlertDialog.Builder(context).setMessage("last c").show();
			
			if (clickedCellIndex == get2ndLastCellIndex()) {
				// 2nd Last cell clicked possible EMpty cell side can be
				// Up,Left,or Down
				// Down Side is last cell so will handle differently
				if (cells[getLastCellIndex()].getJigsawImageIndex() == emptyCellIndex) {
					swapCellsImageIndex(get2ndLastCellIndex(),
							getLastCellIndex());
					invalidate();
				} else {
					// Any other cell other than last and first(actually covered
					// in first row check) is clicked from last column Possible
					// Empty cell side can be Left,Up,or Down
					checkEmptyCellSide(clickedCellIndex, "LUD");
				}
			}
		}// Last Column clicked END
		else if (clickedCellIndex % columns == 0) {// First Column Clicked
			if (clickedCellIndex == columns * (rows - 1)) {
				// last Cell of first Column(First Cell of last row -:D ) is
				// clicked
				// possible side of Empty cell can be Top,Right
				checkEmptyCellSide(clickedCellIndex, "TR");
			} else {
				// Any other cell(Other than last and first(witch is already
				// checked in first row)) from first column is clicked
				// Possible side for Empty cell can be Top,Down,Right
				checkEmptyCellSide(clickedCellIndex, "TDR");
			}
		}// First Column Clicked END
		else if (clickedCellIndex > columns * (rows - 1)
				&& clickedCellIndex < columns * rows) {// Last Row Clicked
			// possible Empty cell side can be Left,Right,or Top...first and
			// last cell clicked are already checked
			new AlertDialog.Builder(context).setMessage("CC="+clickedCellIndex+"\n EC="+emptyCellIndex).show();
				
			checkEmptyCellSide(clickedCellIndex, "LRT");
		}// Last row clicked END
		else {
			// ah First row,column and Last row ,column check is end
			// now clicked in any cell they can be moved in any direction so
			// check for all four sides
			checkEmptyCellSide(clickedCellIndex, "LRTD");// piece of cake
			// calling this
			// method
		}
	}

	private int get2ndLastCellIndex() {
		return jigsawCellImages.length - 2;
	}

	private int getLastCellIndex() {
		return jigsawCellImages.length - 1;
	}

	/**
	 * 
	 * @param clickedCellIndex
	 * @param possibleSides
	 */
	private void checkEmptyCellSide(int clickedCellIndex, String possibleSides) {
		for (int i = 0; i < possibleSides.length(); i++)
			switch (possibleSides.charAt(i)) {
			case 'L':
				if (isEmptyCellIsOnLeft(clickedCellIndex))
					moveLeft(clickedCellIndex);
				break;
			case 'R':
				if (isEmptyCellIsOnRight(clickedCellIndex))
					moveRight(clickedCellIndex);

				break;
			case 'U':
				if (isEmptyCellIsOnUp(clickedCellIndex))
					moveUp(clickedCellIndex);

				break;
			case 'D':
				if (isEmptyCellIsOnDown(clickedCellIndex))
					moveDown(clickedCellIndex);
				break;
			}
	}

	/**
	 * 
	 * @param clickedCellIndex
	 * @return
	 */
	private boolean isEmptyCellIsOnLeft(int clickedCellIndex) {
		return cells[clickedCellIndex - 1].getJigsawImageIndex() == emptyCellIndex;
	}

	/**
	 * 
	 * @param clickedCellIndex
	 * @return
	 */
	private boolean isEmptyCellIsOnRight(int clickedCellIndex) {
		return cells[clickedCellIndex + 1].getJigsawImageIndex() == emptyCellIndex;
	}

	/**
	 * 
	 * @param clickedCellIndex
	 * @return
	 */
	private boolean isEmptyCellIsOnUp(int clickedCellIndex) {
		return cells[clickedCellIndex - setting.getBoardColumnCount()]
				.getJigsawImageIndex() == emptyCellIndex;
	}

	/**
	 * 
	 * @param clickedCellIndex
	 * @return
	 */
	private boolean isEmptyCellIsOnDown(final int clickedCellIndex) {
		return cells[clickedCellIndex + setting.getBoardColumnCount()]
				.getJigsawImageIndex() == emptyCellIndex;
	}

	/**
	 * 
	 * @param currentCellIndex
	 */
	private void moveDown(int currentCellIndex) {
		new AlertDialog.Builder(context).setMessage("d").show();
		
		swapCellsImageIndex(currentCellIndex, currentCellIndex
				+ setting.getBoardColumnCount());
		invalidate();
	}

	/**
	 * 
	 * @param currentCellIndex
	 */
	private void moveUp(int currentCellIndex) {
		new AlertDialog.Builder(context).setMessage("u").show();
		
		swapCellsImageIndex(currentCellIndex, currentCellIndex
				- setting.getBoardColumnCount());
		invalidate();
	}

	/**
	 * 
	 * @param currentCellIndex
	 */
	private void moveLeft(int currentCellIndex) {
		swapCellsImageIndex(currentCellIndex, currentCellIndex - 1);
		invalidate();
	}

	/**
	 * 
	 * @param currentCellIndex
	 */
	private void moveRight(int currentCellIndex) {
		swapCellsImageIndex(currentCellIndex, currentCellIndex + 1);
		invalidate();
	}

}

package com.nav.gamepack.puzzle.jigsaw;

import java.util.Random;

import com.nav.gamepack.R;

import android.R.bool;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author naveed
 * 
 */

public class JigsawBoardView extends View {
	private final String TAG = "JigsawBoardView";
	private Bitmap[] jigsawCellImages;
	Bitmap jigsawOrigionalImage, jigsawScalledImage;
	private boolean isBoardInitialized;
	private JigsawCell[] cells;
	private Context context;
	private int emptyCellIndex;
	private JigsawBoardKeyListener jigsawCellClickListener;
	private int clickedCellIndex;
	private int rowCount, columnCount, cellShuffleCount, cellHeight, cellWidth, mSize;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
		getJigsawScalledImage(mSize);
		prepareJigsawCellSize();
		setMeasuredDimension(mSize, mSize + cellHeight);
	}

	/**
	 * 
	 */
	private void prepareJigsawCellSize() {
		setCelWidth(jigsawScalledImage.getWidth() / getColumnCount());
		setCellHeight(jigsawScalledImage.getHeight() / getRowCount());

	}

	/**
	 * @param i
	 */
	private void setCellHeight(int height) {
		cellHeight = height;

	}

	/**
	 * @param i
	 */
	private void setCelWidth(int width) {
		cellWidth = width;

	}

	/**
	 * @param jigsawCellClickListener2
	 * @param mWidth
	 * @param mWidth2
	 */
	private Bitmap getScaleImage(Bitmap origionalImage, int mWidth, int mHeight) {
		return Bitmap.createScaledBitmap(origionalImage, mWidth, mHeight, true);
	}

	public Bitmap getJigsawScalledImage(int size) {

		if (jigsawOrigionalImage == null) {
			// TODO Show Select Image View//
			jigsawOrigionalImage = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.image)), getWidth(), getHeight(), true);

		}

		jigsawScalledImage = getScaleImage(jigsawOrigionalImage, size, size);
		return jigsawScalledImage;
	}

	public JigsawBoardView(Context context) {
		this(context, null);
	}

	public JigsawBoardView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	private void init(final Context context, AttributeSet attrs, int defStyle) {
		this.context = context;
		load(context, attrs, defStyle);
		isBoardInitialized = false;
		jigsawCellClickListener = new JigsawBoardKeyListener(this);
		setOnKeyListener(jigsawCellClickListener);
		setFocusableInTouchMode(true);
	}

	public JigsawBoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	/*
	 * 
	 */
	public void prepareJigsawBoardImage() {
		if (jigsawOrigionalImage == null) {
			showImageChooserDialog();
		
		}
		Bitmap img=	getJigsawScalledImage(mSize);


		int imageStartX = 0, imageStartY = 5;
		int currentCell = 0;
		for (int row = 0; row < getRowCount(); row++) {
			for (int column = 0; column < getColumnCount(); column++) {
				Log.i(TAG, "Croping column+" + column + " row=" + row + "startX=" + imageStartX + " startY=" + imageStartY);

				jigsawCellImages[currentCell] = Bitmap.createBitmap(img, imageStartX, imageStartY - 5, cellWidth, cellHeight);
				cells[currentCell] = new JigsawCell(context);
				cells[currentCell].layout(imageStartX, imageStartY, imageStartX + cellWidth, imageStartY + cellHeight);
				cells[currentCell].setJigsawImageIndex(currentCell);
				cells[currentCell].setCellCurrentPosition(currentCell);
				imageStartX += cellWidth;
				currentCell++;
			}
			imageStartX = 0;
			imageStartY += cellHeight;
		}
		cells[currentCell] = new JigsawCell(context);
		cells[currentCell].layout((getColumnCount() - 1) * cellWidth, imageStartY, getColumnCount() * cellWidth, imageStartY + cellHeight);
		cells[currentCell].setJigsawImageIndex(currentCell);
		cells[currentCell].setCellCurrentPosition(currentCell);
		emptyCellIndex = currentCell;
	}

	/**
	 * 
	 */
	private void showImageChooserDialog() {

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
			p1.setColor(Color.RED);
			if (jigsawCellImages[cells[i].getJigsawImageIndex()] != null)
				canvas.drawBitmap(jigsawCellImages[cells[i].getJigsawImageIndex()], cells[i].getLeft(), cells[i].getTop(), p1);

			canvas.drawRect(cells[i].getLeft(), cells[i].getTop(), cells[i].getRight(), cells[i].getBottom(), p1);
			/*
			 * canvas.drawText("C-" + i + " P:" + cells[i].getCurrentPosition(),
			 * cells[i].getLeft() + 2, cells[i].getTop() + 10, p1);
			 * 
			 * p1.setColor(Color.YELLOW); canvas.drawText("C-" + i + " M:" +
			 * cells[i].getJigsawImageIndex(), cells[i].getLeft() + 2,
			 * cells[i].getTop() + 35, p1); p1.setColor(Color.RED);
			 * canvas.drawText("E:" + emptyCellIndex, cells[i].getLeft() + 2,
			 * cells[i].getTop() + 50, p1); p1.setColor(Color.BLUE);
			 * canvas.drawText("Clickd :" + clickedCellIndex, cells[i].getLeft()
			 * + 2, cells[i].getTop() + 70, p1);
			 */

		}
	}

	/*
	 * initialize game..crop jigsaw image.prepar cells.and draw cells
	 */
	public void initBoard(boolean shuffleCells) {
		if (isBoardInitialized)
			return;// Board is already initialized
		int cellCount = getColumnCount() * getRowCount() + 1;
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
		shuffleCells(getShuffleCount());
	}

	/**
	 * @return
	 */
	public int getShuffleCount() {
		return cellShuffleCount;
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
	public int findClickedCellIndex(final float x, final float y) {
		for (int i = 0; i < cells.length; i++)
			if (cells[i].getLeft() < x && cells[i].getRight() > x && cells[i].getTop() < y && cells[i].getBottom() > y) {
				clickedCellIndex = i;
				return i;
			}
		return -1;
	}

	/**
	 * int tempImageIndex = cells[cell1].getJigsawImageIndex();
	 * 
	 */
	public boolean onTouchEvent(MotionEvent event) {
		int cellIndex = findClickedCellIndex(event.getX(), event.getY());
		// TODO handle Click should return true if any cell is swap
		// and call invalidate only if it return true
		if (cellIndex >= 0)
			handelClick(cellIndex);
		invalidate();
		if (isGameOver())
			new AlertDialog.Builder(context).setMessage("GAme Over").setTitle("U Rocks").setNegativeButton("OK ", null).show();
		return false;
	}

	/**
	 * 
	 * @param cell1
	 * @param cell2
	 */
	public void swapCellsImageIndex(int cell1, int cell2) {
		int tempImageIndex = cells[cell1].getJigsawImageIndex();
		cells[cell1].setJigsawImageIndex(cells[cell2].getJigsawImageIndex());
		cells[cell2].setJigsawImageIndex(tempImageIndex);
		if (cells[cell1].getCurrentPosition() == emptyCellIndex)
			emptyCellIndex = cells[cell2].getCurrentPosition();
		else if (cells[cell2].getCurrentPosition() == emptyCellIndex)
			emptyCellIndex = cells[cell1].getCurrentPosition();

	}

	/**
	 * 
	 * @param clickedCellIndex
	 */
	public void handelClick(int clickedCellIndex) {

		// new AlertDialog.Builder(context).setMessage(
		// "Clicked=" + clickedCellIndex + "\n Positiion="
		// + cells[clickedCellIndex].getCurrentPosition()
		// + "\n Empty=" + emptyCellIndex).setNegativeButton("Ok",
		// null).show();
		int columns = getColumnCount();
		int rows = getRowCount();

		if (clickedCellIndex == emptyCellIndex) {
			return;
		}
		if (clickedCellIndex == getLastCellIndex()) { // last cell is clicked
			if (get2ndLastCellIndex() == emptyCellIndex)
				swapCellsImageIndex(clickedCellIndex, get2ndLastCellIndex());
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
		else if ((clickedCellIndex + 1) % columns == 0) {
			// Clicked in Last Column
			if (clickedCellIndex == get2ndLastCellIndex()) {
				if (cells[getLastCellIndex()].getJigsawImageIndex() == emptyCellIndex)
					swapCellsImageIndex(clickedCellIndex, getLastCellIndex());
				else
					checkEmptyCellSide(clickedCellIndex, "LU");
			} else
				checkEmptyCellSide(clickedCellIndex, "LUD");
		}// Last Column clicked END
		else if (clickedCellIndex % columns == 0) {// First Column Clicked
			if (clickedCellIndex == columns * (rows - 1)) {
				// last Cell of first Column(First Cell of last row -:D ) is
				// clicked
				// possible side of Empty cell can be Top,Right
				checkEmptyCellSide(clickedCellIndex, "UR");
			} else {
				// Any other cell(Other than last and first(witch is already
				// checked in first row)) from first column is clicked
				// Possible side for Empty cell can be Top,Down,Right
				checkEmptyCellSide(clickedCellIndex, "UDR");
			}
		}// First Column Clicked END
		else if (clickedCellIndex > columns * (rows - 1) && clickedCellIndex < columns * rows - 1) {// Last
																									// Row
																									// Clicked
			// possible Empty cell side can be Left,Right,or Top...first and
			// last cell clicked are already checked
			// new
			// AlertDialog.Builder(context).setMessage("CC="+clickedCellIndex+"\n EC="+emptyCellIndex).show();

			checkEmptyCellSide(clickedCellIndex, "LRU");
		}// Last row clicked END
		else {
			// ah First row,column and Last row ,column check is end
			// now clicked in any cell they can be moved in any direction so
			// check for all four sides
			checkEmptyCellSide(clickedCellIndex, "LRUD");// piece of cake
			// calling this
			// method
		}
	}

	/**
	 * 
	 * @param cellIndex
	 * @return
	 */
	public boolean isCellIsInTopLeftCorner(int cellIndex) {
		return true;
	}

	/**
	 * 
	 * @param cellIndex
	 * @return
	 */
	public boolean isCellIsInTopRightCorner(int cellIndex) {
		return true;
	}

	/**
	 * 
	 * @param cellIndex
	 * @return
	 */
	public boolean isCellIsInBottomLeftCorner(int cellIndex) {
		return true;
	}

	public int getTopRightCellIndex() {
		return columnCount;
	}

	public int getTopLeftCellIndex() {
		return 0;
	}

	public int getBottomRightCellIndex() {
		return get2ndLastCellIndex();
	}

	public int getBottomLeftCellIndex() {
		return columnCount * (rowCount - 1);

	}

	/**
	 * 
	 * @param cellIndex
	 * @return
	 */
	public boolean isCellIsInBottomRightCorner(int cellIndex) {
		return getBottomRightCellIndex() == cellIndex;
	}

	/**
	 * 
	 * @param cellIndex
	 * @return
	 */
	public boolean isCellIsInLastRow(int cellIndex) {
		return cellIndex >= getBottomLeftCellIndex() && cellIndex <= getBottomRightCellIndex();
	}

	/**
	 * 
	 * @param cellIndex
	 * @return
	 */
	public Boolean isCellIsInFirstColumn(int cellIndex) {

		return (cellIndex % columnCount == 0) && emptyCellIndex != getLastCellIndex();
	}

	/**
	 * 
	 * @param cellIndex
	 * @return
	 */
	public boolean isCellIsInLastColumn(int cellIndex) {
		return cellIndex % columnCount == 2 || cellIndex == getLastCellIndex();
	}

	/**
	 * 
	 * @param cellIndex
	 * @return
	 */
	public boolean isCellIsInFirstRow(int cellIndex) {
		return cellIndex < columnCount;
	}

	/**
	 * 
	 * @return
	 */
	public int get2ndLastCellIndex() {
		return jigsawCellImages.length - 2;
	}

	/**
	 * 
	 * @return
	 */
	public int getLastCellIndex() {
		return jigsawCellImages.length - 1;
	}

	/**
	 * 
	 * @param clickedCellIndex
	 * @param possibleSides
	 */
	public void checkEmptyCellSide(int clickedCellIndex, String possibleSides) {
		// new AlertDialog.Builder(context).setMessage(possibleSides).show();
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
	public boolean isEmptyCellIsOnLeft(int clickedCellIndex) {

		return (clickedCellIndex - 1) == emptyCellIndex;
	}

	/**
	 * 
	 * @param clickedCellIndex
	 * @return
	 */
	public boolean isEmptyCellIsOnRight(int clickedCellIndex) {
		return (clickedCellIndex + 1) == emptyCellIndex;
	}

	/**
	 * 
	 * @param clickedCellIndex
	 * @return
	 */
	public boolean isEmptyCellIsOnUp(int clickedCellIndex) {
		return (clickedCellIndex - columnCount) == emptyCellIndex;
	}

	/**
	 * 
	 * @param clickedCellIndex
	 * @return
	 */
	public boolean isEmptyCellIsOnDown(final int clickedCellIndex) {
		return (clickedCellIndex + columnCount) == emptyCellIndex;
	}

	/**
	 * 
	 * @param currentCellIndex
	 */
	public void moveDown(int currentCellIndex) {
		// new AlertDialog.Builder(context).setMessage("d").show();

		// + setting.getBoardColumnCount());

		int moveToIndex = currentCellIndex + columnCount;
		swapCellsImageIndex(currentCellIndex, moveToIndex);

	}

	/**
	 * 
	 * @param currentCellIndex
	 */
	public void moveUp(int currentCellIndex) {
		int moveToIndex = currentCellIndex - columnCount;
		swapCellsImageIndex(currentCellIndex, moveToIndex);

	}

	/**
	 * 
	 * @param currentCellIndex
	 */
	public void moveLeft(int currentCellIndex) {
		if (currentCellIndex - 1 == emptyCellIndex)
			swapCellsImageIndex(currentCellIndex, currentCellIndex - 1);
	}

	/**
	 * 
	 * @param currentCellIndex
	 */
	public void moveRight(int currentCellIndex) {
		if (currentCellIndex + 1 == emptyCellIndex)
			swapCellsImageIndex(currentCellIndex, currentCellIndex + 1);
	}

	/**
	 * check if empty cell can move left side
	 * 
	 * @return true if empty cell can move left side false otherwise
	 */
	public boolean canEmptyCellMoveLeft() {
		// new AlertDialog.Builder(context).setMessage(
		// "Firstcol" +
		// isCellIsInFirstColumn(emptyCellIndex)+" empty "+emptyCellIndex
		// +"lasr  "+getLastCellIndex()).setNegativeButton("OK", null)
		// .show();
		if (emptyCellIndex == getLastCellIndex())
			return false;
		else
			return !isCellIsInFirstColumn(emptyCellIndex);
	}

	/**
	 * check if empty cell can move rigt side
	 * 
	 * @return true if empty cell can move right side false otherwise
	 */
	public boolean canEmptyCellMoveRight() {
		if (emptyCellIndex == getLastCellIndex())
			return false;
		else
			return !isCellIsInLastColumn(emptyCellIndex);
	}

	/**
	 * check if empty cell can move Up side
	 * 
	 * @return true if empty cell can move Up side false otherwise
	 */
	public boolean canEmptyCellMoveUp() {
		return !isCellIsInFirstRow(emptyCellIndex);
	}

	/**
	 * check if empty cell can move down side
	 * 
	 * @return true if empty cell can move down side false otherwise
	 */
	public Boolean canEmptyCellMoveDown() {
		if (isCellIsInBottomRightCorner(emptyCellIndex)) {
			return true;
		} else if (getLastCellIndex() == emptyCellIndex)
			return false;
		else {
			// Boolean c = isCellIsInLastRow(emptyCellIndex);
			// new AlertDialog.Builder(context).setMessage(
			// "Last Row" + c.toString()).setNegativeButton("OK", null)
			// .show();
			return !isCellIsInLastRow(emptyCellIndex);
		}
	}

	/**
	 * 
	 * @param currentCellIndex
	 * @return
	 */
	public int getUpCellIndex(int currentCellIndex) {
		if (getLastCellIndex() == currentCellIndex)
			return currentCellIndex - 1;
		else
			return currentCellIndex - columnCount;
	}

	/**
	 * 
	 * @param currentCellIndex
	 * @return
	 */
	public int getDownCellIndex(int currentCellIndex) {
		if (get2ndLastCellIndex() == currentCellIndex)
			return currentCellIndex + 1;
		else
			return currentCellIndex + columnCount;
	}

	/**
	 * 
	 * @param currentCellIndex
	 * @return
	 */
	public int getLeftCellIndex(int currentCellIndex) {
		return currentCellIndex - 1;
	}

	/**
	 * 
	 * @param currentCellIndex
	 * @return
	 */
	public int getRightCellIndex(int currentCellIndex) {
		return currentCellIndex + 1;
	}

	/**
	 * 
	 * @return true if move is successfull false otherwise
	 */
	public boolean moveEmptyCellUp() {
		if (canEmptyCellMoveUp()) {
			swapCellsImageIndex(emptyCellIndex, getUpCellIndex(emptyCellIndex));
			invalidate();
			return true;
		} else
			return false;
	}

	/**
	 * 
	 * @return true if move is successfull false otherwise
	 */
	public boolean moveEmptyCellDown() {
		if (canEmptyCellMoveDown()) {
			swapCellsImageIndex(emptyCellIndex, getDownCellIndex(emptyCellIndex));
			invalidate();
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return true if move is successfull false otherwise
	 */

	public boolean moveEmptyCellLeft() {
		if (canEmptyCellMoveLeft()) {
			swapCellsImageIndex(emptyCellIndex, getLeftCellIndex(emptyCellIndex));
			invalidate();
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return true if move is successful false otherwise
	 */
	public boolean moveEmptyCellRight() {
		if (canEmptyCellMoveRight()) {
			swapCellsImageIndex(emptyCellIndex, getRightCellIndex(emptyCellIndex));
			invalidate();
			return true;
		}
		return false;
	}

	public void playInvalidMoveSound() {

	}

	public void setColumnCount(int cCount, boolean redraw) {
		if (columnCount != cCount) {
			columnCount = cCount;
			if (redraw)
				invalidate();
		}
	}

	public void setColumnCount(int cCount) {
		setColumnCount(cCount, false);
	}

	public void setRowCount(int rCount, boolean redraw) {
		if (rowCount != rCount) {
			rowCount = rCount;
			if (redraw)
				invalidate();
		}
	}

	public void setRowCount(int rCount) {
		setRowCount(rCount, false);
	}

	public int getColumnCount() {
		return columnCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	private void load(Context context, AttributeSet attrs, int defStyle) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JigsawBoardView, defStyle, 0);
		cellShuffleCount = a.getInteger(R.styleable.JigsawBoardView_cell_shuffle_count, Integer.parseInt(getResources().getString(R.string.jigsaw_baord_cell_shuffle_count)));

		final int rows = a.getInteger(R.styleable.JigsawBoardView_rows_count, Integer.parseInt(getResources().getString(R.string.jigsaw_baord_default_rows)));
		final int columns = a.getInteger(R.styleable.JigsawBoardView_columns_count, Integer.parseInt(getResources().getString(R.string.jigsaw_baord_default_columns)));

		setRowCount(rows, false);
		setColumnCount(columns, false);

		final int jigsaw_image_id = a.getResourceId(R.styleable.JigsawBoardView_jigsaw_image,-1);
		final Bitmap jigsawImg = BitmapFactory.decodeResource(getResources(), jigsaw_image_id);
		if (jigsawImg != null) {
			jigsawOrigionalImage = jigsawImg;
		}
		a.recycle();
	}

	/**
	 * @param img
	 */
	public void setImage(Bitmap img) {
		jigsawOrigionalImage=img;
		
	}
}

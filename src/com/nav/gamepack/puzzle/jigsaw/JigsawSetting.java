package com.nav.gamepack.puzzle.jigsaw;

import com.nav.gamepack.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

/**
 * @author Naveed Ahmad
 * 
 */

public class JigsawSetting extends View {
	private int defaultShuffleCount;
	private int defaultJigsawBoardWidth;
	private int defaultJigsawBoardHeight;
	private int ShuffleCount;
	private int defaultBoardRowCount;
	private int defaultBoardColumnCount;
	private int boardColumnCount;
	private int boardRowCount;
	/**
	 * @playSoundOnInvalidMove if set to true will play a sound if user tries to play invalid move
	 */
    private boolean playSoundOnInvalidMove;
    
	private int jigwasCellWidth;
	private int jigwasCellHeight;
	private int jigsawBoardWidth;
	private int jigsawBoardHeight;
	private Bitmap currentJigwasImage;

	public JigsawSetting(Context context, int boardWidth, int boardHeight) {
		super(context);
		defaultShuffleCount = 14;
		defaultBoardRowCount =3;
			defaultBoardColumnCount = 3;
		boardColumnCount = boardRowCount = 0;
		playSoundOnInvalidMove=true;
		defaultJigsawBoardHeight = defaultJigsawBoardWidth = (boardHeight>boardWidth ? boardWidth :boardHeight);//use value whose size is less
		this.setJigsawBoardWidth(boardWidth);
		this.setJigsawBoardHeight(boardHeight);
	}

	public JigsawSetting setJigwasImage(Bitmap image) {
		currentJigwasImage = Bitmap.createScaledBitmap(image, jigsawBoardWidth,
				jigsawBoardHeight, true);
		return this;
	}

	public Bitmap getJigwasImage() {
		if (currentJigwasImage == null) {
			// TODO Show Select Image View//
			currentJigwasImage = Bitmap.createScaledBitmap(BitmapFactory
					.decodeStream(getResources().openRawResource(
							R.drawable.image)), jigsawBoardWidth,
					jigsawBoardHeight, true);

		}
		return currentJigwasImage;
	}

	/*
	 * Use to get shuffle count..this count is used to shuffle cell ofjigwas
	 * board. if shuffle count is not set return default count
	 */
	public int getShuffleCount() {
		return (ShuffleCount == 0 ? defaultShuffleCount : ShuffleCount);
	}

	/*
	 * set row count,and column count of board return true if successfull
	 */
	public boolean setBoardSize(int size) {
		if (size > 2) {
			boardColumnCount = boardRowCount = size;
			return true;
		}
		return false;
	}

	/*
	 * return column count of jigsaw board.return default count if ones dose not
	 * set
	 */
	public int getBoardColumnCount() {
		return boardColumnCount == 0 ? defaultBoardColumnCount
				: boardColumnCount;
	}

	/*
	 * 
	 */
	public void setBoardColumnCount(int columnCount) {
		boardColumnCount = columnCount > defaultBoardColumnCount ? columnCount
				: defaultBoardColumnCount;
	}

	/**
	 * 
	 * @param rowCount
	 *            count og jigsaw board rows
	 */
	public void setBoardRowCount(int rowCount) {
		boardRowCount = rowCount > defaultBoardRowCount ? rowCount
				: defaultBoardRowCount;
	}

	/*
	 * return row count of jigsaw board.return default count if ones dose not
	 * set
	 */
	public int getBoardRowCount() {
		return boardRowCount == 0 ? defaultBoardRowCount : boardRowCount;
	}

	/*
	 * return width of jigsaw cell
	 */
	public int getJigsawCellWidth() {
		return jigwasCellWidth;
	}

	/*
	 * return height of jigsaw cell
	 */
	public int getJigsawCellHeight() {
		return jigwasCellHeight;
	}

	/*
	 * initialize jigsaw cell height and width. return JigwasSetting for method
	 * chaining
	 */
	public JigsawSetting prepareJigwasCellDimension() {
		jigwasCellHeight = currentJigwasImage.getHeight() / getBoardRowCount();
		jigwasCellWidth = currentJigwasImage.getWidth() / getBoardColumnCount();
		return this;
	}

	/**
	 * @param jigsawBoardWidth
	 *            the jigsawBoardWidth to set
	 */
	public void setJigsawBoardWidth(int jigsawBoardWidth) {
		this.jigsawBoardWidth = jigsawBoardWidth  > defaultJigsawBoardWidth ? jigsawBoardWidth
				: defaultJigsawBoardWidth;
		//this.jigsawBoardWidth=jigsawBoardWidth;
	}

	/**
	 * @return the jigsawBoardWidth
	 */
	public int getJigsawBoardWidth() {
		return jigsawBoardWidth;
	}

	/**
	 * @param jigsawBoardWidth
	 *            the jigsawBoardWidth to set
	 */
	public void setJigsawBoardHeight(int jigsawBoardHeight) {
	this.jigsawBoardHeight = jigsawBoardHeight > defaultJigsawBoardHeight ? jigsawBoardHeight
				: defaultJigsawBoardHeight;
	//this.jigsawBoardHeight=jigsawBoardHeight;
	}

	/**
	 * @return the jigsawBoardWidth
	 */
	public int getJigsawBoardHeight() {
		return jigsawBoardHeight;
	}

}

package com.nav.gamepack.puzzle.jigsaw;

import android.content.Context;
import android.view.View;

/**
 * @author Naveed Ahmad
 *
 */


public class JigsawCell extends View {
  
	private int cellCurrentPosition;//current position of this cell in board
	private int cellImageIndex;//final position in board
	
	public JigsawCell(Context context) {
		super(context);
		
	}

	/**
	 * @param cellImageIndex the cellImageIndex to set
	 */
	public void setJigsawImageIndex(int jigsawImageIndex) {
		this.cellImageIndex = jigsawImageIndex;
	}

	/**
	 * @return the cellImageIndex
	 */
	public int getJigsawImageIndex() {
		return cellImageIndex;
	}

	/**
	 * @param cellCurrentPosition the cellCurrentPosition to set
	 */
	public void setCellCurrentPosition(int cellCurrentPosition) {
		this.cellCurrentPosition = cellCurrentPosition;
	}

	/**
	 * @return the cellCurrentPosition
	 */
	public int getCurrentPosition() {
		return cellCurrentPosition;
	}
	

}

package com.nav.gamepack.puzzle.jigsaw;

import android.R.bool;
import android.view.KeyEvent;
import android.view.View;

public class JigsawBoardKeyListener implements View.OnKeyListener {

	JigsawBoardView jigsawBoard;

	public JigsawBoardKeyListener(JigsawBoardView board) {
		jigsawBoard = board;
	}

	@SuppressWarnings("static-access")
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
	Boolean result=true;
		if (event.getAction() == KeyEvent.ACTION_UP) {
			if (keyCode == event.KEYCODE_DPAD_DOWN)
				result= jigsawBoard.moveEmptyCellDown();
			if (keyCode == event.KEYCODE_DPAD_RIGHT)
				result=	jigsawBoard.moveEmptyCellRight();
			if (keyCode == event.KEYCODE_DPAD_UP)
				result=	jigsawBoard.moveEmptyCellUp();
			if (keyCode == event.KEYCODE_DPAD_LEFT)
				result=jigsawBoard.moveEmptyCellLeft();
			if(!result){
				jigsawBoard.playInvalidMoveSound();
			}
			return false;
		} else
			return true;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return true;
	}
}

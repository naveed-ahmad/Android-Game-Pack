package com.nav.gamepack.puzzle.jigsaw;

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
		if (event.getAction() == KeyEvent.ACTION_UP) {
			if (keyCode == event.KEYCODE_DPAD_DOWN)
				jigsawBoard.moveEmptyCellDown();
			if (keyCode == event.KEYCODE_DPAD_RIGHT)
				jigsawBoard.moveEmptyCellRight();
			if (keyCode == event.KEYCODE_DPAD_UP)
				jigsawBoard.moveEmptyCellUp();
			if (keyCode == event.KEYCODE_DPAD_LEFT)
				jigsawBoard.moveEmptyCellLeft();
			return false;
		} else
			return true;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return true;
	}
}

package com.nav.gamepack;

import com.nav.gamepack.puzzle.jigsaw.JigsawBoardView;

import android.app.Activity;
import android.os.Bundle;

public class GamePackActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JigsawBoardView board=new JigsawBoardView(this);
        setContentView(board);
    }
}
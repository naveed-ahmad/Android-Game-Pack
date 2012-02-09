package com.nav.gamepack;

import com.nav.gamepack.puzzle.jigsaw.ImageChooserActivity;
import com.nav.gamepack.puzzle.jigsaw.JigsawActivity;
import com.nav.gamepack.puzzle.jigsaw.JigsawBoardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GamePackActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent getImageIntent=new Intent(GamePackActivity.this,ImageChooserActivity.class);
		startActivity(getImageIntent);
    }
}
package com.nav.gamepack;

import java.util.ArrayList;

import com.nav.gamepack.puzzle.jigsaw.ImageChooserActivity;
import com.nav.gamepack.puzzle.jigsaw.JigsawActivity;
import com.nav.gamepack.puzzle.jigsaw.JigsawBoardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class GamePackActivity extends Activity {
	private final int CHOOSE_IMAGE_CODE = 36;
	private final String TAG = "GamePackActivity";
	JigsawBoardView jbv;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
jbv=(JigsawBoardView)findViewById(R.id.jigsawBoardView1);
		Intent getImageIntent = new Intent(GamePackActivity.this, ImageChooserActivity.class);
		startActivityForResult(getImageIntent, CHOOSE_IMAGE_CODE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "response from google");
		if (requestCode == CHOOSE_IMAGE_CODE && resultCode == RESULT_OK) {
			// Yeah we have an image lets get it and play jigsaw :)
			Bitmap img = (Bitmap) data.getParcelableExtra("image");
			
			jbv.setImage(img);
			jbv.prepareJigsawBoardImage();
			jbv.shuffleCells();
			//addContentView(jbv, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		}else
		 new AlertDialog.Builder(this).setMessage("NO").show();
		super.onActivityResult(requestCode, resultCode, data);
	}

}
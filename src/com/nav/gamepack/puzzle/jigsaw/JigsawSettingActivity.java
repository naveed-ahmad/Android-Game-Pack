/**
 * 
 */
package com.nav.gamepack.puzzle.jigsaw;

import java.text.ChoiceFormat;

import com.nav.gamepack.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.SimpleAdapter.ViewBinder;

/**
 * @author naveed
 * 
 */
public class JigsawSettingActivity extends Activity implements OnClickListener, OnSeekBarChangeListener, OnFocusChangeListener {
	private static String TAG = "JigsawSettingActivity";
	private static int GET_IMAGE_REQUEST_IDENTIFIER=1005;
	private Button btnRandomizeCells, btnChangeRowCount, btnChangeColumnCount, btnStartGame, btnChoosePicture;
	private AlertDialog dlgChangeCellSize;
	private SeekBar seekBarCellSize;
	private TextView txtViewSizeCount, txtViewMoreCell;
	private CheckBox chkBoxSameSize;
	private EditText editTextCustomSize;
	private JigsawBoardView jigsawBoard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jigsaw_setting);
		init();
	}

	private void init() {
		btnRandomizeCells = (Button) findViewById(R.id.btnRandomizeCell);
		btnChangeRowCount = (Button) findViewById(R.id.btnChangeRows);
		btnChangeColumnCount = (Button) findViewById(R.id.btnChangeColumns);
		btnStartGame = (Button) findViewById(R.id.btnStartGame);
		btnChoosePicture = (Button) findViewById(R.id.btnChangePicture);
		jigsawBoard = (JigsawBoardView) findViewById(R.id.jigsawBoardViewDemo);

		btnChangeColumnCount.setOnClickListener(this);
		btnChangeRowCount.setOnClickListener(this);
		btnChoosePicture.setOnClickListener(this);
		btnStartGame.setOnClickListener(this);
		btnRandomizeCells.setOnClickListener(this);

		initlizeDialogs();
	}

	/**
	 * 
	 */
	private void initlizeDialogs() {
		LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View cellSizeChooserView = layoutInflater.inflate(R.layout.board_cell_size_chooser, null);
		seekBarCellSize = (SeekBar) cellSizeChooserView.findViewById(R.id.seekBarBoardCellCount);
		txtViewSizeCount = (TextView) cellSizeChooserView.findViewById(R.id.txtViewCellSize);
		chkBoxSameSize = (CheckBox) cellSizeChooserView.findViewById(R.id.checkBoarSameSize);
		editTextCustomSize = (EditText) cellSizeChooserView.findViewById(R.id.editTextCustomSize);
		editTextCustomSize.setOnFocusChangeListener(this);

		seekBarCellSize.setOnSeekBarChangeListener(this);

		dlgChangeCellSize = new AlertDialog.Builder(this).setView(cellSizeChooserView).setPositiveButton("OK", null).setNegativeButton("Cancel", null).create();
		dlgChangeCellSize.setOnShowListener(new DialogInterface.OnShowListener() {
			public void onShow(DialogInterface dialotextg) {
				Button b = dlgChangeCellSize.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						dlgChangeCellSize.dismiss();
						if (txtViewSizeCount.getTag().toString().equalsIgnoreCase("r"))
							jigsawBoard.setRowCount(Integer.parseInt(txtViewSizeCount.getText().toString()));
						else if (txtViewSizeCount.getTag().toString().equalsIgnoreCase("c"))
							jigsawBoard.setColumnCount(Integer.parseInt(txtViewSizeCount.getText().toString()));
						jigsawBoard.initBoard(true, true);
						jigsawBoard.invalidate();
					}
				});
			}
		});

	}

	@Override
	public void onFocusChange(View view, boolean focused) {
		if (view.getId() == editTextCustomSize.getId() && !focused)
			txtViewSizeCount.setText(editTextCustomSize.getText().toString());
	}

	@Override
	public void onClick(View clickedBtn) {
		if (clickedBtn.getId() == btnChangeColumnCount.getId())
			showChangeCellSizeDialogFor("columns");
		else if (clickedBtn.getId() == btnChangeRowCount.getId())
			showChangeCellSizeDialogFor("rows");
		else if (clickedBtn.getId() == btnChoosePicture.getId())
			showChoosePictureDialog();
		else if (clickedBtn.getId() == btnStartGame.getId())
			endActivityWithResult();

	}

	/**
	 * 
	 */
	private void endActivityWithResult() {

	}

	/**
	 * 
	 */
	private void showChoosePictureDialog() {
        Intent chooseImageIntent=new Intent();
        chooseImageIntent.setClass(JigsawSettingActivity.this, ImageChooserActivity.class);
        startActivityForResult(chooseImageIntent, GET_IMAGE_REQUEST_IDENTIFIER);
	}

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "response choose image");
		if (requestCode ==  GET_IMAGE_REQUEST_IDENTIFIER && resultCode == RESULT_OK) {
			// Yeah we have an image lets get it and play jigsaw :)
			Bitmap img = (Bitmap) data.getParcelableExtra("image");
			jigsawBoard.setImage(img);
			jigsawBoard.initBoard(true,true);
			
			// addContentView(jbv, new
			// LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		} 
		else
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * @param string
	 */
	private void showChangeCellSizeDialogFor(String s) {
		dlgChangeCellSize.setTitle("Choose Number of " + s);
		Log.i(TAG, "Opening change board cell count for " + s);
		if (s.equalsIgnoreCase("rows")) {
			chkBoxSameSize.setText("Also Update Columns");
			txtViewSizeCount.setTag("r");
		} else {
			chkBoxSameSize.setText("Also Update Rows");
			txtViewSizeCount.setTag("c");
		}
		dlgChangeCellSize.show();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (progress < 3)
			seekBar.setProgress(3);
		LinearLayout lnrlayoutCustomSize = (LinearLayout) dlgChangeCellSize.findViewById(R.id.lnrLayouCustomCellSize);
		;

		if (progress == 10)
			lnrlayoutCustomSize.setVisibility(View.VISIBLE);
		else
			lnrlayoutCustomSize.setVisibility(View.GONE);

		txtViewSizeCount.setText(seekBar.getProgress() + "");

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
}

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
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Path.FillType;
import android.graphics.Picture;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.ImageView.ScaleType;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

/**
 * @author naveed
 * 
 */
public class JigsawSettingActivity extends Activity implements OnClickListener, OnSeekBarChangeListener {
	private static String TAG = "JigsawSettingActivity";
	private static int GET_IMAGE_REQUEST_IDENTIFIER = 1005;
	private Button btnRandomizeCells, btnChangeRowCount, btnChangeColumnCount, btnStartGame, btnChoosePicture, btnViewImage;// ,
																															// btnBack;
	private ImageButton btnRotateLeft, btnRotateRight;
	private AlertDialog dlgChangeCellSize;
	private SeekBar seekBarCellSize;
	private TextView txtViewSizeCount, txtViewMoreCell;
	private CheckBox chkBoxSameSize;
	private EditText editTextCustomSize;
	private JigsawBoardView jigsawBoard;
	private Bitmap jigsawImage;
	ImageView testImgView;
	View cellSizeChooserView;
	Animation shakeAnimation;
	JigsawSetting setting;

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
		// btnBack = (Button) findViewById(R.id.btnBack);
		jigsawBoard = (JigsawBoardView) findViewById(R.id.jigsawBoardViewDemo);
		jigsawBoard.setCallerActivity(this);
		testImgView = (ImageView) findViewById(R.id.imageView1);
		btnViewImage = (Button) findViewById(R.id.btnViewImage);
		btnChangeColumnCount.setOnClickListener(this);
		btnChangeRowCount.setOnClickListener(this);
		btnChoosePicture.setOnClickListener(this);
		btnStartGame.setOnClickListener(this);
		btnRandomizeCells.setOnClickListener(this);
		btnViewImage.setOnClickListener(this);
		// btnBack.setOnClickListener(this);
		shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);

		setting = JigsawSetting.getSetting();
		setting.enableTouch = true;
		setting.cropImage = true;
		
		setting.jigsawImage = BitmapFactory.decodeResource(getResources(), R.drawable.default_jigsaw_image);
		jigsawBoard.setSetting(setting);
		initlizeDialogs();
	}

	/**
	 * 
	 */
	private void initlizeDialogs() {
		Log.i(TAG, "Init Dialogs");
		LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		cellSizeChooserView = layoutInflater.inflate(R.layout.board_cell_size_chooser, null);
		seekBarCellSize = (SeekBar) cellSizeChooserView.findViewById(R.id.seekBarBoardCellCount);
		txtViewSizeCount = (TextView) cellSizeChooserView.findViewById(R.id.txtViewCellSize);
		chkBoxSameSize = (CheckBox) cellSizeChooserView.findViewById(R.id.checkBoarSameSize);
		editTextCustomSize = (EditText) cellSizeChooserView.findViewById(R.id.editTextCustomSize);
		btnRotateLeft = (ImageButton) findViewById(R.id.btnRotateLeft);
		btnRotateRight = (ImageButton) findViewById(R.id.btnRotateRight);
		seekBarCellSize.setOnSeekBarChangeListener(this);
		editTextCustomSize.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				txtViewSizeCount.setText(editTextCustomSize.getText().toString());
				return false;
			}
		});

		dlgChangeCellSize = new AlertDialog.Builder(this).setView(cellSizeChooserView).setPositiveButton("OK", null).setNegativeButton("Cancel", null).create();
		dlgChangeCellSize.setOnShowListener(new DialogInterface.OnShowListener() {
			public void onShow(DialogInterface dialotextg) {
				Button b = dlgChangeCellSize.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						Log.i(TAG, "changing board size tag is " + txtViewSizeCount.getTag());
						int cellCount = 0;
						try {
							cellCount = Integer.parseInt(txtViewSizeCount.getText().toString());
						} catch (Exception e) {
							Log.e(TAG, "error in parsing cell size", e);
						}
						if (cellCount < 3)
							editTextCustomSize.startAnimation(shakeAnimation);
						else {
							dlgChangeCellSize.dismiss();
							if (txtViewSizeCount.getTag().toString().equalsIgnoreCase("r")) {
								// jigsawBoard.setRowCount(cellCount);
								setting.rowCount = cellCount;
								if (chkBoxSameSize.isChecked())
									// jigsawBoard.setColumnCount(cellCount);
									setting.columnCount = cellCount;
							} else if (txtViewSizeCount.getTag().toString().equalsIgnoreCase("c")) {
								setting.columnCount = cellCount;
								// jigsawBoard.setColumnCount(cellCount);
								if (chkBoxSameSize.isChecked())
									setting.rowCount = cellCount;
								// jigsawBoard.setRowCount(cellCount);
							}
							jigsawBoard.setSetting(setting);
							jigsawBoard.initBoard();
							jigsawBoard.invalidate();
						}
					}
				});
			}
		});
		btnRotateRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				rotateJigsawImage(90);

			}
		});
		btnRotateLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				rotateJigsawImage(-90);
			}
		});
	}

	@Override
	public void onClick(View clickedBtn) {
		if (clickedBtn.getId() == btnChangeColumnCount.getId())
			showChangeCellSizeDialogFor("Columns");
		else if (clickedBtn.getId() == btnChangeRowCount.getId())
			showChangeCellSizeDialogFor("Rows");
		else if (clickedBtn.getId() == btnChoosePicture.getId())
			showChoosePictureDialog();
		else if (clickedBtn.getId() == btnStartGame.getId())
			endActivityWithResult();
		else if (clickedBtn.getId() == btnViewImage.getId())
			showJigsawImageDialog("Jigsaw Imge");
		// else if (clickedBtn.getId() == btnBack.getId())
		// endActivityWithoutResult();
		else if (clickedBtn.getId() == btnRandomizeCells.getId()) {
			jigsawBoard.shuffleCells();
			jigsawBoard.invalidate();
		}

	}

	/**
	 * 
	 */
	private void endActivityWithResult() {
		Intent resultIntent = new Intent();
		// resultIntent.putExtra("imageI", jigsawBoard.getJigsawImage());
		resultIntent.putExtra("rowsCount", jigsawBoard.getRowCount() + "");
		resultIntent.putExtra("rowsColumnsCount", jigsawBoard.getColumnCount());
		resultIntent.putExtra("jigsawCellMap", jigsawBoard.getJigsawCellImageMapping());
		setting.cellImageMapping = jigsawBoard.getJigsawCellImageMapping();
		if (setting != JigsawSetting.getSetting())
			JigsawSetting.setSetting(setting);
		// if (getParent() == null)
		setResult(Activity.RESULT_OK, resultIntent);
		// else
		// getParent().setResult(Activity.RESULT_OK, resultIntent);

		// new AlertDialog.Builder(this).setMessage(msg).show();
		Log.i(TAG, "finishing setting activity with result");

		finish();
	}

	private void endActivityWithoutResult() {
		if (getParent() == null)
			setResult(Activity.RESULT_CANCELED, null);
		else
			getParent().setResult(Activity.RESULT_CANCELED, null);
		Log.i(TAG, "finishing setting activity with out result");
		finish();
	}

	/**
	 * 
	 */
	private void showChoosePictureDialog() {
		Intent chooseImageIntent = new Intent();
		chooseImageIntent.setClass(JigsawSettingActivity.this, ImageChooserActivity.class);
		startActivityForResult(chooseImageIntent, GET_IMAGE_REQUEST_IDENTIFIER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "response choose image");
		if (requestCode == GET_IMAGE_REQUEST_IDENTIFIER && resultCode == RESULT_OK) {
			// Yeah we have an image lets get it and play jigsaw :)
			Bitmap img = (Bitmap) data.getParcelableExtra("image");
			setting.jigsawImage = img;
			jigsawBoard.setSetting(setting);
			jigsawBoard.initBoard();

			Toast.makeText(this, "new image in setting", Toast.LENGTH_LONG);
		} else
			super.onActivityResult(requestCode, resultCode, data);
	}

	private void showJigsawImageDialog(String s) {
		LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dlgView = layoutInflater.inflate(R.layout.slideshow_image_item, null);
		AlertDialog dlgJigsawImg = new AlertDialog.Builder(this).setTitle("Jigsaw Image").setPositiveButton("Back", null).setView(dlgView).create();
		ImageView imgView = (ImageView) dlgView.findViewById(R.id.imageItem);
		imgView.setImageBitmap(JigsawSetting.getSetting().jigsawImage);
		dlgJigsawImg.show();
		dlgJigsawImg.setTitle(s);
	}

	/**
	 * @param string
	 */
	private void showChangeCellSizeDialogFor(String s) {
		jigsawBoard.initializeSensorListener();
		dlgChangeCellSize.setTitle("Choose Number of " + s + " For JigsawBoard");
		dlgChangeCellSize.show();

		Log.i(TAG, "Opening change board cell count for " + s);
		LinearLayout lnrlayoutCustomSize = (LinearLayout) dlgChangeCellSize.findViewById(R.id.lnrLayouCustomCellSize);

		if (s.equalsIgnoreCase("rows")) {
			Log.i(TAG, "showChangeCellSizeDialog for rows");
			chkBoxSameSize.setText("Also Update Columns");
			txtViewSizeCount.setTag("r");
			int rows = jigsawBoard.getRowCount();
			if (rows > 10) {
				editTextCustomSize.setText(rows + "");
			} else {
				seekBarCellSize.setProgress(rows);
			}
			txtViewSizeCount.setText(rows + "");

		} else {
			Log.i(TAG, "showChangeCellSizeDialog for columns");
			chkBoxSameSize.setText("Also Update Rows");
			txtViewSizeCount.setTag("c");

			int columns = jigsawBoard.getColumnCount();
			if (columns > 10) {
				editTextCustomSize.setText(columns + "");
			} else {
				seekBarCellSize.setProgress(columns);
			}

			txtViewSizeCount.setText(columns + "");
		}
		dlgChangeCellSize.show();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (progress < 3)
			seekBar.setProgress(3);
		LinearLayout lnrlayoutCustomSize = (LinearLayout) dlgChangeCellSize.findViewById(R.id.lnrLayouCustomCellSize);
		if (progress >= 10) {
			lnrlayoutCustomSize.setVisibility(View.VISIBLE);
			if (editTextCustomSize.getText().toString().length() == 0)
				editTextCustomSize.setTag("10");
			txtViewSizeCount.setText(editTextCustomSize.getText().toString());
		} else {
			lnrlayoutCustomSize.setVisibility(View.GONE);
			txtViewSizeCount.setText(seekBar.getProgress() + "");
		}

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	private void rotateJigsawImage(float rotateDegree) {
		btnRotateLeft.setEnabled(false);
		btnRotateRight.setEnabled(false);

		Matrix transform = new Matrix();
		Bitmap src = setting.jigsawImage;

		Matrix m = new Matrix();

		// Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(),
		// src.getHeight(), m, false);
		// Canvas canvas = new Canvas(dst);
		// float xOfCentre = src.getWidth() / 2;
		// float yOfCentre = src.getHeight() / 2;
		// transform.setTranslate(xOfCentre, yOfCentre);
		// transform.preRotate(rotateDegree, src.getWidth() / 2, src.getHeight()
		// / 2);
		// canvas.drawBitmap(src, transform, null);
		m.postRotate(rotateDegree);
		try {
			// create a new bitmap from the original using the matrix to
			// transform
			// the result
			setting.jigsawImage = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, true);
		} catch (OutOfMemoryError e) {
			Toast.makeText(this, "Sorry we can't rotate this image", Toast.LENGTH_LONG);
		}
		src = null;
		// jigsawBoard.setJigsawImage(dst);
		jigsawBoard.setSetting(setting);
		jigsawBoard.initBoard();
		jigsawBoard.invalidate();

		btnRotateLeft.setEnabled(true);
		btnRotateRight.setEnabled(true);

	}
}

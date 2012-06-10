/**
 * 
 */
package com.nav.gamepack.puzzle.jigsaw;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

/**
 * @author naveed
 * 
 */
public class JigsawSensorEventListener implements View.OnKeyListener, SensorEventListener {
    private final String TAG="JigsawSensorEventListener";
	JigsawBoardView jigsawBoard;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Boolean accelerometerPresent;
	Activity callerActivity;
	public JigsawSensorEventListener(JigsawBoardView board) {
		jigsawBoard = board;
	}

	void initSensor(Activity callerActivity) {
		this.callerActivity=callerActivity;
		Log.i(TAG,"Initlizing sensor");
		if (mSensorManager == null) {
			Log.i(TAG,"Initlizing  new  sensor");
			mSensorManager = (SensorManager) callerActivity.getSystemService(callerActivity.SENSOR_SERVICE);
           
			mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
			List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
			mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			if (sensorList.size() > 0) {
				accelerometerPresent = true;
				mAccelerometer = sensorList.get(0);
			} else {
				accelerometerPresent = false;
			}
			Log.i(TAG,"Initlizing   sensor done accelerometer  present? "+accelerometerPresent.toString());
		
			Toast.makeText(callerActivity,"sensor accelerometer  present? "+accelerometerPresent, Toast.LENGTH_LONG);
			
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		Boolean result = true;
		if(!JigsawSetting.getSetting().enableKeyPad)
		return result;
		if (event.getAction() == KeyEvent.ACTION_UP) {
			if (keyCode == event.KEYCODE_DPAD_DOWN)
				result = jigsawBoard.moveEmptyCellDown();
			else if (keyCode == event.KEYCODE_DPAD_RIGHT)
				result = jigsawBoard.moveEmptyCellRight();
			else if (keyCode == event.KEYCODE_DPAD_UP)
				result = jigsawBoard.moveEmptyCellUp();
			else if (keyCode == event.KEYCODE_DPAD_LEFT)
				result = jigsawBoard.moveEmptyCellLeft();
			if (!result)
				jigsawBoard.playInvalidMoveSound();
			return false;
		} else
			return true;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.hardware.SensorEventListener#onAccuracyChanged(android.hardware
	 * .Sensor, int)
	 */
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.hardware.SensorEventListener#onSensorChanged(android.hardware
	 * .SensorEvent)
	 */
	@Override
	public void onSensorChanged(SensorEvent se) {
		//jigsawBoard.moveEmptyCellDown();
		Log.i(TAG,"sensor value changed ");
		Boolean a=se.sensor.getType() == Sensor.TYPE_ACCELEROMETER;
		float x1 = se.values[0];
		float y1 = se.values[1];
		float z1 = se.values[2];
		Toast.makeText(callerActivity,"sensor accelerometer value changed x"+x1+" y "+y1+" z"+z1, Toast.LENGTH_LONG);
			
		if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			Log.i(TAG,"sensor accelerometer value changed "+se.toString());
			float x = se.values[0];
			float y = se.values[1];
			float z = se.values[2];
			Log.i(TAG,"sensor accelerometer value changed x"+x+" y "+y+" z"+z);
						new AlertDialog.Builder(callerActivity).setMessage("sensor accelerometer value changed x"+x+" y "+y+" z"+z).setTitle("U Rocks").setNegativeButton("OK ", null).show();
			if (x >= y)
				jigsawBoard.moveEmptyCellDown();
			else if (y < 23)
				jigsawBoard.moveEmptyCellRight();

		}
	}

}

/**
 * 
 */
package com.nav.gamepack.puzzle.jigsaw;

import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.KeyEvent;
import android.view.View;

/**
 * @author naveed
 * 
 */
public class JigsawSensorEventListener implements View.OnKeyListener, SensorEventListener {

	JigsawBoardView jigsawBoard;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Boolean accelerometerPresent;

	public JigsawSensorEventListener(JigsawBoardView board) {
		jigsawBoard = board;
		mSensorManager = (SensorManager) JigsawActivity.getActivity().getSystemService(JigsawActivity.SENSOR_SERVICE);

		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
		List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (sensorList.size() > 0) {
			accelerometerPresent = true;
			mAccelerometer = sensorList.get(0);
		} else {
			accelerometerPresent = false;
		}

	}

	@SuppressWarnings("static-access")
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		Boolean result = true;
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
		if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float x = se.values[0];
			float y = se.values[1];
			float z = se.values[2];

			if (x >= y)
				jigsawBoard.moveEmptyCellDown();
			else if (y < 23)
				jigsawBoard.moveEmptyCellRight();

		}
	}

}

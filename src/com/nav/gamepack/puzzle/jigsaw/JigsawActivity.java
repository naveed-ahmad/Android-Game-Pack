package com.nav.gamepack.puzzle.jigsaw;

import com.nav.gamepack.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Naveed Ahmad
 * 
 */

public class JigsawActivity extends Activity {
	AlertDialog settingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
	}

	public void showSettingDialog() {
		if (settingDialog == null) {

			LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View signinView = layoutInflater.inflate(R.layout.jigsaw_setting, null);
			settingDialog = new AlertDialog.Builder(this).setView(signinView).setTitle("Login Required").setPositiveButton("Signin", null).create();
			settingDialog.setOnShowListener(new DialogInterface.OnShowListener() {
				public void onShow(DialogInterface dialog) {
					Button b = settingDialog.getButton(AlertDialog.BUTTON_POSITIVE);
					b.setOnClickListener(new View.OnClickListener() {
						public void onClick(View view) {

						}
					});
				}
			});
		}
		settingDialog.show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = new MenuInflater(getApplicationContext());
		menuInflater.inflate(R.menu.jigsaw_activity_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean handleMenuCick = true;
		if (item.getItemId() == R.id.menu_jigsaw_setting) {
			showSettingDialog();

		} else if (item.getItemId() == R.id.menu_back) {

		} else if (item.getItemId() == R.id.menu_save_game) {

		}

		else
			handleMenuCick = false;

		return handleMenuCick;
	}

}

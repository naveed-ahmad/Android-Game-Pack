/**
 * 
 */
package com.nav.gamepack.shared;

import com.nav.gamepack.R;

import android.app.ActivityGroup;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * @author naveed
 *
 */
public class WelcomeActivity extends ActivityGroup {

	/**
	 * 
	 */
	public WelcomeActivity() {

	}

	/**
	 * @param singleActivityMode
	 */
	public WelcomeActivity(boolean singleActivityMode) {
		super(singleActivityMode);
	}
   
	public boolean onCreateOptionsMenu(Menu menu){
		//MenuInflater inflater = getMenuInflater();
		//inflater.inflate(R.menu.menu, menu);
		return true;
	}
}

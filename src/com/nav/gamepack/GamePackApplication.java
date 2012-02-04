/**
 * 
 */
package com.nav.gamepack;

/**
 * @author naveed
 *
 */

import android.app.Application;

public class GamePackApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

       // CookieStore.initialize(this);
    }
}

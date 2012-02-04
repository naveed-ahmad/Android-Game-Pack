/**
 * 
 */
package utils;

/**
 * @author naveed
 *
 */

import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.content.Context;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpHead;

import java.io.IOException;

public class CookieStore {
    private static final String LOG_TAG = "Shelves";

    private static final CookieStore sCookieStore;
    static {
        sCookieStore = new CookieStore();
    }

    private CookieStore() {
    }

    public static void initialize(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager.getInstance().removeExpiredCookie();
    }

    public static CookieStore get() {
        return sCookieStore;
    }

    public String getCookie(String url) {
        final CookieManager cookieManager = CookieManager.getInstance();
        String cookie = cookieManager.getCookie(url);

        if (cookie == null || cookie.length() == 0) {
            final HttpHead head = new HttpHead(url);
            HttpEntity entity = null;
            try {
                final HttpResponse response = null;//HttpManager.execute(head);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    entity = response.getEntity();

                    final Header[] cookies = response.getHeaders("set-cookie");
                    for (Header cooky : cookies) {
                        cookieManager.setCookie(url, cooky.getValue());
                    }

                    CookieSyncManager.getInstance().sync();
                    cookie = cookieManager.getCookie(url);
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "Could not retrieve cookie", e);
            } finally {
                if (entity != null) {
                    try {
                        entity.consumeContent();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Could not retrieve cookie", e);                        
                    }
                }
            }
        }

        return cookie;
    }
}

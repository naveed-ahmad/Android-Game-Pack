/**
 * 
 */
package com.nav.gamepack.shared;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

/**
 * @author naveed
 * 
 */
public class GoogleImageSearcher {
	// "//https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=%22jigsaw%22&rsz=8";

	public static final String googleSearchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=";
	private static final String TAG = "GoogleImageSearchder";

	public static ArrayList<Bitmap> getImageURL(String searchString) {
		ArrayList<Bitmap> imageUrlList = new ArrayList<Bitmap>();
		JsonParser responseParser;
		HttpGet httpGet;
		HttpClient httpClient = new DefaultHttpClient();

		try {
			httpGet = new HttpGet(googleSearchUrl + URLEncoder.encode(searchString, "utf-8"));
			HttpResponse response = httpClient.execute(httpGet);

			String gooogleSearchJson = EntityUtils.toString(response.getEntity());
			Log.i(TAG, gooogleSearchJson);
			JsonParser jsonParser = new JsonParser();
			imageUrlList = jsonParser.parseGoogleSearchResponse(gooogleSearchJson);
		} catch (Exception e) {

		}
		return imageUrlList;
	}
}

package com.nav.gamepack.shared;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;

public class JsonParser {
	private final String TAG = "JSONParser";
	private ArrayList<String> list_urls;

	public JsonParser() {
		list_urls = new ArrayList<String>();
	}

	public ArrayList parseGoogleSearchResponse(String _json) {
		list_urls.clear();
		try {
			JSONObject json = new JSONObject(_json);

			JSONObject responseData = json.getJSONObject("responseData");
			JSONArray searchResults = responseData.getJSONArray("results");
			for (int i = 0; i < searchResults.length(); i++) {
				JSONObject searchImageObject = searchResults.getJSONObject(i);
				list_urls.add(searchImageObject.getString("url"));
			}
		} catch (Exception e) {
			Log.e(TAG, "Error in parsing json", e);
		}
		Log.i(TAG, "URL: " + list_urls.toString());
		return list_urls;
	}

}

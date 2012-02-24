/**
 * 
 */
package com.nav.gamepack.puzzle.jigsaw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.NetworkUtils;

import com.nav.gamepack.R;
import com.nav.gamepack.shared.InfiniteGallery;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Gallery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

//"//https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=%22jigsaw%22&rsz=8";
/**
 * @author naveed
 * 
 */
public class ImageChooserActivity extends TabActivity {
	private final String TAG = "ImageChooserActivity";
	private final static String GOOGLE_SEARCH_URL = "//https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=";
	private static final int CAMERA_REQUEST = 1888;
	private static final int GALLERY_IMAGE_SELECT_REQUEST = 1889;

	public static String IMAGE_CHOOSER_IDENTIFIER;
	private Button btnSearchImgFromMobile, btnSearchImgFromGoogle;// ,
																	// btnBrowseImgFromMobile;
	private ImageView imgViewSelectedImage;
	Bitmap selectedImage;
	EditText editTextSearchFromGoogle;
	Animation shakeAnimation;
	TabHost mTabHost;
	ArrayList<Bitmap> gallrayImageList;
	ImageAdapter adapterImageList;
	InfiniteGallery gallryStleSlideShow;
	ListView gallryStyleList;
	GridView gallryStyleGrid;
	 Uri mImageUri;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, NetworkUtils.getipAddress() + "is ip");
		Log.i(TAG, "initializing image chooser ..");
		mTabHost = getTabHost();
		LayoutInflater.from(this).inflate(R.layout.image_chooser, mTabHost.getTabContentView(), true);
		mTabHost.addTab(mTabHost.newTabSpec("tab_gallery").setIndicator("Galery").setContent(R.id.gtabContentViewGalleryStyles));
		mTabHost.addTab(mTabHost.newTabSpec("tab_camera").setIndicator("Camera").setContent(R.id.gtabContentViewCamera));
		mTabHost.addTab(mTabHost.newTabSpec("tab_search").setIndicator("Search").setContent(R.id.gtabContentSearch));
		
		adapterImageList=new ImageAdapter(this);
		
		for (int i = 0; i < getTabWidget().getTabCount(); i++) {
			getTabWidget().getChildAt(i).setId(i);
			getTabWidget().getChildAt(i).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v.getId() == 1)// Camera tab
					{
						showCamera();
						mTabHost.setCurrentTab(0);

					} else
						mTabHost.setCurrentTab(v.getId());
				}
			});
		}
		mTabHost.setCurrentTabByTag("tab_gallery");
		init(getApplicationContext());
		hookListeners(getApplicationContext());
		Log.i(TAG, "initializing done");

	}

	/**
	 * @param applicationContext
	 */
	private void hookListeners(Context applicationContext) {
		OnClickListener btnListeners = new OnClickListener() {
			public void onClick(View v) {
				try {
					if (v.getId() == btnSearchImgFromMobile.getId()) {

						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_IMAGE_SELECT_REQUEST);
					} else if (v.getId() == btnSearchImgFromGoogle.getId()) {
						String queryString = editTextSearchFromGoogle.getText().toString();
						Log.i(TAG, "searching image from google, query=" + queryString);
						if (queryString.length() == 0) {
							editTextSearchFromGoogle.startAnimation(shakeAnimation);
						}
					} else
						Log.i(TAG, "unknown btn ..");
				} catch (Exception e) {
					Log.e(TAG, "Exception send image back to caller ", e);
				}
			}
		};

		btnSearchImgFromMobile.setOnClickListener(btnListeners);
		btnSearchImgFromGoogle.setOnClickListener(btnListeners);

	}

	private void init(Context context) {

		btnSearchImgFromMobile = (Button) findViewById(R.id.btnSearchImgFromMobile);
		btnSearchImgFromGoogle = (Button) findViewById(R.id.btnSearchImgFromGoogle);
		// btnBrowseImgFromMobile = (Button)
		// findViewById(R.id.btnBrowseImgFromMobile);
		editTextSearchFromGoogle = (EditText) findViewById(R.id.editTextSearchImgFromGoogle);
		shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
		// imgViewSelectedImage = (ImageView)
		// findViewById(R.id.imgViewSelectedImage);

		// btnCamera.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// try {
		// Log.i(TAG, "Showing camera ");
		// Intent cameraIntent = new
		// Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		// startActivityForResult(cameraIntent, CAMERA_REQUEST);
		//
		// } catch (Exception e) {
		// Log.e(TAG, "Exception while taking image from camera ", e);
		// }
		// }
		// });
		// DialogInterface.OnClickListener dialogClickListener = new
		// DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int which) {
		// finishWithNoResult();
		// }
		// };
		// final AlertDialog.Builder dialog = new
		// AlertDialog.Builder(this).setTitle("Are you sure to go back?").setMessage("You can not play game without an image").setNegativeButton("Cancel",
		// null)
		// .setPositiveButton("Yes", dialogClickListener);
		//
		// btnBack.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		// try {
		// Log.i(TAG, "Reterning selected image");
		// if (selectedImage == null) {
		// dialog.show();
		// } else {
		// finishWithNoResult();
		// }
		// } catch (Exception e) {
		// Log.e(TAG, "Exception send image back to caller ", e);
		// }
		// }
		// });
		// btnSelectImage.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// try {
		// finishWithResult();
		// } catch (Exception e) {
		// Log.e(TAG, "Exception send image back to caller ", e);
		// }
		// }
		// });
		// btnDeleteImage.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// try {
		//
		// } catch (Exception e) {
		// Log.e(TAG, "Exception send image back to caller ", e);
		// }
		// }
		// });

	}

	/**
	 * 
	 */
	protected void finishWithResult() {
		Log.i(TAG, "Reterning selected image");
		Intent resultIntent = new Intent();
		resultIntent.putExtra("image", selectedImage);
		if (getParent() == null)
			setResult(Activity.RESULT_OK, resultIntent);
		else
			getParent().setResult(Activity.RESULT_OK, resultIntent);

		finish();
	}

	//
	// /**
	// *
	// */
	protected void finishWithNoResult() {
		Intent resultIntent = new Intent();

		if (getParent() == null)
			setResult(Activity.RESULT_CANCELED, resultIntent);
		else
			getParent().setResult(Activity.RESULT_CANCELED, resultIntent);
		finish();

	}

	
	
	
	

	private File createTemporaryFile(String part, String ext) throws Exception
	{
	    File tempDir= Environment.getExternalStorageDirectory();
	    tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
	    if(!tempDir.exists())
	    {
	        tempDir.mkdir();
	    }
	    return File.createTempFile(part, ext, tempDir);
	}

	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG,"result from cam or gallery");
		  Toast.makeText(this, "image result", Toast.LENGTH_SHORT).show();
			 
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			Log.i(TAG,"Result from camera");
			  Toast.makeText(this, "image result cma", Toast.LENGTH_SHORT).show();
				     
			this.getContentResolver().notifyChange(mImageUri, null);
		    ContentResolver cr = this.getContentResolver();
		    Bitmap bitmap;
		    try
		    {
		    	selectedImage = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
		    	finishWithResult();
		    }
		    catch (Exception e)
		    {
		        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
		        Log.d(TAG, "Failed to load", e);
		    }

			
		//	new AlertDialog.Builder(this).setMessage(data.toString()+"ex "+data.getExtras().toString()).show();
//			Object photo = data.getExtras().get("data");
//			
//			data.getParcelableExtra("image");
//			data.getParcelableExtra("data");
			//adapterImageList.addImage((Bitmap)photo);
			
			//selectedImage = photo;
			//finishWithResult();
		}else if (requestCode == GALLERY_IMAGE_SELECT_REQUEST && resultCode == RESULT_OK) {
			Log.i(TAG,"Result from gallery");
			Options o=new Options();
			
			  Toast.makeText(this, "image gallery", Toast.LENGTH_SHORT).show();
				 
			
			        Bitmap mBitmap = null;
			        try {
			        	selectedImage  = Media.getBitmap(this.getContentResolver(),data.getData());
						
			        } catch (FileNotFoundException e) {
			        	Toast.makeText(this,"file not found", Toast.LENGTH_LONG);
			        	e.printStackTrace();
					} catch (IOException e) {
						Toast.makeText(this,"error while geting img", Toast.LENGTH_LONG);
						e.printStackTrace();
					}
			
//			Object o= data.getExtras().get("data");
//			data.getParcelableExtra("image");
//			data.getParcelableExtra("data");
	
//		adapterImageList.addImage(photo);
			finishWithResult();
		}
	}

	//
	//
	// /**
	// * Async task for loading the images from the SD card.
	// */
	// class LoadImagesFromSDCard extends AsyncTask<Object, LoadedImage, Object>
	// {
	// /**
	// * Load images from SD Card in the background, and display each image on
	// the screen.
	// * @see android.os.AsyncTask#doInBackground(Params[])
	// */
	// @Override
	// protected Object doInBackground(Object... params) {
	// // setProgressBarIndeterminateVisibility(true);
	// Bitmap bitmap = null;
	// Bitmap newBitmap = null;
	// Uri uri = null;
	//
	// // Set up an array of the Thumbnail Image ID column we want
	// String[] projection = { MediaStore.Images.Thumbnails._ID };
	// // Create the cursor pointing to the SDCard
	// Cursor cursor =
	// managedQuery(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
	// projection, // Which columns to return
	// null, // Return all rows
	// null, null);
	// int columnIndex =
	// cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
	// int size = cursor.getCount();
	// // If size is 0, there are no images on the SD Card.
	// if (size == 0) {
	// // No Images available, post some message to the user
	// }
	//
	// // This will remember ACTUAL positions with existing bitmaps!
	// mRealBitmapsIndexes = new ArrayList<Integer>();
	//
	// int imageID = 0;
	// boolean run = trTabActivityue;
	// for (int i = 0; i < size; i++) {
	// try {
	// if(cursor != null && !cursor.isClosed() && run){ //if the user quits
	// before all the images are loaded, the cursor will be closed
	// cursor.moveToPosition(i);
	// imageID = cursor.getInt(columnIndex);
	// uri =
	// Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
	// "" + imageID);
	// //temp
	// bitmap =
	// BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
	//
	// if (bitmap != null) {
	// mRealBitmapsIndexes.add(i);
	// newBitmap = Bitmap.createScaledBitmap(bitmap, GRID_IMG_WIDTH,
	// GRID_IMG_HEIGHT, true);
	// bitmap.recycle();
	// if (newBitmap != null) {
	// publishProgress(new LoadedImage(newBitmap));
	// }
	// }
	// }
	// } catch (OutOfMemoryError e) {
	// run = false;
	// Toast.makeText(LoadImagesViaGridActivity.this,
	// R.string.lowMemoryErrorUnableDisplImages, Toast.LENGTH_LONG).show();
	// }
	// catch (Exception e) {
	// e.printStackTrace();
	// //Toast.makeText(LoadImagesViaGridActivity.this,
	// R.string.errorUnableDisplImages, Toast.LENGTH_LONG).show();
	// }
	// }
	// if(cursor != null && !cursor.isClosed())
	// cursor.close();
	// return null;
	// }
	//
	// /**
	// * Add a new LoadedImage in the images grid.
	// * @param value The image.
	// */
	// @Override
	// public void onProgressUpdate(Bitmap... value) {
	// //addImage(value);
	// }
	//
	// /**
	// * Set the visibility of the progress bar to false.
	// * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	// */
	// protected void onPostExecute(Object result) {
	// setProgressBarIndeterminateVisibility(false);
	// }
	// }
	protected void showCamera() {
		try {
			
			Log.i(TAG, "Showing camera ");
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		    File photo=null;
		    try
		    {
		        // place where to store camera taken picture
		        photo = this.createTemporaryFile("picture", ".png");
		        photo.delete();
		    }
		    catch(Exception e)
		    {
		        Log.v(TAG, "Can't create file to take picture!");
		        Toast.makeText(this, "Please check SD card! Image shot is impossible!", 10000);
		    }
		     mImageUri = Uri.fromFile(photo);
		    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
		    
			startActivityForResult(cameraIntent, CAMERA_REQUEST);

		} catch (ActivityNotFoundException anfe) {
			Toast.makeText(getApplicationContext(), "Camera not found", Toast.LENGTH_SHORT);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Camera Not found", Toast.LENGTH_SHORT);
			Log.e(TAG, "Exception while taking image from camera ", e);
		}

	}

	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater inflater = null;

		private int itemLayoutId;
		private List<Bitmap> dataSource;
       
		public ImageAdapter(Context c) {
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void addImage(Bitmap img){
			dataSource.add(img);
		}
		public int getCount() {
			if (dataSource != null) {
				return dataSource.size();
			} else {
				return 0;
			}
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;

			if (view == null) {
				view = inflater.inflate(R.layout.image_item, parent, false);
			}

			((ImageView) view).setImageBitmap(dataSource.get(position));
			return view;
		}
	}

}

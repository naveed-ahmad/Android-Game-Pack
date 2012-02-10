/**
 * 
 */
package com.nav.gamepack.puzzle.jigsaw;

import com.nav.gamepack.R;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Gallery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * @author naveed
 * 
 */
public class ImageChooserActivity extends Activity {
	private final String TAG = "ImageChooserView";
	private static final int CAMERA_REQUEST = 1888;
	public static String IMAGE_CHOOSER_IDENTIFIER;
	private Button btnCamera, btnGallery, btnSearchImage, btnDeleteImage, btnSelectImage, btnBack;
	private ImageView imgViewSelectedImage;
	Bitmap selectedImage;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init(getApplicationContext());
	}

	private void init(Context context) {
		Log.i(TAG, "initializing MovieView");
		setContentView(R.layout.image_chooser);
		btnCamera = (Button) findViewById(R.id.btnCamera);
		btnGallery = (Button) findViewById(R.id.btnGallery);
		btnSearchImage = (Button) findViewById(R.id.btnSearchImage);
		btnDeleteImage = (Button) findViewById(R.id.btnDeleteImage);
		btnSelectImage = (Button) findViewById(R.id.btnSelectImage);
		btnBack = (Button) findViewById(R.id.btnBack);
		imgViewSelectedImage = (ImageView) findViewById(R.id.imgViewSelectedImage);

		btnCamera.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					Log.i(TAG, "Showing camera ");
					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(cameraIntent, CAMERA_REQUEST);

				} catch (Exception e) {
					Log.e(TAG, "Exception while taking image from camera ", e);
				}
			}
		});
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finishWithNoResult();
			}
		};
		final AlertDialog.Builder dialog = new AlertDialog.Builder(this).setTitle("Are you sure to go back?").setMessage("You can not play game without an image").setNegativeButton("Cancel", null)
				.setPositiveButton("Yes", dialogClickListener);

		btnBack.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					Log.i(TAG, "Reterning selected image");
					if (selectedImage == null) {
						dialog.show();
					} else {
						finishWithNoResult();
					}
				} catch (Exception e) {
					Log.e(TAG, "Exception send image back to caller ", e);
				}
			}
		});
		btnSelectImage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					finishWithResult();
				} catch (Exception e) {
					Log.e(TAG, "Exception send image back to caller ", e);
				}
			}
		});
		btnDeleteImage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {

				} catch (Exception e) {
					Log.e(TAG, "Exception send image back to caller ", e);
				}
			}
		});

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

	/**
	 * 
	 */
	protected void finishWithNoResult() {
		Intent resultIntent = new Intent();

		if (getParent() == null)
			setResult(Activity.RESULT_CANCELED, resultIntent);
		else
			getParent().setResult(Activity.RESULT_CANCELED, resultIntent);
		finish();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			imgViewSelectedImage.setImageBitmap(photo);
			selectedImage = photo;
		}
	}
	
//	
//	
//	/**
//	 * Async task for loading the images from the SD card.
//	 */
//	class LoadImagesFromSDCard extends AsyncTask<Object, LoadedImage, Object> {
//	    /**
//	     * Load images from SD Card in the background, and display each image on the screen.
//	     * @see android.os.AsyncTask#doInBackground(Params[])
//	     */
//	    @Override
//	    protected Object doInBackground(Object... params) {
//	        // setProgressBarIndeterminateVisibility(true);
//	        Bitmap bitmap = null;
//	        Bitmap newBitmap = null;
//	        Uri uri = null;
//
//	        // Set up an array of the Thumbnail Image ID column we want
//	        String[] projection = { MediaStore.Images.Thumbnails._ID };
//	        // Create the cursor pointing to the SDCard
//	        Cursor cursor = managedQuery(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, // Which columns to return
//	                null, // Return all rows
//	                null, null);
//	        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
//	        int size = cursor.getCount();
//	        // If size is 0, there are no images on the SD Card.
//	        if (size == 0) {
//	            // No Images available, post some message to the user
//	        }
//
//	        // This will remember ACTUAL positions with existing bitmaps!
//	        mRealBitmapsIndexes = new ArrayList<Integer>();
//
//	        int imageID = 0;
//	        boolean run = true;
//	        for (int i = 0; i < size; i++) {                
//	            try {
//	                if(cursor != null && !cursor.isClosed() && run){ //if the user quits before all the images are loaded, the cursor will be closed 
//	                    cursor.moveToPosition(i);  
//	                    imageID = cursor.getInt(columnIndex);
//	                    uri = Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageID);
//	                    //temp
//	                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//
//	                    if (bitmap != null) {
//	                        mRealBitmapsIndexes.add(i);
//	                        newBitmap = Bitmap.createScaledBitmap(bitmap, GRID_IMG_WIDTH, GRID_IMG_HEIGHT, true);
//	                        bitmap.recycle();
//	                        if (newBitmap != null) {
//	                            publishProgress(new LoadedImage(newBitmap));
//	                        }
//	                    }
//	                }
//	            } catch (OutOfMemoryError e) {
//	                run = false;
//	                Toast.makeText(LoadImagesViaGridActivity.this, R.string.lowMemoryErrorUnableDisplImages, Toast.LENGTH_LONG).show();
//	            }
//	            catch (Exception e) {
//	                e.printStackTrace();
//	                //Toast.makeText(LoadImagesViaGridActivity.this, R.string.errorUnableDisplImages, Toast.LENGTH_LONG).show();
//	            }
//	        }
//	        if(cursor != null && !cursor.isClosed())
//	            cursor.close();
//	        return null;
//	    }
//
//	    /**
//	     * Add a new LoadedImage in the images grid.
//	     * @param value The image.
//	     */
//	    @Override
//	    public void onProgressUpdate(Bitmap... value) {
//	        //addImage(value);
//	    }
//
//	    /**
//	     * Set the visibility of the progress bar to false.
//	     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
//	     */
//	    protected void onPostExecute(Object result) {
//	        setProgressBarIndeterminateVisibility(false);
//	    }
//	}

}

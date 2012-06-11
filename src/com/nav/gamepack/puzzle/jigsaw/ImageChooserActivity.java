/**
 * 
 */
package com.nav.gamepack.puzzle.jigsaw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import utils.NetworkUtils;

import com.nav.gamepack.R;
import com.nav.gamepack.shared.GoogleImageSearcher;
import com.nav.gamepack.shared.InfiniteGallery;

import android.content.ActivityNotFoundException;
import android.content.Context;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;

import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * @author naveed
 * 
 */
public class ImageChooserActivity extends TabActivity {
	private final String TAG = "ImageChooserActivity";
	private final static String GOOGLE_SEARCH_URL = "//https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=";
	private static final int SELECT_IMAGE_FROM_CAMERA_REQUEST = 1889, SELECT_IMAGE_FROM_GALLERY_REQUEST = 1888;;
	ProgressDialog dialogLoadingImage;
	private Button btnSearchImgFromMobile, btnSearchImgFromGoogle;

	private ImageView imgViewSelectedImage;
	Bitmap selectedImage;
	EditText editTextSearchFromGoogle;
	Animation shakeAnimation;
	TabHost mTabHost;
	ArrayList<Bitmap> gallrayImageList;
	GridImageAdapter gridImageAdapter;
	SliderImageAdapter sliderImageAdapter;
	InfiniteGallery galleryStyleSlideShow;
	// ListView galleryStyleList;
	GridView galleryStyleGrid;
	Uri mImageUri;
	File fileTempSelectedImg;
	ImageButton imgBtnGallryStyleSlideshow, imgBtnGallryStyleGrid;// ,
																	// imgBtnGallryStyleList;
	private int imgItemSize;
	ViewFlipper gallryStyleFlipper;
	OnClickListener galleryStyleClickListener;
	OnItemClickListener galleryImageClickListener;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, NetworkUtils.getipAddress() + "is ip");
		Log.i(TAG, "initializing image chooser ..");
		mTabHost = getTabHost();
		LayoutInflater.from(this).inflate(R.layout.image_chooser, mTabHost.getTabContentView(), true);
		mTabHost.addTab(mTabHost.newTabSpec("tab_gallery").setIndicator("Galery").setContent(R.id.gallryStyles));
		mTabHost.addTab(mTabHost.newTabSpec("tab_camera").setIndicator("Camera").setContent(R.id.gtabContentViewCamera));
		mTabHost.addTab(mTabHost.newTabSpec("tab_search").setIndicator("Search").setContent(R.id.gtabContentSearch));

		imgBtnGallryStyleSlideshow = (ImageButton) findViewById(R.id.imgBtnGallryStyleSlideshow);
		imgBtnGallryStyleGrid = (ImageButton) findViewById(R.id.imgBtnGallryStyleGrid);
		// imgBtnGallryStyleList = (ImageButton)
		// findViewById(R.id.imgBtnGallryStyleList);

		galleryStyleSlideShow = (InfiniteGallery) findViewById(R.id.gallryStyleSlideShow);
		// galleryStyleList = (ListView) findViewById(R.id.gallryStyleListView);
		galleryStyleGrid = (GridView) findViewById(R.id.gallryStyleGrid);

		gridImageAdapter = new GridImageAdapter(this);
		sliderImageAdapter = new SliderImageAdapter(this);

		galleryStyleSlideShow.setAdapter(sliderImageAdapter);
		// galleryStyleList.setAdapter(adapterImageList);
		galleryStyleGrid.setAdapter(gridImageAdapter);

		gallryStyleFlipper = (ViewFlipper) findViewById(R.id.gallryStyleViewFlipper);

		Animation s_in = AnimationUtils.loadAnimation(this, R.anim.slidein);
		Animation s_out = AnimationUtils.loadAnimation(this, R.anim.slideout);

		gallryStyleFlipper.setAnimation(s_in);
		gallryStyleFlipper.setOutAnimation(s_out);

		imgItemSize = galleryStyleGrid.getWidth() / 3;
		fileTempSelectedImg = new File(Environment.getExternalStorageDirectory(), "testJIgsawImg.jpg");
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

		galleryStyleClickListener = new OnClickListener() {

			@Override
			public void onClick(View clickedBtn) {

				if (clickedBtn.getId() == imgBtnGallryStyleGrid.getId()) {
					Log.i(TAG, "click on Grid style");
					gallryStyleFlipper.setDisplayedChild(0);

				} else if (clickedBtn.getId() == imgBtnGallryStyleSlideshow.getId()) {
					Log.i(TAG, "click on Gallery style");
					gallryStyleFlipper.setDisplayedChild(1);
				}
				// else if (clickedBtn.getId() == imgBtnGallryStyleList.getId())
				// {
				// Log.i(TAG, "click on List style");
				// gallryStyleFlipper.setDisplayedChild(1);
				// }

			}
		};
		galleryImageClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View clickedImgView, int arg2, long arg3) {
				Log.i(TAG, "User has select an image :) finally");
				selectedImage = gridImageAdapter.getImage((Integer) clickedImgView.getTag());
				finishWithResult();

			}

		};
		imgBtnGallryStyleSlideshow.setOnClickListener(galleryStyleClickListener);
		// imgBtnGallryStyleList.setOnClickListener(galleryStyleClickListener);
		imgBtnGallryStyleGrid.setOnClickListener(galleryStyleClickListener);

		galleryStyleSlideShow.setOnItemClickListener(galleryImageClickListener);
		// galleryStyleList.setOnItemClickListener(galleryImageClickListener);
		galleryStyleGrid.setOnItemClickListener(galleryImageClickListener);

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
						startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE_FROM_GALLERY_REQUEST);
					} else if (v.getId() == btnSearchImgFromGoogle.getId()) {
						String queryString = editTextSearchFromGoogle.getText().toString();
						Log.i(TAG, "searching image from google, query=" + queryString);
						if (queryString.length() == 0) {
							editTextSearchFromGoogle.startAnimation(shakeAnimation);
						} else {
							searchImagesFromGoogle(queryString);
						}
					} else
						Log.i(TAG, "unknown btn ..");
				} catch (Exception e) {
					Log.e(TAG, "Exception send image back to caller ", e);
				}
			}

			private void searchImagesFromGoogle(String queryString) {
				dialogLoadingImage = ProgressDialog.show(getParent(), "Searching Images", "Please wait...", true);

				ArrayList urls = GoogleImageSearcher.getImageURL(queryString);
				if (urls.size() == 0)
					Toast.makeText(getApplicationContext(), "No Image found", Toast.LENGTH_LONG);
				else {
					dialogLoadingImage.setMessage("Found " + urls.size() + " images...");
					for (int i = 0; i < urls.size(); i++) {
						try {
							dialogLoadingImage.setMessage("Loading " + urls.size() + " of " + i + " image");
							URL url = new URL(urls.get(i).toString());
							Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
							addImageToGalleryAdapters(bmp);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}

				dialogLoadingImage.hide();
			}

		};

		btnSearchImgFromMobile.setOnClickListener(btnListeners);
		btnSearchImgFromGoogle.setOnClickListener(btnListeners);

	}

	void addImageToGalleryAdapters(Bitmap bmp) {

		gridImageAdapter.addImage(selectedImage);
		sliderImageAdapter.addImage(selectedImage);
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

	private File createTemporaryFile(String part, String ext) throws Exception {
		File tempDir = Environment.getExternalStorageDirectory();
		tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
		if (!tempDir.exists()) {
			tempDir.mkdir();
		}
		return File.createTempFile(part, ext, tempDir);
	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);

		if (cursor == null)
			return null;

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	private Bitmap getImageFromUri(Uri imgUri) {
		Bitmap img = null;

		return img;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "result from cam or gallery");
		Toast.makeText(this, "image result requets =" + requestCode, Toast.LENGTH_SHORT).show();

		if (requestCode == SELECT_IMAGE_FROM_CAMERA_REQUEST && resultCode == RESULT_OK) {

			if (data == null) {
				Toast.makeText(this, "image path=" + fileTempSelectedImg.getPath(), Toast.LENGTH_SHORT).show();
				try {
					selectedImage = BitmapFactory.decodeFile(fileTempSelectedImg.getPath());
				} catch (Exception e) {
					Toast.makeText(this, "error now real path is " + getRealPathFromURI(data.getData()), Toast.LENGTH_SHORT).show();
					String path = null;
					if (path == null)
						path = data.getData().getPath(); // from File Manager

					if (path != null) {
						selectedImage = BitmapFactory.decodeFile(path);
						Toast.makeText(this, "img is still null ", Toast.LENGTH_LONG);
					}
				}
			} else {
				selectedImage = (Bitmap) data.getExtras().get("data");

			}
			addImageToGalleryAdapters(selectedImage);
			// gridImageAdapter.addImage(selectedImage);
			// sliderImageAdapter.addImage(selectedImage);

		} else if (requestCode == SELECT_IMAGE_FROM_GALLERY_REQUEST && resultCode == RESULT_OK) {
			Log.i(TAG, "Result from gallery");

			Toast.makeText(this, "image gallery", Toast.LENGTH_SHORT).show();

			Bitmap mBitmap = null;
			try {
				selectedImage = Media.getBitmap(this.getContentResolver(), data.getData());
				addImageToGalleryAdapters(selectedImage);

			} catch (FileNotFoundException e) {
				Toast.makeText(this, "file not found", Toast.LENGTH_LONG);
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(this, "error while geting img", Toast.LENGTH_LONG);
				e.printStackTrace();
			}
		}
	}

	protected void showCamera() {
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileTempSelectedImg));
			startActivityForResult(intent, SELECT_IMAGE_FROM_CAMERA_REQUEST);
		} catch (ActivityNotFoundException anfe) {
			Toast.makeText(getApplicationContext(), "Camera not found", Toast.LENGTH_SHORT);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Camera Not found", Toast.LENGTH_SHORT);
			Log.e(TAG, "Exception while taking image from camera ", e);
		}

	}

	public class GalleryImageAdapter extends BaseAdapter {

		protected LayoutInflater inflater = null;

		protected int itemLayoutId;
		protected ArrayList<Bitmap> dataSource;

		public GalleryImageAdapter(Context c) {
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			dataSource = new ArrayList<Bitmap>();
		}

		public void addImage(Bitmap img) {
			dataSource.add(img);
			notifyDataSetChanged();
		}

		public Bitmap getImage(int position) {
			return dataSource.get(position);
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
			ImageView imageView;

			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = new ImageView(getApplicationContext());
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			} else {
				imageView = (ImageView) convertView;
			}
			((ImageView) imageView).setImageBitmap(dataSource.get(position));
			imageView.setTag(position);
			// if (view == null) {
			// view = inflater.inflate(R.layout.gallery_image, parent, false);
			// }
			// view.setTag(position);
			// ((ImageView) view).setImageBitmap(dataSource.get(position));
			return imageView;
			//
		}
	}

	public class SliderImageAdapter extends GalleryImageAdapter {
		public SliderImageAdapter(Context c) {
			super(c);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;

			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = (ImageView) inflater.inflate(R.layout.slideshow_image_item, parent, false);

			} else {
				imageView = (ImageView) convertView;
			}
			((ImageView) imageView).setImageBitmap(dataSource.get(position));
			imageView.setTag(position);

			return imageView;
		}
	}

	public class GridImageAdapter extends GalleryImageAdapter {
		public GridImageAdapter(Context c) {
			super(c);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;

			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = new ImageView(getApplicationContext());
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			} else {
				imageView = (ImageView) convertView;
			}
			((ImageView) imageView).setImageBitmap(dataSource.get(position));
			imageView.setTag(position);

			return imageView;
		}
	}

}

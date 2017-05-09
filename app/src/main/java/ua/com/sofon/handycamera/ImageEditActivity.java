/*
 * Copyright 2017 Dmitriy Ponomarenko, sofon.com.ua
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.com.sofon.handycamera;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import com.bumptech.glide.Glide;

import ua.com.sofon.handycamera.data.GaleryDataSource;

/**
 * Created on 10.09.2016.
 * @author Dimowner
 */
public class ImageEditActivity extends AppCompatActivity {

	private File mImage;
	private ImageView mImageView;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_image);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		mImageView = (ImageView) findViewById(R.id.edit_img_image);

		//This code executed after camera snapshot
		String path = getIntent().getStringExtra(MediaStore.EXTRA_OUTPUT);
		if (path != null && !path.isEmpty()) {
			if (path.contains("file:")) {
				path = path.substring(5, path.length());
			}
			Log.v("ImageEdit", "path = " + path);
			mImage = new File(path);
			Glide.with(getApplicationContext()).load(mImage)
					.thumbnail(0.5f)
					.crossFade()
					.into(mImageView);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_ok, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.action_accept:
				new SaveImageTask().execute(mImage);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

//	private void setPic() {
//		Log.v("ImageEdit", "setPic");
//		// Get the dimensions of the View
//		int targetW = mImageView.getWidth();
//		int targetH = mImageView.getHeight();
//
//		// Get the dimensions of the bitmap
//		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//		bmOptions.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(mImage.getPath(), bmOptions);
//		int photoW = bmOptions.outWidth;
//		int photoH = bmOptions.outHeight;
//
//		// Determine how much to scale down the image
//		int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//
//		// Decode the image file into a Bitmap sized to fill the View
//		bmOptions.inJustDecodeBounds = false;
//		bmOptions.inSampleSize = scaleFactor;
//		bmOptions.inPurgeable = true;
//
//		Bitmap bitmap = BitmapFactory.decodeFile(mImage.getPath(), bmOptions);
//		mImageView.setImageBitmap(bitmap);
//	}

	public class SaveImageTask extends AsyncTask<File, Void, Void> {

		@Override
		protected Void doInBackground(File... items) {
			GaleryDataSource dataSource = GaleryDataSource.getInstance();
			dataSource.insertImage(items[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			super.onPostExecute(v);
			setResult(RESULT_OK);
			finish();
		}
	}

//	public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
//
//		private int width;
//		private int height;
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			Log.v("Load", "onPreExe");
//			Display display = getWindowManager().getDefaultDisplay();
//			Point size = new Point();
//			display.getSize(size);
//			width = size.x;
//			height = size.y;
//		}
//
//		@Override
//		protected Bitmap doInBackground(String... file) {
//			Log.v("Load", "onInBackground");
//			return FileUtil.decodeSampledBitmapFromFile(file[0], width, height);
////			return new ImageItem(ImageItem.NO_ID, "", new Date(), file[0], b);
//		}
//
//		@Override
//		protected void onPostExecute(Bitmap item) {
//			super.onPostExecute(item);
//			Log.v("Load", "onPost");
//			mImageView.setImageBitmap(item);
//		}
//	}
}

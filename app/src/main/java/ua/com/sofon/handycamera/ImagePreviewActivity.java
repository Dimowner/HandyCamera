package ua.com.sofon.handycamera;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import ua.com.sofon.handycamera.data.GaleryDataSource;
import ua.com.sofon.handycamera.gallery.GalleryFragment;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Activity for preview images with zoom.
 * Created on 11.09.2016.
 * @author Dimowner
 */
public class ImagePreviewActivity extends AppCompatActivity {

	/** Tag for logging information. */
	private final String LOG_TAG = "ImagePreviewActivity";

	private PhotoViewAttacher mAttacher;

	private File mImage;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview_image);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		ImageView mImageView = (ImageView) findViewById(R.id.image_preview_photo);
		mAttacher = new PhotoViewAttacher(mImageView);

		if (savedInstanceState == null) {
			mImage = (File) getIntent().getSerializableExtra(GalleryFragment.EXTRAS_KEY_IMAGE);
			Glide.with(getApplicationContext()).load(mImage)
					.thumbnail(0.5f)
					.crossFade()
					.into(mImageView);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("m_image", mImage);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mImage = (File) savedInstanceState.getSerializable("m_image");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mAttacher.cleanup();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_edit_delete, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.action_delete:
				AlertDialog.Builder builder = new AlertDialog.Builder(ImagePreviewActivity.this);
				builder.setTitle(R.string.image_preview_warning);
				builder.setMessage(R.string.image_preview_accept_deletion);
				builder.setPositiveButton(android.R.string.ok, (dialog, id) -> new DeleteImageTask().execute(mImage));
				builder.setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.cancel());
				builder.create().show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public class DeleteImageTask extends AsyncTask<File, Void, Void> {
		@Override
		protected Void doInBackground(File... ids) {
			GaleryDataSource dataSource = GaleryDataSource.getInstance();
			dataSource.deleteImage(ids[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			super.onPostExecute(v);
			finish();
		}
	}
}

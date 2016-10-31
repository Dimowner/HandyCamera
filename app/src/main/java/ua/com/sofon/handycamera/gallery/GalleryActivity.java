package ua.com.sofon.handycamera.gallery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import ua.com.sofon.handycamera.ImageEditActivity;
import ua.com.sofon.handycamera.R;
import ua.com.sofon.handycamera.util.FileUtil;

public class GalleryActivity extends AppCompatActivity {

	private static final String TAG = "GalleryActivity";

	private static final int REQUEST_CODE_MAKE_PHOTO = 1;

	private static final int REQUEST_WRITE_PERMISSION = 2;

	private static final String FILE_PROVIDER_AUTHORITY = "ua.com.sofon.handycamera.fileprovider";

	/** Temp image path */
	String mCurrentPhotoPath;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		GalleryFragment tasksFragment =
				(GalleryFragment) getSupportFragmentManager().findFragmentById(R.id.gallery_fragment);
		if (tasksFragment == null) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.add(R.id.gallery_fragment, GalleryFragment.newInstance());
			transaction.commit();
		}

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(view -> makePhoto());
	}

	private void makePhoto() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			runCameraActivityLollipopAndBeyond();
		} else{
			runCameraActivityPreLollipop();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("m_cur_img_path", mCurrentPhotoPath);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mCurrentPhotoPath = savedInstanceState.getString("m_cur_img_path");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case REQUEST_CODE_MAKE_PHOTO:
					//Open edit ImageEditActivity with camera results.
					Intent intent = new Intent(getApplicationContext(), ImageEditActivity.class);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath);
					startActivity(intent);
					break;
			}
		}
	}

	private void runCameraActivityPreLollipop() {
		try {
			// do something for phones running an SDK before lollipop
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Uri imageUri = Uri.fromFile(FileUtil.createTempImageFile(getApplicationContext()));
			mCurrentPhotoPath = imageUri.getPath();
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, REQUEST_CODE_MAKE_PHOTO);
		} catch (IOException e) {
			Log.e(TAG, "", e);
		}
	}

	private void runCameraActivityLollipopAndBeyond() {
		if (ContextCompat.checkSelfPermission(GalleryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			requestWritePermission();
		} else {
			dispatchTakePictureIntent();
		}
	}

	private void dispatchTakePictureIntent() {
		Log.v(TAG, "dispatchTakePictureIntent");
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = FileUtil.createTempImageFile(getApplicationContext());
			} catch (IOException ex) {
				Log.e(TAG, "", ex);
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				mCurrentPhotoPath = photoFile.getPath();
				Uri photoURI = FileProvider.getUriForFile(this,
						FILE_PROVIDER_AUTHORITY,
						photoFile);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
				startActivityForResult(takePictureIntent, REQUEST_CODE_MAKE_PHOTO);
			}
		}
	}

	/**
	 * Ask permission write files into file system.
	 */
	private void requestWritePermission() {
		ActivityCompat.requestPermissions(
				GalleryActivity.this,
				new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
				REQUEST_WRITE_PERMISSION
		);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
														@NonNull int[] grantResults) {
		if (requestCode == REQUEST_WRITE_PERMISSION) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				dispatchTakePictureIntent();
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
}

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

package ua.com.sofon.handycamera.data;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ua.com.sofon.handycamera.util.FileUtil;

/**
 * Created on 30.10.2016.
 * @author Dimowner
 */
public class GaleryDataSource {

	private static final String TAG = "GaleryDataSource";

	private static GaleryDataSource mInstance;

	public static final int DEFAULT_SIZE = 500;//px
	private int mHeight = DEFAULT_SIZE;
	private int mWidth  = DEFAULT_SIZE;


	// Prevent direct instantiation.
	private GaleryDataSource() {
	}

	public static GaleryDataSource getInstance() {
		if (mInstance == null) {
			mInstance = new GaleryDataSource();
		}
		return mInstance;
	}

	public void setHeight(int height) {
		this.mHeight = height;
	}

	public void setWidth(int width) {
		this.mWidth = width;
	}

	public List<File> getImages() {
		List<File> imgs = new ArrayList<>();

		File imgDir = FileUtil.getDcimStorageDir();
		FileUtil.findAllFilesWithExtension(imgDir, "jpeg", imgs);
		FileUtil.findAllFilesWithExtension(imgDir, "jpg", imgs);

		return imgs;
	}

	private Bitmap loadBitmap(String path) {
		try {
			return FileUtil.decodeSampledBitmapFromFile(path, mWidth, mHeight);
		} catch (OutOfMemoryError e) {
			Log.e(TAG, "", e);
		}
		return null;
	}

	public void insertImage(@NonNull File item) {
		Log.v(TAG, "insertImage item = " + item.toString());
//		String name = item.getName()

		if (item.renameTo(new File(FileUtil.getDcimStorageDir(), item.getName()))) {
			Log.v(TAG, "Successfully inserted");
		} else {
			Log.v(TAG, "Failed to insert");
		}
	}

	public void deleteImage(File img) {
		if (img != null && img.exists()) {
			Log.v(TAG, "deleteImage" + img.getAbsolutePath());
			if (img.delete()) {
				Log.v(TAG, "Deleted");
			} else {Log.v(TAG, "Failed to delete");}
		}
	}
}

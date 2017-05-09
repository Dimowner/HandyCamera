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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom {@link android.content.Loader} for a list of {@link ImageItem}, using the
 * {@link ImagesDataSource} as its source. This Loader is a {@link AsyncTaskLoader} so it queries
 * the data asynchronously.
 */
public class ImagesLoader extends AsyncTaskLoader<List<File>> {

	private GaleryDataSource mDataSource;


	public ImagesLoader(Context context, @NonNull GaleryDataSource dataSource) {
		super(context);
		mDataSource = dataSource;
	}

	@Override
	public List<File> loadInBackground() {
		return mDataSource.getImages();
	}

	@Override
	public void deliverResult(List<File> data) {
		if (isReset()) {
			return;
		}

		if (isStarted()) {
			super.deliverResult(data);
		}

	}

	@Override
	protected void onStartLoading() {
		// Begin monitoring the underlying data source.
		forceLoad();
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	protected void onReset() {
		onStopLoading();
	}
}

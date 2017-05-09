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

package ua.com.sofon.handycamera.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.List;

import ua.com.sofon.handycamera.ImagePreviewActivity;
import ua.com.sofon.handycamera.R;
import ua.com.sofon.handycamera.data.GaleryDataSource;
import ua.com.sofon.handycamera.data.ImagesLoader;

/**
 * Created on 10.09.2016.
 * @author Dimowner
 */
public class GalleryFragment extends Fragment
			implements LoaderManager.LoaderCallbacks<List<File>> {

	/** Tag for logging information. */
	private final String LOG_TAG = "GalleryFragment";

	public static final String EXTRAS_KEY_IMAGE = "image_item";

	public static final int DEFAULT_ITEM_WIDTH = 120;//px

	private GridAdapter mAdapter;


	public static GalleryFragment newInstance() {
		return new GalleryFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
									 ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_galery, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		int dpWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, DEFAULT_ITEM_WIDTH, getResources().getDisplayMetrics());
		mRecyclerView.setLayoutManager(new GridAutofitLayoutManager(getContext(), dpWidth));

		mAdapter = new GridAdapter();
		mAdapter.setOnItemClickListener((adapterView, view1, pos, id) -> {
			Intent intent = new Intent(getContext(), ImagePreviewActivity.class);
			intent.putExtra(EXTRAS_KEY_IMAGE, mAdapter.getItem(pos));
			startActivity(intent);
		});

		mRecyclerView.setAdapter(mAdapter);

		// Create loader for reading data
		getActivity().getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<List<File>> onCreateLoader(int id, Bundle args) {
		int dpSize = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, DEFAULT_ITEM_WIDTH, getResources().getDisplayMetrics());

		GaleryDataSource ds = GaleryDataSource.getInstance();
		ds.setHeight(dpSize);
		ds.setWidth(dpSize);

		return new ImagesLoader(getContext(), ds);
	}

	@Override
	public void onLoadFinished(Loader<List<File>> loader, List<File> data) {
		if (data == null) {
			Log.e(LOG_TAG, "Failed to load images");
		} else {
			mAdapter.setData(data);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<File>> loader) {
	}
}

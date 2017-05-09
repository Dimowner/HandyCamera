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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ua.com.sofon.handycamera.R;

/**
 * Created on 11.09.2016.
 * @author Dimowner
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

	private List<File> data;

	private AdapterView.OnItemClickListener itemClickListener;


	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View v) {
			super(v);
			mView = v;
		}

		public View mView;
	}

	public GridAdapter() {
		this.data = new ArrayList<>();
	}

	public GridAdapter(ArrayList<File> items) {
		if (items != null) {
			this.data = items;
		} else {
			this.data = new ArrayList<>();
		}
	}

	@Override
	public GridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(parent.getContext())
							.inflate(R.layout.grid_item_image, parent, false));

	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {

		final int pos = holder.getAdapterPosition();
		File img = data.get(pos);

		holder.mView.setOnClickListener(v -> {
			if (itemClickListener != null) {
				itemClickListener.onItemClick(null, v, pos, v.getId());
			}
		});
		ImageView ivImg = (ImageView) holder.mView.findViewById(R.id.grid_item_img);
		Glide.with(holder.mView.getContext()).load(img)
				.thumbnail(0.5f)
				.crossFade()
				.into(ivImg);

	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	public void addItem(File item) {
		data.add(item);
		notifyDataSetChanged();
	}

	public void addItems(List<File> items) {
		data.addAll(items);
		notifyDataSetChanged();
	}

	public void setData(List<File> items) {
		data = items;
		notifyDataSetChanged();
	}

	public File getItem(int pos) {
		return data.get(pos);
	}

	public void clear() {
		data.clear();
		notifyDataSetChanged();
	}

	public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
		itemClickListener = listener;
	}
}

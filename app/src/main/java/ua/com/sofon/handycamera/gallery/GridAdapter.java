package ua.com.sofon.handycamera.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.com.sofon.handycamera.R;
import ua.com.sofon.handycamera.data.ImageItem;

/**
 * Created on 11.09.2016.
 * @author Dimowner
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

	private List<ImageItem> data;

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

	public GridAdapter(ArrayList<ImageItem> items) {
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
		ImageItem img = data.get(pos);

		holder.mView.setOnClickListener(v -> {
			if (itemClickListener != null) {
				itemClickListener.onItemClick(null, v, pos, v.getId());
			}
		});
		TextView tvTitle = (TextView) holder.mView.findViewById(R.id.grid_item_title);
		TextView tvDate = (TextView) holder.mView.findViewById(R.id.grid_item_date);
		ImageView ivImg = (ImageView) holder.mView.findViewById(R.id.grid_item_img);

		tvTitle.setText(img.getTitle());
		tvDate.setText(img.getFormattedDate());
		ivImg.setImageBitmap(img.getImg());
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	public void addItem(ImageItem item) {
		data.add(item);
		notifyDataSetChanged();
	}

	public void addItems(List<ImageItem> items) {
		data.addAll(items);
		notifyDataSetChanged();
	}

	public void setData(List<ImageItem> items) {
		data = items;
		notifyDataSetChanged();
	}

	public ImageItem getItem(int pos) {
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

package com.nowar.adapter;

import java.util.ArrayList;
import java.util.List;
import com.nowar.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BluetoothDeviceAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	//public static List<MyItem> list = new ArrayList<MyItem>();
	public  List<BluetoothDeviceItem> mList = new ArrayList<BluetoothDeviceItem>();
	
	public BluetoothDeviceAdapter(Context context ,List<BluetoothDeviceItem> list) {
		mInflater = LayoutInflater.from(context);
		mList = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MyViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.mylistview_item,
					null);
			 holder = new MyViewHolder(convertView);
			 convertView.setTag(holder);
		}else{
			holder = (MyViewHolder) convertView.getTag();
		}
		BluetoothDeviceItem item = mList.get(position);
		holder.icon.setImageResource(item.icon);
		holder.name.setText(item.name);
		holder.number.setText(item.number);
		holder.time.setText(item.time);
		return convertView;
	}
	
	
	private static class MyViewHolder {
		public ImageView icon;
		public TextView name;
		public TextView number;
		public TextView time;

		MyViewHolder(View view) {
			icon = (ImageView) view.findViewById(R.id.icon);
			name = (TextView) view.findViewById(R.id.title);
			number = (TextView) view.findViewById(R.id.msg);
			time = (TextView) view.findViewById(R.id.time);

		}
	}

}

package com.nowar.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nowar.main.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class XialaActivity extends Activity implements
		SwipeRefreshLayout.OnRefreshListener {

	private static final int REFRESH_COMPLETE = 0X110;
	private SwipeRefreshLayout mSwipeLayout;
	private ListView mListView;
	private ArrayAdapter<String> mAdapter;
	private List<String> mDatas = new ArrayList<String>(Arrays.asList("Java",
			"Javascript", "C++", "Ruby", "Json", "HTML"));

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				mDatas.addAll(Arrays.asList("Lucene", "Canvas", "Bitmap"));
				mAdapter.notifyDataSetChanged();
				mSwipeLayout.setRefreshing(false);
				break;

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xiala_view);

		mListView = (ListView) findViewById(R.id.id_listview);
		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);

		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setColorScheme(android.R.color.holo_green_dark,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mDatas);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onRefresh() {
		mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);

	}
}

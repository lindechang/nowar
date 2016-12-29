//package com.nowar.test;
//
//import java.util.Arrays;
//
//import com.nowar.main.R;
//import com.nowar.view.SlidingTabLayout;
//
//import android.content.res.Resources;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
//import android.support.v7.app.ActionBarActivity;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.widget.Toolbar;
////import android.support.v7.app.ActionBarActivity;
//import android.os.Bundle;
//import android.os.Handler;
////import android.support.v7.app.ActionBarDrawerToggle;
////import android.support.v7.widget.Toolbar;
//import android.util.SparseArray;
//import android.view.Gravity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
////import SlidingTabs.SlidingTabLayout;
//
//public class TestTabMainActivity extends ActionBarActivity {
//
//	ActionBarDrawerToggle mDrawerToggle;
//	DrawerLayout mDrawerLayout;
//	SlidingTabLayout mSlidingTabLayout;
//	ViewPager mViewPager;
//	MainTabs tabs;
//	
//	private SwipeRefreshLayout mSwipeLayout;
//	
//	private static final int REFRESH_COMPLETE = 0X110;
//	private Handler mHandler = new Handler()
//	{
//		public void handleMessage(android.os.Message msg)
//		{
//			switch (msg.what)
//			{
//			case REFRESH_COMPLETE:
//				//mDatas.addAll(Arrays.asList("Lucene", "Canvas", "Bitmap"));
//				//mAdapter.notifyDataSetChanged();
//				mSwipeLayout.setRefreshing(false);
//				break;
//
//			}
//		};
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.test_my_main);
//
//		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
//		setSupportActionBar(mToolbar);
//
//		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//		mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(
//				R.color.gainsboro));
//		mViewPager = (ViewPager) findViewById(R.id.view_pager);
//		mViewPager.setOffscreenPageLimit(7); // tabcachesize (=tabcount for
//												// better performance)
//		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
//				mToolbar, R.string.app_name, R.string.app_name);
//		mDrawerLayout.setDrawerListener(mDrawerToggle);
//
//		mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
//
//		// use own style rules for tab layout
//		mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator,
//				android.R.id.text1);
//
//		Resources res = getResources();
//		mSlidingTabLayout.setSelectedIndicatorColors(res
//				.getColor(R.color.gray));
//		mSlidingTabLayout.setDistributeEvenly(true);
//		tabs = new MainTabs();
//		mViewPager.setAdapter(tabs);
//		mSlidingTabLayout.setViewPager(mViewPager);
//		
//		// mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
//		//
//		// mSwipeLayout.setOnRefreshListener(this);
//		// mSwipeLayout.setColorScheme(android.R.color.holo_green_dark,
//		// android.R.color.holo_green_light,
//		// android.R.color.holo_orange_light, android.R.color.holo_red_light);
//
//		// Tab events
//		if (mSlidingTabLayout != null) {
//			mSlidingTabLayout
//					.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//						@Override
//						public void onPageScrolled(int position,
//								float positionOffset, int positionOffsetPixels) {
//
//						}
//
//						@Override
//						public void onPageSelected(int position) {
//
//						}
//
//						@Override
//						public void onPageScrollStateChanged(int state) {
//
//						}
//					});
//		}
//
//		// Click events for Navigation Drawer
//		LinearLayout navButton = (LinearLayout) findViewById(R.id.txtNavButton1);
//		navButton.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				// close drawer if you want
//				/*
//				 * if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT))
//				 * { mDrawerLayout.closeDrawers(); }
//				 */
//				tabs.notifyDataSetChanged();
//				Toast.makeText(v.getContext(), "navitem clicked",
//						Toast.LENGTH_SHORT).show();
//
//				// update loaded Views if you want
//				// mViewPager.getAdapter().notifyDataSetChanged();
//			}
//		});
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu_main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		if (mDrawerToggle.onOptionsItemSelected(item)) {
//			return true;
//		}
//		int id = item.getItemId();
//
//		// noinspection SimplifiableIfStatement
//		if (id == R.id.action_settings) {
//			return true;
//		}
//
//		return super.onOptionsItemSelected(item);
//	}
//
//	@Override
//	protected void onPostCreate(Bundle savedInstanceState) {
//		super.onPostCreate(savedInstanceState);
//		mDrawerToggle.syncState();
//	}
//
//	@Override
//	public void onBackPressed() {
//		if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
//			mDrawerLayout.closeDrawers();
//			return;
//		}
//		super.onBackPressed();
//	}
//
//	/**
//	 * The {@link android.support.v4.view.PagerAdapter} used to display pages in
//	 * this sample. The individual pages are simple and just display two lines
//	 * of text. The important section of this class is the
//	 * {@link #getPageTitle(int)} method which controls what is displayed in the
//	 * {@link SlidingTabLayout}.
//	 */
//	class MainTabs extends PagerAdapter implements OnRefreshListener {
//
//		SparseArray<View> views = new SparseArray<View>();
//
//		/**
//		 * @return the number of pages to display
//		 */
//		@Override
//		public int getCount() {
//			return 7;
//		}
//
//		/**
//		 * @return true if the value returned from
//		 *         {@link #instantiateItem(ViewGroup, int)} is the same object
//		 *         as the {@link View} added to the {@link ViewPager}.
//		 */
//		@Override
//		public boolean isViewFromObject(View view, Object o) {
//			return o == view;
//		}
//
//		/**
//		 * Return the title of the item at {@code position}. This is important
//		 * as what this method returns is what is displayed in the
//		 * {@link SlidingTabLayout}.
//		 * <p/>
//		 * Here we construct one using the position value, but for real
//		 * application the title should refer to the item's contents.
//		 */
//		@Override
//		public CharSequence getPageTitle(int position) {
//			return "Page " + (position + 1);
//		}
//
//		/**
//		 * Instantiate the {@link View} which should be displayed at
//		 * {@code position}. Here we inflate a layout from the apps resources
//		 * and then change the text view to signify the position.
//		 */
//		@Override
//		public Object instantiateItem(ViewGroup container, int position) {
//			// Inflate a new layout from our resources
//			System.out.println("position:" + position);
//			View view;
//			if (position == 0) {
//				 view = getLayoutInflater().inflate(R.layout.tab1_view,
//							container, false);
//				mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.id_swipe_ly);
//
//				mSwipeLayout.setOnRefreshListener(this);
//				mSwipeLayout.setColorScheme(android.R.color.holo_green_dark,
//						android.R.color.holo_green_light,
//						android.R.color.holo_orange_light,
//						android.R.color.holo_red_light);
//			} else {
//				 view = getLayoutInflater().inflate(R.layout.pager_item,
//						container, false);
//				TextView txt = (TextView) view.findViewById(R.id.item_subtitle);
//				txt.setText("Content: " + (position + 1));
//				// Add the newly created View to the ViewPager
//				
//			}
//			container.addView(view);
//
//			views.put(position, view);
//			// Return the View
//			return view;
//		}
//
//		/**
//		 * Destroy the item from the {@link ViewPager}. In our case this is
//		 * simply removing the {@link View}.
//		 */
//		@Override
//		public void destroyItem(ViewGroup container, int position, Object object) {
//			container.removeView((View) object);
//			views.remove(position);
//		}
//
//		@Override
//		public void notifyDataSetChanged() {
//			System.out.println("notifyDataSetChanged:" + "½øÀ´");
//			int position = 0;
//			for (int i = 0; i < views.size(); i++) {
//				position = views.keyAt(i);
//				View view = views.get(position);
//				// Change the content of this view
//				TextView txt = (TextView) view.findViewById(R.id.item_subtitle);
//				txt.setText("This Page " + (position + 1)
//						+ " has been refreshed");
//			}
//			super.notifyDataSetChanged();
//		}
//
//		@Override
//		public void onRefresh() {
//			mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
//			
//		}
//
//	}
//
//	
//}

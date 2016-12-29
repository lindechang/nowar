package com.nowar.test;

import com.nowar.main.R;
import com.nowar.menu.SlidingMenu;
import com.nowar.tools.BitmapUtil;
import com.nowar.tools.test_Bitmap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class TestActivity extends Activity {

	private SlidingMenu mMenu;
	// private Bitmap mBitmap;
	// private static Bitmap bBitmap;
	 private Drawable drawable;
	private ImageView mImageView1;
	private ImageView mImageView2;
	private ImageView mImageView3;
	private ImageView mImageView4;
	private ImageView mImageView5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test2_main);

		mMenu = (SlidingMenu) findViewById(R.id.id_menu);
		 Bitmap mBitmap = test_Bitmap.drawableToBitmap(getResources()
				 .getDrawable(R.drawable.sw_but));
		 mBitmap = test_Bitmap.getRoundedCornerBitmap(mBitmap, (float)(5.0));

		mImageView1 = (ImageView) findViewById(R.id.one);
		mImageView1.setImageBitmap(mBitmap);

		// Bitmap mBitmap = test_Bitmap.drawableToBitmap(getResources()
		// .getDrawable(R.drawable.sw_but));
		// mBitmap = test_Bitmap.createRGBImage(mBitmap, R.color.greenyellow);
		// String path = BitmapUtil.saveBitmap(mBitmap, TestActivity.this);
		// System.out.println("path:" + path);
	}

	private void initView() {
		Bitmap mBitmap;
		mImageView1 = (ImageView) findViewById(R.id.one);
		mImageView2 = (ImageView) findViewById(R.id.two);
		mImageView3 = (ImageView) findViewById(R.id.three);
		mImageView4 = (ImageView) findViewById(R.id.four);
		mImageView5 = (ImageView) findViewById(R.id.five);

		mBitmap = test_Bitmap.drawableToBitmap(getResources().getDrawable(
				R.drawable.ic_4));
		mBitmap = test_Bitmap.createRGBImage(mBitmap, R.color.skyblue);
		BitmapUtil.saveBitmap(mBitmap, TestActivity.this);
		mImageView1.setImageBitmap(mBitmap);
		mBitmap = test_Bitmap.drawableToBitmap(getResources().getDrawable(
				R.drawable.friend));
		mBitmap = test_Bitmap.createRGBImage(mBitmap, R.color.skyblue);
		mImageView2.setImageBitmap(mBitmap);
		mBitmap = test_Bitmap.drawableToBitmap(getResources().getDrawable(
				R.drawable.ic_3));
		mBitmap = test_Bitmap.createRGBImage(mBitmap, R.color.skyblue);
		mImageView3.setImageBitmap(mBitmap);
		mBitmap = test_Bitmap.drawableToBitmap(getResources().getDrawable(
				R.drawable.updata));
		mBitmap = test_Bitmap.createRGBImage(mBitmap, R.color.skyblue);
		mImageView4.setImageBitmap(mBitmap);
		mBitmap = test_Bitmap.drawableToBitmap(getResources().getDrawable(
				R.drawable.exit));
		mBitmap = test_Bitmap.createRGBImage(mBitmap, R.color.skyblue);
		mImageView5.setImageBitmap(mBitmap);

	}

	public void toggleMenu(View view) {
		mMenu.toggle();
	}
}

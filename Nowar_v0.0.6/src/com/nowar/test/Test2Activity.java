package com.nowar.test;

import com.nowar.main.R;
import com.nowar.tools.test_Bitmap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class Test2Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test2);
		ImageView mImageView = (ImageView) findViewById(R.id.test2_imageview);
		Bitmap mBitmap = test_Bitmap.drawableToBitmap(getResources()
				.getDrawable(R.drawable.ic_user));
		mBitmap = test_Bitmap.getRoundedCornerBitmap(mBitmap, (float) (200.0));
		mImageView.setImageBitmap(mBitmap);
	}
}

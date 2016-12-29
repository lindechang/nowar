package weilan.app.popupwindow;

//import com.zbar.lib.CaptureActivity;

import com.zbar.lib.CaptureActivity;

import weilan.app.activity.SearchActivity;
import weilan.app.main.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class MorePopWindow extends PopupWindow implements OnClickListener {
	private View conentView;
	private LinearLayout addss;
	private LinearLayout addsm;
	private LinearLayout addfriendss;
	private LinearLayout addfriendsm;
	private Activity ctx;


	public MorePopWindow(final Activity context) {
		ctx = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.more_popup_dialog, null);
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		// 设置SelectPicPopupWindow的View
		this.setContentView(conentView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(w / 2 + 50);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 刷新状态
		this.update();
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
		this.setBackgroundDrawable(dw);
		// mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimationPreview);

		addss = (LinearLayout) this.getContentView().findViewById(R.id.xiala_ss_id);
		addsm = (LinearLayout) this.getContentView().findViewById(R.id.xiala_sm_id);
		addfriendss = (LinearLayout) this.getContentView().findViewById(R.id.xiala_friend_ss_id);
		addfriendsm = (LinearLayout) this.getContentView().findViewById(R.id.xiala_friend_sm_id);
		addss.setOnClickListener(this);
		addsm.setOnClickListener(this);
		addfriendss.setOnClickListener(this);
		addfriendsm.setOnClickListener(this);


	}

	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
		} else {
			this.dismiss();
		}
	}
	public void showDismiss() {
		this.dismiss();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(addss)) {
			// Intent intent = new Intent();
			// intent.setClass(this, AddDeviceActivity.class);
			// startActivity(intent);
			 Intent intent = new Intent();
			 intent.putExtra("type", "device");
			 intent.setClass(ctx, SearchActivity.class);
			 ctx.startActivity(intent);
			
		}
		if (v.equals(addsm)) {
			//System.out.println("我是扫描");
			 Intent intent = new Intent();
			 intent.putExtra("type", "device");
			 intent.setClass(ctx, CaptureActivity.class);
			 ctx.startActivity(intent);
		}
		if (v.equals(addfriendss)) {
			// Intent intent = new Intent();
			// intent.setClass(this, AddDeviceActivity.class);
			// startActivity(intent);
			 Intent intent = new Intent();
			 intent.putExtra("type", "friend");
			 intent.setClass(ctx, SearchActivity.class);
			 ctx.startActivity(intent);
			
		}
		if (v.equals(addfriendsm)) {
			//System.out.println("我是扫描");
			 Intent intent = new Intent();
			 intent.putExtra("type", "friend");
			 intent.setClass(ctx, CaptureActivity.class);
			 ctx.startActivity(intent);
		}
	}
}

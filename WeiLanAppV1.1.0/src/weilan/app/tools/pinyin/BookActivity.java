package weilan.app.tools.pinyin;

import java.util.ArrayList;
import java.util.List;

import weilan.app.main.R;
import weilan.app.tools.pinyin.AssortView.OnTouchAssortListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.TextView;


public class BookActivity extends Activity {
	/** Called when the activity is first created. */

	private PinyinAdapter adapter;
	private ExpandableListView eListView;
	private AssortView assortView;
	private List<String> names;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_view);
		eListView = (ExpandableListView) findViewById(R.id.book_elist_id);
		assortView = (AssortView) findViewById(R.id.book_assort_id);
		names=new ArrayList<String>();
		names.add("lxz");
		names.add("A酱");
		names.add("芙兰");
		names.add("鱼鱼");
		names.add("妹妹");
		names.add("你好");
		names.add("林小姐");
		names.add("联盟");
		names.add("L");
		names.add("xdsfsdggsdsf");
		names.add("星星");
		names.add("靴刀誓死");
		names.add("Java");
		names.add("倒塌");
		names.add("黑人");
		names.add("a妹");
		names.add("aYa");
		
		adapter = new PinyinAdapter(this,names);
		eListView.setAdapter(adapter);
		
		
		
       //展开所有
		for (int i = 0, length = adapter.getGroupCount(); i < length; i++) {
			eListView.expandGroup(i);
		}
		
		//字母按键回调
		assortView.setOnTouchAssortListener(new OnTouchAssortListener() {
			
			View layoutView=LayoutInflater.from(BookActivity.this)
					.inflate(R.layout.book_pinyin_dialog, null);
			TextView text =(TextView) layoutView.findViewById(R.id.book_content_id);
			PopupWindow popupWindow ;
			
			public void onTouchAssortListener(String str) {
			   int index=adapter.getAssort().getHashList().indexOfKey(str);
			   if(index!=-1)
			   {
					eListView.setSelectedGroup(index);;
			   }
				if(popupWindow!=null){
				text.setText(str);
				}
				else
				{   
				      popupWindow = new PopupWindow(layoutView,
							80, 80,
							false);
					// 显示在Activity的根视图中心
					popupWindow.showAtLocation(getWindow().getDecorView(),
							Gravity.CENTER, 0, 0);
				}
				text.setText(str);
			}

			public void onTouchAssortUP() {
				if(popupWindow!=null)
				popupWindow.dismiss();
				popupWindow=null;
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
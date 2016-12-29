package weilan.app.tools.pinyin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



import weilan.app.main.R;
import net.sourceforge.pinyin4j.PinyinHelper;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PinyinAdapter extends BaseExpandableListAdapter {

	// ×Ö·û´®
	private List<String> strList;

	private AssortPinyinList assort = new AssortPinyinList();

	private Context context;

	private LayoutInflater inflater;
	// ÖÐÎÄÅÅÐò
	private LanguageComparator_CN cnSort = new LanguageComparator_CN();

	public PinyinAdapter(Context context, List<String> strList) {
		super();
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.strList = strList;
		if (strList == null) {
			strList = new ArrayList<String>();
		}

		long time = System.currentTimeMillis();
		// ÅÅÐò
		sort();
		// Toast.makeText(context,
		// String.valueOf(System.currentTimeMillis() - time), 1).show();

	}

	private void sort() {
		// ·ÖÀà
		for (String str : strList) {
			assort.getHashList().add(str);
		}
		assort.getHashList().sortKeyComparator(cnSort);
		for(int i=0,length=assort.getHashList().size();i<length;i++)
		{
			Collections.sort((assort.getHashList().getValueListIndex(i)),cnSort);
		}
		
	}

	public Object getChild(int group, int child) {
		// TODO Auto-generated method stub
		return assort.getHashList().getValueIndex(group, child);
	}

	public long getChildId(int group, int child) {
		// TODO Auto-generated method stub
		return child;
	}

	public View getChildView(int group, int child, boolean arg2,
			View contentView, ViewGroup arg4) {
		// TODO Auto-generated method stub
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.book_item, null);
		}
		TextView textView = (TextView) contentView.findViewById(R.id.book_nameitem_id);
		textView.setText(assort.getHashList().getValueIndex(group, child));
		return contentView;
	}

	public int getChildrenCount(int group) {
		// TODO Auto-generated method stub
		return assort.getHashList().getValueListIndex(group).size();
	}

	public Object getGroup(int group) {
		// TODO Auto-generated method stub
		return assort.getHashList().getValueListIndex(group);
	}

	public int getGroupCount() {
		// TODO Auto-generated method stub
		return assort.getHashList().size();
	}

	public long getGroupId(int group) {
		// TODO Auto-generated method stub
		return group;
	}

	public View getGroupView(int group, boolean arg1, View contentView,
			ViewGroup arg3) {
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.book_list_group_item, null);
			contentView.setClickable(true);
		}
		TextView textView = (TextView) contentView.findViewById(R.id.book_grouptext_id);
		textView.setText(assort.getFirstChar(assort.getHashList()
				.getValueIndex(group, 0)));
		// ½ûÖ¹ÉìÕ¹

		return contentView;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	public AssortPinyinList getAssort() {
		return assort;
	}

}

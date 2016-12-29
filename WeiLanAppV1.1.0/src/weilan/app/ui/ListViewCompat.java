package weilan.app.ui;

//import yundian.tracker.ui.TabAFm.MessageItem;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class ListViewCompat extends ListView {

	private static final String TAG = "ListViewCompat";

	private SlideView mFocusedItemView;

	public ListViewCompat(Context context) {
		super(context);
	}

	public ListViewCompat(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewCompat(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void shrinkListItem(int position) {
		View item = getChildAt(position);

		if (item != null) {
			try {
				((SlideView) item).shrink();
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			int x = (int) event.getX();
			int y = (int) event.getY();
			// ������֪����ǰ�������һ��
			int position = pointToPosition(x, y);
			Log.e(TAG, "postion=" + position);
			if (position != INVALID_POSITION) {
				// �õ���ǰ����е����ݴӶ�ȡ����ǰ�е�item��
				// �������˻��ɣ�ΪʲôҪ��ô�ɣ�Ϊʲô����getChildAt(position)��
				// ��ΪListView����л��棬����㲻��ô�ɣ���Щ�е�view���ǵò����ġ�
				MessageItem data = (MessageItem) getItemAtPosition(position);
				mFocusedItemView = data.slideView;
				Log.e(TAG, "FocusedItemView=" + mFocusedItemView);
			}
		}
		default:
			break;
		}
		// ��ǰ�����view���ͻ����¼�������ʵ������SlideView������
		if (mFocusedItemView != null) {
			mFocusedItemView.onRequireTouchEvent(event);
		}

		return super.onTouchEvent(event);
	}

	 
}

package weilan.app.activity;
//package yundian.tracker.activity;
//
//
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.widget.RadioGroup;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import yundian.tracker.db.TrackerDB;
//import yundian.tracker.main.R;
//import yundian.tracker.ui.*;
//
//
//public class TabMainActivity extends FragmentActivity {
//    /**
//     * Called when the activity is first created.
//     */
//    private RadioGroup rgs;
//    public List<Fragment> fragments = new ArrayList<Fragment>();
//
//    public String hello = "hello ";
//    private TrackerDB mTrackerDB;
//	
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_view);
//    
//        mTrackerDB = new TrackerDB(this);
//        mTrackerDB.Open();
//        fragments.add(new TabBFm_DW());
//        fragments.add(new TabAFm_SW());
////        fragments.add(new TabCFm());
//        fragments.add(new TabCFm_WO());
//       // fragments.add(new TabEFm());
//
//        rgs = (RadioGroup) findViewById(R.id.tabs_rg);
//
//        FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.tab_content, rgs);
//        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener(){
//            @Override
//            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
//                System.out.println("Extra---- " + index + " checked!!! ");
//            }
//        });
//
//    }
//
//}
//

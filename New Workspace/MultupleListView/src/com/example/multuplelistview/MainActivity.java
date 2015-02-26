package com.example.multuplelistview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
ListView lv1,lv2;
MyAdapter adapter;
String[] list1Items ={"List 1 Hi1","List 1 Hi2","List 1 Hi3","List 1 Hi4","List 1 Hi5","List 1 Hi6","List 1 Hi7","List 1 Hi8","List 1 Hi9","List 1 Hi10","List 1 Hi6","List 1 Hi7","List 1 Hi8","List 1 Hi9","List 1 Hi10"};
String[] list2Items ={"List 2 Hi1","List 2 Hi2","List 2 Hi3","List 2 Hi4","List 2 Hi5","List 2 Hi6","List 2 Hi7","List 2 Hi8","List 2 Hi9","List 2 Hi10","List 1 Hi6","List 1 Hi7","List 1 Hi8","List 1 Hi9","List 1 Hi10"};

@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multilist);
		lv1 = (ListView)findViewById(R.id.lstviewUnBlockVenues);
		lv2 = (ListView)findViewById(R.id.lstviewBlockedVenues);
		adapter = new MyAdapter(this,list1Items);
		lv1.setAdapter(adapter);
		lv2.setAdapter(adapter);
		setListViewHeightBasedOnChildren(lv1);
		setListViewHeightBasedOnChildren(lv2);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	  public static void setListViewHeightBasedOnChildren(ListView listView) {
	        System.out.println("phani");
	        ListAdapter listAdapter = listView.getAdapter();
	        if (listAdapter == null) {
	            return;
	        }

	        int totalHeight = 0;
	        for (int i = 0; i < listAdapter.getCount(); i++) {
	            View listItem = listAdapter.getView(i, null, listView);
	            listItem.measure(0, 0);
	            totalHeight += listItem.getMeasuredHeight();
	        }

	        ViewGroup.LayoutParams params = listView.getLayoutParams();
	        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	        
	        listView.setLayoutParams(params);
	        listView.requestLayout();
	    }
}

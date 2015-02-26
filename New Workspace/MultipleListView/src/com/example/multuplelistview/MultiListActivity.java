package com.example.multuplelistview;

import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MultiListActivity extends Activity{
	ListView lv1,lv2;
	private static String[] items={"Anime","Sports","Politics","Love","Jokes","Sarcastic","Confessions","Current affairs","Android","Recorder","Technology","trending","Auto Mobile","Fashion","LifeStyle","Gadgets"};
	int[] icons={ R.drawable.pic};
	    int[] type={0,1,0,1,1,0};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    //setContentView(R.layout.first_layout);
	   
        //lv1.setAdapter(new DishAdapter(getActivity(),items,icons));
        FadingActionBarHelper helper = new FadingActionBarHelper()
        .actionBarBackground(new ColorDrawable(Color.BLUE))
         .headerLayout(R.layout.header)
        .contentLayout(R.layout.first_layout);
    setContentView(helper.createView(this));
    helper.initActionBar(this);
        ActionBar actionBar = getActionBar();
        lv1 = (ListView) findViewById(R.id.firstListView);
        lv2 = (ListView)findViewById(R.id.secondListView);
        lv1.setAdapter(new HomefeedAdapter(this,items,icons,type));
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
        lv2.setAdapter(new DishAdapter(this,items,icons));
        setListViewHeightBasedOnChildren(lv1);
        setListViewHeightBasedOnChildren(lv2);
        lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Intent intent = new Intent(MultiListActivity.this,DishActivity.class);
				intent.putExtra("Position", position);
				intent.putExtra("Items", items);
				startActivity(intent);
				
			}
		});

	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            System.out.println("phani item height "+listItem.getMeasuredHeight());
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        System.out.println("phani height "+params.height);
        //params.height +=params.height+200;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}

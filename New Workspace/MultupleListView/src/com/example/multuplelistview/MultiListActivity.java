package com.example.multuplelistview;


import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

public class MultiListActivity extends Activity{
	ListView lv1,lv2;
	private static String[] items={"Anime","Sports","Politics","Love","Jokes","Sarcastic","Confessions","Current affairs","Android","Recorder","Technology","trending","Auto Mobile","Fashion","LifeStyle","Gadgets"};
	int[] icons={ R.drawable.pic};
	    int[] type={0,1,0,1,1,0};
	    
	    NotifyingScrollView scrollView;
	    private Drawable mActionBarBackgroundDrawable;
	    ActionBar actionBar;
	    ViewGroup lv1Header;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.first_layout);
	   
        //lv1.setAdapter(new DishAdapter(getActivity(),items,icons));
       actionBar = getActionBar();
       actionBar.setDisplayShowHomeEnabled(false);
       actionBar.setDisplayShowTitleEnabled(false);
       LayoutInflater mInflater = LayoutInflater.from(this);
       View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
       mActionBarBackgroundDrawable = getResources().getDrawable(R.color.actionbarColor);
        mActionBarBackgroundDrawable.setAlpha(1);
        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);

       actionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);
       if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
         mActionBarBackgroundDrawable.setCallback(mDrawableCallback);
     }
        lv1 = (ListView) findViewById(R.id.firstListView);
        lv2 = (ListView)findViewById(R.id.secondListView);
        LayoutInflater inflater = getLayoutInflater();
        lv1Header = (ViewGroup)inflater.inflate(R.layout.header, lv1,false);
        lv1.addHeaderView(lv1Header);
       // lv1Header  = (ViewGroup).getLayoutInflater().inflate(R.layout.header, lv1, false);
        scrollView = (NotifyingScrollView)findViewById(R.id.home_layout);
        scrollView.setOnScrollChangedListener(mOnScrollChangedListener);
        lv1.setAdapter(new HomefeedAdapter(this,items,icons,type));
       // actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
        lv2.setAdapter(new DishAdapter(this,items,icons));
        
        
        setListViewHeightBasedOnChildren(lv1);
        setListViewHeightBasedOnChildren(lv2);
        
        

	}
  private Drawable.Callback mDrawableCallback = new Drawable.Callback() {
    @Override
    public void invalidateDrawable(Drawable who) {
        getActionBar().setBackgroundDrawable(who);
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
    }
};

	private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
    public void onScrollChanged(ScrollView who, int l, int t, int oldl,int oldt) {

          final int headerHeight = lv1Header.findViewById(R.id.header_layout).getHeight() - getActionBar().getHeight();

           final float ratio = (float) Math.min(Math.max(t, 0),
               headerHeight) / headerHeight;
       final int newAlpha = (int) (ratio * 255);
       mActionBarBackgroundDrawable.setAlpha(newAlpha);
        /*   final int headerHeight = lv1Header.findViewById(R.id.header_layout)
                    .getHeight() - getActivity().getActionBar().getHeight();
            final float ratio = (float) Math.min(Math.max(t, 0),
                    headerHeight) / headerHeight;
            final int newAlpha = (int) (ratio * 255);
            mActionBarBackgroundDrawable.setAlpha(newAlpha);*/

    }
};
	
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

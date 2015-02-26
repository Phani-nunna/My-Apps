package com.manishkpr.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
	private Context _context;
	
	public ViewPagerAdapter(Context context, FragmentManager fm) {
		super(fm);	
		_context=context;
		
		}
	@Override
	public Fragment getItem(int position) {
		Fragment f = new Fragment();
		Bundle bundle = new Bundle();
		switch(position){
		case 0:
			System.out.println("phani adapter 0");
		
			f=LayoutOne.newInstance("First");	
			/*LayoutOne frag = new LayoutOne();
			//Bundle bundle = new Bundle();
			bundle.putString("key", "First");
			frag.setArguments(bundle);*/
			//return frag;
			//break;
		case 1:
			System.out.println("phani adapter 1");
			f=LayoutTwo.newInstance("Second");	
			/*LayoutTwo twofrag = new LayoutTwo();
		
			bundle.putString("key", "Second");
			twofrag.setArguments(bundle);*/
			//return twofrag;
			
			
			//f=LayoutOne.newInstance("hiii");
			//break;
		}
		return f;
	}
	@Override
	public int getCount() {
		return 2;
	}
	@Override
	public int getItemPosition(Object object) {
		System.out.println("phani getItemPosition "+object);
	    if (object instanceof LayoutOne) {
	        ((LayoutOne) object).update("");
	    }else if(object instanceof LayoutTwo){
	    	((LayoutTwo) object).update("");
	    }
	    
	    //don't return POSITION_NONE, avoid fragment recreation. 
	    return super.getItemPosition(object);
	}
	
  
}

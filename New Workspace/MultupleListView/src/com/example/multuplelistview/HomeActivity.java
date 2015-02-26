package com.example.multuplelistview;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;




/**
 * Created by rajan on 23-08-2014.
 */
public class HomeActivity extends FragmentActivity{
    ImageButton home,profile,plan,search;
    TextView tab;
    ViewPager pager;
    RelativeLayout rel ;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
            setContentView(R.layout.home_layout);
            rel = (RelativeLayout)findViewById(R.id.container);
            ActionBar mActionBar = getActionBar();
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(false);

            LayoutInflater mInflater = LayoutInflater.from(this);
            View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
            home= (ImageButton) mCustomView.findViewById(R.id.home);
            profile= (ImageButton) mCustomView.findViewById(R.id.profile);
            plan= (ImageButton) mCustomView.findViewById(R.id.plans);
            search= (ImageButton) mCustomView.findViewById(R.id.search);
            tab= (TextView) mCustomView.findViewById(R.id.tabname);
            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);


          // SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // enable status bar tint
          //  tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint
           // tintManager.setNavigationBarTintEnabled(true);
          // tintManager.setStatusBarTintDrawable(getResources().getDrawable(R.drawable.colortint));
            MyPagerAdapter pageAdapter = new MyPagerAdapter(getSupportFragmentManager());
            pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(pageAdapter);
            pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i2) {

                }

                @Override
                public void onPageSelected(int i) {
                    clearSelection();
                    tab.setText(MyPagerAdapter.titles[i]);
                    switch(i)
                    {
                        case 0:
                            home.setBackgroundResource(R.drawable.actionbar_selectedtab);
                            break;
                        case 1:
                            search.setBackgroundResource(R.drawable.actionbar_selectedtab);
                            break;
                        case 2:
                            profile.setBackgroundResource(R.drawable.actionbar_selectedtab);
                            break;
                        case 3:
                            plan.setBackgroundResource(R.drawable.actionbar_selectedtab);
                            break;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });

           home.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   pager.setCurrentItem(0,true);
               }
           });

           search.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   pager.setCurrentItem(1,true);
               }
           });

            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pager.setCurrentItem(2,true);
                }
            });

            plan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pager.setCurrentItem(3,true);
                }
            });
        }

    @Override
    public void onBackPressed()
    {
        if(pager.getCurrentItem()==0)
        {
            Intent homeIntent= new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
        }
        else
        pager.setCurrentItem(0,true);
    }

    private void clearSelection() {
        home.setBackgroundColor(Color.parseColor("#00000000"));
        search.setBackgroundColor(Color.parseColor("#00000000"));
        plan.setBackgroundColor(Color.parseColor("#00000000"));
        profile.setBackgroundColor(Color.parseColor("#00000000"));
    }

}

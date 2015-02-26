package com.example.multuplelistview;

import android.app.ActionBar;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FirstFragment extends Fragment {
ListView lv1,lv2;
ImageButton floatb;
public static  int ch=0,cht=1;
    private static FirstFragment instance;

    int myLastVisiblePos;
TextView tv;
RelativeLayout rl;
 ScrollView sv;
    ViewGroup lv1Header;
private static String[] items={"Anime","Sports","Politics","Love","Jokes","Sarcastic","Confessions","Current affairs","Android","Recorder","Technology","trending","Auto Mobile","Fashion","LifeStyle","Gadgets"};
int[] icons={ R.drawable.pic};
    int[] type={0,1,0,1,1,0};
    TextView search,msearch;
    private Drawable mActionBarBackgroundDrawable;
    ActionBar actionBar;
    LinearLayout actionBarLayout;
    NotifyingScrollView scrollView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_layout, container, false);
        lv1 = (ListView) view.findViewById(R.id.firstListView);
        lv2 = (ListView)view.findViewById(R.id.secondListView);
        scrollView  = (NotifyingScrollView)view.findViewById(R.id.home_layout);
        scrollView.setOnScrollChangedListener(mOnScrollChangedListener);


       // floatb = (ImageButton) view.findViewById(R.id.floatbutton);
     //   rl = (RelativeLayout) view.findViewById(R.id.main);
        lv1Header  = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.header, lv1, false);
        ViewGroup lv1Footer = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.footer_layout, lv1, false);
        ViewGroup lv2Footer = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.footer_layout, lv2, false);
        lv1.addHeaderView(lv1Header, null, false);
        //lv1.addFooterView(lv1Footer);
         lv2.addFooterView(lv2Footer);
         actionBar = getActivity().getActionBar();
         mActionBarBackgroundDrawable = getResources().getDrawable(R.color.actionbarColor);
         mActionBarBackgroundDrawable.setAlpha(0);
         /*mActionBarBackgroundDrawable = getResources().getDrawable(R.color.actionbarColor);
         mActionBarBackgroundDrawable.setAlpha(0);*/
         actionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);
         if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
           mActionBarBackgroundDrawable.setCallback(mDrawableCallback);
       }
         
         
         
       // tv = (TextView) header.findViewById(R.id.breakfast);
        msearch= (TextView) lv1Header.findViewById(R.id.search_main);
        //search= (TextView) header.findViewById(R.id.homesearch);
        lv1.setAdapter(new HomefeedAdapter(getActivity(),items,icons,type));
       //lv1.setAdapter(new DishAdapter(getActivity(),items,icons));
       lv2.setAdapter(new DishAdapter(getActivity(),items,icons));
       setListViewHeightBasedOnChildren(lv1);
        setListViewHeightBasedOnChildren(lv2);
      /*  SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintDrawable(getResources().getDrawable(R.drawable.colortint));*/

        //actionBar = getActivity().getActionBar();
      // View vw=  actionBar.getCustomView();
        //actionBarLayout = (LinearLayout)vw.findViewById(R.id.actionbar_header_layout);
       // actionBarLayout.setAlpha(0.5f);
       // actionBarLayout.setBackgroundColor(getResources().getColor(R.color.barcolor));
       // actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.barcolor));

        /*mActionBarBackgroundDrawable = getResources().getDrawable(
                R.color.barcolor);

        mActionBarBackgroundDrawable.setAlpha(0);

        actionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);*/

      /*  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mActionBarBackgroundDrawable.setCallback(mDrawableCallback);
        }*/


        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                  System.out.println("celkon pos "+i);
               Intent dish_intent=new Intent(getActivity(),DishActivity.class);
                dish_intent.putExtra("Position", i-1);
                dish_intent.putExtra("Items", items);
               
                getActivity().startActivity(dish_intent);
               getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
               // getActivity().overridePendingTransition(R.anim.move_from_right,R.anim.moveleft);
            }
        });

      /* lv1.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int currentFirstVisPos = view.getFirstVisiblePosition();


                if (currentFirstVisPos >= myLastVisiblePos) {
                    if (ch == 1) {
                        ch++;
                        cht = 1;
                        TranslateAnimation ta = new TranslateAnimation(floatb.getScaleX(), floatb.getScaleX(), floatb.getScaleY(), floatb.getScaleY() + 200);
                        ta.setDuration(400);
                        floatb.setAnimation(ta);
                        floatb.setVisibility(View.GONE);

                    } else if (ch == 0) {
                        floatb.setVisibility(View.VISIBLE);
                        ch++;
                    }
                }
                if (currentFirstVisPos <= myLastVisiblePos) {
                    if (cht == 1) {
                        floatb.setVisibility(View.VISIBLE);
                        TranslateAnimation ta = new TranslateAnimation(floatb.getScaleX(), floatb.getScaleX(), floatb.getScaleY() + 200, floatb.getScaleY());
                        ta.setDuration(300);
                        floatb.setAnimation(ta);
                        cht++;
                        ch = 1;
                    }
                }
                myLastVisiblePos = currentFirstVisPos;
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }
        });
*/
        /*lv2.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int currentFirstVisPos = view.getFirstVisiblePosition();
                if (currentFirstVisPos > myLastVisiblePos) {
                    if (ch == 1) {
                        ch++;
                        cht = 1;
                        TranslateAnimation ta = new TranslateAnimation(floatb.getScaleX(), floatb.getScaleX(), floatb.getScaleY(), floatb.getScaleY() + 200);
                        ta.setDuration(400);
                        floatb.setAnimation(ta);
                        floatb.setVisibility(View.GONE);

                    } else if (ch == 0) {
                        floatb.setVisibility(View.VISIBLE);
                        ch++;
                    }
                }
                if (currentFirstVisPos < myLastVisiblePos) {
                    if (cht == 1) {
                        floatb.setVisibility(View.VISIBLE);
                        TranslateAnimation ta = new TranslateAnimation(floatb.getScaleX(), floatb.getScaleX(), floatb.getScaleY() + 200, floatb.getScaleY());
                        ta.setDuration(300);
                        floatb.setAnimation(ta);
                        cht++;
                        ch = 1;
                    }
                }
                myLastVisiblePos = currentFirstVisPos;
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }
        });*/



 /*       search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent search_intent=new Intent(getActivity(),SearchActivity.class);
              search_intent.putExtra("from",0);
              getActivity().startActivity(search_intent);
              getActivity().overridePendingTransition(R.anim.move_from_right,R.anim.moveleft);
            }
        });

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent dish_intent=new Intent(getActivity(),Dish_Activity.class);
                getActivity().startActivity(dish_intent);
                getActivity().overridePendingTransition(R.anim.move_from_right,R.anim.moveleft);
            }
        });

        msearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search_intent=new Intent(getActivity(),SearchActivity.class);
                getActivity().startActivity(search_intent);
                getActivity().overridePendingTransition(R.anim.move_from_right,R.anim.moveleft);
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animate = AnimationUtils.loadAnimation(getActivity(), R.anim.animate);
                lv1.startAnimation(animate);
                animate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        lv1.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });

        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lv1.setVisibility(View.VISIBLE);
                Animation animate = AnimationUtils.loadAnimation(getActivity(), R.anim.animate1);
                lv1.startAnimation(animate);
            }
        });*/

        return view;
    }
    private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
      public void onScrollChanged(ScrollView who, int l, int t, int oldl,int oldt) {

            final int headerHeight = lv1Header.findViewById(R.id.header_layout).getHeight() - getActivity().getActionBar().getHeight();

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
    @Override
    public void onResume() {
        instance = this;
        super.onResume();
    }
    private Drawable.Callback mDrawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            getActivity().getActionBar().setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
        }
    };

    public static FirstFragment getInstance() {
        return instance;
    }
    /**
     * This Method is used to setListViewHeight
     * Based on Children it gives Appropriate height
     * @param listView
     */
    public  void setListViewHeightBasedOnChildren(ListView listView) {
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
        //params.height +=params.height+200;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


}

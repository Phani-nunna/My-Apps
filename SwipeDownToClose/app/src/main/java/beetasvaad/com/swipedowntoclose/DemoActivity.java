package beetasvaad.com.swipedowntoclose;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import panel.SlidingUpPanelLayout;

public class DemoActivity extends ActionBarActivity {
    private static final String TAG = "DemoActivity";
    private LinearLayout mLinearLayout;
    private SlidingUpPanelLayout mLayout;
    private LinearLayout demoLayout;
    private ListView mainList;
    private Toolbar toolbar;
    private ListView paneListView;

    String[] items = {"this", "is", "a", "really",
            "silly", "list"};
    String[] values = new String[]{"Android", "iPhone", "WindowsMobile",
            "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
            "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
            "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
            "Android", "iPhone", "WindowsMobile"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_demo);

        setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));
        mainList = (ListView) findViewById(R.id.main_list);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLinearLayout = (LinearLayout) findViewById(R.id.dragView);
        // demoLayout = (LinearLayout)findViewById(R.id.demoLayout);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        paneListView = (ListView) findViewById(R.id.pane_list);
        setSupportActionBar(toolbar);
        mLayout.setEnableDragViewTouchEvents(true);

        //// mLayout.setOverlayed(true);

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final ArrayList<String> list1 = new ArrayList<String>();
        for (int i = 0; i < items.length; ++i) {
            list1.add(items[i]);
        }
        final StableArrayAdapter adapterr = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        mainList.setAdapter(adapter);
        paneListView.setAdapter(adapterr);
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });

       /* paneListView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });*/

        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                /*if (slideOffset < 0.2) {
                    if (getSupportActionBar().isShowing()) {
                        getSupportActionBar().hide();
                    }
                } else {
                    if (!getSupportActionBar().isShowing()) {
                        getSupportActionBar().show();
                    }
                }*/
            }

            @Override
            public void onPanelExpanded(View panel) {
                Log.i(TAG, "onPanelExpanded");

            }

            @Override
            public void onPanelCollapsed(View panel) {
                Log.i(TAG, "onPanelCollapsed");
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                    }
                });
                //hidePane();
                /*if(mLayout.getPanelState() != PanelState.HIDDEN){
                    System.out.println("phani Hidden");
                    mLayout.setPanelState(PanelState.HIDDEN);
                }else {
                    System.out.println("phani Collapsed");
                    mLayout.setPanelState(PanelState.COLLAPSED);
                }*/
                //   mLayout.hidePane();
                // mLayout.setClipPanel(false);
                //   mLayout.setPanelState(PanelState.HIDDEN);
                //   mLayout.setPanelState(PanelState.EXPANDED);
                //  demoLayout.setVisibility(View.GONE);
                // mLayout.setAnchorHidden();
                //  mLinearLayout.setVisibility(View.GONE);
           /*   Log.i(TAG," panel state "+mLayout.getPanelState());
                if (mLayout.getPanelState() != PanelState.HIDDEN) {
                    mLayout.setPanelState(PanelState.HIDDEN);
                    //item.setTitle(R.string.action_show);
                } else {
                    mLayout.setPanelState(PanelState.COLLAPSED);
                  //  item.setTitle(R.string.action_hide);
                }
*/
            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.i(TAG, "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                Log.i(TAG, "onPanelHidden");
            }
        });
        /*Button f = (Button) findViewById(R.id.follow);
        f.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.setPanelState(PanelState.HIDDEN);
            }
        });*/

      /*  TextView t = (TextView) findViewById(R.id.main);
        t.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.setPanelState(PanelState.COLLAPSED);
            }
        });

        t = (TextView) findViewById(R.id.name);
        t.setText(Html.fromHtml(getString(R.string.hello)));
        Button f = (Button) findViewById(R.id.follow);
        f.setText(Html.fromHtml(getString(R.string.follow)));
        f.setMovementMethod(LinkMovementMethod.getInstance());
        f.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.twitter.com/umanoapp"));
                startActivity(i);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

   /* @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_toggle: {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
                        System.out.println("phani hide menu");
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                        item.setTitle(R.string.action_show);
                    } else {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        item.setTitle(R.string.action_hide);
                    }
                }
                return true;
            }
            case R.id.action_anchor: {
                if (mLayout != null) {
                    if (mLayout.getAnchorPoint() == 1.0f) {
                        mLayout.setAnchorPoint(0.7f);
                        mLayout.setPanelState(PanelState.ANCHORED);
                        item.setTitle(R.string.action_anchor_disable);
                    } else {
                        mLayout.setAnchorPoint(1.0f);
                        mLayout.setPanelState(PanelState.COLLAPSED);
                        item.setTitle(R.string.action_anchor_enable);
                    }
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    public void hidePane() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        }.start();

    }

}

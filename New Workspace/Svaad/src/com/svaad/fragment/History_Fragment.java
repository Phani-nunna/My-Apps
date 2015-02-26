package com.svaad.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.svaad.R;
import com.svaad.Dto.CamRestaurantDetailDto;
import com.svaad.activity.SearchDish_Activity;
import com.svaad.adapter.CamSearchAdapter;
import com.svaad.databaseDAO.DatabaseDAO;
import com.svaad.utils.Constants;

public class History_Fragment extends Fragment implements OnClickListener {

	private DatabaseDAO da;
	private List<CamRestaurantDetailDto> reslist;
	private ListView listview;
	private ProgressBar pbar;
	private TextView tvNoHistory;
	private String path;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.history_fragment, container,
				false);

		init(view);
		
		
		Bundle bundle = getArguments();
		if (bundle != null) {
			path = bundle.getString("path");
		}

		da = new DatabaseDAO(getActivity());
		reslist = da.getResHistoryTable();
		if (reslist != null && reslist.size() > 0) {
			listview.setAdapter(new CamSearchAdapter(getActivity(), 0, reslist));
		} else {
			tvNoHistory.setVisibility(View.VISIBLE);
		}

		return view;
	}

	private void init(View view) {

		listview = (ListView) view.findViewById(R.id.listView);
		pbar = (ProgressBar) view.findViewById(R.id.pbar);
		tvNoHistory = (TextView) view.findViewById(R.id.tvNoHistory);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (reslist != null && reslist.size() > 0) {
					CamRestaurantDetailDto restautantDetail = reslist.get(arg2);
					if (restautantDetail != null) {

						Intent i = new Intent(getActivity(),
								SearchDish_Activity.class);
//						i.setFlags(i.FLAG_ACTIVITY_CLEAR_TASK);
						i.putExtra(Constants.DATA, restautantDetail);
						if(path!=null)
						{
						i.putExtra("path", path);
						}
						startActivity(i);

						getActivity().overridePendingTransition(
								R.anim.slide_up, R.anim.slide_out);
					}

				}
			}
		});

	}

	@Override
	public void onClick(View v) {

	}

}

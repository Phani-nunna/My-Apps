package com.svaad.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.svaad.R;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.adapter.PhotosGridAdapter;
import com.svaad.asynctask.PhotoAsynctask;
import com.svaad.interfaces.SvaadFeedCallback;
import com.svaad.interfaces.SvaadProgressCallback;
import com.svaad.responseDto.FeedResponseDto;

public class PhotoCountFragment extends Fragment implements SvaadFeedCallback,OnScrollListener,SvaadProgressCallback{
	private GridView gv;
	private TextView tvNoResults;
	private ProgressBar pbar;
	private FeedResponseDto feedResponseDto;
	
	String loginUserId,profileuserId;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.photo_grid_layout,
				container, false);
		initializeList();

		gv = (GridView) view.findViewById(R.id.dishDetailListView);
		tvNoResults = (TextView) view.findViewById(R.id.tvNoResults);
		pbar = (ProgressBar) view.findViewById(R.id.pbar);
		
		if(this.getArguments()!=null)
		{
			loginUserId=this.getArguments().getString("loginUserId");
			profileuserId=this.getArguments().getString("profileuserId");
			
			if(loginUserId!=null&& loginUserId.length()>0)
			{
				getPhotosList(loginUserId);
			}
			else if(profileuserId!=null&& profileuserId.length()>0)
			{
				getPhotosList(profileuserId);
			}
		}
		
	

		gv.setAdapter(new PhotosGridAdapter(getActivity(), 0,
				feedResponseDto.getResults()));

		gv.setOnScrollListener(this);
		
		return view;

	}
	
	private void getPhotosList(String userId) 
	{
		new PhotoAsynctask(getActivity(), this,userId).execute();
	}

	private void initializeList() {
		if (feedResponseDto == null) {
			feedResponseDto = new FeedResponseDto();
			feedResponseDto.setResults(new ArrayList<FeedDetailDto>());
		} else if (feedResponseDto.getResults() == null) {
			feedResponseDto.setResults(new ArrayList<FeedDetailDto>());
		}
	}


	@Override
	public void setResponse(Object feedResponseDto) {
		
		if (feedResponseDto == null) {
			return;
		}
		addResult((FeedResponseDto) feedResponseDto);
		PhotosGridAdapter feedAdapter = (PhotosGridAdapter) gv.getAdapter();
		if (feedAdapter != null) {
			feedAdapter.setFeedDtos(this.feedResponseDto.getResults());
			feedAdapter.notifyDataSetChanged();
		}
		
	}
	
	private void addResult(FeedResponseDto feedResponseDto) {
		if (feedResponseDto != null && feedResponseDto.getResults() != null
				&& feedResponseDto.getResults().size() > 0) {
			List<FeedDetailDto> feedDto = feedResponseDto.getResults();
			if (feedDto != null && feedDto.size() > 0) {
				this.feedResponseDto.getResults().addAll(feedDto);

			}

		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public void progressOn() {
		
		pbar.setVisibility(View.VISIBLE);
		
	}

	@Override
	public void progressOff() {
		
		pbar.setVisibility(View.GONE);
	}

}

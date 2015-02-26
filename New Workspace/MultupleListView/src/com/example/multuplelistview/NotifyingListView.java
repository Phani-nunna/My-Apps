package com.example.multuplelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class NotifyingListView extends ListView {

	/**
	 * @author Cyril Mottier
	 */
	public interface OnScrollChanged {
		void onScrollChanged(ListView notifyingListView, int l, int t,
				int oldl, int oldt);
	}

	private OnScrollListener mOnScrollChangedListener;

	public NotifyingListView(Context context) {
		super(context);
	}

	public NotifyingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NotifyingListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setOnScrollChangedListener(OnScrollListener listener) {
		mOnScrollChangedListener = listener;
	}

}
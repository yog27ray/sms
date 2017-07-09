package com.yog27ray.contactsync.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Handle endless scrolling
 */
public abstract class EndlessRecyclerOnScrollListener extends
		RecyclerView.OnScrollListener {
	private static final int VISIBLE_THRESHOLD = 10;
	public static String LOG_TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();
	private int previousTotal;
	private boolean loading = false;
	private int currentPage;

	private final LinearLayoutManager linearLayoutManager;

	protected EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
		this.linearLayoutManager = linearLayoutManager;
		initializeScroller();
	}

	private void initializeScroller () {
		this.currentPage = 0;
		this.previousTotal = 0;
	}

	@Override
	public void onScrolled (RecyclerView recyclerView, int dx, int dy) {
		super.onScrolled(recyclerView, dx, dy);

		int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
		int totalItemCount = linearLayoutManager.getItemCount();

		if (loading) {
			if (totalItemCount > previousTotal) {
				loading = false;
				previousTotal = totalItemCount;
			}
		} else {
			if (lastVisibleItem + VISIBLE_THRESHOLD >= totalItemCount) {
				currentPage++;
				onLoadMore(currentPage);
				loading = true;
			}
		}
	}

	public abstract void onLoadMore (int currentPage);

	public int getCurrentPage () {
		return this.currentPage;
	}

	public void setCurrentPage (int page) {
		currentPage = page;
	}

	public void resetScrollListener () {
		initializeScroller();
	}
}

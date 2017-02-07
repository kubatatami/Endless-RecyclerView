package com.github.ybq.endless;

import android.support.v7.widget.RecyclerView;

@SuppressWarnings("WeakerAccess")
public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private boolean loading; // True if we are still waiting for the last set of data to load.

    private int visibleThreshold = 2; // The minimum amount of items to have below your current scroll position before loading more.

    private int currentPage = 1;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (canLoadNextPage(recyclerView)) {
            currentPage++;
            loading = true;
            onLoadMore(currentPage);
        }
    }

    private boolean canLoadNextPage(RecyclerView recyclerView) {
        RecyclerViewPositionHelper recyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = recyclerViewHelper.getItemCount();
        int firstVisibleItem = recyclerViewHelper.findFirstVisibleItemPosition();
        return !loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold);
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public void setVisibleThreshold(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isLoading() {
        return loading;
    }

    public abstract void onLoadMore(int currentPage);
}
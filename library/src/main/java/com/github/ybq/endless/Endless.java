package com.github.ybq.endless;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 */
public class Endless {

    private final EndlessScrollListener listener;
    private LoadMoreListener loadMoreListener;
    private RecyclerView recyclerView;
    private EndlessAdapter mAdapter;
    private View loadMoreView;
    private int totalItemCount;

    public static Endless applyTo(RecyclerView recyclerView, View loadMoreView) {
        Endless endless = new Endless(recyclerView, loadMoreView);
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null) {
            endless.setAdapter(adapter);
        }
        return endless;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private Endless(final RecyclerView recyclerView, View loadMoreView) {
        this.recyclerView = recyclerView;
        this.loadMoreView = loadMoreView;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (!(adapter instanceof EndlessAdapter)) {
            setAdapter(adapter);
        }
        recyclerView.addOnScrollListener(listener = new EndlessScrollListener() {
            @Override
            public void onLoadMore(final int currentPage) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canLoadMoreAvailable() && loadMoreListener != null && !mAdapter.isLoading()) {
                            mAdapter.setLoading(true);
                            loadMoreListener.onLoadMore(currentPage);
                        }
                    }
                });

            }
        });
    }

    private boolean canLoadMoreAvailable() {
        return mAdapter.getItemCount() < totalItemCount;
    }

    public void setVisibleThreshold(int visibleThreshold) {
        this.listener.setVisibleThreshold(visibleThreshold);
    }

    public void setTotalItemCount(int totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

    public void setCurrentPage(int currentPage) {
        this.listener.setCurrentPage(currentPage);
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void loadMoreComplete() {
        mAdapter.setLoading(false);
        listener.setLoading(false);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null) {
            return;
        }
        if (adapter instanceof EndlessAdapter) {
            recyclerView.setAdapter(adapter);
        }
        recyclerView.setAdapter(EndlessAdapter.wrap(adapter, loadMoreView));
        mAdapter = (EndlessAdapter) recyclerView.getAdapter();
    }

    public void removeFromRecyclerView() {
        recyclerView.removeOnScrollListener(listener);
    }

    public interface LoadMoreListener {
        void onLoadMore(int page);
    }
}

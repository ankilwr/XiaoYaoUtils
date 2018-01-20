package com.shihang.pulltorefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.shihang.pulltorefresh.inter.NotifyListener;
import com.shihang.pulltorefresh.view.SwipeRefreshLayout;
import com.yanzhenjie.recyclerview.swipe.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView.LoadMoreAction;

public class PullRecyclerView extends FrameLayout {

    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeMenuRecyclerView recyclerView;
    private LoadListener listener;
    private ViewGroup emptyParent;
    private boolean headerEnable, loadMoreEnable;

    public PullRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public PullRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.layout_pull_to_refresh, this);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.loadingColor);
        recyclerView = findViewById(R.id.recyclerView);
        emptyParent = findViewById(R.id.emptyParent);
        setPullEnable(false, false);
        recyclerView.setNotifyListener(new NotifyListener() {
            @Override
            public void notifyDataSetChanged(int count) {
                initDatasLayout(count);
            }
        });
    }

    private void initDatasLayout(int count){
        if(count > 0){
            emptyParent.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.GONE);
            emptyParent.setVisibility(View.VISIBLE);
        }
    }


    public void setAdapter(RecyclerView.Adapter adapter){
        recyclerView.setAdapter(adapter);
    }

    public void setAdapter(RecyclerView.Adapter adapter, View emptyView){
        emptyParent.removeAllViews();
        if(emptyParent != null){
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            emptyParent.addView(emptyView, params);
        }
        recyclerView.setAdapter(adapter);
        initDatasLayout(adapter.getItemCount());
    }


    public interface LoadListener extends SwipeRefreshLayout.OnRefreshListener, SwipeMenuRecyclerView.LoadMoreListener {}

    private LoadListener pullListener = new LoadListener() {
        @Override
        public void onRefresh() {
            if(listener != null) listener.onRefresh();
            //下拉刷新中不能加载更多
            recyclerView.setRefreshIngState();
        }

        @Override
        public void onLoadMore() {
            if(listener != null) listener.onLoadMore();
            //加载更多中不能下拉刷新
            swipeRefreshLayout.setEnabled(false);
        }
    };

    public void setPullListener(LoadListener listener){
        this.listener = listener;
        swipeRefreshLayout.setOnRefreshListener(pullListener);
        recyclerView.setLoadMoreListener(pullListener);
    }

    public void setPullEnable(boolean header, boolean footer){
        setPullEnable(header, footer, null);
    }

    public void setPullEnable(boolean header, boolean footer, LoadMoreAction loadMoreAction){
        headerEnable = header;
        loadMoreEnable = footer;
        swipeRefreshLayout.setEnabled(header);
        recyclerView.setLoadMoreEnable(footer, loadMoreAction);
    }

    /** 代码调用下拉刷新 */
    public void pullRefreshing(){
        if(swipeRefreshLayout.isRefreshing()) return;
        swipeRefreshLayout.setRefreshing(true);
    }



    public void loadFinish(boolean isRefresh){
        loadFinish(isRefresh, false);
    }

    public void loadFinish(boolean isRefresh, boolean hasMore){
        loadFinish(isRefresh, hasMore, true);
    }


    /**
     * @param isRefresh:是否是 true:刷新 (false: 加载更多)
     * @param hasMore : true:可以加载更多, false:已经是最有一页
     * @param noDataVisibleMore : true:显示提示item(如：----已经到底了----), false:不显示提示item
     */
    public void loadFinish(boolean isRefresh, boolean hasMore, boolean noDataVisibleMore){
        if(isRefresh){
            if(swipeRefreshLayout.isRefreshing()){
                swipeRefreshLayout.setRefreshResult(true);
            }
            //重置加载更多item
            recyclerView.setRefreshSuccess(hasMore);
        }else{
            //重置刷新开关
            swipeRefreshLayout.setEnabled(headerEnable);
            recyclerView.loadMoreFinish(hasMore, noDataVisibleMore);
        }
    }

    public void loadError(boolean isRefresh){
        if(isRefresh){
            if(swipeRefreshLayout.isRefreshing()){
                swipeRefreshLayout.setRefreshResult(false);
            }
            //重置加载更多item
            recyclerView.setRefreshError();
        }else{
            //重置刷新开关
            swipeRefreshLayout.setEnabled(headerEnable);
            recyclerView.loadMoreError(0, "加载失败");
        }
    }



    public SwipeMenuRecyclerView getSwipeRecyclerView(){
        return recyclerView;
    }




}

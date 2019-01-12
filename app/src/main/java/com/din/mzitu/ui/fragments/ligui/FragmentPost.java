package com.din.mzitu.ui.fragments.ligui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.din.mzitu.adapter.PostAllAdapter;
import com.din.mzitu.adapter.ViewHolder;
import com.din.mzitu.api.LiGui;
import com.din.mzitu.base.BaseAdapter;
import com.din.mzitu.base.BaseFragment;
import com.din.mzitu.bean.PostAllBean;
import com.din.mzitu.ui.activities.PostSingleActivity;
import com.din.mzitu.ui.fragments.main.FragmentLiGui;
import com.din.mzitu.ui.fragments.mzitu.FragmentPostSingle;

import java.util.List;

import io.reactivex.ObservableEmitter;

public class FragmentPost extends BaseFragment implements BaseAdapter.OnItemClickListener {

    public static final String POST_ALL = "post_all";
    public static final String POSITION = "position";

    public static FragmentPost newInstance(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(POST_ALL, url);
        FragmentPost fragmentPost = new FragmentPost();
        fragmentPost.setArguments(bundle);
        return fragmentPost;
    }

    public static FragmentPost newInstance(String url, int position) {
        Bundle bundle = new Bundle();
        bundle.putString(POST_ALL, url);
        bundle.putInt(POSITION, position);
        FragmentPost fragmentPost = new FragmentPost();
        fragmentPost.setArguments(bundle);
        return fragmentPost;
    }

    @Override
    protected int getPageFragment() {
        return getArguments().getInt(POSITION);
    }

    @Override
    protected void observableTask(ObservableEmitter emitter) {
        String url = getArguments().getString(POST_ALL);
        emitter.onNext(LiGui.getInstance().parseLiGuiMainData(page++, url));
    }

    @Override
    public PostAllAdapter getAdapter() {
        return new PostAllAdapter(this);
    }

    @Override
    public StaggeredGridLayoutManager getLayoutManager() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // 解决屏闪
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        return layoutManager;
    }

    @Override
    protected void observerData(Object p0) {
        listBeans.addAll((List<PostAllBean>) p0);
        adapter.addBeanData(listBeans);
        swipeRefresh.setRefreshing(false);          // 获取数据之后，刷新停止
    }

    @Override
    public void onItemClick(ViewHolder holder, int position) {
        PostAllBean bean = (PostAllBean) listBeans.get(position - 1);         // 获取数据的item
        Intent intent = new Intent(getActivity(), PostSingleActivity.class);
        intent.putExtra(FragmentPostSingle.POST_URL, bean.getUrl());      // 将URL和TITLE传递到下一个页面
        intent.putExtra(FragmentPostSingle.POST_TITLE, bean.getTitle());
        intent.putExtra(FragmentPostSingle.POST_WEBSITE, FragmentLiGui.LIGUI);
        startActivity(intent);
    }
}
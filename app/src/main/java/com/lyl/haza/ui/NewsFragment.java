package com.lyl.haza.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lyl.haza.BaseFragment;
import com.lyl.haza.R;
import com.lyl.haza.bean.NewsBean;
import com.lyl.haza.bean.TabBean;
import com.lyl.haza.contract.NewsContract;
import com.lyl.haza.impl.present.NewsPresenterImpl;
import com.lyl.haza.utils.StringUtils;
import com.lyl.haza.widget.LoadingEmptyLayout;
import com.squareup.picasso.Picasso;

/**
 * Created by lyl on 2016/11/17.
 * </P>
 */
public class NewsFragment extends BaseFragment implements NewsContract.NewsView, SwipeRefreshLayout.OnRefreshListener {

    public static final String EXTRA_TAB = "extra_tab";

    private LoadingEmptyLayout mEmptyLayout;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;

    private NewsContract.NewsPresenter mPresenter;
    private TabBean mBean;

    public static NewsFragment newInstance(TabBean bean) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_TAB, bean);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBean = getArguments().getParcelable(EXTRA_TAB);
        if (mBean == null) {
            Toast.makeText(getActivity(), "数据错误", Toast.LENGTH_SHORT).show();
        } else {
            mPresenter = new NewsPresenterImpl(this, mBean.getType());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEmptyLayout = (LoadingEmptyLayout) view.findViewById(R.id.loading);
        mEmptyLayout.showLoading();
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(new DividerItemBigDecoration());
        mAdapter = new RecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(view.getContext(), R.color.colorAccent),
                ContextCompat.getColor(view.getContext(), R.color.colorPrimary),
                ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void showData() {
        if (mRefreshLayout.isRefreshing())
            mRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
        if (!mEmptyLayout.isContent())
            mEmptyLayout.showContent();
    }

    @Override
    public void showEmpty() {
        mEmptyLayout.showEmpty("暂无" + mBean.getTitle());
    }

    @Override
    public void showError() {
        if (mRefreshLayout.isRefreshing())
            mRefreshLayout.setRefreshing(false);
        mEmptyLayout.showError("获取" + mBean.getTitle() + "失败", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmptyLayout.showLoading();
                mPresenter.getData();
            }
        });
    }

    @Override
    public void showErrToast(String errMsg) {
        Toast.makeText(getActivity(), errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        mPresenter.getData();
    }

    private class RecyclerAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final NewsBean bean = mPresenter.getItems().get(position);
            ((MyViewHolder) holder).onBind(bean);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsDetailActivity.start(getActivity(), bean);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mPresenter.getItems() == null ? 0 : mPresenter.getItems().size();
        }
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;
        private TextView mTitle;
        private TextView mDate;
        private TextView mAuthor;
        private TextView mLabel;

        public MyViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.news_image);
            mTitle = (TextView) itemView.findViewById(R.id.news_title);
            mDate = (TextView) itemView.findViewById(R.id.news_date);
            mAuthor = (TextView) itemView.findViewById(R.id.news_author);
            mLabel = (TextView) itemView.findViewById(R.id.news_label);
        }

        private void onBind(NewsBean bean) {
            mTitle.setText(bean.getTitle());
            mDate.setText(bean.getDate());
            mAuthor.setText(bean.getAuthor_name());
            mLabel.setText(bean.getRealtype());
            if (!StringUtils.isEmpty(bean.getThumbnail_pic_s()))
                Picasso.with(itemView.getContext())
                        .load(bean.getThumbnail_pic_s())
                        .placeholder(R.drawable.default_loading)
                        .error(R.drawable.default_loading)
                        .into(mImageView);
            else
                mImageView.setImageResource(R.drawable.default_loading);
        }
    }
}

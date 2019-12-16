package com.nixsol.mahesh.ui.home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nixsol.mahesh.R;
import com.nixsol.mahesh.adapter.FactListAdapter;
import com.nixsol.mahesh.common.api.ApiClient;
import com.nixsol.mahesh.common.ui.BaseFragment;
import com.nixsol.mahesh.common.utils.LogUtil;
import com.nixsol.mahesh.model.response.Fact;
import com.nixsol.mahesh.model.response.FactResponse;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = getClass().getName();
    private Context mContext;

    @BindView(R.id.recylerView)
    RecyclerView mRecylerView;

    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.layoutConnection)
    LinearLayout layoutConnection;

    @BindView(R.id.buttonRetry)
    Button buttonRetry;
    @BindView(R.id.textInternet)
    TextView textInternet;

    private FactListAdapter adapter;
    private LinearLayoutManager manager;
    private ArrayList<Fact> facts;

    public static HomeFragment newInstance() {
        HomeFragment historyFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        historyFragment.setArguments(bundle);
        return historyFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        super.setView(view);
        initUI();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecylerView.setLayoutManager(manager);
        facts = new ArrayList<>();

        if (hasConnectivity()) {
            visibleHomeUI(true);
            sendApiRequest(false);
        } else {
            visibleHomeUI(false);
        }
    }

    private void initUI() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void setRefreshing(boolean flag) {
        mSwipeRefreshLayout.setRefreshing(flag);
    }

    @OnClick(R.id.buttonRetry)
    public void retryClick(View view) {
        onRefresh();
    }

    private void setAdapterToRecyclerView(FactResponse factResponse) {
        visibleHomeUI(true);
        if (!TextUtils.isEmpty(factResponse.getName())) {
            setAppTitle(factResponse.getName());
        }
        facts = getFilterList(factResponse.getFactList());
        notifyAdapter();
    }

    private void notifyAdapter() {
        if (adapter == null) {
            facts = new ArrayList<>();
            adapter = new FactListAdapter(mContext, facts);
            mRecylerView.setAdapter(adapter);
        }
        adapter.setItems(facts);
        adapter.notifyDataSetChanged();
    }

    private void visibleHomeUI(boolean flag) {
        layoutConnection.setVisibility(flag ? View.GONE : View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    private ArrayList<Fact> getFilterList(ArrayList<Fact> factArrayList) {
        for (int i = 0; i < factArrayList.size(); ) {
            try {
                if (factArrayList.get(i).checkNull()) {
                    factArrayList.remove(i);
                } else {
                    i++;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return factArrayList;
    }

    private void setAppTitle(String title) {
        getActivity().setTitle(title);
    }

    @Override
    public void onRefresh() {
        if (hasConnectivity()) {
            visibleHomeUI(true);
            sendApiRequest(true);
        } else {
            visibleHomeUI(false);
        }
    }

    private void sendApiRequest(final boolean isFromRefresh) {
        new ApiClient().getFacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (!isFromRefresh)
                            showProgressDialog(getString(R.string.str_msg_loading_facts));
                        else
                            setRefreshing(true);
                        notifyBlankAdapter();
                    }
                })
                .subscribe(new Subscriber<FactResponse>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.info(TAG, "Get Fact Api :: onCompleted");
                        if (!isFromRefresh)
                            dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        handleException(e);
                        LogUtil.printRetroFitError(e);
                    }

                    @Override
                    public void onNext(FactResponse factResponse) {
                        LogUtil.printObject(factResponse.getFactList());
                        setAdapterToRecyclerView(factResponse);
                        setRefreshing(false);
                    }
                });
    }

    private void notifyBlankAdapter() {
        adapter = null;
        notifyAdapter();
    }

    private void handleException(Throwable t) {
        if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException || t instanceof ConnectException)) {
            if (t instanceof SocketTimeoutException || t instanceof TimeoutException) {
                textInternet.setText(getString(R.string.server_time_out));
            } else if (t instanceof UnknownHostException) {
                textInternet.setText(getString(R.string.unable_to_connect));
            } else if (t instanceof retrofit2.adapter.rxjava.HttpException
                    || t instanceof HttpException) {
                textInternet.setText(getString(R.string.server_auth_failed));
            } else {
                textInternet.setText(getString(R.string.no_internet_connection));
            }
        } else {
            textInternet.setText(getString(R.string.no_internet_connection));
        }
        visibleHomeUI(false);
    }
}

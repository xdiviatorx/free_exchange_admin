package com.technologies.mobile.free_exchange_admin.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.technologies.mobile.free_exchange_admin.R;
import com.technologies.mobile.free_exchange_admin.adapters.OffersAdapter;
import com.technologies.mobile.free_exchange_admin.callbacks.OnDownloadListener;
import com.technologies.mobile.free_exchange_admin.rest.RestClient;
import com.technologies.mobile.free_exchange_admin.rest.RetrofitService;

/**
 * Created by diviator on 14.11.2016.
 */

public class RejectedOffersFragment extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener, OnDownloadListener{

    public static final String REJECT_ACTION = "REJECT_ACTION";

    ListView lvOffers;
    OffersAdapter offersAdapter;

    BroadcastReceiver receiver;

    SwipeRefreshLayout srl;

    public RejectedOffersFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        receiver = new RejectReceiver();
        IntentFilter filter = new IntentFilter(REJECT_ACTION);
        getActivity().registerReceiver(receiver,filter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rejected_offers,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view){
        lvOffers = (ListView) view.findViewById(R.id.lvOffers);
        offersAdapter = new OffersAdapter(getContext(),R.layout.item_offer_no_buttons);
        lvOffers.setAdapter(offersAdapter);
        offersAdapter.init(RestClient.REJECTED_STATUS);
        lvOffers.setOnScrollListener(this);
        offersAdapter.setOnDownloadListener(this);

        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        srl.setOnRefreshListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        if (i >= Math.max(i2 - 10, 10)) {
            offersAdapter.additionalDownloading(i2);
        }
    }

    @Override
    public void onRefresh() {
        offersAdapter.init(RestClient.REJECTED_STATUS);
    }

    @Override
    public void onDownloaded(int count) {
        srl.setRefreshing(false);
    }

    class RejectReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            offersAdapter.init(RestClient.REJECTED_STATUS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }
}

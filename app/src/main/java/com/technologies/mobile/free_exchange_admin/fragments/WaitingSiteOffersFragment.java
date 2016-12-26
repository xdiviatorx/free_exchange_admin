package com.technologies.mobile.free_exchange_admin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.technologies.mobile.free_exchange_admin.R;
import com.technologies.mobile.free_exchange_admin.activities.EditOfferSiteActivity;
import com.technologies.mobile.free_exchange_admin.activities.SiteRejectDialogActivity;
import com.technologies.mobile.free_exchange_admin.adapters.SiteOffersAdapter;
import com.technologies.mobile.free_exchange_admin.callbacks.OnButtonClickCallback;
import com.technologies.mobile.free_exchange_admin.callbacks.OnDownloadListener;
import com.technologies.mobile.free_exchange_admin.rest.RestClient;

import static com.technologies.mobile.free_exchange_admin.activities.EditOfferVkActivity.RESULT_APPROVED;
import static com.technologies.mobile.free_exchange_admin.activities.OfferDialogActivity.RESULT_DELETED;


/**
 * Created by diviator on 19.11.2016.
 */

public class WaitingSiteOffersFragment extends Fragment implements AbsListView.OnScrollListener, OnButtonClickCallback, OnDownloadListener, SwipeRefreshLayout.OnRefreshListener{

    public static final int REJECT_REQUEST = 178;
    public static final int APPROVE_REQUEST = 179;

    ListView lvOffers;
    SiteOffersAdapter siteOffersAdapter;

    int mLastPosition = -1;

    SwipeRefreshLayout srl;

    TextView tvOffersCount;
    int mOffersCount = 0;

    public WaitingSiteOffersFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_waiting_site_offers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        View includeView = view.findViewById(R.id.offersCountLayout);
        tvOffersCount = (TextView) includeView.findViewById(R.id.tvOffersCount);

        lvOffers = (ListView) view.findViewById(R.id.lvOffers);
        siteOffersAdapter = new SiteOffersAdapter(getContext(), R.layout.item_offer);
        lvOffers.setAdapter(siteOffersAdapter);
        siteOffersAdapter.init(RestClient.WAITING_STATUS);
        siteOffersAdapter.setOnButtonClickCallback(this);
        lvOffers.setOnScrollListener(this);
        siteOffersAdapter.setOnDownloadListener(this);

        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        srl.setOnRefreshListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        if (i >= Math.max(i2 - 10, 10)) {
            siteOffersAdapter.additionalDownloading(i2);
        }
    }

    @Override
    public void onButtonClick(int type, int pos) {
        switch (type) {
            case ButtonType.REJECT: {
                mLastPosition = pos;
                Intent intent = new Intent(getContext(), SiteRejectDialogActivity.class);
                intent.putExtra(SiteRejectDialogActivity.OFFER, siteOffersAdapter.getItem(pos));
                startActivityForResult(intent, REJECT_REQUEST);
                break;
            }
            case ButtonType.ACCEPT: {
                mLastPosition = pos;
                //Intent intent = new Intent(getContext(), SiteApproveDialogActivity.class);
                //intent.putExtra(SiteApproveDialogActivity.OFFER, siteOffersAdapter.getItem(pos));

                Intent intent = new Intent(getContext(), EditOfferSiteActivity.class);
                intent.putExtra(EditOfferSiteActivity.OFFER_EXTRA,siteOffersAdapter.getItem(pos));
                startActivityForResult(intent, APPROVE_REQUEST);
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REJECT_REQUEST && resultCode == RESULT_DELETED ) {
            siteOffersAdapter.deleteOffer(mLastPosition);
            mOffersCount--;
            tvOffersCount.setText(String.valueOf(mOffersCount));
        }
        if( requestCode == APPROVE_REQUEST && resultCode == RESULT_APPROVED){
            siteOffersAdapter.deleteOffer(mLastPosition);
            mOffersCount--;
            tvOffersCount.setText(String.valueOf(mOffersCount));
        }
    }

    @Override
    public void onRefresh() {
        siteOffersAdapter.init(RestClient.WAITING_STATUS);
    }

    @Override
    public void onDownloaded(int count) {
        srl.setRefreshing(false);
        mOffersCount = count;
        if( tvOffersCount != null ) {
            tvOffersCount.setText(String.valueOf(count));
        }
    }
}

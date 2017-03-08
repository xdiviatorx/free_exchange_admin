package com.technologies.mobile.free_exchange_admin.activities;

import android.os.Bundle;
import android.view.View;

import com.technologies.mobile.free_exchange_admin.R;
import com.technologies.mobile.free_exchange_admin.model.Category;
import com.technologies.mobile.free_exchange_admin.rest.model.Offer;
import com.technologies.mobile.free_exchange_admin.rest.queries.SiteApprover;

public class EditOfferSiteActivity extends EditOfferVkActivity {

    public static final String OFFER_EXTRA = "OFFER_EXTRA";
    private Offer mOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEditText() {
        //super.initEditText();
        mOffer = getIntent().getParcelableExtra(OFFER_EXTRA);
        mEtText.setText(mOffer.getText());
    }

    @Override
    protected void initCategoriesSpinner() {
        super.initCategoriesSpinner();
        getCategoriesSpinner().setSelection(getCategoriesAdapter().getIndexById(mOffer.getCategory()));
    }

    @Override
    protected void initAdapter() {
        //super.initAdapter();
        Offer offer = getIntent().getParcelableExtra(OFFER_EXTRA);
        mRecyclerPostPhotosAdapter.addPhotos(offer.getVKApiPhotoList());
    }

    @Override
    public void onClick(View view) {
        //super.onClick(view);
        switch (view.getId()){
            case R.id.bCheck:{
                approver = new SiteApprover((Offer) getIntent().getParcelableExtra(OFFER_EXTRA),
                        mEtText.getText().toString(),mRecyclerPostPhotosAdapter.getData(),
                        ((Category) mSCategories.getSelectedItem()).getId());
                approver.setApproverCallback(this);
                approver.approveRequest(mIsChanged);
                break;
            }
        }
    }



}

package com.technologies.mobile.free_exchange_admin.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.technologies.mobile.free_exchange_admin.R;
import com.technologies.mobile.free_exchange_admin.activities.ImagePreviewActivity;
import com.technologies.mobile.free_exchange_admin.activities.EditOfferVkActivity;
import com.technologies.mobile.free_exchange_admin.activities.VkRejectDialogActivity;
import com.technologies.mobile.free_exchange_admin.adapters.VkOffersAdapter;
import com.technologies.mobile.free_exchange_admin.callbacks.OnButtonClickCallback;
import com.technologies.mobile.free_exchange_admin.callbacks.OnUserNameClickListener;
import com.technologies.mobile.free_exchange_admin.callbacks.OnVkDownloadListener;
import com.technologies.mobile.free_exchange_admin.callbacks.OnImageSetClickCallback;
import com.technologies.mobile.free_exchange_admin.rest.model.VkUser;
import com.technologies.mobile.free_exchange_admin.rest.queries.VkGetOffers;
import com.technologies.mobile.free_exchange_admin.rest.queries.VkMessageSender;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPostArray;

import java.util.ArrayList;
import java.util.Map;

import static com.technologies.mobile.free_exchange_admin.activities.MainActivity.OFFER_REQUEST;

/**
 * Created by diviator on 14.11.2016.
 */

public class WaitingVkOffersFragment extends Fragment implements
        OnVkDownloadListener, OnButtonClickCallback, AbsListView.OnScrollListener,
        OnImageSetClickCallback, SwipeRefreshLayout.OnRefreshListener, OnUserNameClickListener {

    public static final int REJECT_REQUEST = 78;

    ListView lvOffers;
    VkOffersAdapter mVkOffersAdapter;

    VkGetOffers vkGetOffers;

    int mLastPosition = -1;

    SwipeRefreshLayout srl;

    TextView tvOffersCount;
    int mOffersCount = 0;

    public WaitingVkOffersFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_waiting_vk_offers, container, false);
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
        mVkOffersAdapter = new VkOffersAdapter(getContext(), R.layout.item_vk_offer);
        mVkOffersAdapter.setOnButtonClickCallback(this);
        mVkOffersAdapter.setOnImageSetClickCallback(this);
        mVkOffersAdapter.setOnUserNameClickListener(this);
        lvOffers.setAdapter(mVkOffersAdapter);

        vkGetOffers = new VkGetOffers();
        vkGetOffers.setOnVkDownloadListener(this);
        vkGetOffers.init();
        lvOffers.setOnScrollListener(this);

        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        srl.setOnRefreshListener(this);
    }

    @Override
    public void onPostsDownloaded(VKPostArray vkPostArray, Map<Long,VkUser> userMap, int count) {
        mVkOffersAdapter.addUsers(userMap);
        mVkOffersAdapter.addPost(vkPostArray);
        mVkOffersAdapter.notifyDataSetChanged();

        srl.setRefreshing(false);

        mOffersCount=count;
        tvOffersCount.setText(String.valueOf(count));
    }

    @Override
    public void onButtonClick(int type, int pos) {
        switch (type) {
            case OnButtonClickCallback.ButtonType.ACCEPT: {
                mLastPosition = pos;
                /*VkOfferQueries vkPostOffer = new VkOfferQueries(mVkOffersAdapter.getItem(pos));
                long postId = mVkOffersAdapter.getItemId(pos);
                vkPostOffer.accept(postId);
                mVkOffersAdapter.deletePost(pos);*/
                Intent intent = new Intent(getContext(), EditOfferVkActivity.class);
                intent.putExtra(EditOfferVkActivity.VK_POST_EXTRA, mVkOffersAdapter.getItem(pos));

                ArrayList<VKApiPhoto> photos = new ArrayList<>();
                for (int i = 0; i < mVkOffersAdapter.getItem(pos).attachments.size(); i++) {
                    VKAttachments.VKApiAttachment attachment = mVkOffersAdapter.getItem(pos).attachments.get(i);
                    if (attachment.getType().equals(VKAttachments.TYPE_PHOTO)) {
                        VKApiPhoto vkApiPhoto = (VKApiPhoto) attachment;
                        photos.add(vkApiPhoto);
                    }
                }

                intent.putExtra(EditOfferVkActivity.VK_PHOTO_ATTACHMENTS_EXTRA, photos);
                startActivityForResult(intent, OFFER_REQUEST);
                break;
            }
            case OnButtonClickCallback.ButtonType.REJECT: {
                mLastPosition = pos;
                Intent intent = new Intent(getContext(), VkRejectDialogActivity.class);
                intent.putExtra(VkRejectDialogActivity.OFFER, mVkOffersAdapter.getItem(pos));
                intent.putExtra(VkRejectDialogActivity.VK_USER, mVkOffersAdapter.getUser(mVkOffersAdapter.getItem(pos).from_id));
                startActivityForResult(intent, REJECT_REQUEST);
                break;
            }
        }
        mVkOffersAdapter.notifyDataSetChanged();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        if (i >= Math.max(i2 - 10, 10)) {
            vkGetOffers.additionalDownloading(i2);
        }
    }

    @Override
    public void onImageSetClick(String[] urls) {
        Intent intent = new Intent(getContext(), ImagePreviewActivity.class);
        intent.putExtra(ImagePreviewActivity.IMAGES, urls);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REJECT_REQUEST && resultCode == VkRejectDialogActivity.RESULT_DELETED) {
            mVkOffersAdapter.deletePost(mLastPosition);
            mOffersCount--;
            tvOffersCount.setText(String.valueOf(mOffersCount));
        }
        if (requestCode == OFFER_REQUEST && resultCode == EditOfferVkActivity.RESULT_APPROVED) {
            mVkOffersAdapter.deletePost(mLastPosition);
            mOffersCount--;
            tvOffersCount.setText(String.valueOf(mOffersCount));
        }
    }

    @Override
    public void onRefresh() {
        mVkOffersAdapter.clear();
        vkGetOffers.init();
    }

    @Override
    public void onUserNameClicked(final VKApiPost vkApiPost, final VkUser vkUser) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View content = LayoutInflater.from(getContext()).inflate(R.layout.dialog_send_message,null,false);
        builder.setView(content)
                .setTitle(vkUser.getFullName())
                .setCancelable(true)
                .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String message = ((EditText)content.findViewById(R.id.etMessageText)).getText().toString();
                        VkMessageSender messageSender = new VkMessageSender(getContext());
                        messageSender.sendMessageToUser(vkUser.getId(),message);
                    }
                })
                .setNegativeButton(R.string.cancel,null);
        builder.create().show();
    }
}

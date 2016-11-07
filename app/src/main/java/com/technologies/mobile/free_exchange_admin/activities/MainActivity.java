package com.technologies.mobile.free_exchange_admin.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import com.technologies.mobile.free_exchange_admin.R;
import com.technologies.mobile.free_exchange_admin.adapters.VkOffersAdapter;
import com.technologies.mobile.free_exchange_admin.callbacks.OnButtonClickCallback;
import com.technologies.mobile.free_exchange_admin.callbacks.OnDownloadListener;
import com.technologies.mobile.free_exchange_admin.callbacks.OnImageSetClickCallback;
import com.technologies.mobile.free_exchange_admin.rest.model.VkPost;
import com.technologies.mobile.free_exchange_admin.rest.queries.VkGetOffers;
import com.technologies.mobile.free_exchange_admin.rest.queries.VkPostOffer;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKPostArray;
import com.vk.sdk.util.VKUtil;

public class MainActivity extends AppCompatActivity implements
        OnDownloadListener, OnButtonClickCallback, AbsListView.OnScrollListener, OnImageSetClickCallback{

    public static final int LOGIN_REQUEST = 100;

    Toolbar toolbar;

    ListView lvOffers;
    VkOffersAdapter mVkOffersAdapter;

    VkGetOffers vkGetOffers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        login();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initViews() {
        initToolbar();
        initList();
    }

    private void initList() {
        lvOffers = (ListView) findViewById(R.id.lvOffers);
        mVkOffersAdapter = new VkOffersAdapter(this, R.layout.item_offer);
        mVkOffersAdapter.setOnButtonClickCallback(this);
        mVkOffersAdapter.setOnImageSetClickCallback(this);
        lvOffers.setAdapter(mVkOffersAdapter);

        vkGetOffers = new VkGetOffers();
        vkGetOffers.setOnDownloadListener(this);
        vkGetOffers.init();
        lvOffers.setOnScrollListener(this);
    }

    private void login() {
        if (!VKSdk.isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout: {
                VKSdk.logout();
                login();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostsDownloaded(VkPost[] vkPosts) {
        mVkOffersAdapter.addPost(vkPosts);
        mVkOffersAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST) {
            initViews();
        }
    }

    @Override
    public void onButtonClick(int type, int pos) {
        switch (type) {
            case ButtonType.ACCEPT: {
                VkPostOffer vkPostOffer = new VkPostOffer(mVkOffersAdapter.getItem(pos));
                long postId = mVkOffersAdapter.getItemId(pos);
                vkPostOffer.accept(postId);
                mVkOffersAdapter.deletePost(pos);
                break;
            }
            case ButtonType.REJECT: {
                VkPostOffer vkPostOffer = new VkPostOffer(mVkOffersAdapter.getItem(pos));
                long postId = mVkOffersAdapter.getItemId(pos);
                vkPostOffer.reject(postId);
                mVkOffersAdapter.deletePost(pos);
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
        Intent intent = new Intent(this,ImagePreviewActivity.class);
        intent.putExtra(ImagePreviewActivity.IMAGES,urls);
        startActivity(intent);
    }
}

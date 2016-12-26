package com.technologies.mobile.free_exchange_admin.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.technologies.mobile.free_exchange_admin.R;
import com.technologies.mobile.free_exchange_admin.adapters.ViewPagerAdapter;
import com.technologies.mobile.free_exchange_admin.fragments.ApprovedOffersFragment;
import com.technologies.mobile.free_exchange_admin.fragments.RejectedOffersFragment;
import com.technologies.mobile.free_exchange_admin.fragments.WaitingSiteOffersFragment;
import com.technologies.mobile.free_exchange_admin.fragments.WaitingVkOffersFragment;
import com.technologies.mobile.free_exchange_admin.rest.queries.VkChatFinder;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;

public class MainActivity extends AppCompatActivity{

    public static final String LOG_TAG = "mainActivity";

    public static final int OFFER_REQUEST = 200;

    public static final int LOGIN_REQUEST = 100;

    Toolbar toolbar;

    private TabLayout tabLayout;
    private ViewPager viewPager;

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

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WaitingSiteOffersFragment(), "Сайт\nОжидание");
        adapter.addFragment(new WaitingVkOffersFragment(), "ВК\nОжидание");
        adapter.addFragment(new RejectedOffersFragment(), "\nОтклонено");
        adapter.addFragment(new ApprovedOffersFragment(), "\nОдобрено");
        viewPager.setAdapter(adapter);
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

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                Log.e(LOG_TAG,"CHAT ID BEFORE REMOVING = " + sp.getInt(VkChatFinder.REJECT_CHAT_ID_PREFERENCES,-1));
                sp.edit().remove(VkChatFinder.REJECT_CHAT_ID_PREFERENCES).apply();
                Log.e(LOG_TAG,"CHAT ID AFTER REMOVING = " + sp.getInt(VkChatFinder.REJECT_CHAT_ID_PREFERENCES,-1));

                login();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST) {
            initViews();
        }
    }
}

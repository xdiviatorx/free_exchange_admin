package com.technologies.mobile.free_exchange_admin.activities;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.technologies.mobile.free_exchange_admin.R;
import com.technologies.mobile.free_exchange_admin.adapters.RecyclerPostPhotosAdapter;
import com.technologies.mobile.free_exchange_admin.callbacks.ApproverCallback;
import com.technologies.mobile.free_exchange_admin.callbacks.MyRecyclerItemTouchCallback;
import com.technologies.mobile.free_exchange_admin.callbacks.OnListItemsChangeListener;
import com.technologies.mobile.free_exchange_admin.rest.model.Check;
import com.technologies.mobile.free_exchange_admin.rest.queries.Approver;
import com.technologies.mobile.free_exchange_admin.rest.queries.VkApprover;
import com.technologies.mobile.free_exchange_admin.views.ApproveCheckboxesView;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.methods.VKApiPhotos;
import com.vk.sdk.api.methods.VKApiWall;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiPost;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditOfferVkActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,
        TextWatcher, OnListItemsChangeListener, View.OnClickListener, ApproverCallback{

    private static final String LOG_TAG = "offerActivity";

    public static final int RESULT_CANCEL = 111;
    public static final int RESULT_APPROVED = 222;

    public static final String VK_POST_EXTRA = "VK_POST_EXTRA";
    public static final String VK_PHOTO_ATTACHMENTS_EXTRA = "VK_PHOTO_ATTACHMENTS_EXTRA";

    CheckBox mCbIsDeferred;
    LinearLayout mLlDateAndTimePickers;

    Toolbar mToolbar;

    TimePicker mTimePicker;
    DatePicker mDatePicker;

    Button mBCheck;
    EditText mEtText;

    RecyclerPostPhotosAdapter mRecyclerPostPhotosAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    Approver approver;

    boolean mIsChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_offer);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initViews();
        initToolbar();
    }

    protected void initViews(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCbIsDeferred = (CheckBox) findViewById(R.id.cbIsDeferred);
        mLlDateAndTimePickers = (LinearLayout) findViewById(R.id.llDateAndTimePickers);

        mCbIsDeferred.setOnCheckedChangeListener(this);

        mTimePicker = (TimePicker) findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true);

        mDatePicker = (DatePicker) findViewById(R.id.datePicker);

        mBCheck = (Button) findViewById(R.id.bCheck);
        mBCheck.setOnClickListener(this);

        mEtText = (EditText) findViewById(R.id.etText);
        initEditText();
        mEtText.addTextChangedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvPhotos);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerPostPhotosAdapter = new RecyclerPostPhotosAdapter(this);
        initAdapter();
        mRecyclerView.setAdapter(mRecyclerPostPhotosAdapter);
        ItemTouchHelper.Callback callback = new MyRecyclerItemTouchCallback(mRecyclerPostPhotosAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        mRecyclerPostPhotosAdapter.setOnListItemsChangeListener(this);

    }

    protected void initEditText(){
        VKApiPost mVkApiPost = getIntent().getParcelableExtra(VK_POST_EXTRA);
        mEtText.setText(mVkApiPost.text);
    }

    protected void initAdapter(){
        ArrayList<VKApiPhoto> mVkApiPhotos = getIntent().getParcelableArrayListExtra(VK_PHOTO_ATTACHMENTS_EXTRA);
        mRecyclerPostPhotosAdapter.addPhotos(mVkApiPhotos);
    }

    protected void initToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                setResult(RESULT_CANCEL);
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if( b ){
            mLlDateAndTimePickers.setVisibility(View.VISIBLE);
        }else{
            mLlDateAndTimePickers.setVisibility(View.GONE);
        }
    }

    protected void setChanged(){
        if( !mIsChanged ) {
            mIsChanged = true;
            mBCheck.setText(R.string.save_and_check);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        setChanged();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onListItemsChanged() {
        setChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bCheck:{
                approver = new VkApprover((VKApiPost)getIntent().getParcelableExtra(VK_POST_EXTRA),
                        mEtText.getText().toString(),mRecyclerPostPhotosAdapter.getData());
                approver.setApproverCallback(this);
                approver.approveRequest(mIsChanged);
                break;
            }
        }
    }

    @Override
    public void showDialog(Check check) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        final View view = ApproveCheckboxesView.getCheckboxes(check,this);
        builder.setView(view);
        builder.setTitle(R.string.choose_publish_place);
        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean vk = ((CheckBox) view.findViewById(R.id.cbVk)).isChecked();
                boolean site = ((CheckBox) view.findViewById(R.id.cbSite)).isChecked();
                boolean telegramm = ((CheckBox) view.findViewById(R.id.cbTelegramm)).isChecked();
                if( mCbIsDeferred.isChecked() ){
                    int year = mDatePicker.getYear();
                    int month = mDatePicker.getMonth();
                    int dayOfMonth = mDatePicker.getDayOfMonth();
                    int hourOfDay;
                    int minutesOfHour;
                    if (Build.VERSION.SDK_INT < 23 ) {
                        hourOfDay = mTimePicker.getCurrentHour();
                        minutesOfHour = mTimePicker.getCurrentMinute();
                    }else{
                        hourOfDay = mTimePicker.getHour();
                        minutesOfHour = mTimePicker.getMinute();
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year,month,dayOfMonth,hourOfDay,minutesOfHour);
                    long unixTime = calendar.getTimeInMillis()/1000;
                    Log.e(LOG_TAG,"UNIXTIME = " + unixTime);
                    approver.publish(vk, unixTime, site, telegramm);
                }else {
                    approver.publish(vk, site, telegramm);
                }
                setResult(RESULT_APPROVED);
                finish();
            }
        });
        builder.create().show();
    }
}

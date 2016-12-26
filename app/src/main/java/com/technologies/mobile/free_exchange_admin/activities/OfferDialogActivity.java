package com.technologies.mobile.free_exchange_admin.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.technologies.mobile.free_exchange_admin.R;

public class OfferDialogActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String LOG_TAG = "OfferDialog";

    public static final String OFFER = "OFFER";

    public static final int RESULT_DELETED = 137;

    EditText mEtRejectMessage;
    Button mBYes;
    Button mBNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_dialog);

        initViews();
        initPost();
    }

    protected void initViews(){
        mEtRejectMessage = (EditText) findViewById(R.id.etRejectMessage);
        mBYes = (Button) findViewById(R.id.bYes);
        mBYes.setOnClickListener(this);
        mBNo = (Button) findViewById(R.id.bNo);
        mBNo.setOnClickListener(this);
    }

    protected void initPost(){

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bYes:{
                query();
                break;
            }
            case R.id.bNo:{
                finish();
                break;
            }
        }
    }

    protected void query(){

    }
}

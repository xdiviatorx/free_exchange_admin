package com.technologies.mobile.free_exchange_admin.callbacks;

/**
 * Created by diviator on 02.11.2016.
 */

public interface OnButtonClickCallback {

    class ButtonType {public static final int ACCEPT = 1; public static final int REJECT = 0;}

    void onButtonClick(int type, int pos);

}

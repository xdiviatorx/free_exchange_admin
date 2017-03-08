package com.technologies.mobile.free_exchange_admin.rest.queries;

import com.technologies.mobile.free_exchange_admin.callbacks.ApproverCallback;
import com.technologies.mobile.free_exchange_admin.rest.model.Check;

import org.json.JSONArray;

/**
 * Created by diviator on 26.11.2016.
 */

public abstract class Approver {

    ApproverCallback mApproverCallback;

    boolean vk;
    boolean site;
    boolean telegramm;

    public void approveRequest(boolean isChanged){
        if( isChanged ){
            editAndCheck();
        }else{
            check();
        }
    }

    public void setApproverCallback(ApproverCallback approverCallback) {
        this.mApproverCallback = approverCallback;
    }

    public void publish(boolean vk, boolean site, boolean telegramm){
        this.vk = vk;
        this.site = site;
        this.telegramm = telegramm;

        if( vk ){
            publishVk();
        }

        siteSynchronization();

        if( !vk && !site ){
            noPublishing();
        }
    }

    public void publish(boolean vk, long unixTime, boolean site, boolean telegramm){
        this.vk = vk;
        this.site = site;
        this.telegramm = telegramm;

        if( vk ){
            publishVk(unixTime);
        }

        siteSynchronization();

        if( !vk && !site ){
            noPublishing();
        }
    }

    protected abstract void check();

    protected abstract void editAndCheck();

    protected void showApproveDialog(Check check){
        if( mApproverCallback != null ){
            mApproverCallback.showDialog(check);
        }
    }

    protected abstract void publishVk();

    protected abstract void publishVk(long unixTime);

    protected abstract void siteSynchronization();

    protected abstract void noPublishing();

    protected JSONArray createPublishTo(){
        JSONArray res = new JSONArray();
        if( vk ){
            res.put("vk");
        }
        if( site ){
            res.put("site");
        }
        if( telegramm ){
            res.put("telegramm");
        }
        return res;
    }

    protected String createVkLinks(){
        return "Еще больше обменов на нашем сайте и в мобильном приложении\nobmendar.ru\nhttps://play.google.com/store/apps/details?id=com.technologies.mobile.free_exchange&rdid=com.technologies.mobile.free_exchange&pli=1";
    }

}

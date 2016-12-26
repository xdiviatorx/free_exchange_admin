package com.technologies.mobile.free_exchange_admin.views;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.technologies.mobile.free_exchange_admin.R;
import com.technologies.mobile.free_exchange_admin.rest.model.Check;

/**
 * Created by diviator on 26.11.2016.
 */

public class ApproveCheckboxesView {

    public static View getCheckboxes(Check check, Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_checkboxes,null,false);

        if( !check.getCanPublishVk().isCan() ){
            view.findViewById(R.id.cbVk).setEnabled(false);
            ((TextView)view.findViewById(R.id.tvVkMessage)).setTextColor(Color.RED);
        }
        ((TextView)view.findViewById(R.id.tvVkMessage)).setText(constructMessage(check.getCanPublishVk().getWarnings(),check.getCanPublishVk().getErrors()));
        if( !check.getCanPublishSite().isCan() ){
            view.findViewById(R.id.cbSite).setEnabled(false);
            ((TextView)view.findViewById(R.id.tvSiteMessage)).setTextColor(Color.RED);
        }
        ((TextView)view.findViewById(R.id.tvSiteMessage)).setText(constructMessage(check.getCanPublishSite().getWarnings(),check.getCanPublishSite().getErrors()));
        if( !check.getCanPublishTelegramm().isCan() ){
            view.findViewById(R.id.cbTelegramm).setEnabled(false);
            ((TextView)view.findViewById(R.id.tvTelegrammMessage)).setTextColor(Color.RED);
        }
        ((TextView)view.findViewById(R.id.tvTelegrammMessage)).setText(constructMessage(check.getCanPublishTelegramm().getWarnings(),check.getCanPublishTelegramm().getErrors()));

        return view;
    }

    private static String constructMessage(String[] warn, String[] err){
        String res = "";
        if( warn != null && warn.length != 0 ){
            res+="Предупреждения: ";
            for( int i = 0; i < warn.length; i++ ){
                if( i != 0 ){
                    res+=", ";
                }
                res+=warn[i];
            }
            res+="\n";
        }
        if( err != null && err.length != 0 ){
            res+="Ошибки: ";
            for( int i = 0; i < err.length; i++ ){
                if( i != 0 ){
                    res+=", ";
                }
                res+=err[i];
            }
            res+="\n";
        }
        return res;
    }

}

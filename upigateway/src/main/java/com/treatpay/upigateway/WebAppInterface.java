package com.treatpay.upigateway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.util.List;

public class WebAppInterface {
    Context mContext;
    //replace with your UPI handle
    String payeeAddress = Global.upi_id;
    String payeeName = "AMPM365 Online Services Private Limited";
    String transactionNote = "Wallet Topup";
    String amount = Global.amount.toString();
    String currencyUnit = "INR";

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
    @JavascriptInterface
    public String upiHandlers(){
        Uri uri = Uri.parse("upi://pay?pa="+payeeAddress+"&pn="+payeeName+"&tn="+transactionNote+
                "&am="+amount+"&cu="+currencyUnit);
        Log.d("Apurav UPI deeplink", "onClick: uri: "+uri);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        List<ResolveInfo> activities =  mContext.getPackageManager().queryIntentActivities(intent,0);
        StringBuilder apps = new StringBuilder();
        boolean first = true;
        Log.d("Test",activities.toString());
        for (ResolveInfo act : activities) {
            ActivityInfo ai = act.activityInfo;
            Log.d("###",""+" :: "+ai.applicationInfo.packageName+" | "+ai.name);
            if(first){
                apps.append(ai.applicationInfo.packageName+"|"+ai.name);
                first=!first;
            }else{
                apps.append(",").append(ai.applicationInfo.packageName+"|"+ai.name);

            }
        }
        Log.d("Apps","ABC");
        return  apps.toString();
    }

    @JavascriptInterface
    public void openUPIHandler(String upiPackage,String activity){
//        Uri uri = Uri.parse("upi://pay?pa="+payeeAddress+"&pn="+payeeName+"&tn="+transactionNote+
//                "&am="+amount+"&cu="+currencyUnit+"&tr="+Global.order_id+"&mc=0000&mam=1");
        Uri uri = Uri.parse(Wrap.r2(payeeAddress,payeeName,transactionNote,amount,Global.order_id));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setClassName(upiPackage,activity);
        ((Activity) mContext).startActivityForResult(intent,23);

    }
}

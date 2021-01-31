package com.treatpay.upigateway;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class UPIGateway extends AppCompatActivity {

    String resp = "";
    private WebView mWebView;
    URL url;
    Intent intent;
    String amount,email,e1="",iv="",amt,deviceId,response;
    MCrypt mcrypt;
    HttpsURLConnection con;
    JSONObject jsonObject;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_webview);
        mWebView = findViewById(R.id.activity_gateway);
        progress = findViewById(R.id.progress);
        Intent intent = getIntent();
        amount=intent.getStringExtra("amount");
        Global.amount=Double.parseDouble(amount);
        Global.timestamp=intent.getStringExtra("timestamp");
        Global.api_key=intent.getStringExtra("api_key");
        Global.salt=intent.getStringExtra("salt");
        Global.initiator_id=intent.getStringExtra("initiator_id");
        Global.aid=intent.getStringExtra("aid");
        Global.api_key=intent.getStringExtra("api_key");
        deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        mWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        if(amount.equals("") || Global.amount<1 || Global.amount>5000) {
            showToastMessage("Invalid Amount");
        } else {
            amt=Global.amount.toString();
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            e1=amt+"#"+Global.initiator_id+"#"+Global.timestamp+"#"+Global.api_key+"#"+Global.aid+"#"+Global.salt+"#"+deviceId;
            int len=e1.length();
            Random random=new Random();
            iv=Wrap.r1(16);
            mcrypt = new MCrypt(iv,Wrap.t6());
            int index=random.nextInt(len-5)+5;
            try {
                e1=mcrypt.bytesToHex( mcrypt.encrypt(e1) );
                e1=Base64.encodeToString(e1.getBytes(),Base64.DEFAULT);
                e1=mcrypt.bytesToHex( mcrypt.encrypt(e1) );
                e1=e1+Wrap.t8()+String.valueOf(Wrap.enc(index));
                String a,b;
                a=e1.substring(0,index);
                b=e1.substring(index);
                e1=a+iv+b;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                url=new URL(Wrap.t9());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            new GenerateOrderID().execute();
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.copyBackForwardList().getCurrentIndex() > 0) {
            if(mWebView.getUrl().contains(Wrap.t4())) {
                this.finish();
            } else {
                mWebView.goBack();
            }
        }
        else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 23) {
            String msg="Unknown Error";
            if(data != null)
            {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    for (String key : bundle.keySet()) {
                        Log.d("abab", key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
                    }
                }
                String bank_ref="";
                if(!Global.app.equals("in.org.npci.upiapp")) {
                    if(!data.getStringExtra("Status").equals("FAILURE"))
                    {
                        resp=data.getStringExtra("response");
                        response=resp;
                        String[] resps=resp.split("&");

                        for(int i=0;i<resps.length;i++)
                        {
                            String[] x=resps[i].split("=");
                            if(x[0].equals("Status"))
                            {
                                msg=x[1];
                            }
                            if(x[0].equals("txnId"))
                            {
                                bank_ref=x[1];
                            }
                        }
                    }
                    else
                    {
                        msg="FAILURE";
                        try {
                            resp=data.getStringExtra("response");
                            response=resp;
                            String[] resps=resp.split("&");

                            for(int i=0;i<resps.length;i++)
                            {
                                String[] x=resps[i].split("=");
                                if(x[0].equals("Status"))
                                {
//                                msg=x[1];
                                }
                                if(x[0].equals("txnId"))
                                {
                                    bank_ref=x[1];
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        resp=data.getStringExtra("response");
                        response=resp;
                        String[] resps=resp.split("&");

                        for(int i=0;i<resps.length;i++)
                        {
                            String[] x=resps[i].split("=");
                            if(x[0].equals("Status"))
                            {
//                                msg=x[1];
                            }
                            if(x[0].equals("txnId"))
                            {
                                bank_ref=x[1];
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    url=new URL(Wrap.t7());

                    Long tsLong = System.currentTimeMillis()/1000;
                    String ts = tsLong.toString();
                    e1=Global.order_id+"#"+msg+"#"+bank_ref+"#"+Global.aid+"#"+Global.timestamp;
                    int len=e1.length();
                    Random random=new Random();
                    iv=Wrap.r1(16);
                    mcrypt = new MCrypt(iv,Wrap.t6());
                    int index=random.nextInt(len-5)+5;
                    try {
                        e1=mcrypt.bytesToHex( mcrypt.encrypt(e1) );
                        e1= Base64.encodeToString(e1.getBytes(),Base64.DEFAULT);
                        e1=mcrypt.bytesToHex( mcrypt.encrypt(e1) );
                        Integer integer=Wrap.enc(index);
                        e1=e1+Wrap.t8()+integer.toString();
                        String a,b;
                        a=e1.substring(0,index);
                        b=e1.substring(index);
                        e1=a+iv+b;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            new submitResponse().execute();
        }
    }
    public void showToast(String toast) {
        Toast.makeText(UPIGateway.this, toast, Toast.LENGTH_LONG).show();
    }

    class GenerateOrderID extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        String server_response;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }
        protected String doInBackground(String... args) {
            try {
                con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                ContentValues params = new ContentValues();
                params.put("e1", e1);
                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getFormData(params));
                writer.flush();
                writer.close();
                os.close();

                con.connect();
                int responseCode=con.getResponseCode();
                if(responseCode == HttpsURLConnection.HTTP_OK){
                    server_response = readStream(con.getInputStream());
                    server_response = new String(mcrypt.decrypt(server_response));
                    server_response = new String(Base64.decode(server_response,Base64.DEFAULT));
                    server_response = new String(mcrypt.decrypt(server_response));
                    jsonObject=new JSONObject(server_response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String arg) {
            progress.setVisibility(View.GONE);
            try {
                if (jsonObject.getString("status").equals("1")) {
                    String order_id=jsonObject.getString("order_id");
                    Global.order_id=order_id;
                    Global.upi_id=jsonObject.getString("upi_id");
                    mWebView.addJavascriptInterface(new WebAppInterface(UPIGateway.this), "ApuSDK");
                    mWebView.loadUrl(Wrap.t5());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class submitResponse extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        String server_response;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }
        protected String doInBackground(String... args) {
            try {
                con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                ContentValues params = new ContentValues();
                params.put("e1", e1);
                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getFormData(params));
                writer.flush();
                writer.close();
                os.close();

                con.connect();
                int responseCode=con.getResponseCode();
                if(responseCode == HttpsURLConnection.HTTP_OK){
                    server_response = readStream(con.getInputStream());
                    server_response = new String(mcrypt.decrypt(server_response));
                    server_response = new String(Base64.decode(server_response,Base64.DEFAULT));
                    server_response = new String(mcrypt.decrypt(server_response));
                    jsonObject=new JSONObject(server_response);
                    Log.d("server_response",server_response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String arg) {
            progress.setVisibility(View.GONE);
            try {
                if (jsonObject.getString("status").equals("1")) {
                    mWebView.loadUrl(Wrap.t4()+resp+"&amount="+amount);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent resp = new Intent();
                            resp.putExtra("response",response);
                            setResult(RESULT_OK, resp);
                            finish();
                        }
                    }, 5000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(UPIGateway.this, message, Toast.LENGTH_LONG).show();
    }

    public String getFormData(ContentValues contentValues) throws UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> entry : contentValues.valueSet()) {
            if (first)
                first = false;
            else
                sb.append("&");

            sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
        }
        return sb.toString();
    }
}
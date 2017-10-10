package proj.excellaglobal;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewDatabase;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class MainActivity extends AppCompatActivity implements AdvancedWebView.Listener {
    ProgressDialog prDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private AdvancedWebView mWebView;
    private String tokenid ="", notificasn="";
RelativeLayout internet;

    String url="http://66.201.99.67/~dashboardexcella/?controller=AuthController&action=login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        internet=findViewById(R.id.internet);
        sharedPreferences=getSharedPreferences("dummydata",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        mWebView = (AdvancedWebView) findViewById(R.id.web);
        if(isNetworkStatusAvialable (getApplicationContext())) {
            internet.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
          //  Toast.makeText(getApplicationContext(), "internet avialable", Toast.LENGTH_SHORT).show();
            mWebView.setListener(this, this);
            mWebView.loadUrl(url);
            WebViewDatabase webViewDB = WebViewDatabase.getInstance(MainActivity.this);

            prDialog = new ProgressDialog(MainActivity.this);




            CookieSyncManager.createInstance(getBaseContext());
            CookieSyncManager.getInstance().startSync();
            CookieSyncManager.getInstance().sync();

            mWebView.setWebViewClient(new WebViewClient());
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.loadUrl("sendDataToAndroid(document.getElementById('form-username').value)");
            mWebView.loadUrl(url);




            //mWebView.addJavascriptInterface(new JSInterface(this), "Android"); //You will access this via Android.method(args);

            mWebView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    CookieSyncManager.getInstance().sync();
                }
            });

        } else {
            mWebView.setVisibility(View.GONE);
            internet.setVisibility(View.VISIBLE);
            //Toast.makeText(getApplicationContext(), "internet is not avialable", Toast.LENGTH_SHORT).show();

        }
            }
    public class ButtonClickJavascriptInterface {
        Context mContext;
        ButtonClickJavascriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void onButtonClick(String toast) {

            /*Log.d("dtttt",toast);
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();*/
        }
    }


    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }


    @Override
    public void onBackPressed() {
        if (!mWebView.onBackPressed()) { return; }
        // ...
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        prDialog.setMessage("Please wait ...");
        prDialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onPageFinished(final String url) {
if (prDialog.isShowing())
{
    prDialog.dismiss();
}
        if (this.url.equalsIgnoreCase(url)) return;
        // Available on KitKat and later

        mWebView.evaluateJavascript(
                "document.getElementsByClassName('alert alert-error').length",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String errorMessageCount) {
                        if ("0".equals(errorMessageCount)) {
                            if (prDialog.isShowing())
                            {
                                prDialog.dismiss();
                            }
                           /// Toast.makeText(MainActivity.this, "OKAY", Toast.LENGTH_SHORT).show();
                            ButtonClickJavascriptInterface myJavaScriptInterface = new ButtonClickJavascriptInterface(MainActivity.this);
                            mWebView.addJavascriptInterface(myJavaScriptInterface, "MyFunction");
                            mWebView.getSettings().setJavaScriptEnabled(true);

                            if (mWebView.getTitle()!=null && !mWebView.getTitle().isEmpty()){

                                if (mWebView.getTitle().contains("Dashboard")) {
                                    final String[] words = mWebView.getTitle().split("\\s+");
                                    Log.d("sdfsfs", words[2]);


                                    editor.putString("user", mWebView.getTitle());

                                    FirebaseMessaging.getInstance().subscribeToTopic("test");
                                    FirebaseInstanceId.getInstance().getToken();
                                    Thread t=new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String Token=FirebaseInstanceId.getInstance().getToken();
                                            if (Token!=null){
                                                Log.d("thisismytoken",Token);
                                                editor.putString("Token",Token);
                                                editor.commit();
                                                StrictMode.ThreadPolicy threadPolicy=new StrictMode.ThreadPolicy.Builder().build();
                                                StrictMode.setThreadPolicy(threadPolicy);
                                                registerToken(Token,words[2]);

                                            }
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });t.start();
                                }

                            }


                            // all ok
                        } else {
                            if (prDialog.isShowing())
                            {
                                prDialog.dismiss();
                            }
                            /*internet.setVisibility(View.VISIBLE);
                            mWebView.setVisibility(View.GONE);*/
                            //Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            // error message detected
                        }
                    }
                }
        );

    }
    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
                if(netInfos.isConnected())
                    return true;
        }
        return false;
    }
    ///////////////////////getUsername
  /*  public class ButtonClickJavascriptInterface {
        Context mContext;
        ButtonClickJavascriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void onButtonClick(String toast) {


            //
           *//* SharedPreferences sharedPreferences=getSharedPreferences("dummydata",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("user",toast);
            editor.apply();
            FirebaseMessaging.getInstance().subscribeToTopic("test");
            FirebaseInstanceId.getInstance().getToken();*//*
           Log.d("hello",toast);
            Log.d("asdsa","sdfsf");


            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }*/
    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        //
        // Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
        internet.setVisibility(View.VISIBLE);
        mWebView.setVisibility(View.GONE);
        if (prDialog.isShowing())
        {
            prDialog.dismiss();
        }


    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) { }

    private void registerToken(String token,String user) {

        OkHttpClient client=new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("username",user)
                .add("token",token)
                .build();














































































































































































































































































































































































































































































































        okhttp3.Request request=new  okhttp3.Request.Builder()
                .url("http://66.201.99.67/~dashboardexcella/api/insert_token.php")
                .post(body)
                .build();
        try {
            client.newCall(request).execute();
            editor.clear();
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

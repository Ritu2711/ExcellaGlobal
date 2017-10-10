package proj.excellaglobal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by sai on 3/10/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        sharedPreferences=getSharedPreferences("dummydata",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        String token=FirebaseInstanceId.getInstance().getToken();

        Log.d("thisisfirebase","sdfsf");
        Log.d("thisisfirebase","Token");

        String user= sharedPreferences.getString("user","");
        Log.d("thisisfirebase",user+"-"+token);
       // registerToken(token,user);
    }

  /*  private void registerToken(String token,String user) {

        OkHttpClient client=new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("Token",token)
                .add("User",user)
                .build();

        okhttp3.Request request=new  okhttp3.Request.Builder()
                .url("http://androidandme.in/register.php")
                .post(body)
                .build();
        try {
            client.newCall(request).execute();
            editor.clear();
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }*/
}
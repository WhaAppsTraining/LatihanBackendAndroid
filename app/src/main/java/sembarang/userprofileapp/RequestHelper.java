package sembarang.userprofileapp;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class RequestHelper {
    private static final String TAG = "RequestHelper";
    private static MediaType JSON;
    private static OkHttpClient okHttpClient;

    public static String getData(String url) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = getOkHttpClient().newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                return null;
            } else {
                return responseBody.string();
            }
        } catch (@NonNull IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    public static void getData(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        getOkHttpClient().newCall(request).enqueue(callback);
    }

    public static String postData(String url, String json) {
        try {
            JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = getOkHttpClient().newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                return null;
            } else {
                return responseBody.string();
            }
        } catch (@NonNull IOException ie) {
            Log.e(TAG, ie.getLocalizedMessage());
        }
        //Failed Processing Post Data to Server
        return null;
    }

    public static String postData(String url, RequestBody requestBody) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = getOkHttpClient().newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                return null;
            } else {
                return responseBody.string();
            }
        } catch (@NonNull IOException ie) {
            Log.e(TAG, ie.getLocalizedMessage());
        }
        //Failed Processing Post Data to Server
        return null;
    }

    public static void postData(String url, String json, Callback callback) {
        JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();
        getOkHttpClient().newCall(request).enqueue(callback);
    }

    private static void addLoggingInterceptor(OkHttpClient.Builder builder) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
    }

    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            addLoggingInterceptor(okHttpClientBuilder);
            okHttpClient = okHttpClientBuilder.build();
        }
        return okHttpClient;
    }
}

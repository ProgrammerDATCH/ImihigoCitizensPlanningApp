package com.imihigocizitensplanning.app.UsefullClasses;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class ApiDataFetcher {

    private final OkHttpClient client = new OkHttpClient();

    public void fetchDataFromApi(String apiUrl, final DataFetchCallback callback) {
        Request request = new Request.Builder()
                .url(apiUrl)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Handle the response and pass it to the callback
                    String responseData = response.body().string();
                    callback.onSuccess(responseData);
                } else {
                    // Handle the error and pass it to the callback
                    callback.onError(new Exception("Request failed with code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network request failure and pass it to the callback
                callback.onError(e);
            }
        });
    }

    public interface DataFetchCallback {
        void onSuccess(String data);
        void onError(Exception e);
    }
}

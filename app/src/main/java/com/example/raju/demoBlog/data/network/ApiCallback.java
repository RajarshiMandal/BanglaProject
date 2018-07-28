package com.example.raju.demoBlog.data.network;

import android.support.annotation.NonNull;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ApiCallback<T> implements Callback<T> {

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            String responseErrorBody = "";
            try {
                responseErrorBody = response.errorBody().string();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
            onError(call, response.code() + " " + responseErrorBody);
        }

    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        onError(call, t.getMessage());
    }

    protected abstract void onSuccess(T responseBody);

    protected abstract void onError(Call<T> call, String errorMessage);

}

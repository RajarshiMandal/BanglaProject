package com.example.raju.demoBlog.data.network;

import android.support.annotation.NonNull;

import retrofit2.Call;

public class NetworkState {

    public static final NetworkState LOADING = new NetworkState(Status.LOADING);
    public static final NetworkState SUCCESS = new NetworkState(Status.SUCCESS);
    @NonNull
    private Status status;
    private String message;

    public NetworkState(@NonNull Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public NetworkState(@NonNull Status status) {
        this.status = status;
    }

    public static NetworkState error(String message) {
        return new NetworkState(Status.ERROR, message);
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

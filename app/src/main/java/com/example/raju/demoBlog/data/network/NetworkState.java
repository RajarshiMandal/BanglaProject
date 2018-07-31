package com.example.raju.demoBlog.data.network;

import android.support.annotation.NonNull;

public class NetworkState {

    @NonNull
    private Status status;
    private String message;

    public static final NetworkState LOADING = new NetworkState(Status.LOADING, "Loading");
    public static final NetworkState SUCCESS = new NetworkState(Status.SUCCESS);
    public static final NetworkState SAVING = new NetworkState(Status.SAVING, "Getting Ready!");
    public static final NetworkState RETRY = new NetworkState(Status.RETRY, "Retrying");

    private NetworkState(@NonNull Status status, String message) {
        this.status = status;
        this.message = message;
    }

    private NetworkState(@NonNull Status status) {
        this.status = status;
    }

    public static NetworkState Error(String message) {
        return new NetworkState(Status.ERROR, message);
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

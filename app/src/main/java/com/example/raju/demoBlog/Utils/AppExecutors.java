package com.example.raju.demoBlog.Utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum AppExecutors {

    APP_EXECUTORS(
            Executors.newSingleThreadExecutor(),
            Executors.newFixedThreadPool(3),
            new MainThreadExecutor()
    );

    private final ExecutorService diskIO;
    private final Executor mainThread;
    private final ExecutorService networkIO;

    AppExecutors(ExecutorService diskIO, ExecutorService networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    /**
     * For single threaded execution
     */
    public ExecutorService diskIO() {
        return diskIO;
    }

    /**
     * For multithreaded execution
     */
    public ExecutorService networkIO() {
        return networkIO;
    }

    /**
     * Threading for execution on main
     */
    public Executor mainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}

package ru.zenkin.commentapp;

import android.app.Application;

import androidx.room.Room;

import ru.zenkin.commentapp.database.AppDatabase;

public class App extends Application {

    public static App instance;
    private AppDatabase appDatabase;
    private static final String BASENAME = "comments";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, BASENAME)
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}

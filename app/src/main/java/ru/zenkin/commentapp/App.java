package ru.zenkin.commentapp;

import android.app.Application;

import androidx.room.Room;

import ru.zenkin.commentapp.repositories.db.AppDatabase;
import ru.zenkin.commentapp.di.ComponentsManager;

public class App extends Application {

    public static App instance;
    private AppDatabase appDatabase;
    private static final String BASENAME = "comments";
    private static ComponentsManager componentsManager;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponentManager();
        initDatabase();
        instance = this;
    }

    private void initDatabase() {
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, BASENAME)
                .build();
    }

    public static ComponentsManager getComponentsManager() {
        return componentsManager;
    }

    private void initComponentManager() {
        componentsManager = new ComponentsManager(this);
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}

package ru.zenkin.commentapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ru.zenkin.commentapp.database.dao.CommentDao;
import ru.zenkin.commentapp.model.Comment;

@Database(entities = {Comment.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context){
     if (instance == null){
         instance = Room.databaseBuilder(context.getApplicationContext(),
                 AppDatabase.class, "database").allowMainThreadQueries().build();
     }
     return instance;
    }

    public static void destroyInstance(){
     instance = null;
    }

    public abstract CommentDao commentDao();
}

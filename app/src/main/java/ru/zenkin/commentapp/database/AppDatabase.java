package ru.zenkin.commentapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ru.zenkin.commentapp.database.dao.CommentDao;
import ru.zenkin.commentapp.model.Comment;

@Database(entities = {Comment.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CommentDao commentDao();
}

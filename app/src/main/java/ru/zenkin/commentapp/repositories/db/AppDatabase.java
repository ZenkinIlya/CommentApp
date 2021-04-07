package ru.zenkin.commentapp.repositories.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.zenkin.commentapp.data.dao.CommentDao;
import ru.zenkin.commentapp.models.entities.Comment;

@Database(entities = {Comment.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CommentDao commentDao();
}

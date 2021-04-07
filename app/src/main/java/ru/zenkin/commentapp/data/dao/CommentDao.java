package ru.zenkin.commentapp.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import ru.zenkin.commentapp.models.entities.Comment;

@Dao
public interface CommentDao {

    @Query("SELECT * FROM comment")
    Flowable<List<Comment>> getAll();

    /*Поиск комментария по id*/
    @Query("SELECT * FROM comment WHERE id = :id")
    Single<Comment> getById(long id);

    @Insert
    void insert(Comment comment);

    @Update
    void update(Comment comment);

    @Query("DELETE FROM comment")
    void deleteAll();
}

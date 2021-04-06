package ru.zenkin.commentapp.network.services;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.zenkin.commentapp.model.Comment;

public interface CommentService {
    @GET("comments")
    public Observable<List<Comment>> getComments(@Query("postId") Long postId);
}

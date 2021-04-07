package ru.zenkin.commentapp.data.network;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.zenkin.commentapp.models.entities.Comment;

public interface CommentService {
    @GET("comments")
    public Observable<List<Comment>> getComments(@Query("postId") Long postId);
}

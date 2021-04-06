package ru.zenkin.commentapp.network.repositories;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import ru.zenkin.commentapp.model.Comment;
import ru.zenkin.commentapp.network.RetrofitFactory;

public class CommentRepository {

    public Observable<List<Comment>> getComments(Long postId){
        return RetrofitFactory.getCommentService().getComments(postId);

    }
}

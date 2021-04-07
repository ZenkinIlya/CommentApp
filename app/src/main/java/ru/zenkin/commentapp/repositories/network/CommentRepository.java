package ru.zenkin.commentapp.repositories.network;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import ru.zenkin.commentapp.App;
import ru.zenkin.commentapp.data.network.CommentService;
import ru.zenkin.commentapp.models.entities.Comment;

public class CommentRepository {

    @Inject
    CommentService commentService;

    public Observable<List<Comment>> getComments(Long postId){
        App.getComponentsManager().getNetworkComponent().inject(this);
        return commentService.getComments(postId);

    }
}

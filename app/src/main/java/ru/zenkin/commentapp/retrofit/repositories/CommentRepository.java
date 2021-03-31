package ru.zenkin.commentapp.retrofit.repositories;

import android.util.Log;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import ru.zenkin.commentapp.model.Comment;
import ru.zenkin.commentapp.retrofit.RetrofitFactory;

public class CommentRepository {

    private static final String TAG = "tgCommentRep";

    public Observable<List<Comment>> getComments(int postId){
        return RetrofitFactory.getCommentService().getComments(postId);

    }
}

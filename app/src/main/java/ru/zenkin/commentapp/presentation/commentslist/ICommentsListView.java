package ru.zenkin.commentapp.presentation.commentslist;

import java.util.ArrayList;

import ru.zenkin.commentapp.models.entities.Comment;

public interface ICommentsListView {
    void onShowLoading(boolean show);
    void onGetCommentListFromServer(ArrayList<Comment> comments);
    void onGetCommentListFromDatabase(ArrayList<Comment> comments);
    void onShowToast(String message);
    void onSearchCommentInDatabaseError(Comment comment);
    void onSearchCommentInDatabaseSuccess(Comment comment);
}

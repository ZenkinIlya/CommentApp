package ru.zenkin.commentapp.views;

import java.util.ArrayList;

import ru.zenkin.commentapp.model.Comment;

public interface IMainView {
    void onShowLoading(boolean show);
    void onGetCommentListFromServer(ArrayList<Comment> comments);
    void onGetCommentListFromDatabase(ArrayList<Comment> comments);
    void onShowToast(String message);
    void onSearchCommentInDatabaseError(Comment comment);
    void onSearchCommentInDatabaseSuccess(Comment comment);
}

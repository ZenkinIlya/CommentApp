package ru.zenkin.commentapp.presentation.commentslist;

import java.util.ArrayList;

import ru.zenkin.commentapp.models.entities.Comment;

public interface ICommentListPresenter {
    void getCommentListFromServer();
    void getCommentListFromDatabase();
    void searchCommentInDatabase(ArrayList<Comment> comments);
    void addCommentInDatabase(Comment comment);
    void updateCommentInDatabase(Comment comment);
    void deleteDatabase();
    void disposeAll();
}

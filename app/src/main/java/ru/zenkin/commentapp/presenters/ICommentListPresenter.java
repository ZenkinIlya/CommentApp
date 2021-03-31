package ru.zenkin.commentapp.presenters;

import java.util.ArrayList;

import ru.zenkin.commentapp.model.Comment;

public interface ICommentListPresenter {
    void getCommentListFromServer();
    void getCommentListFromDatabase();
    void searchCommentInDatabase(ArrayList<Comment> comments);
    void addCommentInDatabase(Comment comment);
    void updateCommentInDatabase(Comment comment);
}

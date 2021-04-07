package ru.zenkin.commentapp.presentation.commentslist;

import android.view.View;

import ru.zenkin.commentapp.models.entities.Comment;

public interface Callback {
    void onClickComment(Comment comment, View view);
}

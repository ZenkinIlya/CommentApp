package ru.zenkin.commentapp.adapters;

import android.view.View;

import ru.zenkin.commentapp.model.Comment;

public interface Callback {
    void onClickComment(Comment comment, View view);
}

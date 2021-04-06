package ru.zenkin.commentapp.utils;

import java.util.Comparator;

import ru.zenkin.commentapp.model.Comment;

public class CommentComparator implements Comparator<Comment> {
    @Override
    public int compare(Comment comment, Comment comment2) {
        if(comment.getTime() > comment2.getTime())
            return -1;
        else if(comment.getTime() < comment2.getTime())
            return 1;
        else
            return 0;
    }
}

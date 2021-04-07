package ru.zenkin.commentapp.di.components;

import javax.inject.Singleton;

import dagger.Component;
import ru.zenkin.commentapp.di.module.CommentModule;
import ru.zenkin.commentapp.di.module.NetworkModule;
import ru.zenkin.commentapp.presentation.commentslist.CommentsListActivity;
import ru.zenkin.commentapp.presentation.commentslist.ICommentsListView;

@Component(modules = CommentModule.class)
@Singleton
public interface CommentComponent {

    void inject(CommentsListActivity commentsListActivity);
}

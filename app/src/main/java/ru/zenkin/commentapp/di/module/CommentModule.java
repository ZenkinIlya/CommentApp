package ru.zenkin.commentapp.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.zenkin.commentapp.presentation.commentslist.CommentListPresenter;
import ru.zenkin.commentapp.presentation.commentslist.CommentsListActivity;
import ru.zenkin.commentapp.presentation.commentslist.ICommentsListView;

@Module
public class CommentModule {

    private CommentsListActivity commentsListActivity;

    public CommentModule(CommentsListActivity commentsListActivity) {
        this.commentsListActivity = commentsListActivity;
    }

    @Provides
    @Singleton
    CommentListPresenter provideCommentListPresenter(){
        return new CommentListPresenter(commentsListActivity);
    }
}

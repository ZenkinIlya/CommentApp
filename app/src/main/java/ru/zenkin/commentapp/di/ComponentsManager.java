package ru.zenkin.commentapp.di;

import android.content.Context;

import ru.zenkin.commentapp.di.components.CommentComponent;
import ru.zenkin.commentapp.di.components.DaggerCommentComponent;
import ru.zenkin.commentapp.di.components.DaggerNetworkComponent;
import ru.zenkin.commentapp.di.components.NetworkComponent;
import ru.zenkin.commentapp.di.module.CommentModule;
import ru.zenkin.commentapp.di.module.NetworkModule;
import ru.zenkin.commentapp.presentation.commentslist.CommentsListActivity;

/** Класс для поставки компонент (Создается в App)*/
public class ComponentsManager {

  private final Context context;

  private CommentComponent commentComponent;
  protected NetworkComponent networkComponent;

  public ComponentsManager(Context context) {
    this.context = context.getApplicationContext();
  }

  public CommentComponent getCommentComponent(CommentsListActivity commentsListActivity) {
    if (commentComponent == null) {
      commentComponent = DaggerCommentComponent.builder()
              .commentModule(new CommentModule(commentsListActivity))
              .build();
    }
    return commentComponent;
  }

  public NetworkComponent getNetworkComponent() {
    if (networkComponent == null) {
      networkComponent = DaggerNetworkComponent.builder()
              .build();
    }
    return networkComponent;
  }
}
package ru.zenkin.commentapp.presentation.commentslist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.zenkin.commentapp.App;
import ru.zenkin.commentapp.R;
import ru.zenkin.commentapp.databinding.ActivityMainBinding;
import ru.zenkin.commentapp.models.entities.Comment;
import ru.zenkin.commentapp.presentation.comment.CommentActivity;

public class CommentsListActivity extends AppCompatActivity implements ICommentsListView, Callback {

    private static final String TAG = "tgMainAct";
    private ActivityMainBinding bind;

    private CommentAdapter commentAdapter;

    @Inject
    CommentListPresenter commentListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getComponentsManager().getCommentComponent(this).inject(this);
        super.onCreate(savedInstanceState);
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        initAdapter();
    }

    private void initAdapter() {
        Log.i(TAG, "initAdapter: Инициализация адаптера");
        commentAdapter = new CommentAdapter();
        commentAdapter.setCallback(this);
        bind.mainCommentList.setHasFixedSize(true);
        bind.mainCommentList.setLayoutManager(new LinearLayoutManager(this));
        bind.mainCommentList.setAdapter(commentAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //При возобновлении работы Activity
        //Загружаем данные из БД
        commentListPresenter.getCommentListFromDatabase();
        //Обращаемся к серверу
        commentListPresenter.getCommentListFromServer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        commentListPresenter.disposeAll();
    }

    /*Получаем очередной список комментариев с сервера*/
    @Override
    public void onGetCommentListFromServer(ArrayList<Comment> comments) {
        //Попытка сохранить список комментариев в БД
        commentListPresenter.searchCommentInDatabase(comments);

    }

    /*Только в этом методе происходит обновление контента.
     * При любом изменении БД будет выполняться этот метод*/
    @Override
    public void onGetCommentListFromDatabase(ArrayList<Comment> comments) {
        Log.d(TAG, "onGetCommentListFromDatabase: Обновление списка комментариев");
        commentAdapter.setComments(comments);
    }

    @Override
    public void onShowToast(String message) {
        Toast.makeText(this,  message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowLoading(boolean show) {
        if (show){
            bind.mainProgressIndicator.setVisibility(View.VISIBLE);
        }else {
            bind.mainProgressIndicator.setVisibility(View.INVISIBLE);
        }
    }

    /*Вызывается в случае если такого комментария в БД нет*/
    @Override
    public void onSearchCommentInDatabaseError(Comment comment) {
        commentListPresenter.addCommentInDatabase(comment);
    }

    /*Вызывается в случае если необходимо обновить комментарий с таким же id*/
    @Override
    public void onSearchCommentInDatabaseSuccess(Comment comment) {
        commentListPresenter.updateCommentInDatabase(comment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_delete_database){
            commentListPresenter.deleteDatabase();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickComment(Comment comment, View view) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("comment", comment);
        startActivity(intent);
    }
}
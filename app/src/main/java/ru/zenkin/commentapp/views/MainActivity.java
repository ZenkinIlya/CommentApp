package ru.zenkin.commentapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import ru.zenkin.commentapp.adapters.CommentAdapter;
import ru.zenkin.commentapp.database.AppDatabase;
import ru.zenkin.commentapp.databinding.ActivityMainBinding;
import ru.zenkin.commentapp.model.Comment;
import ru.zenkin.commentapp.presenters.CommentListPresenter;

public class MainActivity extends AppCompatActivity implements IMainView {

    private ActivityMainBinding bind;

    private CommentAdapter commentAdapter;
    private CommentListPresenter commentListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        initAdapter();

        AppDatabase db = AppDatabase.getInstance(this);

        commentListPresenter = new CommentListPresenter(this, db);
        commentListPresenter.getCommentListFromDatabase();
        commentListPresenter.getCommentListFromServer();
    }

    private void initAdapter() {
        commentAdapter = new CommentAdapter();
        bind.mainCommentList.setHasFixedSize(true);
        bind.mainCommentList.setLayoutManager(new LinearLayoutManager(this));
        bind.mainCommentList.setAdapter(commentAdapter);
    }

    @Override
    public void onShowLoading(boolean show) {
//        bind.mainRecyclerView.setVisibility(show);
    }

    /*Получаем очередной список комментариев с сервера*/
    @Override
    public void onGetCommentListFromServer(ArrayList<Comment> comments) {
        //Попытка Сохранить список комментариев в БД
        commentListPresenter.searchCommentInDatabase(comments);

    }

    /*Только в этом методе происходит обновление контента.
     * При любом изменении БД будет выполняться этот метод*/
    @Override
    public void onGetCommentListFromDatabase(ArrayList<Comment> comments) {
        commentAdapter.setComments(comments);
    }

    @Override
    public void onShowToast(String message) {
        Toast.makeText(this,  message, Toast.LENGTH_SHORT).show();
    }
}
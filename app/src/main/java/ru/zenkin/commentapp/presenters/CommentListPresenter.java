package ru.zenkin.commentapp.presenters;

import android.util.Log;

import java.util.ArrayList;
import java.util.Observable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ru.zenkin.commentapp.database.AppDatabase;
import ru.zenkin.commentapp.model.Comment;
import ru.zenkin.commentapp.retrofit.repositories.CommentRepository;
import ru.zenkin.commentapp.views.IMainView;

public class CommentListPresenter implements ICommentListPresenter {

    private static final String TAG = "tgCommentListPres";
    private final IMainView iMainView;
    private AppDatabase db;
    private final CommentRepository commentRepository;

    public CommentListPresenter(IMainView iMainView, AppDatabase db) {
        this.iMainView = iMainView;
        this.db = db;
        commentRepository = new CommentRepository();
    }

    @Override
    public void getCommentListFromServer() {
        Log.d(TAG, "getCommentListFromServer: ");

        iMainView.onShowLoading(true);

        commentRepository.getComments(1)
                .subscribeOn(Schedulers.io())  //Запрос в io потоке
                .doOnNext(commentList -> Log.d(TAG, "getCommentListFromServer: commentList = " + commentList.size()))
                .observeOn(AndroidSchedulers.mainThread()) //Все остальное в main потоке
                .subscribe(commentList -> iMainView.onGetCommentListFromServer((ArrayList<Comment>) commentList),
                        error -> {
                            Log.e(TAG, "getCommentListFromServer: " + error.getLocalizedMessage());
                            iMainView.onShowLoading(false);
                            iMainView.onShowToast(error.getLocalizedMessage());
                        },
                        () -> {
                            Log.i(TAG, "getCommentListFromServer: Completed");
                            iMainView.onShowLoading(false);
                            iMainView.onShowToast("Комментарии загружены с сервера");
                        });
    }

    @Override
    public void getCommentListFromDatabase() {
        Log.d(TAG, "getCommentListFromDatabase: ");

        iMainView.onShowLoading(true);

        //Запрос будет выполнен не в UI потоке
        db.commentDao().getAll()
                .doOnNext(commentList -> Log.d(TAG, "getCommentListFromDatabase: commentList = " + commentList.size()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(commentList -> iMainView.onGetCommentListFromDatabase((ArrayList<Comment>) commentList),
                        error -> {
                            Log.e(TAG, "getCommentListFromDatabase: " + error.getLocalizedMessage());
                            iMainView.onShowLoading(false);
                            iMainView.onShowToast(error.getLocalizedMessage());
                        },
                        () -> {
                            Log.i(TAG, "getCommentListFromServer: Completed");
                            iMainView.onShowLoading(false);
                            iMainView.onShowToast("Комментарии загружены из базы");
                        });
    }

    @Override
    public void searchCommentInDatabase(ArrayList<Comment> comments) {
        Log.d(TAG, "searchCommentInDatabase: ");

        iMainView.onShowLoading(true);

        for (Comment comment: comments){
            db.commentDao().getById(comment.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            commentFromDb -> {
                                if (!comment.equals(commentFromDb)){
                                    iMainView.onShowToast("Комментарий обновлен");
                                    Log.d(TAG, "searchCommentInDatabase: Обновляем комментарий в БД");
                                    db.commentDao().update(comment);
                                }
                            },
                            error -> {
                                Log.d(TAG, "searchCommentInDatabase: Добавляем комментарий в БД");
                                db.commentDao().insert(comment);
                            });
        }
    }

    @Override
    public void addCommentInDatabase(Comment comment) {
//        Log.d(TAG, "addCommentListInDatabase: Добавляем комментарий в БД");
//        db.commentDao().insert(comment);
    }

    @Override
    public void updateCommentInDatabase(Comment comment) {
//        if (!comment.equals(commentFromDb)){
//            Log.d(TAG, "addCommentListInDatabase: Обновляем комментарий в БД");
//            db.commentDao().update(comment);
//        }
    }
}

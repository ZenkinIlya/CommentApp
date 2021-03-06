package ru.zenkin.commentapp.presentation.commentslist;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ru.zenkin.commentapp.App;
import ru.zenkin.commentapp.repositories.db.AppDatabase;
import ru.zenkin.commentapp.models.entities.Comment;
import ru.zenkin.commentapp.repositories.network.CommentRepository;

public class CommentListPresenter implements ICommentListPresenter {

    private static final String TAG = "tgCommentListPres";
    private final ICommentsListView iCommentsListView;
    private final AppDatabase db;
    private final CommentRepository commentRepository;
    private final CompositeDisposable compositeDisposable;

    public CommentListPresenter(ICommentsListView iCommentsListView) {
        this.iCommentsListView = iCommentsListView;
        this.db = App.getInstance().getAppDatabase();
        commentRepository = new CommentRepository();
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getCommentListFromServer() {
        Log.i(TAG, "getCommentListFromServer()");

        Disposable subscribe = Observable.interval(0,60, TimeUnit.SECONDS)
                .map(aLong -> getCount(aLong))  //Получаем postId
                .doOnNext(count -> Log.d(TAG, "getCommentListFromServer: count = " + count))
                .observeOn(AndroidSchedulers.mainThread())  //Показ значка загрузки в main потоке
                .doOnNext(aLong -> iCommentsListView.onShowLoading(true))
                .observeOn(Schedulers.io()) //Поход на сервер и вывод в лог выполняется в бекграунде
                .flatMap(integerObservable -> commentRepository.getComments(integerObservable))
                .flatMap(commentList -> Observable.fromIterable(commentList))
                .map(comment -> {
                    comment.setTime(getCurrentTime());
                    return comment;
                })
                .buffer(5)  //Собираю в кучу комменты. На выходе получаю Observable<List<Comment>>
                // При toList doOnNext не доступен
                .doOnNext(commentList -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Comment comment : commentList) {
                        stringBuilder.append(comment.getId()).append(" ");
                    }
                    Log.d(TAG, "getCommentListFromServer: id комментариев загруженных с сервера = " + stringBuilder.toString());

                })
                .subscribeOn(Schedulers.io())  //Влияет на первые три этапа (interval, map, doOnNext)
                .observeOn(AndroidSchedulers.mainThread()) //Методы Observer выполняются в main потоке
                .subscribe(
                        commentList -> {
                            iCommentsListView.onGetCommentListFromServer((ArrayList<Comment>) commentList);
                            iCommentsListView.onShowToast("Комментарии загружены с сервера");
                            iCommentsListView.onShowLoading(false);},
                        throwable -> {
                            Log.e(TAG, "getCommentListFromServer: " + throwable.getLocalizedMessage());
                            iCommentsListView.onShowToast(throwable.getLocalizedMessage());
                            iCommentsListView.onShowLoading(false); }
                            );

        compositeDisposable.add(subscribe);
    }

    private long getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    @Override
    public void disposeAll() {
        if (!compositeDisposable.isDisposed()){
            Log.d(TAG, "disposeAll: compositeDisposable.clear()");
            compositeDisposable.clear();
        }
    }

    //Вывод числа от 1 до 32
    private Long getCount(Long aLong) {
        long result;
        aLong++;
        result = aLong % 32;
        if (result == 0){
            result = 32;
        }
        return result;
    }

    @Override
    public void getCommentListFromDatabase() {
        Log.i(TAG, "getCommentListFromDatabase()");

        //Работает как слушатель, реагирует на любое изменение БД
        Disposable subscribe = db.commentDao().getAll()
                .doOnNext(commentList -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Comment comment : commentList) {
                        stringBuilder.append(comment.getId()).append(" ");
                    }
                    Log.d(TAG, "getCommentListFromDatabase: id комментариев загруженных из БД = " + stringBuilder.toString());

                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        commentList -> iCommentsListView.onGetCommentListFromDatabase((ArrayList<Comment>) commentList),
                        error -> {
                            Log.e(TAG, "getCommentListFromDatabase: " + error.getLocalizedMessage());
                            iCommentsListView.onShowToast(error.getLocalizedMessage());
                        },
                        () -> {
                            Log.i(TAG, "getCommentListFromServer: Completed");
                            iCommentsListView.onShowToast("Комментарии загружены из базы");
                        });

        compositeDisposable.add(subscribe);
    }

    @Override
    public void searchCommentInDatabase(ArrayList<Comment> comments) {
        Log.d(TAG, "searchCommentInDatabase()");

        /*Ищем комментарий в БД с таким же id как у комментариев из полученного списка*/
        for (Comment comment: comments){
            Disposable subscribe = db.commentDao().getById(comment.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            commentFromDb -> {
                                Log.d(TAG, "searchCommentInDatabase: Найден комментарий в БД с таким же id = " + comment.getId());
                                if (!comment.equals(commentFromDb)) {
                                    Log.i(TAG, "searchCommentInDatabase: Комментарий необходимо обновить");
                                    iCommentsListView.onSearchCommentInDatabaseSuccess(comment);
                                }else {
                                    Log.i(TAG, "searchCommentInDatabase: Комментарий не нуждается в обновлении");
                                }
                            },
                            error -> {
                                Log.d(TAG, "searchCommentInDatabase: В БД не найден комментарий с id = " + comment.getId());
                                iCommentsListView.onSearchCommentInDatabaseError(comment);
                            });

            compositeDisposable.add(subscribe);
        }
    }

    @Override
    public void addCommentInDatabase(Comment comment) {
        Log.d(TAG, "addCommentInDatabase()");

        Disposable subscribe = Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
                    Log.d(TAG, "addCommentInDatabase: Добавление комментария с id = " + comment.getId());
                    db.commentDao().insert(comment);
                    emitter.onNext(true);
                    emitter.onComplete();
                })
                .subscribeOn(Schedulers.io())
                .subscribe(aBoolean -> Log.d(TAG, "addCommentInDatabase: " + aBoolean),
                        throwable -> Log.d(TAG, "addCommentInDatabase: " + throwable.getLocalizedMessage()));

        compositeDisposable.add(subscribe);
    }

    @Override
    public void updateCommentInDatabase(Comment comment) {
        Log.d(TAG, "updateCommentInDatabase()");

        Disposable subscribe = Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            Log.d(TAG, "addCommentInDatabase: Обновление комментария с id = " + comment.getId());
            db.commentDao().update(comment);
            emitter.onNext(true);
            emitter.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .subscribe(
                        aBoolean -> Log.d(TAG, "updateCommentInDatabase: " + aBoolean),
                        throwable -> Log.d(TAG, "updateCommentInDatabase: " + throwable.getLocalizedMessage()));

        compositeDisposable.add(subscribe);
    }

    @Override
    public void deleteDatabase() {
        Log.d(TAG, "deleteDatabase()");

        Disposable subscribe = Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            db.commentDao().deleteAll();
            emitter.onNext(true);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .subscribe(aBoolean -> Log.d(TAG, "deleteDatabase: " + aBoolean),
                        throwable -> Log.d(TAG, "deleteDatabase: " + throwable.getLocalizedMessage()));

        compositeDisposable.add(subscribe);
    }
}

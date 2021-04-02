package ru.zenkin.commentapp.presenters;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ru.zenkin.commentapp.App;
import ru.zenkin.commentapp.database.AppDatabase;
import ru.zenkin.commentapp.model.Comment;
import ru.zenkin.commentapp.retrofit.repositories.CommentRepository;
import ru.zenkin.commentapp.views.IMainView;

public class CommentListPresenter implements ICommentListPresenter {

    private static final String TAG = "tgCommentListPres";
    private final IMainView iMainView;
    private AppDatabase db;
    private final CommentRepository commentRepository;

    public CommentListPresenter(IMainView iMainView) {
        this.iMainView = iMainView;
        this.db = App.getInstance().getAppDatabase();
        commentRepository = new CommentRepository();
    }

    @Override
    public void getCommentListFromServer() {
        Log.i(TAG, "getCommentListFromServer()");

        iMainView.onShowLoading(true);

        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            arrayList.add(i);
        }

        Observable.fromIterable(arrayList)
                .repeatWhen(completed -> completed.delay(10, TimeUnit.SECONDS))
                .doOnNext(integer -> Log.d(TAG, "getCommentListFromServer: integer = " +integer))
                .concatMap(integer -> commentRepository.getComments(integer))
                .subscribeOn(Schedulers.io())  //Запрос в io потоке
                .doOnNext(commentList -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Comment comment: commentList){
                        stringBuilder.append(comment.getId()).append(" ");
                    }
                    Log.d(TAG, "getCommentListFromServer: id комментариев загруженных с сервера = " + stringBuilder.toString());

                })
                .observeOn(AndroidSchedulers.mainThread()) //Все остальное в main потоке
                .subscribe(
                        commentList -> {
                            iMainView.onGetCommentListFromServer((ArrayList<Comment>) commentList);
                            },
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


/*        commentRepository.getComments(1)
                .repeatWhen(completed -> completed.delay(5, TimeUnit.SECONDS))
                .subscribeOn(Schedulers.io())  //Запрос в io потоке
                .doOnNext(commentList -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Comment comment: commentList){
                        stringBuilder.append(comment.getId()).append(" ");
                    }
                    Log.d(TAG, "getCommentListFromServer: id комментариев загруженных с сервера = " + stringBuilder.toString());

                })
                .observeOn(AndroidSchedulers.mainThread()) //Все остальное в main потоке
                .subscribe(
                        commentList -> {
                            iMainView.onGetCommentListFromServer((ArrayList<Comment>) commentList);
                            Log.d(TAG, "getCommentListFromServer: count = ");
                        },
                        error -> {
                            Log.e(TAG, "getCommentListFromServer: " + error.getLocalizedMessage());
                            iMainView.onShowLoading(false);
                            iMainView.onShowToast(error.getLocalizedMessage());
                        },
                        () -> {
                            Log.i(TAG, "getCommentListFromServer: Completed");
                            iMainView.onShowLoading(false);
                            iMainView.onShowToast("Комментарии загружены с сервера");
                        });*/
    }

    @Override
    public void getCommentListFromDatabase() {
        Log.i(TAG, "getCommentListFromDatabase()");

        iMainView.onShowLoading(true);

        //Запрос будет выполнен не в UI потоке
        //Работает как слушатель, реагирует на любое изменение БД
        db.commentDao().getAll()
                .doOnNext(commentList -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Comment comment: commentList){
                        stringBuilder.append(comment.getId()).append(" ");
                    }
                    Log.d(TAG, "getCommentListFromDatabase: id комментариев загруженных из БД = " + stringBuilder.toString());

                })
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
        Log.d(TAG, "searchCommentInDatabase()");

        iMainView.onShowLoading(true);

        /*Ищем комментарий в БД с таким же id как у комментариев из полученного списка*/
        for (Comment comment: comments){
            db.commentDao().getById(comment.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            commentFromDb -> {
                                Log.d(TAG, "searchCommentInDatabase: Найден комментарий в БД с таким же id = " + comment.getId());
                                if (!comment.equals(commentFromDb)){
                                    Log.i(TAG, "searchCommentInDatabase: Комментарий необходимо обновить");
                                    iMainView.onSearchCommentInDatabaseSuccess(comment);
                                }
                                iMainView.onShowLoading(false);
                            },
                            error -> {
                                Log.d(TAG, "searchCommentInDatabase: В БД не найден комментарий с id = " + comment.getId());
                                iMainView.onSearchCommentInDatabaseError(comment);
                                iMainView.onShowLoading(false);
                            });
        }
    }

    @Override
    public void addCommentInDatabase(Comment comment) {
        Log.d(TAG, "addCommentInDatabase()");

        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> emitter) throws Throwable {
                Log.d(TAG, "addCommentInDatabase: Добавление комментария с id = " + comment.getId());
                db.commentDao().insert(comment);
                emitter.onNext(true);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(aBoolean -> Log.d(TAG, "addCommentInDatabase: " +aBoolean),
                        throwable -> Log.d(TAG, "addCommentInDatabase: " +throwable.getLocalizedMessage()));
    }

    @Override
    public void updateCommentInDatabase(Comment comment) {
        Log.d(TAG, "updateCommentInDatabase()");

        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> emitter) throws Throwable {
                Log.d(TAG, "addCommentInDatabase: Обновление комментария с id = " + comment.getId());
                db.commentDao().update(comment);
                emitter.onNext(true);
//                emitter.onError(new Exception());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(
                        aBoolean -> {
                            Log.d(TAG, "updateCommentInDatabase: " +aBoolean);},
                        throwable -> Log.d(TAG, "updateCommentInDatabase: " +throwable.getLocalizedMessage()));

        /*Completable.fromAction(() -> db.commentDao().update(comment))
                .subscribeOn(Schedulers.io())
                .subscribe();*/
    }

    @Override
    public void deleteDatabase() {
        Log.d(TAG, "deleteDatabase()");

        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            db.commentDao().deleteAll();
            emitter.onNext(true);
//                emitter.onError(new Exception());
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .subscribe(aBoolean -> Log.d(TAG, "deleteDatabase: " +aBoolean),
                        throwable -> Log.d(TAG, "deleteDatabase: " +throwable.getLocalizedMessage()));
    }
}

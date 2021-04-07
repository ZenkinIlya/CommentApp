package ru.zenkin.commentapp.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.zenkin.commentapp.data.network.CommentService;

@Module
public class NetworkModule {

    private static final String BASE_URl = " http://jsonplaceholder.typicode.com/";

    @Provides
    @Singleton
    OkHttpClient getOkHttpClientInstance(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        //Показ тела запроса/ответа
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    Retrofit getRetrofitClient(OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    CommentService getCommentService(Retrofit retrofit) {
        return retrofit.create(CommentService.class);
    }
}

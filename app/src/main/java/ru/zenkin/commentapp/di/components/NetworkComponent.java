package ru.zenkin.commentapp.di.components;

import javax.inject.Singleton;

import dagger.Component;
import ru.zenkin.commentapp.di.module.NetworkModule;
import ru.zenkin.commentapp.repositories.network.CommentRepository;

@Component(modules = NetworkModule.class)
@Singleton
public interface NetworkComponent {

    void inject(CommentRepository commentRepository);
}

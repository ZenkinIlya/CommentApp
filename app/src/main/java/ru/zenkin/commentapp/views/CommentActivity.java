package ru.zenkin.commentapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ru.zenkin.commentapp.R;
import ru.zenkin.commentapp.databinding.ActivityCommentBinding;

public class CommentActivity extends AppCompatActivity {

    ActivityCommentBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());


    }
}
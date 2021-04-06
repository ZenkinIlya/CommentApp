package ru.zenkin.commentapp.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.zenkin.commentapp.R;
import ru.zenkin.commentapp.databinding.ActivityCommentBinding;
import ru.zenkin.commentapp.model.Comment;

public class CommentActivity extends AppCompatActivity {

    ActivityCommentBinding bind;
    private Comment comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        comment = new Comment(0, 0,"default", "default@mail.ru", "defaultBody");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            comment = (Comment) bundle.getSerializable("comment");
        }
        initView();
        initListeners();
    }

    private void initListeners() {
        bind.commentEmail.setOnClickListener(view -> {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{comment.getEmail()});
                    intent.setData(Uri.parse("mailto:"));
                    startActivity(intent);
                });
    }

    private void initView() {
        bind.commentPostId.setText(String.valueOf(comment.getPostId()));
        bind.commentId.setText(String.valueOf(comment.getId()));
        bind.commentName.setText(comment.getName());
        bind.commentEmail.setText(comment.getEmail());
        bind.commentBody.setText(comment.getBody());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
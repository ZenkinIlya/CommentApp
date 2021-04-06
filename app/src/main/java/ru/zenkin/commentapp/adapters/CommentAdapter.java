package ru.zenkin.commentapp.adapters;

import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import ru.zenkin.commentapp.databinding.CommentItemListBinding;
import ru.zenkin.commentapp.model.Comment;
import ru.zenkin.commentapp.utils.CommentComparator;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private final ArrayList<Comment> comments = new ArrayList<>();
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private final CommentComparator commentComparator = new CommentComparator();
    private Callback callback;

    public void setComments(ArrayList<Comment> inputComments) {
        comments.clear();
        comments.addAll(inputComments);
        Collections.sort(comments, commentComparator);
        notifyDataSetChanged();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CommentItemListBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CommentItemListBinding bind;

        public ViewHolder(@NonNull CommentItemListBinding commentItemListBinding){
            super(commentItemListBinding.getRoot());
            commentItemListBinding.getRoot().setOnClickListener(this);
            this.bind = commentItemListBinding;
        }

        public void bind(Comment comment){
            bind.messagePostId.setText(String.valueOf(comment.getPostId()));
            bind.messageName.setText(comment.getName());
            bind.messageTime.setText(simpleDateFormat.format(comment.getTime()));
        }

        @Override
        public void onClick(View view) {
            callback.onClickComment(comments.get(getAdapterPosition()), view);
        }
    }

}

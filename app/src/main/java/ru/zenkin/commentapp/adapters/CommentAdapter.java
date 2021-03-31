package ru.zenkin.commentapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ru.zenkin.commentapp.databinding.CommentItemListBinding;
import ru.zenkin.commentapp.model.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Comment> comments = new ArrayList<>();
    private String timeOfReceiveComments;

    public void setComments(ArrayList<Comment> inputComments) {
        comments.addAll(inputComments);
        setTimeOfReceiveComments();
        notifyDataSetChanged();
    }

    //Время получение комментариев
    private void setTimeOfReceiveComments(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        timeOfReceiveComments = simpleDateFormat.format(calendar.getTime());
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        CommentItemListBinding bind;

        public ViewHolder(@NonNull CommentItemListBinding commentItemListBinding) {
            super(commentItemListBinding.getRoot());
            this.bind = commentItemListBinding;
        }

        public void bind(Comment comment){
            bind.messagePostId.setText(String.valueOf(comment.getPostId()));
            bind.messageName.setText(comment.getName());
            bind.messageTime.setText(timeOfReceiveComments);
        }
    }
}

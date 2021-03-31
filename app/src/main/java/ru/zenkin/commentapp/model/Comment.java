package ru.zenkin.commentapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Comment {

    //PrimaryKey for Room
    @PrimaryKey(autoGenerate = true)
    private long idKey;

    //Время считывания комментария с сервера
    private long time;

    //Номер сообщения, к которому относится комментарий
    private int postId;

    //Идентификатор комментария
    private int id;

    //Имя пользователя
    private String name;

    //Адрес пользователя
    private String email;

    //Текст комментария
    private String body;

    public Comment(int postId, int id, String name, String email, String body) {
        this.postId = postId;
        this.id = id;
        this.name = name;
        this.email = email;
        this.body = body;
    }

    public long getIdKey() {
        return idKey;
    }

    public void setIdKey(long idKey) {
        this.idKey = idKey;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "idKey=" + idKey +
                ", time=" + time +
                ", postId=" + postId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return postId == comment.postId &&
                id == comment.id &&
                Objects.equals(name, comment.name) &&
                Objects.equals(email, comment.email) &&
                Objects.equals(body, comment.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, id, name, email, body);
    }
}

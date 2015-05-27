package fr.insalyon.telecom.chat.model;

import java.io.Serializable;

public class Post implements Serializable {

  private String author;
  private String message;

  public Post(String author, String message) {
    this.author = author;
    this.message = message;
  }

  public String getAuthor() {
    return author;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return "Post{" +
        "author='" + author + '\'' +
        ", message='" + message + '\'' +
        '}';
  }
}

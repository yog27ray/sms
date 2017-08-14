package com.msgque.play.listener;

public class ListListener {
  public interface Filter<T> {
    boolean check(T item);
  }

  public interface Duplicate<T> {
    boolean isDuplicate(T a, T b);
  }

  public interface Map<X, Y> {
    Y map(X item);
  }

  public interface Join<X> {
    String join(X item);
  }

  public interface Reduce<X> {
    float reduce(X item);
  }
}

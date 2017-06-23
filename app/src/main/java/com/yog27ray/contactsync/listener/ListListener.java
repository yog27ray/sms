package com.yog27ray.contactsync.listener;

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
}

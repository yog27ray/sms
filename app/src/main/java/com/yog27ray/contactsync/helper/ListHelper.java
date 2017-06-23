package com.yog27ray.contactsync.helper;

import com.yog27ray.contactsync.listener.ListListener;

import java.util.ArrayList;
import java.util.List;

public class ListHelper {

  public <T> List<T> filter(List<T> list, ListListener.Filter<T> listener) {
    List<T> newList = new ArrayList<>();
    for (T item: list) {
      if (listener.check(item)) newList.add(item);
    }
    return newList;
  }

  public <T> List<T> removeDuplicate(List<T> list, ListListener.Duplicate<T> listener) {
    List<T> newList = new ArrayList<>();
    boolean isNew;
    for (T a: list)  {
      isNew = true;
      for (T b: newList) {
        if (listener.isDuplicate(a, b)) {
          isNew = false;
          break;
        }
      }
      if (isNew) newList.add(a);
    }
    return newList;
  }

  public <X, Y> List<Y> map(List<X> list, ListListener.Map<X, Y> listener) {
    List<Y> newList = new ArrayList<>();
    for (X item: list) newList.add(listener.map(item));
    return newList;
  }
}

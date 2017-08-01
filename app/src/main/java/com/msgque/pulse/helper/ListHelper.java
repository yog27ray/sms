package com.msgque.pulse.helper;

import com.msgque.pulse.listener.ListListener;

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

  public<T> String join(List<T> list, ListListener.Join<T> listener) {
    String result = "";
    for (T item: list) result += listener.join(item) + ",";
    if (!result.isEmpty()) result = result.substring(0, result.length() - 1);
    return result;
  }

  public <T> float reduce(List<T> list, float initial, ListListener.Reduce<T> listener) {
    for (T item: list) initial += listener.reduce(item);
    return initial;
  }
}

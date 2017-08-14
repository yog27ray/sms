package com.msgque.play.listener;

import java.util.List;

public class AdapterListener {
  public interface SelectionChange<T> {
    void change(List<T> list);
  }
}

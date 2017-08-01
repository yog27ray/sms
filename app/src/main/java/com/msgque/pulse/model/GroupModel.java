package com.msgque.pulse.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

public class GroupModel extends BaseObservable {
  public int id;
  public int count;
  public String name;
  private boolean selected = true;

  @Bindable
  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
    notifyPropertyChanged(BR.selected);
  }
}

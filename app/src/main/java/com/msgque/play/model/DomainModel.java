package com.msgque.play.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

public class DomainModel extends BaseObservable {
  private String name;
  private String classkey;
  private String status;
  private String price;
  private Integer userId;
  private boolean existing;
  private boolean selected;

  @Bindable
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    notifyPropertyChanged(BR.name);
  }

  @Bindable
  public String getClasskey() {
    return classkey;
  }

  public void setClasskey(String classkey) {
    this.classkey = classkey;
    notifyPropertyChanged(BR.classkey);
  }

  @Bindable
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
    notifyPropertyChanged(BR.status);
  }

  @Bindable
  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
    notifyPropertyChanged(BR.price);
  }

  @Bindable
  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
    notifyPropertyChanged(BR.selected);
  }

  @Bindable
  public boolean isExisting() {
    return existing;
  }

  public void setExisting(boolean existing) {
    this.existing = existing;
    notifyPropertyChanged(BR.existing);
  }
}

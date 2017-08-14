package com.msgque.play.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.msgque.play.BR;

public class SmsModel extends BaseObservable{
  public String text;
  public String groupId;
  public String numbers;
  public String campaign;
  public String senderId;
  public int routeId = -1;

  @Bindable
  public String getSenderId() {
    return senderId;
  }

  public void setSenderId(String senderId) {
    this.senderId = senderId;
    notifyPropertyChanged(BR.senderId);
  }

  @Bindable
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
    notifyPropertyChanged(BR.text);
  }

  @Bindable
  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
    notifyPropertyChanged(BR.groupId);
  }

  @Bindable
  public String getNumbers() {
    return numbers;
  }

  public void setNumbers(String numbers) {
    this.numbers = numbers;
    notifyPropertyChanged(BR.numbers);
  }

  @Bindable
  public String getCampaign() {
    return campaign;
  }

  public void setCampaign(String campaign) {
    this.campaign = campaign;
    notifyPropertyChanged(BR.campaign);
  }

  @Bindable
  public int getRouteId() {
    return routeId;
  }

  public void setRouteId(int routeId) {
    this.routeId = routeId;
    notifyPropertyChanged(BR.routeId);
  }
}

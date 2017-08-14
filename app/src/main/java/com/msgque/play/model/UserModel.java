package com.msgque.play.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

public class UserModel extends BaseObservable {
  public Integer id;
  public String name;
  public Integer roleId;
  public String otp;
  public String email;
  private String mobile;
  public Integer admin;
  public Integer balance;
  private String companyName;
  private String companyAddress;
  private String supportName;
  private String supportMobile;
  private String supportEmail;

  public UserModel() {
  }

  public UserModel(int id) {
    this.id = id;
  }

  @Bindable
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    notifyPropertyChanged(BR.name);
  }

  @Bindable
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
    notifyPropertyChanged(BR.email);
  }

  @Bindable
  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
    notifyPropertyChanged(BR.mobile);
  }

  @Bindable
  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
    notifyPropertyChanged(BR.companyName);
  }

  @Bindable
  public String getCompanyAddress() {
    return companyAddress;
  }

  public void setCompanyAddress(String companyAddress) {
    this.companyAddress = companyAddress;
    notifyPropertyChanged(BR.companyAddress);
  }

  @Bindable
  public String getSupportName() {
    return supportName;
  }

  public void setSupportName(String supportName) {
    this.supportName = supportName;
    notifyPropertyChanged(BR.supportName);
  }

  @Bindable
  public String getSupportMobile() {
    return supportMobile;
  }

  public void setSupportMobile(String supportMobile) {
    this.supportMobile = supportMobile;
    notifyPropertyChanged(BR.supportMobile);
  }

  @Bindable
  public String getSupportEmail() {
    return supportEmail;
  }

  public void setSupportEmail(String supportEmail) {
    this.supportEmail = supportEmail;
    notifyPropertyChanged(BR.supportEmail);
  }
}

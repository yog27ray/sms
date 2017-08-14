package com.msgque.play.model;

import android.content.Intent;
import android.databinding.BaseObservable;

public class NavItem extends BaseObservable{
  public int id;
  public String name;
  public String link;
  public String icon;
  public String activeIcon;
  public boolean outsideapp;
  public boolean inApp;
  public Intent open;

  public NavItem() {
  }

  public NavItem(String name, Intent open) {
    inApp = true;
    this.name = name;
    this.open = open;
  }
}

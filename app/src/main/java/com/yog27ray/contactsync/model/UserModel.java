package com.yog27ray.contactsync.model;

public class UserModel {
  public Integer id;
  public String name;
  public int group_id;
  public String email_id;
  public int admin_flag = 0;
  public Integer admin;

  public UserModel() {
  }

  public UserModel(int id) {
    this.id = id;
  }
}

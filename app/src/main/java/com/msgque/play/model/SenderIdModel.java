package com.msgque.play.model;

public class SenderIdModel {
  private Integer id;
  private Integer senderIdStatusId;
  private String name;
  private UserModel CreatedBy;

  public Integer getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

package com.msgque.play.model;

import java.util.Calendar;

public class RouteModel {
  private int id;
  private String name;
  private Integer active;
  private Calendar createdAt;
  private Calendar deletedAt;
  private Calendar updatedAt;

  public int getId() {
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

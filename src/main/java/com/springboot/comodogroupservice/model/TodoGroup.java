package com.springboot.comodogroupservice.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TodoGroup {

  @Id
  @GeneratedValue
  private Long id;
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  private TodoUser todoUser;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TodoUser getTodoUser() {
    return todoUser;
  }

  public void setTodoUser(TodoUser todoUser) {
    this.todoUser = todoUser;
  }

  @Override
  public String toString() {
    return "TodoGroup [id=" + id + ", name=" + name + ", todoUser=" + todoUser + "]";
  }

}

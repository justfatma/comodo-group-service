package com.springboot.comodogroupservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.springboot.comodogroupservice.model.TodoGroup;
import com.springboot.comodogroupservice.model.TodoGroupIO;
import com.springboot.comodogroupservice.model.TodoUser;
import com.springboot.comodogroupservice.model.TodoUserIO;
import com.springboot.comodogroupservice.repository.TodoGroupRepository;


@Service
public class TodoGroupService {

  @Autowired
  private TodoGroupRepository todoGroupRepository;

  public TodoGroupIO getGroupById(Long groupId) {
    Optional<TodoGroup> groupOp = todoGroupRepository.findById(groupId);

    if (groupOp.isPresent()) {
      return fromEntityToModel(groupOp.get());
    } else {
      return null;
    }
  }


  public List<TodoGroupIO> getGroupList() {

    List<TodoGroupIO> groupIOList = new ArrayList<>();
    List<TodoGroup> groupList = todoGroupRepository.findAll();

    for (TodoGroup group : groupList) {
      groupIOList.add(fromEntityToModel(group));
    }

    if (groupIOList.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } else {
      return groupIOList;
    }
  }


  public TodoGroupIO saveGroup(TodoGroupIO model) {

    TodoGroup group = fromModelToEntity(model);
    TodoGroup savedGroup = todoGroupRepository.save(group);
    return fromEntityToModel(savedGroup);
  }


  public TodoGroupIO updateGroup(Long id, TodoGroupIO groupIO) {

    TodoGroup todoGroup = fromModelToEntity(groupIO);

    TodoGroup currentGroup =
        todoGroupRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
            "TodoGroupService/updateGroup : Group id not found. -> " + id));

    currentGroup.setName(todoGroup.getName());

    TodoGroup savedGroupTodo = todoGroupRepository.save(currentGroup);
    return fromEntityToModel(savedGroupTodo);

  }


  public Long deleteGroup(Long todoGroupId) {

    TodoGroup currentGroupTodo =
        todoGroupRepository.findById(todoGroupId).orElseThrow(() -> new ResourceNotFoundException(
            "TodoGroupService/deleteGroup : todoGroupId not found. -> " + todoGroupId));

    todoGroupRepository.delete(currentGroupTodo);
    return todoGroupId;
  }

  private TodoGroup fromModelToEntity(TodoGroupIO groupIO) {

    TodoGroup group = new TodoGroup();
    group.setId(groupIO.getId());
    group.setName(groupIO.getName());
    group.setTodoUser(fromModelToEntity(groupIO.getTodoUserIO()));

    return group;
  }

  private TodoGroupIO fromEntityToModel(TodoGroup group) {

    TodoGroupIO groupIO = new TodoGroupIO();
    groupIO.setId(group.getId());
    groupIO.setName(group.getName());
    groupIO.setTodoUserIO(fromEntityToModel(group.getTodoUser()));

    return groupIO;
  }

  private TodoUser fromModelToEntity(TodoUserIO userIO) {

    TodoUser user = new TodoUser();
    user.setId(userIO.getId());
    user.setName(userIO.getName());
    user.setEmail(userIO.getEmail());
    user.setPassword(userIO.getPassword());
    user.setSurname(userIO.getSurname());

    return user;
  }

  private TodoUserIO fromEntityToModel(TodoUser user) {

    TodoUserIO userIO = new TodoUserIO();
    userIO.setId(user.getId());
    userIO.setName(user.getName());
    userIO.setEmail(user.getEmail());
    userIO.setPassword(user.getPassword());
    userIO.setSurname(user.getSurname());

    return userIO;
  }

}

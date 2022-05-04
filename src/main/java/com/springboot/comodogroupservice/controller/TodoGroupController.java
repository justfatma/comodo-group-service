package com.springboot.comodogroupservice.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.springboot.comodogroupservice.model.TodoGroupIO;
import com.springboot.comodogroupservice.rabbitmq.GroupMessage;
import com.springboot.comodogroupservice.rabbitmq.MQConfig;
import com.springboot.comodogroupservice.service.TodoGroupService;

@RestController
public class TodoGroupController {

  @Autowired
  private TodoGroupService todoGroupService;

  @Autowired
  private RabbitTemplate template;

  @GetMapping("groups")
  public ResponseEntity<List<TodoGroupIO>> getGroupList() {

    return new ResponseEntity<>(todoGroupService.getGroupList(), HttpStatus.OK);
  }


  @GetMapping("groups/{groupId}")
  public ResponseEntity<TodoGroupIO> getGroupById(@PathVariable Long groupId) {

    return new ResponseEntity<>(todoGroupService.getGroupById(groupId), HttpStatus.OK);
  }

  @PostMapping("groups")
  public ResponseEntity<TodoGroupIO> saveGroup(@RequestBody TodoGroupIO todoGroupIO) {

    TodoGroupIO groupIO = todoGroupService.saveGroup(todoGroupIO);

    if (groupIO != null) {
      // ***************************************************//
      GroupMessage userMessage = new GroupMessage();
      userMessage.setTodoGroupId(groupIO.getId());
      userMessage.setTodoGroupName(groupIO.getName());
      userMessage.setTodoUserId(groupIO.getTodoUserIO().getId());
      userMessage.setGroupDeleted(false);
      publishMessage(userMessage);
      // ***************************************************//
      return new ResponseEntity<>(groupIO, HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
    }
  }


  @PutMapping("groups/{groupId}")
  public ResponseEntity<TodoGroupIO> updateGroup(@PathVariable Long groupId,
      @RequestBody TodoGroupIO todoGroupIO) {

    TodoGroupIO groupIO = todoGroupService.updateGroup(groupId, todoGroupIO);

    if (groupIO != null) {
      // ***************************************************//
      GroupMessage userMessage = new GroupMessage();
      userMessage.setTodoGroupId(groupIO.getId());
      userMessage.setTodoGroupName(groupIO.getName());
      userMessage.setTodoUserId(groupIO.getTodoUserIO().getId());
      userMessage.setGroupDeleted(false);
      publishMessage(userMessage);
      // ***************************************************//
      return new ResponseEntity<>(groupIO, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
  }


  @DeleteMapping("groups/{groupId}")
  public ResponseEntity<String> deleteGroup(@PathVariable Long groupId) {

    Long id = todoGroupService.deleteGroup(groupId);

    if (id != null) {
      // ***************************************************//
      GroupMessage userMessage = new GroupMessage();
      userMessage.setTodoGroupId(id);
      userMessage.setGroupDeleted(true);
      publishMessage(userMessage);
      // ***************************************************//
      return new ResponseEntity<>("Group deleted successfully! id: " + id, HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Group is not found! id: " + id, HttpStatus.NOT_FOUND);
    }

  }


  public void publishMessage(GroupMessage groupMessage) {

    groupMessage.setMessageId(UUID.randomUUID().toString());
    groupMessage.setMessageDate(new Date());
    template.convertAndSend(MQConfig.EXCHANGE, MQConfig.ROUTING_KEY, groupMessage);
  }

}

package com.springboot.comodogroupservice;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import com.springboot.comodogroupservice.model.TodoGroupIO;
import com.springboot.comodogroupservice.model.TodoUser;
import com.springboot.comodogroupservice.model.TodoUserIO;
import com.springboot.comodogroupservice.repository.UserRepository;
import com.springboot.comodogroupservice.service.TodoGroupService;


@SpringBootTest
class ComodoGroupServiceApplicationTests {

  @Autowired
  private TodoGroupService todoGroupService;

  @Autowired
  private UserRepository userRepository;

  public TodoGroupIO setUp() {

    TodoGroupIO groupIO = new TodoGroupIO();
    groupIO.setName("Home&Kids");

    TodoUser user = new TodoUser();
    user.setId(999L);
    user.setName("Nida");
    user.setSurname("Karasu");
    user.setEmail("nida_karasu@gmail.com");
    user.setPassword("password_nida");

    TodoUser savedUser = userRepository.save(user);

    groupIO.setTodoUserIO(fromEntityToModel(savedUser));

    TodoGroupIO savedGroupIO = todoGroupService.saveGroup(groupIO);

    return savedGroupIO;
  }


  @Test
  void shouldGetGroupById() {

    TodoGroupIO savedGroupIO = setUp();

    TodoGroupIO checkedGroup = todoGroupService.getGroupById(savedGroupIO.getId());

    assertAll(() -> assertEquals("Home&Kids", checkedGroup.getName()),
        () -> assertEquals("nida_karasu@gmail.com", checkedGroup.getTodoUserIO().getEmail()),
        () -> assertEquals("Nida", checkedGroup.getTodoUserIO().getName()));
  }

  @Test
  void shouldGetGroupList() {

    setUp();
    List<TodoGroupIO> groupList = todoGroupService.getGroupList();
    assertTrue(groupList.size() > 0);
  }

  @Test
  void shouldSaveGroup() {
    TodoGroupIO savedGroupIO = setUp();

    assertAll(() -> assertEquals("Home&Kids", savedGroupIO.getName()),
        () -> assertEquals("nida_karasu@gmail.com", savedGroupIO.getTodoUserIO().getEmail()),
        () -> assertEquals("Nida", savedGroupIO.getTodoUserIO().getName()));
  }

  @Test
  void shouldUpdateGroup() {
    TodoGroupIO savedGroupIO = setUp();

    savedGroupIO.setName("Kitchen&Grocery");

    TodoGroupIO updatedGroupIO = todoGroupService.updateGroup(savedGroupIO.getId(), savedGroupIO);

    assertEquals("Kitchen&Grocery", updatedGroupIO.getName());
  }

  @Test
  void shouldDeleteGroup() {

    TodoGroupIO savedGroupIO = setUp();

    todoGroupService.deleteGroup(savedGroupIO.getId());

    try {
      todoGroupService.getGroupById(savedGroupIO.getId());
    } catch (ResourceNotFoundException e) {
      assertEquals("getGroupById groupId:" + savedGroupIO.getId(), e.getMessage());
    }

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

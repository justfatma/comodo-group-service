package com.springboot.comodogroupservice.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.springboot.comodogroupservice.model.TodoUser;
import com.springboot.comodogroupservice.model.TodoUserIO;
import com.springboot.comodogroupservice.repository.UserRepository;


@Component
public class MessageListener {

  @Autowired
  private UserRepository userRepository;

  @RabbitListener(queues = MQConfig.QUEUE)
  public void listener(UserMessage message) {
    System.out.println(message);

    TodoUser todoUser = fromModelToEntity(message.getTodoUserIO());
    userRepository.save(todoUser);
    System.out.println("Todo user is saved successfully");
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
}

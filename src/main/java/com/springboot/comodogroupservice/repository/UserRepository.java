package com.springboot.comodogroupservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.springboot.comodogroupservice.model.TodoUser;


@Repository
public interface UserRepository extends JpaRepository<TodoUser, Long> {

}

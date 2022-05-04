package com.springboot.comodogroupservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.springboot.comodogroupservice.model.TodoGroup;

@Repository
public interface TodoGroupRepository extends JpaRepository<TodoGroup, Long> {

}

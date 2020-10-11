package com.pa.spring.springcloud.repository;

import com.pa.spring.springcloud.model.Assignment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssignmentRepository extends CrudRepository<Assignment, Long> {
    Optional<Assignment> findByName(String name);
}
package com.pa.spring.springcloud.repository;

import com.pa.spring.springcloud.model.Skill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillRepository extends CrudRepository<Skill, Long> {
    Optional<Skill> findByNameLikeIgnoreCase(String name);
}
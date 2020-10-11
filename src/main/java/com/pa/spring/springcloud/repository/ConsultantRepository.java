package com.pa.spring.springcloud.repository;

import com.pa.spring.springcloud.model.Consultant;
import com.pa.spring.springcloud.model.Skill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultantRepository extends CrudRepository<Consultant, Long> {
    List<Consultant> findAllBySkillsContaining(Skill skill);
}
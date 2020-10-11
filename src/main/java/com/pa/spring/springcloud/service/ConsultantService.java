package com.pa.spring.springcloud.service;

import com.pa.spring.springcloud.model.Consultant;
import com.pa.spring.springcloud.repository.ConsultantRepository;
import com.pa.spring.springcloud.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ConsultantService {
    private final ConsultantRepository consultantRepository;
    private final SkillRepository skillRepository;

    @Autowired
    public ConsultantService(ConsultantRepository consultantRepository, SkillRepository skillRepository) {
        this.consultantRepository = consultantRepository;
        this.skillRepository = skillRepository;
    }

    public List<Consultant> findConsultantsWithSkills(String skillNames) {
        return Arrays.stream(skillNames.split(","))
                .map(String::trim)
                .map(skillRepository::findByNameLikeIgnoreCase)
                .filter(Optional::isPresent).map(Optional::get)
                .map(consultantRepository::findAllBySkillsContaining)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Consultant> findAvailableConsultantsWithSkills(String skillNames) {
        return findConsultantsWithSkills(skillNames).stream()
                .filter(Consultant::isAvailable)
                .collect(Collectors.toList());
    }

    public Iterable<Consultant> findAll() {
        return consultantRepository.findAll();
    }

    public Optional<Consultant> findById(Long id) {
        return consultantRepository.findById(id);
    }

    public Consultant save(Consultant consultant) {
        return consultantRepository.save(consultant);
    }

    public void delete(Consultant assignment) {
        consultantRepository.delete(assignment);
    }
}
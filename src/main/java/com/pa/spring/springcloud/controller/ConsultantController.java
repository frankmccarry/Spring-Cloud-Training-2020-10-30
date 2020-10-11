package com.pa.spring.springcloud.controller;

import com.pa.spring.springcloud.model.Consultant;
import com.pa.spring.springcloud.model.Skill;
import com.pa.spring.springcloud.service.ConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/consultants")
public class ConsultantController {
    private final ConsultantService consultantService;

    @Autowired
    public ConsultantController(ConsultantService consultantService) {
        this.consultantService = consultantService;
    }

    @GetMapping
    public ResponseEntity<Iterable> findAll() {
        return ResponseEntity.ok(consultantService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consultant> findById(@PathVariable Long id) {
        return ResponseEntity.of(consultantService.findById(id));
    }

    @GetMapping(params = "skills")
    public ResponseEntity<List> findConsultantsWithSkill(@RequestParam(name = "skills") String skills) {
        return ResponseEntity.ok(consultantService.findConsultantsWithSkills(skills));
    }

    @GetMapping(params = {"skills", "available"})
    public ResponseEntity<List> findAvailableConsultantsWithSkill(@RequestParam(name = "skills") String skills) {
        return ResponseEntity.ok(consultantService.findAvailableConsultantsWithSkills(skills));
    }

    @PostMapping
    public ResponseEntity<Consultant> create(@Valid @RequestBody Consultant consultant) {
        final Consultant savedConsultant = consultantService.save(consultant);
        return ResponseEntity.created(URI.create("/consultant/" + savedConsultant.getId()))
                .body(savedConsultant);
    }

    @GetMapping("/{id}/skills")
    public ResponseEntity<List<Skill>> findSkillsById(@PathVariable Long id) {
        final Optional<Consultant> consultant = consultantService.findById(id);
        if (consultant.isPresent()) {
            return ResponseEntity.ok(consultant.get().getSkills());
        }
        return ResponseEntity.notFound().build();
    }
}
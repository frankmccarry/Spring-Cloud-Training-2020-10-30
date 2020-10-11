package com.pa.spring.springcloud.controller;

import com.pa.spring.springcloud.model.Skill;
import com.pa.spring.springcloud.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/skills")
public class SkillController {
    private final SkillService skillService;

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<Iterable> findAll() {
        return ResponseEntity.ok(skillService.findAll());
    }

    @PostMapping
    public ResponseEntity<Skill> create(@Valid @RequestBody Skill skill) {
        final Skill savedSkill = skillService.save(skill);
        return ResponseEntity.created(URI.create("/skills/" + savedSkill.getId()))
                .body(savedSkill);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Skill> findById(@PathVariable Long id) {
        return ResponseEntity.of(skillService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        final Optional<Skill> currentSkill = skillService.findById(id);
        currentSkill.ifPresent(skillService::delete);
        return (currentSkill.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Skill> createOrReplace(@PathVariable("id") Long id, @Valid @RequestBody Skill skill) {
        final boolean isSkillPresent = skillService.findById(id).isPresent();
        final Skill savedSkill = skillService.save(new Skill(skill, id));
        return isSkillPresent ? ResponseEntity.ok(savedSkill) : ResponseEntity.created(URI.create("/skills/" + id))
                .body(savedSkill);
    }
}
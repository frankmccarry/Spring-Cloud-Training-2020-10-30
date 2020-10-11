package com.pa.spring.springcloud.controller;

import com.pa.spring.springcloud.model.Assignment;
import com.pa.spring.springcloud.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {
    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public ResponseEntity<Iterable> findAll() {
        return ResponseEntity.ok(assignmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assignment> findById(@PathVariable Long id) {
        return ResponseEntity.of(assignmentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Assignment> create(@Valid @RequestBody Assignment assignment) {
        final Assignment savedAssignment = assignmentService.save(assignment);
        return ResponseEntity.created(URI.create("/assignments/" + savedAssignment.getId()))
                .body(savedAssignment);
    }
}
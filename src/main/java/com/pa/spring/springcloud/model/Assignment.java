package com.pa.spring.springcloud.model;

import com.pa.spring.springcloud.constants.ValidationMessage;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, unique = true)
    private Long id;
    @Column(nullable = false, unique = true)
    @NotEmpty
    private String name;

    @SuppressWarnings("unused") // Required for serialisation
    private Assignment() {
    }

    public Assignment(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(ValidationMessage.Assignment.ASSIGNMENT_NAME_NOT_EMPTY);
        }
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || other != null
                && other.getClass() == Assignment.class
                && this.equals((Assignment) other);
    }

    private boolean equals(Assignment other) {
        return Objects.equals(id, other.id) &&
                Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
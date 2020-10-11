package com.pa.spring.springcloud.model;

import com.pa.spring.springcloud.constants.ValidationMessage;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Entity
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, unique = true)
    private Long id;
    @Column(nullable = false, unique = true)
    @NotEmpty
    private String name;
    private String description;

    @SuppressWarnings("unused") // Required for serialisation
    private Skill() {
    }

    public Skill(String name, String description) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(ValidationMessage.Skill.SKILL_NAME_NOT_EMPTY);
        }
        this.name = name;
        this.description = description;
    }

    public Skill(Skill skill, Long id) {
        this.id = id;
        this.name = skill.name;
        this.description = skill.description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || other != null
                && other.getClass() == Skill.class
                && this.equals((Skill) other);
    }

    private boolean equals(Skill other) {
        return Objects.equals(id, other.id) &&
                Objects.equals(name, other.name) &&
                Objects.equals(description, other.description);
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
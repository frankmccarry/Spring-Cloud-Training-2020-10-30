package com.pa.spring.springcloud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pa.spring.springcloud.constants.ValidationMessage;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
public class Consultant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    @NotEmpty
    private String fullName;
    @Column(nullable = false, unique = true)
    @NotEmpty
    @Email
    private String email;
    @ManyToOne
    private Assignment assignment;
    @ManyToMany
    private List<Skill> skills;

    @SuppressWarnings("unused") // Required for serialisation
    private Consultant() {
    }

    public Consultant(String fullName, String email, Assignment assignment, List<Skill> skills) {
        if (fullName == null || fullName.isEmpty()) {
            throw new IllegalArgumentException(ValidationMessage.Consultant.CONSULTANT_NAME_NOT_EMPTY);
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException(ValidationMessage.Consultant.CONSULTANT_EMAIL_NOT_EMPTY);
        }
        this.fullName = fullName;
        this.email = email;
        this.assignment = assignment;
        this.skills = skills == null ? Collections.emptyList() : skills;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    public boolean isAvailable() {
        return assignment == null;
    }

    public List<Skill> getSkills() {
        return Collections.unmodifiableList(skills);
    }

    public Assignment getAssignment() {
        return assignment;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, email);
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || other != null
                && other.getClass() == Consultant.class
                && this.equals((Consultant) other);
    }

    private boolean equals(Consultant other) {
        return Objects.equals(id, other.id) &&
                Objects.equals(fullName, other.fullName) &&
                Objects.equals(email, other.email) &&
                Objects.equals(assignment, other.assignment) &&
                skills.containsAll(other.skills);
    }

    @Override
    public String toString() {
        return "Consultant{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", assignment=" + assignment +
                ", skills=" + skills +
                '}';
    }
}
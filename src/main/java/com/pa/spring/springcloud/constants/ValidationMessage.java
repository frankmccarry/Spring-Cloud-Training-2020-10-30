package com.pa.spring.springcloud.constants;

public final class ValidationMessage {
    public static final class Assignment {
        public static final String ASSIGNMENT_NAME_NOT_EMPTY = "Assignment name cannot be null or empty";
    }
    public static final class Consultant {
        public static final String CONSULTANT_NAME_NOT_EMPTY = "Consultant name cannot be null or empty";
        public static final String CONSULTANT_EMAIL_NOT_EMPTY = "Consultant email cannot be null or empty";
        public static final String CONSULTANT_EMAIL_VALID_EMAIL = "Consultant email must be a valid email address";
    }
    public static final class Skill {
        public static final String SKILL_NAME_NOT_EMPTY = "Skill name cannot be null or empty";
    }
}
package unit.pa.spring.springcloud.util;

import com.pa.spring.springcloud.model.Assignment;
import com.pa.spring.springcloud.model.Consultant;
import com.pa.spring.springcloud.model.Skill;

import java.util.Arrays;
import java.util.Collections;

public class Constants {
    public static final class Assignments {
        public static final Assignment RENTOKIL = new Assignment("Rentokil");
        public static final Assignment HOME_OFFICE = new Assignment("Home Office");
    }


    public static final class Skills {
        public static final Skill JAVA = new Skill("Java", "Java programming language");
        public static final Skill SPRING = new Skill("Spring", "Spring Framework");
        public static final Skill C_SHARP = new Skill("C#", "C# programming language");
        public static final Skill JAVASCRIPT = new Skill("Javascript", "Javascript language");
    }

    public static final class Consultants {
        public static final Consultant JOHN_DOE_AVAILABLE_JAVA = new Consultant("John Doe", "john@doe", null, Collections.singletonList(Skills.JAVA));
        public static final Consultant JOHN_DOE_AVAILABLE_C_SHARP = new Consultant("John Doe", "john@doe", null, Collections.singletonList(Skills.C_SHARP));
        public static final Consultant JOHN_DOE_RENTOKIL_JAVA = new Consultant("John Doe", "john@doe", Assignments.RENTOKIL, Collections.singletonList(Skills.JAVA));
        public static final Consultant JOHN_DOE_AVAILABLE_JAVA_ALTERNATE_EMAIL = new Consultant("John Doe", "john.doe@doe", null, Collections.singletonList(Skills.JAVA));
        public static final Consultant JANE_DOE_RENTOKIL_JAVA_SPRING = new Consultant("Jane Doe", "jane@doe", Assignments.RENTOKIL, Arrays.asList(Skills.JAVA, Skills.SPRING));
        public static final Consultant JANE_DOE_RENTOKIL_SPRING_JAVA = new Consultant("Jane Doe", "jane@doe", Assignments.RENTOKIL, Arrays.asList(Skills.SPRING, Skills.JAVA));
        public static final Consultant CON_SULTANT_AVAILABLE_NO_SKILLS = new Consultant("Con Sultant", "con@sultant", null, null);
    }
}

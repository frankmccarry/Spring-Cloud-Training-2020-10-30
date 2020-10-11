package integration.pa.spring.springcloud.repository;

import com.pa.spring.springcloud.SpringCloudApplication;
import com.pa.spring.springcloud.model.Assignment;
import com.pa.spring.springcloud.model.Consultant;
import com.pa.spring.springcloud.model.Skill;
import com.pa.spring.springcloud.repository.ConsultantRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest()
@ContextConfiguration(classes = SpringCloudApplication.class)
public class ConsultantRepositoryTest {
    private Skill JAVA = new Skill("Java", "Java programming language");
    private Skill SPRING = new Skill("Spring", "Java framework");
    private Skill C_SHARP = new Skill("C#", "C# programming language");
    private Assignment OXFORD_BROOKES = new Assignment("Oxford Brookes");

    private final Consultant C_SHARP_CONSULTANT = new Consultant("John Sharp", "John.Sharp@paconsulting.com", OXFORD_BROOKES, Collections.singletonList(C_SHARP));
    private final Consultant SPRING_JAVA_CONSULTANT = new Consultant("Alex Pritchard", "Alex.Pritchard@paconsulting.com", null, Arrays.asList(SPRING, JAVA));
    private final Consultant JAVA_CONSULTANT = new Consultant("Leo Wrest", "Leo.Wrest@paconsulting.com", OXFORD_BROOKES, Collections.singletonList(JAVA));

    @Autowired
    private ConsultantRepository consultantRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setUp() {
        JAVA = entityManager.persist(JAVA);
        SPRING = entityManager.persist(SPRING);
        C_SHARP = entityManager.persist(C_SHARP);
        OXFORD_BROOKES = entityManager.persist(OXFORD_BROOKES);
    }

    @After
    public void tearDown() {
        consultantRepository.deleteAll();
        entityManager.clear();
    }

    @Test
    public void findBySkill_SingleJavaConsultant_FindsJavaConsultant() {
        // Given there is a single Java consultant
        entityManager.persist(JAVA_CONSULTANT);
        entityManager.flush();

        // When we find all consultants by Java skill
        final List<Consultant> found = consultantRepository.findAllBySkillsContaining(JAVA);

        // Then we find a single Consultant, that matches Leo
        assertEquals(1, found.size());
        assertTrue(found.contains(JAVA_CONSULTANT));
    }

    @Test
    public void findBySkill_MultipleConsultants_FindsJavaConsultants() {
        // Given there are three consultants, two of which have a Java skill among their multiple skills
        entityManager.persist(JAVA_CONSULTANT);
        entityManager.persist(SPRING_JAVA_CONSULTANT);
        entityManager.persist(C_SHARP_CONSULTANT);
        entityManager.flush();

        // When we find all consultants by Java skill
        final List<Consultant> found = consultantRepository.findAllBySkillsContaining(JAVA);

        // Then we find two Consultants, that match our Java consultants
        assertEquals(2, found.size());
        assertTrue(found.containsAll(Arrays.asList(JAVA_CONSULTANT, SPRING_JAVA_CONSULTANT)));
    }

    @Test
    public void findBySkill_NoConsultantsWithSkill_ReturnsEmptyList() {
        // Given there are two consultants without the C# skill
        entityManager.persist(JAVA_CONSULTANT);
        entityManager.persist(SPRING_JAVA_CONSULTANT);
        entityManager.flush();

        // When we find all consultants by C# skill
        final List<Consultant> found = consultantRepository.findAllBySkillsContaining(C_SHARP);

        // Then we find no consultants (empty list)
        assertEquals(0, found.size());
    }
}
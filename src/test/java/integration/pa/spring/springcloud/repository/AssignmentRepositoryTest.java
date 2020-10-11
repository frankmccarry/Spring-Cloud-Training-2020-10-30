package integration.pa.spring.springcloud.repository;

import com.pa.spring.springcloud.SpringCloudApplication;
import com.pa.spring.springcloud.model.Assignment;
import com.pa.spring.springcloud.repository.AssignmentRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest()
@ContextConfiguration(classes = SpringCloudApplication.class)
public class AssignmentRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @After
    public void tearDown() {
        assignmentRepository.deleteAll();
        entityManager.clear();
    }

    @Test
    public void findByName_AssignmentExistsWithName_FindsAssignment() {
        // given there is an assignment with the name "Rentokil"
        final Assignment testAssignment = new Assignment("Rentokil");
        entityManager.persist(testAssignment);
        entityManager.flush();

        // when findByName is called with "Rentokil"
        Optional<Assignment> found = assignmentRepository.findByName("Rentokil");

        // then the "Rentokil" assignment is found
        assertTrue(found.isPresent());
        assertEquals(testAssignment.getName(), found.get().getName());
    }

    @Test
    public void findByName_NoAssignments_DoesNotFindAssignment() {
        // given there are no assignments

        // when findByName is called with "Null Assignment"
        Optional<Assignment> found = assignmentRepository.findByName("Null Assignment");

        // then no assignment is found
        assertFalse(found.isPresent());
    }
}
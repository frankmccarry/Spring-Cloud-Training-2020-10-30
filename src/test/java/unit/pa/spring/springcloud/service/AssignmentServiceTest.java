package unit.pa.spring.springcloud.service;

import com.pa.spring.springcloud.model.Assignment;
import com.pa.spring.springcloud.repository.AssignmentRepository;
import com.pa.spring.springcloud.service.AssignmentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static unit.pa.spring.springcloud.util.Constants.Assignments.RENTOKIL;

@RunWith(MockitoJUnitRunner.class)
public class AssignmentServiceTest {
    @Mock
    private AssignmentRepository assignmentRepository;
    @InjectMocks
    private AssignmentService assignmentService;

    @Test
    public void findAll_WhenCalled_DelegatesToRepository() {
        assignmentService.findAll();
        verify(assignmentRepository).findAll();
    }

    @Test
    public void findById_WhenCalled_DelegatesToRepository() {
        assignmentService.findById(1L);
        verify(assignmentRepository).findById(1L);
    }

    @Test
    public void save_WhenCalled_DelegatesToRepository() {
        // given we have a valid assignment
        final Assignment validAssignment = RENTOKIL;

        // when the save method is called on the assignment service
        final Assignment serviceAssignment = assignmentService.save(validAssignment);

        // then the call is delegated to the save method of the assignment repository
        final Assignment repositoryAssignment = verify(assignmentRepository).save(validAssignment);
        assertEquals(serviceAssignment, repositoryAssignment);
    }

    @Test
    public void delete_WhenCalled_DelegatesToRepository() {
        // given we have a valid assignment
        final Assignment validAssignment = RENTOKIL;

        // when the delete method is called on the assignment service
        assignmentService.delete(validAssignment);

        // then the call is delegated to the delete method of the assignment repository
        verify(assignmentRepository).delete(validAssignment);
    }
}
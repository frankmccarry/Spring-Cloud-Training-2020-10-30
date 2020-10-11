package unit.pa.spring.springcloud.model;

import com.pa.spring.springcloud.SpringCloudApplication;
import com.pa.spring.springcloud.model.Assignment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static unit.pa.spring.springcloud.util.Constants.Assignments.HOME_OFFICE;
import static unit.pa.spring.springcloud.util.Constants.Assignments.RENTOKIL;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringCloudApplication.class)
public class AssignmentTest {

    @Test
    public void equals_ObjectsAreIdentical_ReturnsTrue() {
        // given we have two identical objects
        // when we call equals
        // then we get true
        assertEquals(RENTOKIL, RENTOKIL);
        assertEquals(RENTOKIL, new Assignment("Rentokil"));
    }

    @Test
    public void equals_ObjectsAreNotIdentical_ReturnsFalse() {
        // given we have two different objects
        // when we call equals
        // then we get false
        assertNotEquals(RENTOKIL, HOME_OFFICE);
        assertNotEquals(RENTOKIL, null);
        assertNotEquals(RENTOKIL, new Object());
    }

    @Test
    public void hashCode_ObjectsAreIdentical_HashCodeEqual() {
        // given we have two identical objects
        // when we call hashcode
        // then we get identical hash codes
        assertEquals(RENTOKIL.hashCode(), new Assignment("Rentokil").hashCode());
    }

    @Test
    public void hashCode_ObjectsAreNotIdentical_HashCodeNotEqual() {
        // given we have two different objects
        // when we call hashcode
        // then we get different hash codes
        assertNotEquals(RENTOKIL.hashCode(), HOME_OFFICE.hashCode());
    }

    @Test
    public void getName_WhenCalled_ReturnsCorrectValue() {
        assertEquals("Rentokil", RENTOKIL.getName());
        assertEquals(" ", new Assignment(" ").getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void assignment_NullAssignmentName_ThrowsException() {
        new Assignment(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assignment_EmptyAssignmentName_ThrowsException() {
        new Assignment("");
    }

    @Test
    public void toString_WhenCalled_ReturnsStringValue() {
        // given we have a valid Assignment object
        // when we call toString
        // then we receive a valid String object (i.e. non-null) without Exceptions
        assertNotNull(HOME_OFFICE.toString());
    }
}
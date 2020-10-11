package unit.pa.spring.springcloud.model;

import com.pa.spring.springcloud.SpringCloudApplication;
import com.pa.spring.springcloud.model.Consultant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static unit.pa.spring.springcloud.util.Constants.Assignments.RENTOKIL;
import static unit.pa.spring.springcloud.util.Constants.Consultants.*;
import static unit.pa.spring.springcloud.util.Constants.Skills.JAVA;
import static unit.pa.spring.springcloud.util.Constants.Skills.SPRING;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringCloudApplication.class)
public class ConsultantTest {

    @Test
    public void equals_ObjectsAreIdentical_ReturnsTrue() {
        // given we have two identical objects
        // when we call equals
        // then we get true
        assertEquals(JOHN_DOE_AVAILABLE_JAVA,
                new Consultant("John Doe", "john@doe", null, Collections.singletonList(JAVA)));
        assertEquals(JANE_DOE_RENTOKIL_JAVA_SPRING, JANE_DOE_RENTOKIL_SPRING_JAVA);
    }

    @Test
    public void equals_ObjectsAreNotIdentical_ReturnsFalse() {
        // given we have two different objects
        // when we call equals
        // then we get false
        assertNotEquals(JOHN_DOE_AVAILABLE_JAVA, JANE_DOE_RENTOKIL_JAVA_SPRING);
        assertNotEquals(JOHN_DOE_AVAILABLE_JAVA, JOHN_DOE_AVAILABLE_JAVA_ALTERNATE_EMAIL);
        assertNotEquals(JOHN_DOE_AVAILABLE_JAVA, JOHN_DOE_RENTOKIL_JAVA);
        assertNotEquals(JOHN_DOE_AVAILABLE_JAVA, CON_SULTANT_AVAILABLE_NO_SKILLS);
        assertNotEquals(JOHN_DOE_AVAILABLE_JAVA, null);
        assertNotEquals(JOHN_DOE_AVAILABLE_JAVA, new Object());
    }

    @Test
    public void hashCode_ObjectsAreIdentical_HashCodeEqual() {
        // given we have two identical objects
        // when we call hashcode
        // then we get identical hash codes
        assertEquals(CON_SULTANT_AVAILABLE_NO_SKILLS.hashCode(), new Consultant("Con Sultant", "con@sultant", null, null).hashCode());
    }

    @Test
    public void hashCode_ObjectsAreNotIdentical_HashCodeNotEqual() {
        // given we have two different objects
        // when we call hashcode
        // then we get different hash codes
        assertNotEquals(JOHN_DOE_AVAILABLE_JAVA.hashCode(), JANE_DOE_RENTOKIL_JAVA_SPRING.hashCode());
    }

    @Test
    public void getFullName_WhenCalled_ReturnsCorrectValue() {
        assertEquals("John Doe", JOHN_DOE_AVAILABLE_JAVA.getFullName());
        assertEquals("Con Sultant", CON_SULTANT_AVAILABLE_NO_SKILLS.getFullName());
    }

    @Test
    public void isAvailable_WhenCalled_ReturnsCorrectValue() {
        assertTrue(JOHN_DOE_AVAILABLE_JAVA.isAvailable());
        assertFalse(JANE_DOE_RENTOKIL_JAVA_SPRING.isAvailable());
    }

    @Test
    public void getSkills_WhenCalled_ReturnsCorrectValue() {
        assertEquals(Collections.singletonList(JAVA), JOHN_DOE_AVAILABLE_JAVA.getSkills());
        assertEquals(Collections.emptyList(), CON_SULTANT_AVAILABLE_NO_SKILLS.getSkills());
    }

    @Test
    public void getAssignment_WhenCalled_ReturnsCorrectValue() {
        assertNull(JOHN_DOE_AVAILABLE_JAVA.getAssignment());
        assertEquals(RENTOKIL, JANE_DOE_RENTOKIL_JAVA_SPRING.getAssignment());
    }

    @Test
    public void getEmail_WhenCalled_ReturnsCorrectValue() {
        assertEquals("john@doe", JOHN_DOE_AVAILABLE_JAVA.getEmail());
        assertEquals("con@sultant", CON_SULTANT_AVAILABLE_NO_SKILLS.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void consultant_NullConsultantFullName_ThrowsException() {
        new Consultant(null, "valid@email", RENTOKIL, Arrays.asList(JAVA, SPRING));
    }

    @Test(expected = IllegalArgumentException.class)
    public void consultant_EmptyConsultantFullName_ThrowsException() {
        new Consultant("", "valid@email", RENTOKIL, Arrays.asList(JAVA, SPRING));
    }

    @Test(expected = IllegalArgumentException.class)
    public void consultant_NullConsultantEmail_ThrowsException() {
        new Consultant("John Doe", null, RENTOKIL, Arrays.asList(JAVA, SPRING));
    }

    @Test
    public void toString_WhenCalled_ReturnsStringValue() {
        // given we have a valid Consultant object
        // when we call toString
        // then we receive a valid String object (i.e. non-null) without Exceptions
        assertNotNull(JOHN_DOE_AVAILABLE_JAVA.toString());
    }
}
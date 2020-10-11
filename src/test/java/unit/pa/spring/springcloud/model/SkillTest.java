package unit.pa.spring.springcloud.model;

import com.pa.spring.springcloud.SpringCloudApplication;
import com.pa.spring.springcloud.model.Skill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static unit.pa.spring.springcloud.util.Constants.Skills.C_SHARP;
import static unit.pa.spring.springcloud.util.Constants.Skills.JAVA;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringCloudApplication.class)
public class SkillTest {

    @Test
    public void equals_ObjectsAreIdentical_ReturnsTrue() {
        // given we have two identical objects
        // when we call equals
        // then we get true
        assertEquals(JAVA, JAVA);
        assertEquals(JAVA, new Skill("Java", "Java programming language"));
    }

    @Test
    public void equals_ObjectsAreNotIdentical_ReturnsFalse() {
        // given we have two different objects
        // when we call equals
        // then we get false
        assertNotEquals(JAVA, C_SHARP);
        assertNotEquals(JAVA, new Skill("Java", "Programming language"));
        assertNotEquals(JAVA, null);
        assertNotEquals(JAVA, new Object());
    }

    @Test
    public void hashCode_ObjectsAreIdentical_HashCodeEqual() {
        // given we have two identical objects
        // when we call hashcode
        // then we get identical hash codes
        assertEquals(JAVA.hashCode(), new Skill("Java", "Java programming language").hashCode());
    }

    @Test
    public void hashCode_ObjectsAreNotIdentical_HashCodeNotEqual() {
        // given we have two different objects
        // when we call hashcode
        // then we get different hash codes
        assertNotEquals(JAVA.hashCode(), C_SHARP.hashCode());
    }

    @Test
    public void getName_WhenCalled_ReturnsCorrectValue() {
        assertEquals("Java", new Skill("Java", "Java Programming Language").getName());
        assertEquals(" ", new Skill(" ", null).getName());
    }

    @Test
    public void getDescription_WhenCalled_ReturnsCorrectValue() {
        assertEquals("Java Programming Language", new Skill("Java", "Java Programming Language").getDescription());
        assertEquals("", new Skill("Skill", "").getDescription());
        assertNull(new Skill(" ", null).getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void skill_NullSkillName_ThrowsException() {
        // given we try to create a Skill with a null skill name
        // when we call the constructor
        // then we receive an IllegalArgumentException
        new Skill(null, "Skill description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void skill_EmptySkillName_ThrowsException() {
        // given we try to create a Skill with a null skill name
        // when we call the constructor
        // then we receive an IllegalArgumentException
        new Skill("", "Skill description");
    }

    @Test
    public void toString_WhenCalled_ReturnsStringValue() {
        // given we have a valid Skill object
        // when we call toString
        // then we receive a valid String object (i.e. non-null) without Exceptions
        assertNotNull(JAVA.toString());
    }
}
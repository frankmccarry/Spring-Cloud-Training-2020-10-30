package integration.pa.spring.springcloud.repository;

import com.pa.spring.springcloud.SpringCloudApplication;
import com.pa.spring.springcloud.model.Skill;
import com.pa.spring.springcloud.repository.SkillRepository;
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

@DataJpaTest()
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringCloudApplication.class)
public class SkillRepositoryTest {
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private TestEntityManager entityManager;

    @After
    public void tearDown() {
        skillRepository.deleteAll();
        entityManager.clear();
    }

    @Test
    public void findByNameLikeIgnoreCase_GivenExactName_ReturnsSkill() {
        // given a Skill "My Skill" exists
        final Skill mySkill = new Skill("My Skill", "Skill Description");
        entityManager.persist(mySkill);
        entityManager.flush();

        // when findByNameLikeIgnoreCase is called with the Skill's exact name
        Optional<Skill> found = skillRepository.findByNameLikeIgnoreCase("My Skill");

        // then we find the skill
        assertTrue(found.isPresent());
        assertEquals(mySkill, found.get());
    }

    @Test
    public void findByNameLikeIgnoreCase_GivenLowerCaseName_ReturnsSkill() {
        // given a skill "MY UPPERCASE SKILL" exists
        final Skill mySkill = new Skill("MY UPPERCASE SKILL", "Skill Description");
        entityManager.persist(mySkill);
        entityManager.flush();

        // when findByNameLikeIgnoreCase is called with the Skill's name in lowercase
        Optional<Skill> found = skillRepository.findByNameLikeIgnoreCase("my uppercase skill");

        // then we find the skill
        assertTrue(found.isPresent());
        assertEquals(mySkill, found.get());
    }

    @Test
    public void findByNameLikeIgnoreCase_GivenFirstCharacterOfName_ReturnsSkill() {
        // given a skill "My Skill" exists
        final Skill mySkill = new Skill("My Skill", "Skill Description");
        entityManager.persist(mySkill);
        entityManager.flush();

        // when findByNameLikeIgnoreCase is called with the first character of the Skill's name
        Optional<Skill> found = skillRepository.findByNameLikeIgnoreCase("M%");

        // then we find the skill
        assertTrue(found.isPresent());
        assertEquals(mySkill, found.get());
    }

    @Test
    public void findByNameLikeIgnoreCase_NoSkillsMatchingSearch_DoesNotReturnSkill() {
        // given a skill "My Skill" exists
        final Skill mySkill = new Skill("My Skill", "Skill Description");
        entityManager.persist(mySkill);
        entityManager.flush();

        // when findByNameLikeIgnoreCase is called with a totally different string "Not This Skill"
        Optional<Skill> found = skillRepository.findByNameLikeIgnoreCase("Not This Skill");

        // then we don't find the skill
        assertFalse(found.isPresent());
    }
}
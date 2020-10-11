package unit.pa.spring.springcloud.service;

import com.pa.spring.springcloud.model.Skill;
import com.pa.spring.springcloud.repository.SkillRepository;
import com.pa.spring.springcloud.service.SkillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static unit.pa.spring.springcloud.util.Constants.Skills.*;

@RunWith(MockitoJUnitRunner.class)
public class SkillServiceTest {
    @Mock
    private SkillRepository skillRepository;
    @InjectMocks
    private SkillService skillService;

    @Test
    public void findAll_WhenCalled_DelegatesToRepository() {
        // given we have a list of valid skills
        when(skillRepository.findAll()).thenReturn(Arrays.asList(JAVA, C_SHARP, JAVASCRIPT));

        // when the findAll method is called on the skill service
        final List<Skill> serviceSkills = StreamSupport.stream(skillService.findAll().spliterator(), true).collect(Collectors.toList());

        // then the call is delegated to the findAll method in the skill repository
        verify(skillRepository).findAll();
        assertEquals(3, serviceSkills.size());
        assertTrue(serviceSkills.contains(JAVA));
        assertTrue(serviceSkills.contains(C_SHARP));
        assertTrue(serviceSkills.contains(JAVASCRIPT));
    }

    @Test
    public void findById_WhenCalled_DelegatesToRepository() {
        // given we have a valid skill
        when(skillRepository.findById(1L)).thenReturn(Optional.of(JAVA));

        // when the findById method is called on the skill service
        final Optional<Skill> serviceSkill = skillService.findById(1L);

        // then the call is delegated to the findById method in the skill repository
        verify(skillRepository).findById(1L);
        assertTrue(serviceSkill.isPresent());
        assertEquals(JAVA, serviceSkill.get());
    }

    @Test
    public void save_WhenCalled_DelegatesToRepository() {
        // given we have a valid skill
        // when the save method is called on the skill service
        final Skill serviceSkill = skillService.save(JAVA);

        // then the call is delegated to the save method of the skill repository
        final Skill repositorySkill = verify(skillRepository).save(JAVA);
        assertEquals(serviceSkill, repositorySkill);
    }

    @Test
    public void delete_WhenCalled_DelegatesToRepository() {
        // given we have a valid skill
        // when the delete method is called on the skill service
        skillService.delete(JAVA);

        // then the call is delegated to the delete method of the skill repository
        verify(skillRepository).delete(JAVA);
    }
}
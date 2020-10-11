package unit.pa.spring.springcloud.service;

import com.pa.spring.springcloud.model.Consultant;
import com.pa.spring.springcloud.repository.ConsultantRepository;
import com.pa.spring.springcloud.repository.SkillRepository;
import com.pa.spring.springcloud.service.ConsultantService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static unit.pa.spring.springcloud.util.Constants.Consultants.*;
import static unit.pa.spring.springcloud.util.Constants.Skills.C_SHARP;
import static unit.pa.spring.springcloud.util.Constants.Skills.JAVA;

@RunWith(MockitoJUnitRunner.class)
public class ConsultantServiceTest {

    @Mock
    private ConsultantRepository consultantRepository;
    @Mock
    private SkillRepository skillRepository;
    @InjectMocks
    private ConsultantService consultantService;

    @Before
    public void setUp() {
        when(skillRepository.findByNameLikeIgnoreCase("java")).thenReturn(Optional.of(JAVA));
        when(skillRepository.findByNameLikeIgnoreCase("c#")).thenReturn(Optional.of(C_SHARP));
    }

    @Test
    public void findConsultantsWithSkills_SingleSkill_ReturnsConsultantsWithSkill() {
        // given we have two java consultants
        when(consultantRepository.findAllBySkillsContaining(JAVA)).thenReturn(Arrays.asList(JOHN_DOE_AVAILABLE_JAVA, JOHN_DOE_RENTOKIL_JAVA));

        // when we find all consultants with the "java" skill
        final List<Consultant> consultants = consultantService.findConsultantsWithSkills("java");

        // then we get back 2 consultants
        assertEquals(2, consultants.size());
        assertTrue(consultants.contains(JOHN_DOE_AVAILABLE_JAVA));
        assertTrue(consultants.contains(JOHN_DOE_RENTOKIL_JAVA));
    }

    @Test
    public void findConsultantsWithSkills_MultipleSkills_ReturnsConsultantsWithAnySkills() {
        // given we have two java consultants and one C# consultant
        when(consultantRepository.findAllBySkillsContaining(JAVA)).thenReturn(Arrays.asList(JOHN_DOE_AVAILABLE_JAVA, JOHN_DOE_RENTOKIL_JAVA));
        when(consultantRepository.findAllBySkillsContaining(C_SHARP)).thenReturn(Collections.singletonList(JOHN_DOE_AVAILABLE_C_SHARP));

        // when we find all consultants with either the "java" or "c#" skill
        final List<Consultant> consultants = consultantService.findConsultantsWithSkills("java,c#");

        // then we get back 3 consultants
        assertEquals(3, consultants.size());
        assertTrue(consultants.contains(JOHN_DOE_AVAILABLE_JAVA));
        assertTrue(consultants.contains(JOHN_DOE_RENTOKIL_JAVA));
        assertTrue(consultants.contains(JOHN_DOE_AVAILABLE_C_SHARP));
    }

    @Test
    public void findAvailableConsultantsWithSkills_SingleSkill_ReturnsConsultantsWithSkill() {
        // given we two java consultants with one available
        when(consultantRepository.findAllBySkillsContaining(JAVA)).thenReturn(Arrays.asList(JOHN_DOE_AVAILABLE_JAVA, JOHN_DOE_RENTOKIL_JAVA));

        // when we find all available consultants with the "java" skill
        final List<Consultant> consultants = consultantService.findAvailableConsultantsWithSkills("java");

        // then we get back one consultant
        assertEquals(1, consultants.size());
        assertTrue(consultants.contains(JOHN_DOE_AVAILABLE_JAVA));
    }

    @Test
    public void findAvailableConsultantsWithSkills_MultipleSkills_ReturnsConsultantsWithAnySkills() {
        // given we have two java consultants and one C# consultant, with two available
        when(consultantRepository.findAllBySkillsContaining(JAVA)).thenReturn(Arrays.asList(JOHN_DOE_AVAILABLE_JAVA, JOHN_DOE_RENTOKIL_JAVA));
        when(consultantRepository.findAllBySkillsContaining(C_SHARP)).thenReturn(Collections.singletonList(JOHN_DOE_AVAILABLE_C_SHARP));

        // when we find all available consultants with either the "java" or "c#" skill
        final List<Consultant> consultants = consultantService.findAvailableConsultantsWithSkills("java,c#");

        // then we get back two consultants
        assertEquals(2, consultants.size());
        assertTrue(consultants.contains(JOHN_DOE_AVAILABLE_JAVA));
        assertTrue(consultants.contains(JOHN_DOE_AVAILABLE_C_SHARP));
    }

    @Test
    public void findAvailableConsultantsWithSkills_NoConsultantsWithSkill_ReturnsEmptyList() {
        // given we have no java consultants
        when(consultantRepository.findAllBySkillsContaining(JAVA)).thenReturn(Collections.emptyList());

        // when we find all consultants with the "java" skill
        final List<Consultant> consultants = consultantService.findAvailableConsultantsWithSkills("java");

        // then we get back no consultants
        assertEquals(0, consultants.size());
    }

    @Test
    public void findAll_WhenCalled_DelegatesToRepository() {
        // given we have a list of valid consultants
        when(consultantRepository.findAll()).thenReturn(Arrays.asList(JANE_DOE_RENTOKIL_JAVA_SPRING, JOHN_DOE_AVAILABLE_JAVA, CON_SULTANT_AVAILABLE_NO_SKILLS));

        // when the findAll method is called on the consultant service
        final List<Consultant> consultants = StreamSupport.stream(consultantService.findAll().spliterator(), true).collect(Collectors.toList());

        // then the call is delegated to the findAll method of the consultant repository
        verify(consultantRepository).findAll();
        assertEquals(3, consultants.size());
        assertTrue(consultants.contains(JANE_DOE_RENTOKIL_JAVA_SPRING));
        assertTrue(consultants.contains(JOHN_DOE_AVAILABLE_JAVA));
        assertTrue(consultants.contains(CON_SULTANT_AVAILABLE_NO_SKILLS));
    }

    @Test
    public void findById_WhenCalled_DelegatesToRepository() {
        // given we have a valid consultant
        when(consultantRepository.findById(1L)).thenReturn(Optional.of(JANE_DOE_RENTOKIL_JAVA_SPRING));

        // when the findById method is called on the consultant service
        final Optional<Consultant> consultant = consultantService.findById(1L);

        // then the call is delegated to the findById method of the consultant repository
        verify(consultantRepository).findById(1L);
        assertTrue(consultant.isPresent());
        assertEquals(JANE_DOE_RENTOKIL_JAVA_SPRING, consultant.get());
    }

    @Test
    public void save_WhenCalled_DelegatesToRepository() {
        // given we have a valid consultant
        final Consultant validConsultant = JANE_DOE_RENTOKIL_JAVA_SPRING;

        // when the save method is called on the consultant service
        final Consultant serviceConsultant = consultantService.save(validConsultant);

        // then the call is delegated to the save method of the consultant repository
        final Consultant repositoryConsultant = verify(consultantRepository).save(validConsultant);
        assertEquals(serviceConsultant, repositoryConsultant);
    }

    @Test
    public void delete_WhenCalled_DelegatesToRepository() {
        // given we have a valid consultant
        final Consultant validConsultant = JANE_DOE_RENTOKIL_JAVA_SPRING;

        // when the delete method is called on the consultant service
        consultantService.delete(validConsultant);

        // then the call is delegated to the delete method of the consultant repository
        verify(consultantRepository).delete(validConsultant);
    }
}
package integration.pa.spring.springcloud.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pa.spring.springcloud.SpringCloudApplication;
import com.pa.spring.springcloud.model.Consultant;
import com.pa.spring.springcloud.model.Skill;
import com.pa.spring.springcloud.service.ConsultantService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static unit.pa.spring.springcloud.util.Constants.Consultants.*;
import static unit.pa.spring.springcloud.util.Constants.Skills.JAVA;
import static unit.pa.spring.springcloud.util.Constants.Skills.SPRING;

@SpringBootTest(classes = SpringCloudApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ConsultantControllerTest {
    private static final TypeReference<List<Consultant>> CONSULTANT_LIST_TYPE_REF = new TypeReference<>() {
    };
    private static final TypeReference<List<Skill>> SKILL_LIST_TYPE_REF = new TypeReference<>() {
    };
    private static final String ENDPOINT = "/consultants";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultantService service;

    @Test
    public void findConsultant_NoConsultants_Should404() throws Exception {
        // Given we have no consultants with id 1
        when(service.findById(1L)).thenReturn(Optional.empty());

        // When we request the consultant with id 1
        // Then we get a 404
        mockMvc.perform(get(ENDPOINT + "/1")).andExpect(status().isNotFound());
    }

    @Test
    public void findConsultant_ConsultantExists_ShouldReturnConsultant() throws Exception {
        // Given we have a consultant with id 1
        when(service.findById(1L)).thenReturn(Optional.of(JOHN_DOE_RENTOKIL_JAVA));

        // When we request the consultant with id 1
        final String response = mockMvc.perform(get(ENDPOINT + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get the requested consultant
        assertEquals(JOHN_DOE_RENTOKIL_JAVA, objectMapper.readValue(response, Consultant.class));
    }

    @Test
    public void findConsultants_NoConsultants_ShouldReturnEmptyJSONArray() throws Exception {
        // Given we have no consultants
        when(service.findAll()).thenReturn(Collections.emptyList());

        // When we request all consultants
        final String response = mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get an empty JSON array
        final List<Consultant> consultants = objectMapper.readValue(response, CONSULTANT_LIST_TYPE_REF);
        assertEquals(0, consultants.size());
    }

    @Test
    public void findConsultants_SingleConsultant_ShouldReturnJSONArray() throws Exception {
        // Given we have a single consultant
        when(service.findAll()).thenReturn(Collections.singletonList(JOHN_DOE_RENTOKIL_JAVA));

        // When we request all consultants
        final String response = mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get a JSON array, containing the full details of the one Java consultant
        final List<Consultant> consultants = objectMapper.readValue(response, CONSULTANT_LIST_TYPE_REF);
        assertEquals(1, consultants.size());
        assertTrue(consultants.contains(JOHN_DOE_RENTOKIL_JAVA));
    }

    @Test
    public void findConsultants_MultipleConsultants_ShouldReturnJSONArray() throws Exception {
        // Given we have multiple consultants
        when(service.findAll()).thenReturn(Arrays.asList(JOHN_DOE_RENTOKIL_JAVA, JOHN_DOE_AVAILABLE_JAVA));

        // When we request all consultants
        final String response = mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get a JSON array, containing the full details of the two Java consultants
        final List<Consultant> consultants = objectMapper.readValue(response, CONSULTANT_LIST_TYPE_REF);
        assertEquals(2, consultants.size());
        assertTrue(consultants.contains(JOHN_DOE_RENTOKIL_JAVA));
        assertTrue(consultants.contains(JOHN_DOE_AVAILABLE_JAVA));
    }

    @Test
    public void findConsultantSkills_NoConsultants_Should404() throws Exception {
        // Given we have no consultants with id 1
        when(service.findById(1L)).thenReturn(Optional.empty());

        // When we request the consultant's skills with id 1
        // Then we get a 404
        mockMvc.perform(get(ENDPOINT + "/1/skills")).andExpect(status().isNotFound());
    }

    @Test
    public void findConsultantSkills_ConsultantExistsSingleSkills_ShouldReturnConsultantSkill() throws Exception {
        // Given we have a consultant with id 1, with the Java skill
        when(service.findById(1L)).thenReturn(Optional.of(JOHN_DOE_RENTOKIL_JAVA));

        // When we request the consultant's skills with id 1
        final String response = mockMvc.perform(get(ENDPOINT + "/1/skills"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get the requested consultant's skills
        final List<Skill> skills = objectMapper.readValue(response, SKILL_LIST_TYPE_REF);
        assertEquals(1, skills.size());
        assertTrue(skills.stream().anyMatch(s -> s.getName().equalsIgnoreCase(JAVA.getName())));
        assertTrue(skills.stream().anyMatch(s -> s.getDescription().equalsIgnoreCase(JAVA.getDescription())));
    }

    @Test
    public void findConsultantSkills_ConsultantExistsMultipleSkills_ShouldReturnConsultantSkills() throws Exception {
        // Given we have a consultant with id 1, with the Java and Spring skills
        when(service.findById(1L)).thenReturn(Optional.of(JANE_DOE_RENTOKIL_JAVA_SPRING));

        // When we request the consultant's skills with id 1
        final String response = mockMvc.perform(get(ENDPOINT + "/1/skills"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get the requested consultant's skills
        final List<Skill> skills = objectMapper.readValue(response, SKILL_LIST_TYPE_REF);
        assertEquals(2, skills.size());
        assertTrue(skills.stream().anyMatch(s -> s.getName().equalsIgnoreCase(JAVA.getName())));
        assertTrue(skills.stream().anyMatch(s -> s.getDescription().equalsIgnoreCase(JAVA.getDescription())));
        assertTrue(skills.stream().anyMatch(s -> s.getName().equalsIgnoreCase(SPRING.getName())));
        assertTrue(skills.stream().anyMatch(s -> s.getDescription().equalsIgnoreCase(SPRING.getDescription())));
    }

    @Test
    public void findConsultantsWithSkill_NoConsultantsWithSkill_ShouldReturnEmptyJSONArray() throws Exception {
        // Given we have no Java consultants
        when(service.findConsultantsWithSkills("java")).thenReturn(Collections.emptyList());

        // When we call the consultant endpoint with the skills=java query string
        final String response = mockMvc.perform(get(ENDPOINT + "?skills=java"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get an empty JSON array
        final List<Consultant> consultants = objectMapper.readValue(response, CONSULTANT_LIST_TYPE_REF);
        assertEquals(0, consultants.size());
    }

    @Test
    public void findConsultantsWithSkill_SingleConsultantsWithSkill_ShouldReturnJSONArray() throws Exception {
        // Given we have two Java consultants
        when(service.findConsultantsWithSkills("java")).thenReturn(Collections.singletonList(JOHN_DOE_AVAILABLE_JAVA));

        // When we call the consultant endpoint with the skills=java query string
        final String response = mockMvc.perform(get(ENDPOINT + "?skills=java"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get a JSON array, containing the full details of the two Java consultants
        final List<Consultant> consultants = objectMapper.readValue(response, CONSULTANT_LIST_TYPE_REF);
        assertEquals(1, consultants.size());
        assertTrue(consultants.contains(JOHN_DOE_AVAILABLE_JAVA));
    }

    @Test
    public void findConsultantsWithSkill_MultipleConsultantsWithSkill_ShouldReturnJSONArray() throws Exception {
        // Given we have two Java consultants
        when(service.findConsultantsWithSkills("java")).thenReturn(Arrays.asList(JOHN_DOE_RENTOKIL_JAVA, JOHN_DOE_AVAILABLE_JAVA));

        // When we call the consultant endpoint with the skills=java query string
        final String response = mockMvc.perform(get(ENDPOINT + "?skills=java"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get a JSON array, containing the full details of the two Java consultants
        final List<Consultant> consultants = objectMapper.readValue(response, CONSULTANT_LIST_TYPE_REF);
        assertEquals(2, consultants.size());
        assertTrue(consultants.contains(JOHN_DOE_RENTOKIL_JAVA));
        assertTrue(consultants.contains(JOHN_DOE_AVAILABLE_JAVA));
    }

    @Test
    public void findAvailableConsultantsWithSkill_NoConsultantsWithSkill_ShouldReturnEmptyJSONArray() throws Exception {
        // Given we have no available Java consultants
        when(service.findAvailableConsultantsWithSkills("java")).thenReturn(Collections.emptyList());

        // When we call the consultant endpoint with the skills=java&available query string
        final String response = mockMvc.perform(get(ENDPOINT + "?skills=java&available"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get an empty JSON array
        final List<Consultant> consultants = objectMapper.readValue(response, CONSULTANT_LIST_TYPE_REF);
        assertEquals(0, consultants.size());
    }

    @Test
    public void findAvailableConsultantsWithSkill_SingleConsultantsWithSkill_ShouldReturnJSONArray() throws Exception {
        // Given we have one available Java consultant
        when(service.findAvailableConsultantsWithSkills("java")).thenReturn(Collections.singletonList(JOHN_DOE_AVAILABLE_JAVA));

        // When we call the consultant endpoint with the skills=java&available query string
        final String response = mockMvc.perform(get(ENDPOINT + "?skills=java&available"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get a JSON response, containing the full details of our available Java consultant
        final List<Consultant> consultants = objectMapper.readValue(response, CONSULTANT_LIST_TYPE_REF);
        assertEquals(1, consultants.size());
        assertTrue(consultants.contains(JOHN_DOE_AVAILABLE_JAVA));
    }

    @Test
    public void findAvailableConsultantsWithSkill_MultipleConsultantsWithSkill_ShouldReturnJSONArray() throws Exception {
        // Given we have two available Java consultants
        when(service.findAvailableConsultantsWithSkills("java")).thenReturn(Arrays.asList(JOHN_DOE_RENTOKIL_JAVA, JOHN_DOE_AVAILABLE_JAVA));

        // When we call the consultant endpoint with the skills=java&available query string
        final String response = mockMvc.perform(get(ENDPOINT + "?skills=java&available"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get a JSON array, containing the full details of the two available Java consultants
        final List<Consultant> consultants = objectMapper.readValue(response, CONSULTANT_LIST_TYPE_REF);
        assertEquals(2, consultants.size());
        assertTrue(consultants.contains(JOHN_DOE_RENTOKIL_JAVA));
        assertTrue(consultants.contains(JOHN_DOE_AVAILABLE_JAVA));
    }

    @Test
    public void create_ValidConsultant_ShouldReturnConsultant() throws Exception {
        // Given the service returns the passed in consultant
        when(service.save(Mockito.any(Consultant.class))).thenAnswer(a -> a.getArguments()[0]);
        final Consultant postedConsultant = JOHN_DOE_RENTOKIL_JAVA;
        final String postedConsultantJSON = objectMapper.writeValueAsString(postedConsultant);

        // When we post a new consultant
        final String response = mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(postedConsultantJSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get a JSON object, containing the full details of the consultant
        final Consultant returnedConsultant = objectMapper.readValue(response, Consultant.class);
        assertEquals(postedConsultant, returnedConsultant);
    }

    @Test
    public void create_NullConsultantName_ShouldReturn400Error() throws Exception {
        // Given we have a Consultant with a null full name
        final String invalidConsultantJSON = "{\"fullName\": null, \"email\": \"john@example.com\"}";

        // When we post an invalid consultant
        // then we get a Bad Request error
        mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidConsultantJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_EmptyConsultantName_ShouldReturn400Error() throws Exception {
        // Given we have a Consultant with an empty full name
        final String invalidConsultantJSON = "{\"fullName\": \"\", \"email\": \"john@example.com\"}";

        // When we post an invalid consultant
        // then we get a Bad Request error
        mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidConsultantJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_NullEmail_ShouldReturn400Error() throws Exception {
        // Given we have a Consultant with a null email
        final String invalidConsultantJSON = "{\"fullName\": \"John Smith\", \"email\": null}";

        // When we post an invalid consultant
        // then we get a Bad Request error
        mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidConsultantJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_EmptyEmail_ShouldReturn400Error() throws Exception {
        // Given we have a Consultant with an empty email
        final String invalidConsultantJSON = "{\"fullName\": \"John Smith\", \"email\": \"\"}";

        // When we post an invalid consultant
        // then we get a Bad Request error
        mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidConsultantJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_InvalidEmail_ShouldReturn400Error() throws Exception {
        // Given we have a Consultant with an invalid email
        final String invalidConsultantJSON = "{\"fullName\": \"John Smith\", \"email\": \"not.an.email\"}";

        // When we post an invalid consultant
        // then we get a Bad Request error
        mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidConsultantJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_NotAConsultant_ShouldReturn400Error() throws Exception {
        // Given we have an invalid request
        final String invalidRequestJSON = "{\"not\": \"a consultant\"}";

        // When we post an invalid request
        // then we get a Bad Request error
        mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequestJSON))
                .andExpect(status().isBadRequest());
    }
}
package integration.pa.spring.springcloud.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pa.spring.springcloud.SpringCloudApplication;
import com.pa.spring.springcloud.model.Skill;
import com.pa.spring.springcloud.service.SkillService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static unit.pa.spring.springcloud.util.Constants.Skills.C_SHARP;
import static unit.pa.spring.springcloud.util.Constants.Skills.JAVA;

@SpringBootTest(classes = SpringCloudApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class SkillControllerTest {
    private static final TypeReference<List<Skill>> SKILL_LIST_TYPE_REF = new TypeReference<>() {
    };
    private static final String ENDPOINT = "/skills";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkillService service;

    @Test
    public void findSkill_NoSkills_Should404() throws Exception {
        // Given we have no skills with id 1
        when(service.findById(1L)).thenReturn(Optional.empty());

        // When we request the skill with id 1
        // Then we get a 404
        mockMvc.perform(get(ENDPOINT + "/1")).andExpect(status().isNotFound());
    }

    @Test
    public void findSkill_SkillExists_ShouldReturnSkill() throws Exception {
        // Given we have an skill with id 1
        when(service.findById(1L)).thenReturn(Optional.of(JAVA));

        // When we request the skill with id 1
        final String response = mockMvc.perform(get(ENDPOINT + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get the requested skill
        assertEquals(JAVA, objectMapper.readValue(response, Skill.class));
    }

    @Test
    public void findSkills_NoSkills_ShouldReturnEmptyJSONArray() throws Exception {
        // Given we have no skills
        when(service.findAll()).thenReturn(Collections.emptyList());

        // When we request all skills
        final String response = mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get an empty JSON array
        final List<Skill> skills = objectMapper.readValue(response, SKILL_LIST_TYPE_REF);
        assertEquals(0, skills.size());
    }

    @Test
    public void findSkills_SingleSkill_ShouldReturnJSONArray() throws Exception {
        // Given we have a single skill
        when(service.findAll()).thenReturn(Collections.singletonList(JAVA));

        // When we request all skills
        final String response = mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get a JSON array, containing the full details of the one skill
        final List<Skill> skills = objectMapper.readValue(response, SKILL_LIST_TYPE_REF);
        assertEquals(1, skills.size());
        assertTrue(skills.contains(JAVA));
    }

    @Test
    public void findSkills_MultipleSkills_ShouldReturnJSONArray() throws Exception {
        // Given we have multiple skills
        when(service.findAll()).thenReturn(Arrays.asList(JAVA, C_SHARP));

        // When we request all skills
        final String response = mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get a JSON array, containing the full details of the two skills
        final List<Skill> skills = objectMapper.readValue(response, SKILL_LIST_TYPE_REF);
        assertEquals(2, skills.size());
        assertTrue(skills.contains(JAVA));
        assertTrue(skills.contains(C_SHARP));
    }

    @Test
    public void create_ValidSkill_ShouldReturnSkill() throws Exception {
        // Given the service returns the passed in skill
        when(service.save(Mockito.any(Skill.class))).thenAnswer(a -> a.getArguments()[0]);
        final String postedSkillJSON = objectMapper.writeValueAsString(JAVA);

        // When we post a new skill
        final String response = mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(postedSkillJSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get a JSON object, containing the full details of the skill
        final Skill returnedSkill = objectMapper.readValue(response, Skill.class);
        assertEquals(JAVA, returnedSkill);
    }

    @Test
    public void create_NullSkillName_ShouldReturn400Error() throws Exception {
        // Given we have a Skill with a null name
        final String invalidSkillJSON = "{\"name\": null, \"description\": \"skill description\"}";

        // When we post an invalid skill
        // then we get a Bad Request error
        mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidSkillJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_EmptySkillName_ShouldReturn400Error() throws Exception {
        // Given we have a Skill with an empty name
        final String invalidSkillJSON = "{\"name\": \"\", \"description\": \"skill description\"}";

        // When we post an invalid skill
        // then we get a Bad Request error
        mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidSkillJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_NotASkill_ShouldReturn400Error() throws Exception {
        // Given we have an invalid request
        final String invalidRequestJSON = "{\"not\": \"a skill\"}";

        // When we post an invalid request
        // then we get a Bad Request error
        mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequestJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void delete_NoSkills_ShouldReturn404Error() throws Exception {
        // Given we have no skills
        when(service.findById(1L)).thenReturn(Optional.empty());

        // When we delete the skill with id 1
        // Then we get a 404
        mockMvc.perform(delete(ENDPOINT + "/1")).andExpect(status().isNotFound());
        verify(service, never()).delete(any());
    }

    @Test
    public void delete_SkillExists_ShouldDeleteSkill() throws Exception {
        // Given we have a skill with id 1
        when(service.findById(1L)).thenReturn(Optional.of(JAVA));

        // When we delete the skill with id 1
        // Then the skill is deleted
        mockMvc.perform(delete(ENDPOINT + "/1")).andExpect(status().isNoContent());
        verify(service).delete(JAVA);
    }

    @Test
    public void createOrReplace_NoSkills_ShouldCreateSkill() throws Exception {
        // Given we have no skills
        when(service.findById(1L)).thenReturn(Optional.empty());
        when(service.save(Mockito.any(Skill.class))).thenAnswer(a -> a.getArguments()[0]);
        final String postedSkillJSON = objectMapper.writeValueAsString(JAVA);

        // When we post a new skill
        final String response = mockMvc.perform(put(ENDPOINT + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postedSkillJSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get a JSON object, containing the full details of the skill
        final Skill returnedSkill = objectMapper.readValue(response, Skill.class);
        assertEquals(Long.valueOf(1), returnedSkill.getId());
        assertEquals(JAVA.getName(), returnedSkill.getName());
        assertEquals(JAVA.getDescription(), returnedSkill.getDescription());
        verify(service).save(new Skill(JAVA, 1L));
    }

    @Test
    public void createOrReplace_SkillExists_ShouldReplaceSkill() throws Exception {
        // Given we have a skill with id 1
        when(service.findById(1L)).thenReturn(Optional.of(JAVA));
        when(service.save(Mockito.any(Skill.class))).thenAnswer(a -> a.getArguments()[0]);
        final String postedSkillJSON = objectMapper.writeValueAsString(C_SHARP);

        // When we post a new skill
        final String response = mockMvc.perform(put(ENDPOINT + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postedSkillJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get a JSON object, containing the full details of the skill
        final Skill returnedSkill = objectMapper.readValue(response, Skill.class);
        assertEquals(Long.valueOf(1), returnedSkill.getId());
        assertEquals(C_SHARP.getName(), returnedSkill.getName());
        assertEquals(C_SHARP.getDescription(), returnedSkill.getDescription());
        verify(service).save(new Skill(C_SHARP, 1L));
    }

    @Test
    public void createOrReplace_NullSkillName_ShouldReturn400Error() throws Exception {
        // Given we have a Skill with a null name
        final String invalidSkillJSON = "{\"name\": null, \"description\": \"skill description\"}";

        // When we post an invalid skill
        // then we get a Bad Request error
        mockMvc.perform(put(ENDPOINT + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidSkillJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createOrReplace_EmptySkillName_ShouldReturn400Error() throws Exception {
        // Given we have a Skill with an empty name
        final String invalidSkillJSON = "{\"name\": \"\", \"description\": \"skill description\"}";

        // When we post an invalid skill
        // then we get a Bad Request error
        mockMvc.perform(put(ENDPOINT + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidSkillJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createOrReplace_NotASkill_ShouldReturn400Error() throws Exception {
        // Given we have an invalid request
        final String invalidRequestJSON = "{\"not\": \"a skill\"}";

        // When we post an invalid request
        // then we get a Bad Request error
        mockMvc.perform(put(ENDPOINT + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequestJSON))
                .andExpect(status().isBadRequest());
    }
}
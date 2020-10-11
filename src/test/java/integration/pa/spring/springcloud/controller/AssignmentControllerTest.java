package integration.pa.spring.springcloud.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pa.spring.springcloud.SpringCloudApplication;
import com.pa.spring.springcloud.model.Assignment;
import com.pa.spring.springcloud.service.AssignmentService;
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
import static unit.pa.spring.springcloud.util.Constants.Assignments.HOME_OFFICE;
import static unit.pa.spring.springcloud.util.Constants.Assignments.RENTOKIL;

@SpringBootTest(classes = SpringCloudApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class AssignmentControllerTest {
    private static final TypeReference<List<Assignment>> ASSIGNMENT_LIST_TYPE_REF = new TypeReference<>() {
    };

    private static final String ENDPOINT = "/assignments";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssignmentService service;

    @Test
    public void findAssignment_NoAssignments_Should404() throws Exception {
        // Given we have no assignments with id 1
        when(service.findById(1L)).thenReturn(Optional.empty());

        // When we request the assignment with id 1
        // Then we get a 404
        mockMvc.perform(get(ENDPOINT + "/1")).andExpect(status().isNotFound());
    }

    @Test
    public void findAssignment_AssignmentExists_ShouldReturnAssignment() throws Exception {
        // Given we have an assignment with id 1
        when(service.findById(1L)).thenReturn(Optional.of(RENTOKIL));

        // When we request the assignment with id 1
        final String response = mockMvc.perform(get(ENDPOINT + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get the requested assignment
        assertEquals(RENTOKIL, objectMapper.readValue(response, Assignment.class));
    }

    @Test
    public void findAssignments_NoAssignments_ShouldReturnEmptyJSONArray() throws Exception {
        // Given we have no assignments
        when(service.findAll()).thenReturn(Collections.emptyList());

        // When we request all assignments
        final String response = mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we an empty JSON array
        final List<Assignment> assignments = objectMapper.readValue(response, ASSIGNMENT_LIST_TYPE_REF);
        assertEquals(0, assignments.size());
    }

    @Test
    public void findAssignments_SingleAssignment_ShouldReturnJSONArray() throws Exception {
        // Given we have a single assignment
        when(service.findAll()).thenReturn(Collections.singletonList(RENTOKIL));

        // When we request all assignments
        final String response = mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get a JSON array, containing the full details of the one assignment
        final List<Assignment> assignments = objectMapper.readValue(response, ASSIGNMENT_LIST_TYPE_REF);
        assertEquals(1, assignments.size());
        assertTrue(assignments.contains(RENTOKIL));
    }

    @Test
    public void findAssignments_MultipleAssignments_ShouldReturnJSONArray() throws Exception {
        // Given we have multiple assignments
        when(service.findAll()).thenReturn(Arrays.asList(RENTOKIL, HOME_OFFICE));

        // When we request all assignments
        final String response = mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get a JSON array, containing the full details of the two assignments
        final List<Assignment> assignments = objectMapper.readValue(response, ASSIGNMENT_LIST_TYPE_REF);
        assertEquals(2, assignments.size());
        assertTrue(assignments.contains(RENTOKIL));
        assertTrue(assignments.contains(HOME_OFFICE));
    }

    @Test
    public void create_ValidAssignment_ShouldReturnAssignment() throws Exception {
        // Given the service returns the passed in assignment
        when(service.save(Mockito.any(Assignment.class))).thenAnswer(a -> a.getArguments()[0]);
        final Assignment postedAssignment = RENTOKIL;
        final String postedAssignmentJSON = objectMapper.writeValueAsString(postedAssignment);

        // When we post a new assignment
        final String response = mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(postedAssignmentJSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        // Then we get a JSON object, containing the full details of the assignment
        final Assignment returnedAssignment = objectMapper.readValue(response, Assignment.class);
        assertEquals(postedAssignment, returnedAssignment);
    }

    @Test
    public void create_NullAssignmentName_ShouldReturn400Error() throws Exception {
        // Given we have a Assignment with a null name
        final String invalidAssignmentJSON = "{\"name\": null}";

        // When we post an invalid Assignment
        // then we get a Bad Request error
        mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidAssignmentJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_EmptyAssignmentName_ShouldReturn400Error() throws Exception {
        // Given we have a Assignment with a null name
        final String invalidAssignmentJSON = "{\"name\": \"\"}";

        // When we post an invalid Assignment
        // then we get a Bad Request error
        mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidAssignmentJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_NotAnAssignment_ShouldReturn400Error() throws Exception {
        // Given we have an invalid request
        final String invalidRequestJSON = "{\"not\": \"an assignment\"}";

        // When we post an invalid request
        // then we get a Bad Request error
        mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequestJSON))
                .andExpect(status().isBadRequest());
    }
}
package gov.cabinetoffice.api.prototype.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.cabinetoffice.api.prototype.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.prototype.entities.Submission;
import gov.cabinetoffice.api.prototype.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.prototype.services.SubmissionsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubmissionsController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = { SubmissionsController.class, ControllerExceptionsHandler.class })
class SubmissionsControllerTest {

    private static final String BASE_PATH = "/submissions/";

    private final Integer APPLICATION_ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubmissionsService submissionsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getSubmissionByApplicationId_found() throws Exception {
        ApplicationFormEntity applicationForm = ApplicationFormEntity.builder().grantApplicationId(APPLICATION_ID)
                .applicationName("test").grantApplicationId(APPLICATION_ID).build();

        Submission submission = Submission.builder().application(applicationForm).build();
        List<Submission> submissions = List.of(submission);
        when(submissionsService.getSubmissionByApplicationId(APPLICATION_ID)).thenReturn(submissions);

        String expectedJson = objectMapper.writeValueAsString(submissions);

        mockMvc.perform(get(BASE_PATH + APPLICATION_ID).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json(expectedJson));
    }

    @Test
    void getSubmissionByApplicationId_notFound() throws Exception {
        String errorMsg = "No submissions found with application id " + APPLICATION_ID;

        when(submissionsService.getSubmissionByApplicationId(APPLICATION_ID))
                .thenThrow(new SubmissionNotFoundException(errorMsg));

        mockMvc.perform(get(BASE_PATH + APPLICATION_ID).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value(errorMsg));
    }

    @Test
    void getSubmissionByApplicationId_invalidArgument() throws Exception {
        ResultActions actions = mockMvc.perform(get(BASE_PATH + "invalidParameterType"));

        String errorMessage = actions.andReturn().getResponse().getContentAsString();
        int errorCode = actions.andReturn().getResponse().getStatus();
        assertThat(errorCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorMessage).contains("Incorrect parameter type passed");

    }

}
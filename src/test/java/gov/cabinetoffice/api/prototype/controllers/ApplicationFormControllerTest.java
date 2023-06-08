package gov.cabinetoffice.api.prototype.controllers;

import gov.cabinetoffice.api.prototype.dtos.GenericErrorDTO;
import gov.cabinetoffice.api.prototype.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.prototype.exceptions.ApplicationFormException;
import gov.cabinetoffice.api.prototype.services.ApplicationFormService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(ApplicationFormController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = { ApplicationFormController.class })
public class ApplicationFormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationFormService applicationFormService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Integer APPLICATION_ID = 1;

    @Test
    void getApplicationById_found() throws Exception {
        ApplicationFormEntity applicationForm = ApplicationFormEntity.builder()
                .applicationName("test")
                .grantApplicationId(APPLICATION_ID)
                .build();

        when(applicationFormService.getApplicationById(APPLICATION_ID))
                .thenReturn(applicationForm);

        String expectedJson = objectMapper.writeValueAsString(applicationForm);

        mockMvc.perform(get("/application-forms/" + applicationForm.getGrantApplicationId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getApplicationById_notFound() throws Exception {
        when(applicationFormService.getApplicationById(APPLICATION_ID))
                .thenThrow(new ApplicationFormException("No application with id " + APPLICATION_ID + " found"));

        String expectedJson = objectMapper.writeValueAsString(
                new GenericErrorDTO("No application with id " + APPLICATION_ID + " found"));

        mockMvc.perform(get("/application-forms/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(expectedJson));
    }
}

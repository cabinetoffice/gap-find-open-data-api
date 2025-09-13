package gov.cabinetoffice.api.controllers;

import gov.cabinetoffice.api.dtos.submission.*;
import gov.cabinetoffice.api.entities.SchemeEntity;
import gov.cabinetoffice.api.enums.ResponseTypeEnum;
import gov.cabinetoffice.api.exceptions.SubmissionNotFoundException;
import gov.cabinetoffice.api.models.submission.SubmissionQuestion;
import gov.cabinetoffice.api.models.submission.SubmissionQuestionValidation;
import gov.cabinetoffice.api.services.SubmissionsService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.time.Instant;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SubmissionsControllerTest {

	private static final int FUNDING_ORGANISATION_ID = 1;

	final Instant instant = Instant.now();

	final String QUESTION_ID_1 = "APPLICANT_ORG_NAME";

	final String QUESTION_ID_2 = "APPLICANT_ORG_ADDRESS";

	final String QUESTION_ID_3 = "APPLICANT_ORG_COMPANIES_HOUSE";

	final String QUESTION_ID_4 = "CUSTOM_INPUT_1";

	final String SECTION_ID_1 = "ELIGIBILITY";

	final String SECTION_ID_2 = "CUSTOM_SECTION_1";

	final String SECTION_TITLE_1 = "Eligibility";

	final String SECTION_TITLE_2 = "Project Status";

    final SchemeEntity scheme = SchemeEntity.builder()
            .id(1)
            .version(1)
            .funderId(1)
            .lastUpdated(instant)
            .email("test@and.digital")
            .name("Test Scheme")
            .ggisIdentifier("Test GGIS Identifier")
            .build();

    final SubmissionQuestion question1 = SubmissionQuestion.builder()
            .questionId(QUESTION_ID_1)
            .profileField("ORG_NAME")
            .fieldTitle("Enter the name of your organisation")
            .hintText(
                    "This is the official name of your organisation. It could be the name that is registered with Companies House or the Charities Commission")
            .responseType(ResponseTypeEnum.ShortAnswer)
            .response("organisationName")
            .validation(SubmissionQuestionValidation.builder().mandatory(true).minLength(2).maxLength(250).build())
            .build();

    final SubmissionQuestionDTO questionDTO1 = SubmissionQuestionDTO.builder()
            .questionId(question1.getQuestionId())
            .questionTitle(question1.getFieldTitle())
            .questionResponse(question1.getResponse())
            .build();

	final String[] address = { "addressLine1", "addressLine2", "town", "county", "postcode" };

    final SubmissionQuestion question2 = SubmissionQuestion.builder()
            .questionId(QUESTION_ID_2)
            .profileField("ORG_ADDRESS")
            .fieldTitle("Enter your organisation's address")
            .responseType(ResponseTypeEnum.AddressInput)
            .multiResponse(address)
            .validation(SubmissionQuestionValidation.builder().mandatory(true).build())
            .build();

    final SubmissionQuestion question3 = SubmissionQuestion.builder()
            .questionId(QUESTION_ID_3)
            .profileField("ORG_COMPANIES_HOUSE")
            .fieldTitle("Does your organisation have a Companies House number?")
            .hintText(
                    "Funding organisation might use this to identify your organisation when you apply for a grant. It might also be used to check your organisation is legitimate.")
            .responseType(ResponseTypeEnum.YesNo)
            .response("yes")
            .validation(SubmissionQuestionValidation.builder().minLength(5).maxLength(100).build())
            .build();

    final SubmissionQuestionDTO questionDTO3 = SubmissionQuestionDTO.builder()
            .questionId(question3.getQuestionId())
            .questionTitle(question3.getFieldTitle())
            .questionResponse(question3.getResponse())
            .build();

    final SubmissionQuestion question4 = SubmissionQuestion.builder()
            .questionId(QUESTION_ID_4)
            .fieldTitle(
                    "Description of the project, please include information regarding public accessibility (see GOV.UK guidance for a definition of public access) to the newly planted trees")
            .hintText("Optional additional helptext")
            .responseType(ResponseTypeEnum.LongAnswer)
            .validation(SubmissionQuestionValidation.builder()
                    .mandatory(true)
                    .minLength(100)
                    .maxLength(2000)
                    .minWords(50)
                    .maxWords(400)
                    .build())
            .response("description of the project")
            .build();

    final SubmissionQuestionDTO questionDTO4 = SubmissionQuestionDTO.builder()
            .questionId(question4.getQuestionId())
            .questionTitle(question4.getFieldTitle())
            .questionResponse(question4.getResponse())
            .build();

    final SubmissionSectionDTO submissionSectionDTO2 = SubmissionSectionDTO.builder()
            .sectionId(SECTION_ID_2)
            .sectionTitle(SECTION_TITLE_2)
            .questions(List.of(questionDTO4))
            .build();

    final AddressDTO addressDTO = AddressDTO.builder()
            .addressLine1(address[0])
            .addressLine2(address[1])
            .town(address[2])
            .county(address[3])
            .postcode(address[4])
            .build();

    final SubmissionQuestionDTO questionDTO2 = SubmissionQuestionDTO.builder()
            .questionId(question2.getQuestionId())
            .questionTitle(question2.getFieldTitle())
            .questionResponse(addressDTO)
            .build();

    final SubmissionSectionDTO submissionSectionDTO1 = SubmissionSectionDTO.builder()
            .sectionId(SECTION_ID_1)
            .sectionTitle(SECTION_TITLE_1)
            .questions(List.of(questionDTO1, questionDTO2, questionDTO3))
            .build();

	final String GGIS_REFERENCE_NUMBER = "SCH-000003589";

	// TODO find a nicer solution for the above objects and values

	@Mock
	private SubmissionsService submissionService;

	@InjectMocks
	private SubmissionsController controllerUnderTest;

	@Mock
	private Principal principal;



	@Test
	void getSubmissionsByGgisRefNum_ReturnsExpectedSubmissions() {

		final ApplicationListDTO applications = ApplicationListDTO.builder().build();

		when(principal.getName())
				.thenReturn(String.valueOf(FUNDING_ORGANISATION_ID));

		when(submissionService.getSubmissionsByFundingOrgIdAndGgisReferenceNum(FUNDING_ORGANISATION_ID, GGIS_REFERENCE_NUMBER))
				.thenReturn(applications);

		final ResponseEntity<ApplicationListDTO> methodResponse = controllerUnderTest.getSubmissionsByGgisRefNum(GGIS_REFERENCE_NUMBER, principal);

		verify(submissionService).getSubmissionsByFundingOrgIdAndGgisReferenceNum(FUNDING_ORGANISATION_ID, GGIS_REFERENCE_NUMBER);
		assertThat(methodResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(methodResponse.getBody()).isEqualTo(applications);
	}

	@Test
	void getSubmissionsByGgisRefNum_ThrowsSubmissionNotFoundException() {

        when(principal.getName())
                .thenReturn(String.valueOf(FUNDING_ORGANISATION_ID));

        when(submissionService.getSubmissionsByFundingOrgIdAndGgisReferenceNum(FUNDING_ORGANISATION_ID, GGIS_REFERENCE_NUMBER))
                .thenThrow(new SubmissionNotFoundException("No submissions found"));

        assertThatExceptionOfType(SubmissionNotFoundException.class)
                .isThrownBy(() -> controllerUnderTest.getSubmissionsByGgisRefNum(GGIS_REFERENCE_NUMBER, principal))
                .withMessage("No submissions found");
    }
}

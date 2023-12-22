package gov.cabinetoffice.api.mappers;

import gov.cabinetoffice.api.dtos.submission.AddressDTO;
import gov.cabinetoffice.api.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.dtos.submission.SubmissionQuestionDTO;
import gov.cabinetoffice.api.dtos.submission.SubmissionSectionDTO;
import gov.cabinetoffice.api.enums.ResponseTypeEnum;
import gov.cabinetoffice.api.enums.SubmissionSectionStatus;
import gov.cabinetoffice.api.enums.SubmissionStatus;
import gov.cabinetoffice.api.models.application.ApplicationDefinition;
import gov.cabinetoffice.api.models.submission.SubmissionDefinition;
import gov.cabinetoffice.api.models.submission.SubmissionQuestion;
import gov.cabinetoffice.api.models.submission.SubmissionQuestionValidation;
import gov.cabinetoffice.api.models.submission.SubmissionSection;
import gov.cabinetoffice.api.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.entities.SchemeEntity;
import gov.cabinetoffice.api.entities.Submission;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class SubmissionMapperTest {

	private static final int APPLICATION_ID = 1;

	final LocalDateTime timestamp = LocalDateTime.now();

	final ZonedDateTime zonedDateTime = ZonedDateTime.now();

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

	final ApplicationFormEntity application = ApplicationFormEntity.builder()
		.grantApplicationId(APPLICATION_ID)
		.applicationName("Test Application")
		.created(instant)
		.lastUpdated(instant)
		.definition(new ApplicationDefinition())
		.grantSchemeId(scheme.getId())
		.version(1)
		.lastUpdateBy(1)
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

	final SubmissionSection section1 = SubmissionSection.builder()
		.sectionId(SECTION_ID_1)
		.sectionTitle(SECTION_TITLE_1)
		.sectionStatus(String.valueOf(SubmissionSectionStatus.IN_PROGRESS))
		.questions(List.of(question1, question2, question3))
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

	final SubmissionSection section2 = SubmissionSection.builder()
		.sectionId(SECTION_ID_2)
		.sectionTitle(SECTION_TITLE_2)
		.sectionStatus(String.valueOf(SubmissionSectionStatus.IN_PROGRESS))
		.questions(List.of(question4))
		.build();

	final SubmissionDefinition definition = SubmissionDefinition.builder()
		.sections(List.of(section1, section2))
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

	final String[] date = { "01", "12", "1987" };

	private final UUID SUBMISSION_ID = UUID.fromString("1c2eabf0-b33c-433a-b00f-e73d8efca929");

	final Submission submission = Submission.builder()
		.id(SUBMISSION_ID)
		.scheme(scheme)
		.application(application)
		.version(1)
		.created(timestamp)
		.lastUpdated(timestamp)
		.applicationName("Test Submission")
		.status(SubmissionStatus.IN_PROGRESS)
		.definition(definition)
		.submittedDate(zonedDateTime)
		.build();

	final SubmissionDTO submissionDTO = SubmissionDTO.builder()
		.submissionId(SUBMISSION_ID)
		.submittedTimeStamp(zonedDateTime)
		.grantApplicantEmailAddress(scheme.getEmail())
		.sections(List.of(submissionSectionDTO1, submissionSectionDTO2))
		.build();

	private final SubmissionMapper submissionMapper = Mappers.getMapper(SubmissionMapper.class);

	@Test
	void submissionToSubmissionDto() {
		SubmissionDTO result = submissionMapper.submissionToSubmissionDto(submission);
		assertThat(result).isEqualTo(submissionDTO);
	}

	@Test
	void submissionToSubmissionDto_ReturnsNull() {
		SubmissionDTO result = submissionMapper.submissionToSubmissionDto(null);
		assertThat(result).isNull();
	}

	@Test
	void submissionToSubmissionDto_GrantAdminEmailAddress_isNull_IfSubmissionSchemeIsNull() {
		final Submission submission = Submission.builder()
				.definition(SubmissionDefinition.builder().build())
				.build();
		SubmissionDTO result = submissionMapper.submissionToSubmissionDto(submission);
		assertThat(result.getGrantApplicantEmailAddress()).isNull();
	}

	@Test
	void submissionToSubmissionDto_GrantAdminEmailAddress_isNull_IfSubmissionSchemeEmailAddressIsNull() {
		final SchemeEntity scheme = SchemeEntity.builder().build();
		final Submission submission = Submission.builder()
				.definition(SubmissionDefinition.builder().build())
				.scheme(scheme)
				.build();

		final SubmissionDTO result = submissionMapper.submissionToSubmissionDto(submission);

		assertThat(result.getGrantApplicantEmailAddress()).isNull();
	}

	@Test
	void mapSections() {
		List<SubmissionSectionDTO> result = submissionMapper.mapSections(submission.getDefinition().getSections());
		assertThat(result).isEqualTo(List.of(submissionSectionDTO1, submissionSectionDTO2));
	}

	@Test
	void submissionSectionToSubmissionSectionDto() {
		SubmissionSectionDTO result = submissionMapper.submissionSectionToSubmissionSectionDto(section1);
		assertThat(result).isEqualTo(submissionSectionDTO1);
	}

	@Test
	void submissionSectionToSubmissionSectionDto_ReturnsNull() {
		SubmissionSectionDTO result = submissionMapper.submissionSectionToSubmissionSectionDto(null);
		assertThat(result).isNull();
	}

	@Test
	void submissionSectionListToSubmissionSectionDtoList() {
		List<SubmissionSectionDTO> result = submissionMapper
			.submissionSectionListToSubmissionSectionDtoList(submission.getDefinition().getSections());
		assertThat(result).isEqualTo(List.of(submissionSectionDTO1, submissionSectionDTO2));
	}

	@Test
	void submissionQuestionListToSubmissionQuestionDtoList() {
		List<SubmissionQuestionDTO> result = submissionMapper
			.submissionQuestionListToSubmissionQuestionDtoList(section1.getQuestions());
		assertThat(result).isEqualTo(List.of(questionDTO1, questionDTO2, questionDTO3));
	}

	@Test
	void submissionQuestionToSubmissionQuestionDto() {
		SubmissionQuestionDTO result = submissionMapper.submissionQuestionToSubmissionQuestionDto(question1);
		assertThat(result).isEqualTo(questionDTO1);
	}

	@Test
	void submissionQuestionToSubmissionQuestionDto_HandlesNullValues() {
		final String questionTitle = "Enter the name of your organisation";
		final SubmissionQuestion question1 = SubmissionQuestion.builder()
				.questionId(QUESTION_ID_1)
				.profileField("ORG_NAME")
				.fieldTitle(questionTitle)
				.hintText(
						"This is the official name of your organisation. It could be the name that is registered with Companies House or the Charities Commission")
				.responseType(ResponseTypeEnum.ShortAnswer)
				.response(null)
				.multiResponse(null)
				.validation(SubmissionQuestionValidation.builder().mandatory(true).minLength(2).maxLength(250).build())
				.build();

		final SubmissionQuestionDTO dto = SubmissionQuestionDTO.builder()
				.questionId(QUESTION_ID_1)
				.questionTitle(questionTitle)
				.build();

		SubmissionQuestionDTO result = submissionMapper.submissionQuestionToSubmissionQuestionDto(question1);
		assertThat(result).isEqualTo(dto);
	}

	@Test
	void buildDate() {
		LocalDate result = submissionMapper.buildDate(date);
		assertThat(result).isEqualTo(LocalDate.of(1987, 12, 1));
	}

	@Test
	void buildAddress() {
		AddressDTO result = submissionMapper.buildAddress(address);
		assertThat(result).isEqualTo(addressDTO);
	}

	@Test
	void getQuestionResponseByResponseType__shortAnswerType() {
		Object result = submissionMapper.getQuestionResponseByResponseType(question1);
		assertThat(result).isEqualTo(question1.getResponse());
	}

	@Test
	void getQuestionResponseByResponseType__YesNoType() {
		Object result = submissionMapper.getQuestionResponseByResponseType(question3);
		assertThat(result).isEqualTo(question3.getResponse());
	}

	@Test
	void getQuestionResponseByResponseType__LongAnswerType() {
		Object result = submissionMapper.getQuestionResponseByResponseType(question4);
		assertThat(result).isEqualTo(question4.getResponse());
	}

	@Test
	void getQuestionResponseByResponseType__DropdownType() {
		SubmissionQuestion submissionQuestion = SubmissionQuestion.builder()
			.questionId("testId")
			.responseType(ResponseTypeEnum.Dropdown)
			.fieldTitle("Test Dropdown")
			.response("Test Dropdown Response")
			.build();
		Object result = submissionMapper.getQuestionResponseByResponseType(submissionQuestion);
		assertThat(result).isEqualTo(submissionQuestion.getResponse());
	}

	@Test
	void getQuestionResponseByResponseType__NumericType() {
		SubmissionQuestion submissionQuestion = SubmissionQuestion.builder()
			.questionId("testId")
			.responseType(ResponseTypeEnum.Numeric)
			.fieldTitle("Test Numeric")
			.response("1")
			.build();
		Object result = submissionMapper.getQuestionResponseByResponseType(submissionQuestion);
		assertThat(result).isEqualTo(submissionQuestion.getResponse());
	}

	@Test
	void getQuestionResponseByResponseType__MultipleSelectionType() {
		String[] multiResponse = { "a", "b", "c" };
		SubmissionQuestion submissionQuestion = SubmissionQuestion.builder()
			.questionId("testId")
			.responseType(ResponseTypeEnum.MultipleSelection)
			.fieldTitle("Test Multiple selection")
			.multiResponse(multiResponse)
			.build();
		Object result = submissionMapper.getQuestionResponseByResponseType(submissionQuestion);
		assertThat(result).isEqualTo(submissionQuestion.getMultiResponse());
	}

	@Test
	void getQuestionResponseByResponseType__AddressType() {
		AddressDTO addressDTO = AddressDTO.builder()
			.addressLine1(address[0])
			.addressLine2(address[1])
			.town(address[2])
			.county(address[3])
			.postcode(address[4])
			.build();
		Object result = submissionMapper.getQuestionResponseByResponseType(question2);
		assertThat(result).isEqualTo(addressDTO);
	}

	@Test
	void getQuestionResponseByResponseType__DateType() {
		String[] date = { "01", "12", "1987" };
		SubmissionQuestion submissionQuestion = SubmissionQuestion.builder()
			.questionId("testId")
			.responseType(ResponseTypeEnum.Date)
			.fieldTitle("Test Date")
			.multiResponse(date)
			.build();
		LocalDate expectedResult = LocalDate.of(1987, 12, 1);
		Object result = submissionMapper.getQuestionResponseByResponseType(submissionQuestion);
		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	void getQuestionResponseByResponseType__Default() {
		SubmissionQuestion submissionQuestion = SubmissionQuestion.builder()
			.questionId("testId")
			.responseType(ResponseTypeEnum.SingleSelection)
			.fieldTitle("Test singleSelection")
			.multiResponse(date)
			.build();
		Object result = submissionMapper.getQuestionResponseByResponseType(submissionQuestion);
		assertThat(result).isEqualTo("");
	}

}
package gov.cabinetoffice.api.prototype.mappers;

import gov.cabinetoffice.api.prototype.config.S3ConfigProperties;
import gov.cabinetoffice.api.prototype.dtos.submission.AddressDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionQuestionDTO;
import gov.cabinetoffice.api.prototype.dtos.submission.SubmissionSectionDTO;
import gov.cabinetoffice.api.prototype.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.prototype.entities.GrantApplicant;
import gov.cabinetoffice.api.prototype.entities.GrantApplicantOrganisationProfile;
import gov.cabinetoffice.api.prototype.entities.GrantAttachment;
import gov.cabinetoffice.api.prototype.entities.SchemeEntity;
import gov.cabinetoffice.api.prototype.entities.Submission;
import gov.cabinetoffice.api.prototype.enums.ResponseTypeEnum;
import gov.cabinetoffice.api.prototype.enums.SubmissionSectionStatus;
import gov.cabinetoffice.api.prototype.enums.SubmissionStatus;
import gov.cabinetoffice.api.prototype.models.application.ApplicationDefinition;
import gov.cabinetoffice.api.prototype.models.submission.SubmissionDefinition;
import gov.cabinetoffice.api.prototype.models.submission.SubmissionQuestion;
import gov.cabinetoffice.api.prototype.models.submission.SubmissionQuestionValidation;
import gov.cabinetoffice.api.prototype.models.submission.SubmissionSection;
import gov.cabinetoffice.api.prototype.services.GrantAttachmentService;
import gov.cabinetoffice.api.prototype.services.S3Service;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
class CustomSubmissionMapperImplTest {

	@Mock
	S3ConfigProperties s3ConfigProperties;

	@Mock
	S3Service s3Service;

	@InjectMocks
	CustomSubmissionMapperImpl customSubmissionMapper;

	private static final int APPLICATION_ID = 1;

	private final UUID GRANT_ATTACHMENT_ID = UUID.randomUUID();

	final LocalDateTime timestamp = LocalDateTime.now();

	final ZonedDateTime zonedDateTime = ZonedDateTime.now();

	final Instant instant = Instant.now();

	final String QUESTION_ID_1 = "APPLICANT_ORG_NAME";

	final String QUESTION_ID_2 = "APPLICANT_ORG_ADDRESS";

	final String QUESTION_ID_3 = "APPLICANT_ORG_COMPANIES_HOUSE";

	final String QUESTION_ID_4 = "CUSTOM_INPUT_1";

	final String QUESTION_ID_5 = "FILE_UPLOAD_1";

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
		.sectionStatus(SubmissionSectionStatus.IN_PROGRESS)
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
		.sectionStatus(SubmissionSectionStatus.IN_PROGRESS)
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

	private final long APPLICANT_ID = 1;

	private final long PROFILE_ID = 1;

	final GrantApplicantOrganisationProfile grantApplicantOrganisationProfile = GrantApplicantOrganisationProfile
		.builder()
		.id(PROFILE_ID)
		.legalName("AND Digital")
		.charityCommissionNumber("45")
		.companiesHouseNumber("000010")
		.addressLine1("AND Digital")
		.addressLine2("9 George Square")
		.town("Glasgow")
		.postcode("G2 1QQ")
		.county("Renfrewshire")
		.build();

	private final UUID APPLICANT_USER_ID = UUID.fromString("75ab5fbd-0682-4d3d-a467-01c7a447f07c");

	final GrantApplicant grantApplicant = GrantApplicant.builder()
		.id(APPLICANT_ID)
		.userId(APPLICANT_USER_ID)
		.organisationProfile(grantApplicantOrganisationProfile)
		.build();

	private final UUID SUBMISSION_ID = UUID.fromString("1c2eabf0-b33c-433a-b00f-e73d8efca929");

	final Submission submission = Submission.builder()
		.id(SUBMISSION_ID)
		.applicant(grantApplicant)
		.scheme(scheme)
		.application(application)
		.version(1)
		.created(timestamp)
		.createdBy(grantApplicant)
		.lastUpdated(timestamp)
		.lastUpdatedBy(grantApplicant)
		.applicationName("Test Submission")
		.status(SubmissionStatus.IN_PROGRESS)
		.definition(definition)
		.submittedDate(zonedDateTime)
		.build();

	final SubmissionDTO submissionDTO = SubmissionDTO.builder()
		.submissionId(SUBMISSION_ID)
		.applicationFormName(application.getApplicationName())
		.ggisReferenceNumber(scheme.getGgisIdentifier())
		.grantAdminEmailAddress(scheme.getEmail())
		.submittedTimeStamp(zonedDateTime)
		.grantApplicantEmailAddress(scheme.getEmail())
		.sections(List.of(submissionSectionDTO1, submissionSectionDTO2))
		.build();

	@InjectMocks
	private CustomSubmissionMapperImpl customSubmissionMapperImpl;

	@Mock
	GrantAttachmentService grantAttachmentService;

	// didn't test the other methods because they are tested in SubmissionMapperTest

	@Test
	void submissionToSubmissionDto() {
		SubmissionDTO result = customSubmissionMapperImpl.submissionToSubmissionDto(submission);
		assertThat(result).isEqualTo(submissionDTO);
	}

	@Test
	void submissionSectionToSubmissionSectionDto() {
		SubmissionSectionDTO result = customSubmissionMapperImpl.submissionSectionToSubmissionSectionDto(section1);
		assertThat(result).isEqualTo(submissionSectionDTO1);
	}

	// @Test
	// void buildUploadResponse() {
	// SubmissionQuestion submissionQuestion = SubmissionQuestion.builder()
	// .questionId("testId")
	// .responseType(ResponseTypeEnum.SingleFileUpload)
	// .fieldTitle("Test Upload")
	// .multiResponse(date)
	// .attachmentId(GRANT_ATTACHMENT_ID)
	// .build();
	//
	// GrantAttachment grantAttachment = GrantAttachment.builder()
	// .id(GRANT_ATTACHMENT_ID)
	// .location("location")
	// .build();
	//
	// when(grantAttachmentService.getGrantAttachmentById(GRANT_ATTACHMENT_ID)).thenReturn(grantAttachment);
	// String result = customSubmissionMapperImpl.buildUploadResponse(submissionQuestion);
	// assertThat(result).isEqualTo(grantAttachment.getLocation());
	// }

}
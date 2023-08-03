package gov.cabinetoffice.api.mappers;

import gov.cabinetoffice.api.config.S3ConfigProperties;
import gov.cabinetoffice.api.dtos.submission.ApplicationDto;
import gov.cabinetoffice.api.dtos.submission.ApplicationListDTO;
import gov.cabinetoffice.api.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.dtos.submission.SubmissionSectionDTO;
import gov.cabinetoffice.api.entities.ApplicationFormEntity;
import gov.cabinetoffice.api.entities.GrantAttachment;
import gov.cabinetoffice.api.entities.SchemeEntity;
import gov.cabinetoffice.api.entities.Submission;
import gov.cabinetoffice.api.models.submission.SubmissionDefinition;
import gov.cabinetoffice.api.models.submission.SubmissionQuestion;
import gov.cabinetoffice.api.models.submission.SubmissionSection;
import gov.cabinetoffice.api.services.GrantAttachmentService;
import gov.cabinetoffice.api.services.S3Service;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CustomSubmissionMapperImplTest {

	@Mock
	private GrantAttachmentService grantAttachmentService;

	@Mock
	private S3Service s3Service;

	@Mock
	private S3ConfigProperties s3ConfigProperties;

	@InjectMocks
	private CustomSubmissionMapperImpl customSubmissionMapperImpl;

	@Test
	void buildUploadResponse() {
		final UUID GRANT_ATTACHMENT_ID = UUID.randomUUID();
		final String expectedResult = "presignedUrl";

		final SubmissionQuestion submissionQuestion = SubmissionQuestion.builder()
			.attachmentId(GRANT_ATTACHMENT_ID)
			.build();

		final GrantAttachment grantAttachment = GrantAttachment.builder()
			.id(GRANT_ATTACHMENT_ID)
			.location("www.amazonaws.com/location")
			.build();

		when(grantAttachmentService.getGrantAttachmentById(GRANT_ATTACHMENT_ID)).thenReturn(grantAttachment);
		when(s3Service.createPresignedURL(any(), any())).thenReturn(expectedResult);

		final String result = customSubmissionMapperImpl.buildUploadResponse(submissionQuestion);
		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	void submissionToSubmissionDto() {
		final Submission submission = SubmissionMapperTestData.getSubmission();
		final SubmissionDTO submissionDto = SubmissionMapperTestData.getSubmissionDto();

		final SubmissionDTO result = customSubmissionMapperImpl.submissionToSubmissionDto(submission);

		assertThat(result).isEqualTo(submissionDto);
	}

	@Test
	void submissionToSubmissionDto_returnNullIfSubmissionIsNull() {
		final SubmissionDTO result = customSubmissionMapperImpl.submissionToSubmissionDto(null);
		assertThat(result).isNull();
	}

	@Test
	void submissionSectionToSubmissionSectionDto() {
		final SubmissionSection section = SubmissionMapperTestData.getSubmissionSection();
		final SubmissionSectionDTO sectionDTO = SubmissionMapperTestData.getSubmissionSectionDto();

		final SubmissionSectionDTO result = customSubmissionMapperImpl
			.submissionSectionToSubmissionSectionDto(section);

		assertThat(result).isEqualTo(sectionDTO);
	}

	@Test
	void submissionSectionToSubmissionSectionDto_returnNullIfSubmissionIsNull() {
		final SubmissionSectionDTO result = customSubmissionMapperImpl.submissionSectionToSubmissionSectionDto(null);
		assertThat(result).isNull();
	}

	@Test
	void submissionApplicationApplicationName() {
		final Submission submission = SubmissionMapperTestData.getSubmission();

		final String result = customSubmissionMapperImpl.submissionApplicationApplicationName(submission);

		assertThat(result).isEqualTo(submission.getApplication().getApplicationName());
	}

	@Test
	void submissionApplicationApplicationName_returnNullIfSubmissionIsNull() {
		final String result = customSubmissionMapperImpl.submissionApplicationApplicationName(null);
		assertThat(result).isNull();
	}

	@Test
	void submissionApplicationApplicationName_returnNullIfSubmissionApplicationIsNull() {
		final Submission submission = Submission.builder().application(null).build();
		final String result = customSubmissionMapperImpl.submissionApplicationApplicationName(submission);
		assertThat(result).isNull();
	}

	@Test
	void submissionSchemeEmail() {
		final Submission submission = SubmissionMapperTestData.getSubmission();

		final String result = customSubmissionMapperImpl.submissionSchemeEmail(submission);

		assertThat(result).isEqualTo(submission.getScheme().getEmail());
	}

	@Test
	void submissionSchemeEmail_returnNullIfSubmissionIsNull() {
		final String result = customSubmissionMapperImpl.submissionSchemeEmail(null);
		assertThat(result).isNull();
	}

	@Test
	void submissionSchemeEmail_returnNullIfSubmissionSchemeIsNull() {
		final Submission submission = Submission.builder().scheme(null).build();
		final String result = customSubmissionMapperImpl.submissionSchemeEmail(submission);
		assertThat(result).isNull();
	}

	@Test
	void submissionSchemeGgisIdentifier() {
		final Submission submission = SubmissionMapperTestData.getSubmission();

		final String result = customSubmissionMapperImpl.submissionSchemeGgisIdentifier(submission);

		assertThat(result).isEqualTo(submission.getScheme().getGgisIdentifier());
	}

	@Test
	void submissionSchemeGgisIdentifier_returnNullIfSubmissionIsNull() {
		final String result = customSubmissionMapperImpl.submissionSchemeGgisIdentifier(null);
		assertThat(result).isNull();
	}

	@Test
	void submissionSchemeGgisIdentifier_returnNullIfSubmissionSchemeIsNull() {
		final Submission submission = Submission.builder().scheme(null).build();
		final String result = customSubmissionMapperImpl.submissionSchemeGgisIdentifier(submission);
		assertThat(result).isNull();
	}

	@Test
	void submissionDefinitionSections() {
		final Submission submission = SubmissionMapperTestData.getSubmission();

		final List<SubmissionSection> result = customSubmissionMapperImpl.submissionDefinitionSections(submission);

		assertThat(result).isEqualTo(submission.getDefinition().getSections());
	}

	@Test
	void submissionDefinitionSections_returnNullIfSubmissionIsNull() {
		final List<SubmissionSection> result = customSubmissionMapperImpl.submissionDefinitionSections(null);
		assertThat(result).isNull();
	}

	@Test
	void submissionDefinitionSections_returnNullIfSubmissionDefintionIsNull() {
		final Submission submission = Submission.builder().definition(null).build();
		final List<SubmissionSection> result = customSubmissionMapperImpl.submissionDefinitionSections(submission);
		assertThat(result).isNull();
	}

	@Test
	void submissionListToApplicationListDto_HandlesEmptyList() {
		final List<Submission> submissions = Collections.emptyList();

		final ApplicationListDTO applications = customSubmissionMapperImpl.submissionListToApplicationListDto(submissions);

		assertThat(applications.getApplications()).isEmpty();
		assertThat(applications.getNumberOfResults()).isZero();
	}

	@Test
	void submissionListToApplicationListDto_HandlesNullList() {
		final List<Submission> submissions = null;

		final ApplicationListDTO applications = customSubmissionMapperImpl.submissionListToApplicationListDto(submissions);

		assertThat(applications.getApplications()).isEmpty();
		assertThat(applications.getNumberOfResults()).isZero();
	}

	@Test
	void submissionListToApplicationListDto_ReturnsExpectedResult() {

		// build a submission for one application form
		final ApplicationFormEntity application = ApplicationFormEntity.builder()
				.grantApplicationId(1)
				.applicationName("Woodland services grant")
				.build();

		final SchemeEntity scheme = SchemeEntity.builder()
				.ggisIdentifier("WSG-001")
				.email("gavin.cook@and.digital")
				.build();

		final UUID submissionId1 = UUID.randomUUID();
		final Submission submission = Submission.builder()
				.id(submissionId1)
				.application(application)
				.scheme(scheme)
				.definition(
						SubmissionDefinition
						.builder()
						.sections(Collections.emptyList())
						.build()
				)
				.build();

		// build a second submission for a different application form
		final ApplicationFormEntity application2 = ApplicationFormEntity.builder()
				.grantApplicationId(2)
				.applicationName("Suburban services grant")
				.build();

		final SchemeEntity scheme2 = SchemeEntity.builder()
				.ggisIdentifier("WSG-001")
				.email("gavin.cook@and.digital")
				.build();

		final UUID submissionId2 = UUID.randomUUID();
		final Submission submission2 = Submission.builder()
				.id(submissionId2)
				.application(application2)
				.scheme(scheme2)
				.definition(
						SubmissionDefinition
								.builder()
								.sections(Collections.emptyList())
								.build()
				)
				.build();

		final List<Submission> submissions = List.of(submission, submission2);

		// with a bit of luck, we'll have two Application DTOs here
		final ApplicationListDTO methodResponse = customSubmissionMapperImpl.submissionListToApplicationListDto(submissions);

		assertThat(methodResponse.getNumberOfResults()).isEqualTo(2);

		final ApplicationDto applicationDto1 = methodResponse.getApplications().get(0);
		assertThat(applicationDto1.getApplicationFormName()).isEqualTo(application.getApplicationName());
		assertThat(applicationDto1.getGgisReferenceNumber()).isEqualTo(scheme.getGgisIdentifier());
		assertThat(applicationDto1.getSubmissions()).hasSize(1);
		assertThat(applicationDto1.getSubmissions().get(0).getSubmissionId()).isEqualTo(submissionId1);

		final ApplicationDto applicationDto2 = methodResponse.getApplications().get(1);
		assertThat(applicationDto2.getApplicationFormName()).isEqualTo(application2.getApplicationName());
		assertThat(applicationDto2.getGgisReferenceNumber()).isEqualTo(scheme2.getGgisIdentifier());
		assertThat(applicationDto2.getSubmissions()).hasSize(1);
		assertThat(applicationDto2.getSubmissions().get(0).getSubmissionId()).isEqualTo(submissionId2);
	}

}
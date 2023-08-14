package gov.cabinetoffice.api.mappers;

import gov.cabinetoffice.api.dtos.submission.AddressDTO;
import gov.cabinetoffice.api.dtos.submission.SubmissionDTO;
import gov.cabinetoffice.api.dtos.submission.SubmissionQuestionDTO;
import gov.cabinetoffice.api.dtos.submission.SubmissionSectionDTO;
import gov.cabinetoffice.api.entities.*;
import gov.cabinetoffice.api.enums.ResponseTypeEnum;
import gov.cabinetoffice.api.enums.SubmissionSectionStatus;
import gov.cabinetoffice.api.enums.SubmissionStatus;
import gov.cabinetoffice.api.models.application.ApplicationDefinition;
import gov.cabinetoffice.api.models.submission.SubmissionDefinition;
import gov.cabinetoffice.api.models.submission.SubmissionQuestion;
import gov.cabinetoffice.api.models.submission.SubmissionQuestionValidation;
import gov.cabinetoffice.api.models.submission.SubmissionSection;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class SubmissionMapperTestData {

    private static final int APPLICATION_ID = 1;

    private static final LocalDateTime timestamp = LocalDateTime.now();

    private static final ZonedDateTime zonedDateTime = ZonedDateTime.now();

    private static final Instant instant = Instant.now();

    private static final String QUESTION_ID_1 = "APPLICANT_ORG_NAME";

    private static final String QUESTION_ID_2 = "APPLICANT_ORG_ADDRESS";

    private static final String QUESTION_ID_3 = "APPLICANT_ORG_COMPANIES_HOUSE";

    private static final String SECTION_ID_1 = "ELIGIBILITY";

    private static final String SECTION_TITLE_1 = "Eligibility";

    private static final SchemeEntity scheme = SchemeEntity.builder()
            .id(1)
            .version(1)
            .funderId(1)
            .lastUpdated(instant)
            .email("test@and.digital")
            .name("Test Scheme")
            .ggisIdentifier("Test GGIS Identifier")
            .build();

    private static final ApplicationFormEntity application = ApplicationFormEntity.builder()
            .grantApplicationId(APPLICATION_ID)
            .applicationName("Test Application")
            .created(instant)
            .lastUpdated(instant)
            .definition(new ApplicationDefinition())
            .grantSchemeId(scheme.getId())
            .version(1)
            .lastUpdateBy(1)
            .build();

    private static final SubmissionQuestion question1 = SubmissionQuestion.builder()
            .questionId(QUESTION_ID_1)
            .profileField("ORG_NAME")
            .fieldTitle("Enter the name of your organisation")
            .hintText(
                    "This is the official name of your organisation. It could be the name that is registered with Companies House or the Charities Commission")
            .responseType(ResponseTypeEnum.ShortAnswer)
            .response("organisationName")
            .validation(SubmissionQuestionValidation.builder().mandatory(true).minLength(2).maxLength(250).build())
            .build();

    private static final String[] address = { "addressLine1", "addressLine2", "town", "county", "postcode" };

    private static final SubmissionQuestion question2 = SubmissionQuestion.builder()
            .questionId(QUESTION_ID_2)
            .profileField("ORG_ADDRESS")
            .fieldTitle("Enter your organisation's address")
            .responseType(ResponseTypeEnum.AddressInput)
            .multiResponse(address)
            .validation(SubmissionQuestionValidation.builder().mandatory(true).build())
            .build();

    private static final SubmissionQuestion question3 = SubmissionQuestion.builder()
            .questionId(QUESTION_ID_3)
            .profileField("ORG_COMPANIES_HOUSE")
            .fieldTitle("Does your organisation have a Companies House number?")
            .hintText(
                    "Funding organisation might use this to identify your organisation when you apply for a grant. It might also be used to check your organisation is legitimate.")
            .responseType(ResponseTypeEnum.YesNo)
            .response("yes")
            .validation(SubmissionQuestionValidation.builder().minLength(5).maxLength(100).build())
            .build();

    private static final String[] date = { "01", "12", "1987" };
    private static final SubmissionQuestion question4 = SubmissionQuestion.builder()
            .questionId("test")
            .responseType(ResponseTypeEnum.Date)
            .multiResponse(date)
            .build();

    private static final SubmissionSection section1 = SubmissionSection.builder()
            .sectionId(SECTION_ID_1)
            .sectionTitle(SECTION_TITLE_1)
            .sectionStatus(String.valueOf(SubmissionSectionStatus.IN_PROGRESS))
            .questions(List.of(question1, question2, question3, question4))
            .build();

    private static final SubmissionDefinition definition = SubmissionDefinition.builder()
            .sections(List.of(section1))
            .build();

    private static final long APPLICANT_ID = 1;

    private static final long PROFILE_ID = 1;

    private static final GrantApplicantOrganisationProfile grantApplicantOrganisationProfile = GrantApplicantOrganisationProfile
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

    private static final String APPLICANT_USER_ID = "75ab5fbd-0682-4d3d-a467-01c7a447f07c";

    private static final GrantApplicant grantApplicant = GrantApplicant.builder()
            .id(APPLICANT_ID)
            .userId(APPLICANT_USER_ID)
            .organisationProfile(grantApplicantOrganisationProfile)
            .build();

    private static final UUID SUBMISSION_ID = UUID.fromString("1c2eabf0-b33c-433a-b00f-e73d8efca929");

    private static final Submission submission = Submission.builder()
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


    private static final SubmissionQuestionDTO questionDTO1 = SubmissionQuestionDTO.builder()
            .questionId(question1.getQuestionId())
            .questionTitle(question1.getFieldTitle())
            .questionResponse(question1.getResponse())
            .build();

    private static final SubmissionQuestionDTO questionDTO3 = SubmissionQuestionDTO.builder()
            .questionId(question3.getQuestionId())
            .questionTitle(question3.getFieldTitle())
            .questionResponse(question3.getResponse())
            .build();

    private static final AddressDTO addressDTO = AddressDTO.builder()
            .addressLine1(address[0])
            .addressLine2(address[1])
            .town(address[2])
            .county(address[3])
            .postcode(address[4])
            .build();

    private static final SubmissionQuestionDTO questionDTO2 = SubmissionQuestionDTO.builder()
            .questionId(question2.getQuestionId())
            .questionTitle(question2.getFieldTitle())
            .questionResponse(addressDTO)
            .build();

    private static final SubmissionQuestionDTO questionDTO4 = SubmissionQuestionDTO.builder()
            .questionId(question4.getQuestionId())
            .questionTitle(question4.getFieldTitle())
            .questionResponse(LocalDate.of(1987, 12, 1))
            .build();

    private static final SubmissionSectionDTO submissionSectionDTO1 = SubmissionSectionDTO.builder()
            .sectionId(SECTION_ID_1)
            .sectionTitle(SECTION_TITLE_1)
            .questions(List.of(questionDTO1, questionDTO2, questionDTO3, questionDTO4))
            .build();

    private static final SubmissionDTO submissionDTO = SubmissionDTO.builder()
            .submissionId(SUBMISSION_ID)
            .submittedTimeStamp(zonedDateTime)
            .grantApplicantEmailAddress(scheme.getEmail())
            .sections(List.of(submissionSectionDTO1))
            .build();

    public static Submission getSubmission() {
        return submission;
    }

    public static SubmissionDTO getSubmissionDto() {
        return submissionDTO;
    }

    public static SubmissionSection getSubmissionSection() {
        return section1;
    }

    public static SubmissionSectionDTO getSubmissionSectionDto() {
        return submissionSectionDTO1;
    }
}

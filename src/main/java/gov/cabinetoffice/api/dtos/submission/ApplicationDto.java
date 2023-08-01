package gov.cabinetoffice.api.dtos.submission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationDto {

    private String applicationFormName;

    private String grantAdminEmailAddress;

    private String ggisReferenceNumber;

    @Builder.Default
    private List<SubmissionDTO> submissions = new ArrayList<>();
}
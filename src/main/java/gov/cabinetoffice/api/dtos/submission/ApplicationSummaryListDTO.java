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
public class ApplicationSummaryListDTO {
    // total number of applications across all pages
    private int totalApplications;

    // total number of pages of applications in the result set (page size 100)
    private int totalApplicationPages;

    @Builder.Default
    private List<ApplicationSummaryDTO> applications = new ArrayList<>();
}

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
public class ApplicationListDTO {
    // number of applications in this page window (distinct applications)
    private int numberOfResults;

    // number of submissions in this page window
    private int submissionCount;

    @Builder.Default
    private List<ApplicationDTO> applications = new ArrayList<>();
}

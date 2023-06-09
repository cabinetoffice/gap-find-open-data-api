package gov.cabinetoffice.api.prototype.dtos.submission;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.cabinetoffice.api.prototype.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionDefinition {

    @Builder.Default
    private List<SubmissionSection> sections = new ArrayList<>();

    public static SubmissionDefinition transformApplicationDefinitionToSubmissionOne(String applicationDefinitionJson)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(applicationDefinitionJson, SubmissionDefinition.class);
    }

    public SubmissionSection getSectionById(String sectionId) {
        return this.sections.stream().filter((section) -> Objects.equals(section.getSectionId(), sectionId)).findFirst()
                .orElseThrow(() -> new NotFoundException("Section with id " + sectionId + " does not exist"));
    }

}

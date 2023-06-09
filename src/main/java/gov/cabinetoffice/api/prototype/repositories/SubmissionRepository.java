package gov.cabinetoffice.api.prototype.repositories;

import gov.cabinetoffice.api.prototype.entities.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, UUID> {

    List<Submission> findByApplicationGrantApplicationId(Integer applicationId);

}

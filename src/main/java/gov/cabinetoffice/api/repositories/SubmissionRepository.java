package gov.cabinetoffice.api.repositories;

import gov.cabinetoffice.api.entities.Submission;
import gov.cabinetoffice.api.enums.SubmissionStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
	List<Submission> findByStatusAndApplicationGrantApplicationId(SubmissionStatus status, Integer applicationId);

	List<Submission> findByStatusAndApplicationGrantApplicationId(SubmissionStatus status, Integer applicationId, Pageable pageable);

	int countByStatusAndApplicationGrantApplicationId(SubmissionStatus status, Integer applicationId);
}

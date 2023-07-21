package gov.cabinetoffice.api.repositories;

import gov.cabinetoffice.api.entities.ApplicationFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationFormRepository extends JpaRepository<ApplicationFormEntity, Integer> {

}

package gov.cabinetoffice.api.prototype.repositories;

import gov.cabinetoffice.api.prototype.entities.GrantAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GrantAttachmentRepository extends JpaRepository<GrantAttachment, UUID> {

}

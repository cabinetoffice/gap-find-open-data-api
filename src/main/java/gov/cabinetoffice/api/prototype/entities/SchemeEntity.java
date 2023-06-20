package gov.cabinetoffice.api.prototype.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "grant_scheme")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchemeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "grant_scheme_id")
	private Integer id;

	@Column(name = "funder_id", nullable = false)
	private Integer funderId;

	@Column(name = "version", nullable = false)
	@Builder.Default
	private Integer version = 1;

	@Column(name = "created_date", nullable = false)
	@Builder.Default
	private Instant createdDate = Instant.now();

	@Column(name = "created_by", nullable = false)
	private Integer createdBy;

	@Column(name = "last_updated")
	private Instant lastUpdated;

	@Column(name = "last_updated_by")
	private Integer lastUpdatedBy;

	@Column(name = "ggis_identifier", nullable = false)
	private String ggisIdentifier;

	@Column(name = "scheme_name", nullable = false)
	private String name;

	@Column(name = "scheme_contact")
	private String email;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		SchemeEntity that = (SchemeEntity) o;
		return this.id != null && Objects.equals(this.id, that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}

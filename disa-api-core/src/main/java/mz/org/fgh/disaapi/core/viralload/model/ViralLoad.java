/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import mz.co.msaude.boot.frameworks.model.GenericEntity;
import mz.org.fgh.disaapi.core.viralload.dao.ViralLoadDAO;

/**
 * @author Stélio Moiane
 *
 */
@NamedQueries({ @NamedQuery(name = ViralLoadDAO.QUERY_NAME.findAll, query = ViralLoadDAO.QUERY.findAll) })
@Entity
@Table(name = "VlData")
public class ViralLoad extends GenericEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "REFNO")
	private String nid;

	@Column(name = "DOB")
	private LocalDate dateOfBirth;

	public String getNid() {
		return this.nid;
	}

	public void setNid(final String nid) {
		this.nid = nid;
	}

	public LocalDate getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(final LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

}

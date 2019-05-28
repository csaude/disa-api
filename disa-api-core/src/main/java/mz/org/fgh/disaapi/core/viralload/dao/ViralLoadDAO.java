/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.dao;

import java.util.List;

import mz.co.msaude.boot.frameworks.dao.GenericDAO;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;

/**
 * @author Stélio Moiane
 *
 */
public interface ViralLoadDAO extends GenericDAO<ViralLoad, Long> {
	class QUERY {
		public static final String findAll = "SELECT vl FROM ViralLoad vl";
	}

	class QUERY_NAME {
		public static final String findAll = "ViralLoad.findAll";
	}

	List<ViralLoad> findAll() throws BusinessException;
}

/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.stereotype.Service;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.co.msaude.boot.frameworks.model.UserContext;
import mz.co.msaude.boot.frameworks.service.AbstractService;
import mz.org.fgh.disaapi.core.exception.NotFoundBusinessException;
import mz.org.fgh.disaapi.core.viralload.dao.ViralLoadDAO;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;

/**
 * @author Stélio Moiane
 *
 */
@Service(ViralLoadServiceImpl.NAME)
public class ViralLoadServiceImpl extends AbstractService implements ViralLoadService {

	public static final String NAME = "mz.org.fgh.disaapi.core.viralload.service.ViralLoadServiceImpl";

	@Inject
	private ViralLoadDAO viralLoadDAO;

	@Override
	public ViralLoad createViralLoad(final UserContext context, final ViralLoad viralLoad) throws BusinessException {
		this.viralLoadDAO.create(context, viralLoad);
		return viralLoad;
	}

	@Override
	public ViralLoad updateViralLoad(UserContext context, ViralLoad viralLoad) throws BusinessException {
		this.viralLoadDAO.update(context, viralLoad);
		return viralLoad;
	}

	@Override
	public ViralLoad updateViralLoad(UserContext context, ViralLoad viralLoad, Map<String, Object> propertyValues)
			throws BusinessException {


		List<String> requestIds = Arrays.asList(viralLoad.getRequestId());
		List<ViralLoad> vls = this.viralLoadDAO.findViralLoadByRequestId(requestIds, EntityStatus.ACTIVE);

		if (vls.isEmpty()) {
			throw new NotFoundBusinessException(
					"Viral load " + viralLoad.getRequestId() + " was not found.");
		}

		ViralLoad dbVl = vls.get(0);

		validateStatus(viralLoad, dbVl);

		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(dbVl);
		bw.setPropertyValues(propertyValues);

		return this.updateViralLoad(context, (ViralLoad) bw.getWrappedInstance());
	}

	private void validateStatus(ViralLoad viralLoad, ViralLoad dbVl) throws BusinessException {
		if (ViralLoadStatus.PROCESSED == dbVl.getViralLoadStatus()) {
			throw new BusinessException(
					"Cannot reschedule viral load " + viralLoad.getRequestId() + ". It has already been processed.");
		}
	}
}

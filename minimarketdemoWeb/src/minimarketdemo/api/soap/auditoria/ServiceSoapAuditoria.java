package minimarketdemo.api.soap.auditoria;

import java.util.Date;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;

import com.sun.tools.sjavac.comp.dependencies.PublicApiCollector;

import java.util.List;

import minimarketdemo.model.auditoria.managers.ManagerAuditoria;
import minimarketdemo.model.core.entities.AudBitacora;
import minimarketdemo.model.core.utils.ModelUtil;

@WebService(serviceName = "ServiceSOAPAuditoria")
public class ServiceSoapAuditoria {
	@EJB
	private ManagerAuditoria mAuditoria;

	@WebMethod(operationName = "findBitacoraAyer")
	public List<AudBitacora> findBitacora() {
		return mAuditoria.findBitacoraByFecha(ModelUtil.addDays(new Date(), -1), new Date());
	}

	/**@WebMethod(operationName = "eliminarBitacora")
	public void eliminarBitacora() {
		mAuditoria.eliminarBitacora();
	}**/

}

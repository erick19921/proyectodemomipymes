package minimarketdemo.controller.seguridades;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import minimarketdemo.model.core.entities.SegModulo;
import minimarketdemo.model.seguridades.managers.ManagerSeguridades;

@Named
@RequestScoped
public class BeanSegModulos implements Serializable {
	@EJB
	private ManagerSeguridades mSeguridades;
	private List<SegModulo> listaModulos;

	public BeanSegModulos() {
		
	}
	@PostConstruct
	public void inicializacion() {
		System.out.println("BeanSegModulos inicializado...");
	}
	
	public String actionCargarMenuModulos() {
		listaModulos=mSeguridades.findAllModulos();
		return "modulos";
	}

	public List<SegModulo> getListaModulos() {
		return listaModulos;
	}

	public void setListaModulos(List<SegModulo> listaModulos) {
		this.listaModulos = listaModulos;
	}

}

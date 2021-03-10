package minimarketdemo.model.seguridades.managers;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minimarketdemo.model.auditoria.managers.ManagerAuditoria;
import minimarketdemo.model.core.entities.SegAsignacion;
import minimarketdemo.model.core.entities.SegModulo;
import minimarketdemo.model.core.entities.SegUsuario;
import minimarketdemo.model.core.managers.ManagerDAO;
import minimarketdemo.model.core.utils.ModelUtil;
import minimarketdemo.model.seguridades.dtos.LoginDTO;

/**
 * Implementa la logica de manejo de usuarios y autenticacion.
 * Funcionalidades principales:
 * <ul>
 * 	<li>Verificacion de credenciales de usuario.</li>
 *  <li>Asignacion de modulos a un usuario.</li>
 * </ul>
 * @author mrea
 */
@Stateless
@LocalBean
public class ManagerSeguridades {
	@EJB
	private ManagerDAO mDAO;
	@EJB
	private ManagerAuditoria mAuditoria;
    /**
     * Default constructor. 
     */
    public ManagerSeguridades() {
        
    }
    /**
     * Funcion de inicializacion de datos de usuarios.
     */
    public void inicializarDemo() throws Exception {
    	mAuditoria.mostrarLog(getClass(), "inicializarDemo", "Inicializacion de bdd demo.");
    	List<SegUsuario> listaUsuarios=mDAO.findAll(SegUsuario.class);
    	for(SegUsuario u:listaUsuarios) {
    		mAuditoria.mostrarLog(getClass(), "inicializarDemo", "Eliminacion de usuario "+u.getCorreo()+" "+u.getIdSegUsuario());
    		mDAO.eliminar(SegUsuario.class, u.getIdSegUsuario());
    	}
    	//creacion del usuario administrador:
		SegUsuario administrador=new SegUsuario();
		administrador.setActivo(true);
		administrador.setApellidos("admin");
		administrador.setClave("admin");
		administrador.setCorreo("admin@minimarketdemo.com");
		administrador.setNombres("admin");
		administrador.setCodigo("admin");
		mDAO.insertar(administrador);
		mAuditoria.mostrarLog(getClass(), "inicializarDemo", "Usuario administrador creado.");
		//inicializacion de modulos:
		SegModulo modulo=new SegModulo();
		modulo.setNombreModulo("Seguridades");
		modulo.setRutaAcceso("seguridades");
		mDAO.insertar(modulo);
		modulo=new SegModulo();
		modulo.setNombreModulo("Auditoría");
		modulo.setRutaAcceso("auditoria");
		mDAO.insertar(modulo);
		mAuditoria.mostrarLog(getClass(), "inicializarDemo", "Módulos creados.");
		//asignacion de accesos:
		asignarModulo(1, 1);
		asignarModulo(1, 2);
		mAuditoria.mostrarLog(getClass(), "inicializarDemo", "Inicializacion de bdd demo terminada.");
    }
    
    /**
     * Funcion de autenticacion mediante el uso de credenciales.
     * @param idSegUsuario El codigo del usuario que desea autenticarse.
     * @param clave La contrasenia.
     * @return La ruta de acceso al sistema.
     * @throws Exception
     */
    public LoginDTO login(int idSegUsuario,String clave) throws Exception{
    	if(ModelUtil.isEmpty(clave)) {
    		mAuditoria.mostrarLog(getClass(), "login", "Debe indicar una clave "+idSegUsuario);
    		throw new Exception("Debe indicar una clave");
    	}
    	SegUsuario usuario=(SegUsuario) mDAO.findById(SegUsuario.class, idSegUsuario);
    	if(usuario==null) {
    		mAuditoria.mostrarLog(getClass(), "login", "No existe usuario "+idSegUsuario);
    		throw new Exception("Error en credenciales.");
    	}
    		
    	if(usuario.getClave().equals(clave)) {
    		if(usuario.getActivo()==false) {
        		mAuditoria.mostrarLog(getClass(), "login", "Intento de login usuario desactivado "+idSegUsuario);
        		throw new Exception("El usuario esta desactivado.");
        	}
    		mAuditoria.mostrarLog(getClass(), "login", "Login exitoso "+idSegUsuario);
    		//crear DTO:
    		LoginDTO loginDTO=new LoginDTO();
    		loginDTO.setIdSegUsuario(usuario.getIdSegUsuario());
    		loginDTO.setCorreo(usuario.getCorreo());
    		//obtener la lista de modulos a los que tiene acceso:
    		List<SegAsignacion> listaAsignaciones=findAsignacionByUsuario(usuario.getIdSegUsuario());
    		for(SegAsignacion asig:listaAsignaciones) {
    			SegModulo modulo=asig.getSegModulo();
    			loginDTO.getListaModulos().add(modulo);
    		}
    		return loginDTO;
    	}
    	mAuditoria.mostrarLog(getClass(), "login", "No coincide la clave "+idSegUsuario);
    	throw new Exception("Error en credenciales");
    }
    
    public void cerrarSesion(int idSegUsuario) {
    	mAuditoria.mostrarLog(getClass(), "cerrarSesion", "Cerrar sesión usuario: "+idSegUsuario);
    }
    
    public void accesoNoPermitido(int idSegUsuario,String ruta) {
    	mAuditoria.mostrarLog(getClass(), "accesoNoPermitido", "Usuario "+idSegUsuario+" intento no autorizado a "+ruta);
    }
    
    public List<SegUsuario> findAllUsuarios(){
    	return mDAO.findAll(SegUsuario.class, "apellidos");
    }
    
    public void insertarUsuario(SegUsuario nuevoUsuario) throws Exception {
    	nuevoUsuario.setCodigo("n/a");
    	mDAO.insertar(nuevoUsuario);
    }
    
    public void actualizarUsuario(SegUsuario edicionUsuario) throws Exception {
    	SegUsuario usuario=(SegUsuario) mDAO.findById(SegUsuario.class, edicionUsuario.getIdSegUsuario());
    	usuario.setApellidos(edicionUsuario.getApellidos());
    	usuario.setClave(edicionUsuario.getClave());
    	usuario.setCorreo(edicionUsuario.getCorreo());
    	usuario.setCodigo(edicionUsuario.getCodigo());
    	usuario.setNombres(edicionUsuario.getNombres());
    	mDAO.actualizar(usuario);
    }
    
    public void activarDesactivarUsuario(int idSegUsuario) throws Exception {
    	SegUsuario usuario=(SegUsuario) mDAO.findById(SegUsuario.class, idSegUsuario);
    	usuario.setActivo(!usuario.getActivo());
    	System.out.println("activar/desactivar "+usuario.getActivo());
    	mDAO.actualizar(usuario);
    }
    
    public void eliminarUsuario(int idSegUsuario) throws Exception {
    	SegUsuario usuario=(SegUsuario) mDAO.findById(SegUsuario.class, idSegUsuario);
    	if(usuario.getSegAsignacions().size()>0)
    		throw new Exception("No se puede elmininar el usuario porque tiene asignaciones de módulos.");
    	mDAO.eliminar(SegUsuario.class, usuario.getIdSegUsuario());
    }
    
    public List<SegModulo> findAllModulos(){
    	return mDAO.findAll(SegModulo.class, "nombreModulo");
    }
    
    public List<SegAsignacion> findAsignacionByUsuario(int idSegUsuario){
    	String consulta="o.segUsuario.idSegUsuario="+idSegUsuario;
		List<SegAsignacion> listaAsignaciones=mDAO.findWhere(SegAsignacion.class, consulta, null);
		return listaAsignaciones;
    }
    
    /**
     * Permite asignar el acceso a un modulo del inventario de sistemas.
     * @param idSegUsuario El codigo del usuario.
     * @param idSegModulo El codigo del modulo que se va a asignar.
     * @throws Exception
     */
    public void asignarModulo(int idSegUsuario,int idSegModulo) throws Exception{
    	//Buscar los objetos primarios:
    	SegUsuario usuario=(SegUsuario)mDAO.findById(SegUsuario.class, idSegUsuario);
    	SegModulo modulo=(SegModulo)mDAO.findById(SegModulo.class, idSegModulo);
    	//crear la relacion:
    	SegAsignacion asignacion=new SegAsignacion();
    	asignacion.setSegModulo(modulo);
    	asignacion.setSegUsuario(usuario);
    	//guardar la asignacion:
    	mDAO.insertar(asignacion);
    	mAuditoria.mostrarLog(getClass(), "asignarModulo", "Modulo "+idSegModulo+" asigando a usuario "+idSegUsuario);
    }
    
    public void eliminarAsignacion(int idSegAsignacion) throws Exception {
    	mDAO.eliminar(SegAsignacion.class, idSegAsignacion);
    }

}

package minimarketdemo.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the bitacoraproductos database table.
 * 
 */
@Entity
@Table(name="bitacoraproductos")
@NamedQuery(name="Bitacoraproducto.findAll", query="SELECT b FROM Bitacoraproducto b")
public class Bitacoraproducto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_bitacora")
	private Integer idBitacora;

	private String accion;

	private Integer cantidad;

	//bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="codigo")
	private Producto producto;

	public Bitacoraproducto() {
	}

	public Integer getIdBitacora() {
		return this.idBitacora;
	}

	public void setIdBitacora(Integer idBitacora) {
		this.idBitacora = idBitacora;
	}

	public String getAccion() {
		return this.accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}
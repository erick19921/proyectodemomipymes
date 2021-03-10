package minimarketdemo.model.core.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the producto database table.
 * 
 */
@Entity
@NamedQuery(name="Producto.findAll", query="SELECT p FROM Producto p")
public class Producto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer codigo;

	private String nombre;

	private BigDecimal precio;

	//bi-directional many-to-one association to Bitacoraproducto
	@OneToMany(mappedBy="producto")
	private List<Bitacoraproducto> bitacoraproductos;

	//bi-directional one-to-one association to Bodega
	@OneToOne(mappedBy="producto")
	private Bodega bodega;

	public Producto() {
	}

	public Integer getCodigo() {
		return this.codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BigDecimal getPrecio() {
		return this.precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public List<Bitacoraproducto> getBitacoraproductos() {
		return this.bitacoraproductos;
	}

	public void setBitacoraproductos(List<Bitacoraproducto> bitacoraproductos) {
		this.bitacoraproductos = bitacoraproductos;
	}

	public Bitacoraproducto addBitacoraproducto(Bitacoraproducto bitacoraproducto) {
		getBitacoraproductos().add(bitacoraproducto);
		bitacoraproducto.setProducto(this);

		return bitacoraproducto;
	}

	public Bitacoraproducto removeBitacoraproducto(Bitacoraproducto bitacoraproducto) {
		getBitacoraproductos().remove(bitacoraproducto);
		bitacoraproducto.setProducto(null);

		return bitacoraproducto;
	}

	public Bodega getBodega() {
		return this.bodega;
	}

	public void setBodega(Bodega bodega) {
		this.bodega = bodega;
	}

}
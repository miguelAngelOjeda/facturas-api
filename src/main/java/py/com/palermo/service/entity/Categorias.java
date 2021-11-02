/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.palermo.service.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 *
 * @author miguel.ojeda
 */
@Entity
@Table(name = "CATEGORIAS", schema = "PUBLIC")
public class Categorias extends Base{

    private static final long serialVersionUID = 1574657L;
    private static final String SECUENCIA = "seq_categoria_id";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = SECUENCIA)
    @SequenceGenerator(name=SECUENCIA, sequenceName=SECUENCIA, allocationSize = 1)
    @Column(name = "id")
    private Long id;
    
    @NotBlank(message = "Ingrese Nombre")
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @NotBlank(message = "Ingrese Codigo")
    @Basic(optional = false)
    @Column(name = "CODIGO")
    private String codigo;
    
    @NotBlank(message = "Ingrese Cantidad")
    @Basic(optional = false)
    @Column(name = "CANTIDAD")
    private Long cantidad;
    
    @NotBlank(message = "Ingrese Porcentaje")
    @Basic(optional = false)
    @Column(name = "PORC_DESCUENTO")
    private Double porcDescuento;
    
    public Categorias() {
    }

    public Categorias(Long id) {
            this.setId(id);
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPorcDescuento() {
        return porcDescuento;
    }

    public void setPorcDescuento(Double porcDescuento) {
        this.porcDescuento = porcDescuento;
    }
   
    
}

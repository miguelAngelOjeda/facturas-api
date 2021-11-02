/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.palermo.service.entity;

import java.math.BigDecimal;
import java.util.List;
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
@Table(name = "PRODUCTOS", schema = "PUBLIC")
public class Productos extends Base {

    private static final long serialVersionUID = -9149680520407250259L;
    private static final String SECUENCIA = "seq_producto_id";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = SECUENCIA)
    @SequenceGenerator(name = SECUENCIA, sequenceName = SECUENCIA, allocationSize = 1)
    @Column(name = "id")
    private Long id;
    
    @NotBlank(message = "Ingrese Nombre")
    @Column(name = "nombre")
    private String nombre;
    
    @NotBlank(message = "Ingrese Codigo")
    @Basic(optional = false)
    @Column(name = "CODIGO")
    private String codigo;
    
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @Basic(optional = false)
    @Column(name = "COSTO_IVA")
    private BigDecimal costoIva;   
      
    
    public Productos() {

    }

    public Productos(Long id) {
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getCostoIva() {
        return costoIva;
    }

    public void setCostoIva(BigDecimal costoIva) {
        this.costoIva = costoIva;
    }
  
}

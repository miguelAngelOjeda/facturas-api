/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package py.com.palermo.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
/**
 *
 * @author miguel.ojeda
 */

@MappedSuperclass
public class Base implements Serializable {

    private static final long serialVersionUID = -9149680520407250259L;

    @Column(name = "ACTIVO")
    private String activo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "FECHA_CREACION")
    private Timestamp fechaCreacion;
    
    @JsonIgnore
    @Column(name = "ID_USUARIO_CREACION")
    private Long idUsuarioCreacion;
 
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "FECHA_MODIFICACION")
    private Timestamp fechaModificacion;
    
    @JsonIgnore
    @Column(name = "ID_USUARIO_MODIFICACION")
    private Long idUsuarioModificacion;
    
    @JsonIgnore
    @Column(name = "FECHA_ELIMINACION")
    private Timestamp fechaEliminacion;
    
    @JsonIgnore
    @Column(name = "ID_USUARIO_ELIMINACION")
    private Long idUsuarioEliminacion;
    
    
    public Base() {

    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getIdUsuarioCreacion() {
        return idUsuarioCreacion;
    }

    public void setIdUsuarioCreacion(Long idUsuarioCreacion) {
        this.idUsuarioCreacion = idUsuarioCreacion;
    }

    public Timestamp getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Timestamp fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Long getIdUsuarioModificacion() {
        return idUsuarioModificacion;
    }

    public void setIdUsuarioModificacion(Long idUsuarioModificacion) {
        this.idUsuarioModificacion = idUsuarioModificacion;
    }

    public Timestamp getFechaEliminacion() {
        return fechaEliminacion;
    }

    public void setFechaEliminacion(Timestamp fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }

    public Long getIdUsuarioEliminacion() {
        return idUsuarioEliminacion;
    }

    public void setIdUsuarioEliminacion(Long idUsuarioEliminacion) {
        this.idUsuarioEliminacion = idUsuarioEliminacion;
    }
               
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.palermo.service.ejb.managerImpl;


import javax.ejb.Stateless;
import py.com.palermo.service.ejb.manager.ProductosManager;
import py.com.palermo.service.entity.Productos;


/**
 *
 * @author miguel.ojeda
 */
@Stateless
public class ProductosManagerImpl extends GenericDaoImpl<Productos, Long>
        implements ProductosManager {

    @Override
    protected Class<Productos> getEntityBeanType() {
        return Productos.class;
    }
}

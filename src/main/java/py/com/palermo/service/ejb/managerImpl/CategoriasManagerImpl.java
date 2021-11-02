/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.palermo.service.ejb.managerImpl;


import javax.ejb.Stateless;
import py.com.palermo.service.ejb.manager.CategoriasManager;
import py.com.palermo.service.entity.Categorias;


/**
 *
 * @author miguel.ojeda
 */
@Stateless
public class CategoriasManagerImpl extends GenericDaoImpl<Categorias, Long>
        implements CategoriasManager {

    @Override
    protected Class<Categorias> getEntityBeanType() {
        return Categorias.class;
    }
}

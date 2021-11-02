/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.palermo.service.ejb.managerImpl;


import javax.ejb.Stateless;
import py.com.palermo.service.ejb.manager.PreciosManager;
import py.com.palermo.service.entity.Precios;


/**
 *
 * @author miguel.ojeda
 */
@Stateless
public class PreciosManagerImpl extends GenericDaoImpl<Precios, Long>
        implements PreciosManager {

    @Override
    protected Class<Precios> getEntityBeanType() {
        return Precios.class;
    }
}

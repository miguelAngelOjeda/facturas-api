/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.palermo.service.ejb.managerImpl;


import javax.ejb.Stateless;
import py.com.palermo.service.ejb.manager.FacturasCabeceraManager;
import py.com.palermo.service.entity.FacturasCabecera;


/**
 *
 * @author miguel.ojeda
 */
@Stateless
public class FacturasCabeceraManagerImpl extends GenericDaoImpl<FacturasCabecera, Long>
        implements FacturasCabeceraManager {

    @Override
    protected Class<FacturasCabecera> getEntityBeanType() {
        return FacturasCabecera.class;
    }
}

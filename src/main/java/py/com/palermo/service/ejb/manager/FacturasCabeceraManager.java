/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package py.com.palermo.service.ejb.manager;


import javax.ejb.Local;
import py.com.palermo.service.entity.FacturasCabecera;



/**
 *
 * @author miguel.ojeda
 */
@Local
public interface FacturasCabeceraManager extends GenericDao<FacturasCabecera, Long>{   
    
}

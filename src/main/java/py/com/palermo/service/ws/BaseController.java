/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.palermo.service.ws;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.springframework.stereotype.Controller;
import py.com.palermo.service.ejb.manager.CategoriasManager;
import py.com.palermo.service.ejb.manager.FacturasCabeceraManager;
import py.com.palermo.service.ejb.manager.PreciosManager;
import py.com.palermo.service.ejb.manager.ProductosManager;



/**
 *
 * @author miguel.ojeda
 */
@Controller
public class BaseController {

    private Context context;
   
    protected CategoriasManager categoriasManager;
    protected PreciosManager preciosManager;
    protected ProductosManager productosManager;
    protected FacturasCabeceraManager facturasCabeceraManager;
    
    
    protected void inicializarFacturasCabeceraManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (facturasCabeceraManager == null) {
            try {

                facturasCabeceraManager = (FacturasCabeceraManager) context.lookup("java:app/service-beta/FacturasCabeceraManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarCategoriasManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (categoriasManager == null) {
            try {

                categoriasManager = (CategoriasManager) context.lookup("java:app/service-beta/CategoriasManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarPreciosManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (preciosManager == null) {
            try {

                preciosManager = (PreciosManager) context.lookup("java:app/service-beta/PreciosManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
    protected void inicializarProductosManager() throws Exception {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (NamingException e1) {
                throw new RuntimeException("No se puede inicializar el contexto", e1);
            }
        }
        if (productosManager == null) {
            try {

                productosManager = (ProductosManager) context.lookup("java:app/service-beta/ProductosManagerImpl");
            } catch (NamingException ne) {
                throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
            }
        }
    }
    
}

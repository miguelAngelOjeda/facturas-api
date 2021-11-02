/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.palermo.service.ws;

import com.google.gson.Gson;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import py.com.palermo.service.entity.Precios;
import py.com.palermo.service.entity.Productos;
import py.com.palermo.service.utils.ResponseDTO;
import py.com.palermo.service.utils.ResponseListDTO;

/**
 *
 * @author miguel.ojeda
 */
@Controller
@RequestMapping(value = "/productos")
public class ProductosController extends BaseController {

    String atributos = "id,nombre,codigo";

    @GetMapping
    public @ResponseBody
    ResponseListDTO listar(@ModelAttribute("_search") boolean filtrar,
            @ModelAttribute("filters") String filtros,
            @ModelAttribute("page") Integer pagina,
            @ModelAttribute("rows") Integer cantidad,
            @ModelAttribute("sidx") String ordenarPor,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("all") boolean todos) {

        ResponseListDTO retorno = new ResponseListDTO();

        Productos model = new Productos();
        model.setActivo("S");

        List<Map<String, Object>> listMapGrupos = null;
        try {
            inicializarProductosManager();
            Gson gson = new Gson();
            String camposFiltros = null;
            String valorFiltro = null;

            pagina = pagina != null ? pagina : 1;
            Long total = 0L;

            if (!todos) {
                total = Long.parseLong(productosManager.list(model).size() + "");
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = Integer.parseInt(total.toString()) - Integer.parseInt(total.toString()) % cantidad;
                pagina = Integer.parseInt(total.toString()) / cantidad;
            }

            listMapGrupos = productosManager.listAtributos(model, atributos.split(","), todos, inicio, cantidad,
                    ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro,
                    null, null, null, null, null, null, null, null, true);

            if (todos) {
                total = Long.parseLong(listMapGrupos.size() + "");
            }

            Integer totalPaginas = Integer.parseInt(total.toString()) / cantidad;

            retorno.setRecords(total);
            retorno.setTotal(totalPaginas + 1);
            retorno.setRows(listMapGrupos);
            retorno.setPage(pagina);
            retorno.setStatus(200);
            retorno.setMessage("OK");

        } catch (Exception e) {
            e.printStackTrace();
            retorno.setStatus(500);
            retorno.setMessage("Error interno del servidor.");
        }

        return retorno;
    }

    /**
     * Mapping para el metodo GET de la vista visualizar.(visualizar Productos)
     *
     * @param id de la entidad
     * @return
     */
    @GetMapping("/{id}")
    public @ResponseBody
    ResponseDTO getObject(
            @ModelAttribute("id") Long id) {
        ResponseDTO response = new ResponseDTO();
        try {
            inicializarProductosManager();

            Productos model = productosManager.get(id);

            response.setModel(model);
            response.setStatus(model == null ? 404 : 200);
            response.setMessage(model == null ? "Registro no encontrado" : "OK");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.setMessage("Error interno del servidor.");
        }

        return response;
    }

    /**
     * Mapping para el metodo GET para retornar precio del producto por
     * cantidad.(visualizar Productos)
     *
     * @param codProd
     * @param cantidad
     * @return
     */
    @GetMapping("/precio")
    public @ResponseBody
    ResponseDTO getObjectPrecio(
            @ModelAttribute("codProd") String codProd,
            @ModelAttribute("cantidad") Long cantidad) {
        ResponseDTO response = new ResponseDTO();
        List<String> atrMay = new ArrayList<>();
        List<Object> objMay = new ArrayList<>();
        List<String> atrMen = new ArrayList<>();
        List<Object> objMen = new ArrayList<>();
        Productos model;
        Precios precio;
        try {
            inicializarProductosManager();
            inicializarPreciosManager();

            model = new Productos();
            model.setCodigo(codProd);

            model = productosManager.get(model);

            precio = new Precios();
            precio.setActivo("S");
            precio.setProducto(model);

            atrMay.add("cantidadMaxima");
            objMay.add(cantidad);
            
            atrMen.add("cantidadMinima");
            objMen.add(cantidad);

            List<Map<String, Object>> listMapGrupos = preciosManager.listAtributos(precio, "id,precio".split(","), true, Integer.MIN_VALUE, Integer.BYTES, "id".split(","),
                    "desc".split(","), true, true, null, null, null, null, null, null, null, null, atrMay, objMay, atrMen, objMen, null, true, null, null);
            
            precio.setPrecio(listMapGrupos.isEmpty() ? 0.0 : Double.parseDouble(listMapGrupos.get(0).get("precio").toString()));
            precio.setTotal(precio.getPrecio() * cantidad);
            
            response.setModel(precio);
            response.setStatus(listMapGrupos.isEmpty() ? 404 : 200);
            response.setMessage(listMapGrupos.isEmpty() ? "Registro no encontrado" : "OK");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.setMessage("Error interno del servidor.");
        }

        return response;
    }

    /**
     * Mapping para el metodo POST de la vista crear.(crear Productos)
     *
     * @param model entidad Categoria recibida de la vista
     * @param errors
     * @return
     */
    @PostMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public @ResponseBody
    ResponseDTO create(
            @RequestBody @Valid Productos model,
            Errors errors) {
        ResponseDTO response = new ResponseDTO();
        try {
            inicializarProductosManager();

            if (errors.hasErrors()) {

                response.setStatus(400);
                response.setMessage(errors.getAllErrors()
                        .stream()
                        .map(x -> x.getDefaultMessage())
                        .collect(Collectors.joining(",")));
                return response;
            }

            Productos dato = new Productos();
            dato.setNombre(model.getNombre().toUpperCase());

            Map<String, Object> objMaps = productosManager.getLike(dato, "id".split(","));

            if (objMaps != null) {
                response.setStatus(205);
                response.setMessage("Ya existe un registro con el mismo nombre.");
                return response;
            }

            dato = new Productos();

            Integer numeroCodigo = productosManager.total(dato) + 1;

            model.setCodigo("PRO-" + numeroCodigo);
            model.setActivo("S");
            model.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            model.setFechaModificacion(new Timestamp(System.currentTimeMillis()));

            productosManager.save(model);

            response.setStatus(200);
            response.setMessage("El Producto ha sido guardado");
            response.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.setMessage("Error interno del servidor.");
        }

        return response;
    }

    /**
     * Mapping para el metodo PUT de la vista actualizar.(actualizar Productos)
     *
     * @param id de la entidad
     * @param model entidad Categoria recibida de la vista
     * @param errors
     * @return
     */
    @PutMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public @ResponseBody
    ResponseDTO update(
            @ModelAttribute("id") Long id,
            @RequestBody @Valid Productos model,
            Errors errors) {
        ResponseDTO response = new ResponseDTO();
        try {
            inicializarProductosManager();

            if (errors.hasErrors()) {

                response.setStatus(400);
                response.setMessage(errors.getAllErrors()
                        .stream()
                        .map(x -> x.getDefaultMessage())
                        .collect(Collectors.joining(",")));
                return response;
            }

            Productos dato = productosManager.get(id);

            Productos object = new Productos();
            object.setNombre(model.getNombre().toUpperCase());

            Map<String, Object> objectMaps = productosManager.getLike(object, "id".split(","));
            if (objectMaps != null
                    && objectMaps.get("id").toString().compareToIgnoreCase(dato.getId().toString()) != 0) {
                response.setStatus(205);
                response.setMessage("Ya existe un registro con el mismo nombre.");
                return response;
            }

            model.setCodigo(dato.getCodigo());
            model.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            model.setFechaCreacion(dato.getFechaCreacion());
            model.setIdUsuarioCreacion(dato.getIdUsuarioCreacion());
            model.setIdUsuarioEliminacion(dato.getIdUsuarioEliminacion());

            productosManager.update(model);

            response.setStatus(200);
            response.setMessage("El Producto ha sido guardado");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.setMessage("Error interno del servidor.");
        }

        return response;
    }

    /**
     * Mapping para el metodo DELETE de la vista.(eliminar Productos)
     *
     * @param id de la entidad
     * @return
     */
    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseDTO deleteObject(
            @ModelAttribute("id") Long id) {
        ResponseDTO response = new ResponseDTO();
        try {
            inicializarProductosManager();

            Productos model = productosManager.get(id);
            model.setActivo("N");
            model.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));

            productosManager.update(model);

            response.setModel(model);
            response.setStatus(200);
            response.setMessage("Registro eliminado con exito.");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.setMessage("Error interno del servidor.");
        }

        return response;
    }

}

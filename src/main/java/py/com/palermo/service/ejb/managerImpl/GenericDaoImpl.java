package py.com.palermo.service.ejb.managerImpl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.googlecode.genericdao.search.ExampleOptions;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.hibernate.HibernateMetadataUtil;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;

import org.hibernate.jpa.HibernateEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.com.palermo.service.ejb.manager.GenericDao;


public abstract class GenericDaoImpl<T, ID extends Serializable> implements
        GenericDao<T, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericDaoImpl.class);

    protected JPASearchProcessor processor;

    @PersistenceContext
    private EntityManager em;

    public GenericDaoImpl() {
    }

    protected abstract Class<T> getEntityBeanType();

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public EntityManager getEm() {

        if (em == null) {
            throw new IllegalStateException("EntityManager no esta seteado");
        }

        return em;
    }

    protected SessionFactory getSessionFactory() {

        if (this.em.getDelegate() instanceof HibernateEntityManager) {
            return ((HibernateEntityManager) this.getEm().getDelegate())
                    .getSession().getSessionFactory();
        } else {
            return ((Session) this.getEm().getDelegate()).getSessionFactory();
        }

    }

    private Session getSession() {

        if (this.em.getDelegate() instanceof HibernateEntityManager) {
            return ((HibernateEntityManager) this.getEm().getDelegate())
                    .getSession();
        } else {
            return ((Session) this.getEm().getDelegate());
        }

    }

    @Override
    public int total(T ejemplo) {
        return list(ejemplo, true).size();
    }
    
    @Override
    public Long total(T ejemplo, String orderBy, String orderByDirList) {
        return new Long(this.listAtributos(ejemplo, "id".split(","), true, 0, null,
                orderBy.split(","), orderByDirList.split(","), false, true, null, null, null, null, null, null, null, null, null, null, false).size());
    }
    
    @Override
    public int total(T ejemplo, boolean like) {
        JPASearchProcessor jpaSP = new JPASearchProcessor(
                HibernateMetadataUtil.getInstanceForSessionFactory(this
                        .getSessionFactory()));

        Search searchConfig = this.getSearchConfig(jpaSP, ejemplo, null, true,
                null, null, null,
                null, like, true, null, null, null, null, null,
                null, null, null, null, null, true, null, "");

        return jpaSP.count(em, searchConfig);
    }

    @Override
    public int total(T ejemplo, String[] atributos,
            List<String> atrMayIgual, List<Object> objMayIgual, List<String> atrMenIgual, List<Object> objMenIgual,
            String campoComparacion, List<Object> valoresComparacion, String tipoFiltro, String[] camposNotNull, String tipoFiltroNull) {
        JPASearchProcessor jpaSP = new JPASearchProcessor(
                HibernateMetadataUtil.getInstanceForSessionFactory(this
                        .getSessionFactory()));

        Search searchConfig = this.getSearchConfig(jpaSP, ejemplo, atributos, true,
                null, null, null,
                null, true, true, null, null, campoComparacion, valoresComparacion, tipoFiltro,
                atrMayIgual, objMayIgual, atrMenIgual, objMenIgual, null, true, camposNotNull, tipoFiltroNull);

        return jpaSP.count(em, searchConfig);
    }
    
    @Override
    public int total(T ejemplo, String[] atributos,
            List<String> atrMayIgual, List<Object> objMayIgual, List<String> atrMenIgual, List<Object> objMenIgual,
            String campoComparacion, List<Object> valoresComparacion, String tipoFiltro,
            String campoComparacion1, List<Object> valoresComparacion1, String tipoFiltro1,
            String[] camposNotNull, String tipoFiltroNull) {
        JPASearchProcessor jpaSP = new JPASearchProcessor(
                HibernateMetadataUtil.getInstanceForSessionFactory(this
                        .getSessionFactory()));

        Search searchConfig = this.getSearchConfig(jpaSP, ejemplo, atributos, true,
                null, null, null,
                null, true, true, null, null, 
                campoComparacion, valoresComparacion, tipoFiltro,
                campoComparacion1, valoresComparacion1, tipoFiltro1,
                atrMayIgual, objMayIgual, atrMenIgual, objMenIgual, null, true, camposNotNull, tipoFiltroNull);

        return jpaSP.count(em, searchConfig);
    }
    
    @Override
    public int total(T ejemplo, String[] atributos,
            List<String> atrMay, List<Object> objMay, List<String> atrMen, List<Object> objMen,
            String campoComparacion, List<Object> valoresComparacion, String tipoFiltro) {
        JPASearchProcessor jpaSP = new JPASearchProcessor(
                HibernateMetadataUtil.getInstanceForSessionFactory(this
                        .getSessionFactory()));

        Search searchConfig = this.getSearchConfig(jpaSP, ejemplo, atributos, true,
                null, null, null,
                null, true, true, null, null, campoComparacion, valoresComparacion, tipoFiltro,
                null, null, null, null,atrMay, objMay, atrMen, objMen, null, true);

        return jpaSP.count(em, searchConfig);
    }

    @Override
    public int total(T ejemplo, String[] atributos,
            String[] orderByAttrList, String[] orderByDirList,
            String camposDistintos, boolean distintos,
            String[] camposNotNull, String tipoFiltroNull) {
        
        JPASearchProcessor jpaSP = new JPASearchProcessor(
                HibernateMetadataUtil.getInstanceForSessionFactory(this
                        .getSessionFactory()));

        Search searchConfig = this.getSearchConfig(jpaSP, ejemplo, null, true,
                null, null, orderByAttrList,
                orderByDirList, false, false, null, null, null, null, null,
                null, null, null, null, camposDistintos, distintos, camposNotNull, tipoFiltroNull);

        return jpaSP.count(em, searchConfig);
    }

    @Override
    public T get(ID id) {
        return (T) getEm().find(getEntityBeanType(), id);
    }

    @Override
    public Map<String, Object> getLike(T ejemplo, String[] atributos) {

        List<Map<String, Object>> lista = this.listAtributos(ejemplo,
                atributos);

        if (lista.isEmpty()) {
            return null;
        } else if (lista.size() == 1) {
            return lista.get(0);
        }

        throw new NonUniqueResultException("Se encontro mas de un "
                + this.getEntityBeanType().getCanonicalName()
                + " para el pedido dado");
    }

    @Override
    public T get(T ejemplo) {
        List<T> list = this.list(ejemplo, false, 0, 1, null,
                null, false,
                false, null, null, null,
                null, null, null, null, null, false);

        if (list.isEmpty()) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        }

        throw new NonUniqueResultException("Se encontro mas de un "
                + this.getEntityBeanType().getCanonicalName()
                + " para el pedido dado");
    }

    @Override
    public Map<String, Object> getAtributos(T ejemplo, String[] atributos) {
        List<Map<String, Object>> lista = this.listAtributos(ejemplo,
                atributos);

        if (lista.isEmpty()) {
            return null;
        }

        if (lista.size() == 1) {
            return lista.get(0);
        }

        throw new NonUniqueResultException("Se encontro mas de un "
                + this.getEntityBeanType().getCanonicalName()
                + " para el pedido dado");
    }

    @Override
    public Map<String, Object> getAtributos(T ejemplo, String[] atributos, boolean like, boolean caseSensitive) {
        List<Map<String, Object>> lista = this.listAtributos(ejemplo, atributos, true, 0, 2,
                null, null, like, caseSensitive, null, null, null, null, null, null, null, null, null, null, false);

        if (lista.isEmpty()) {
            return null;
        }

        if (lista.size() == 1) {
            return lista.get(0);
        }

        throw new NonUniqueResultException("Se encontro mas de un "
                + this.getEntityBeanType().getCanonicalName()
                + " para el pedido dado");
    }

    @Override
    public void save(T entity) throws Exception {
        this.getEm().persist(entity);
    }

    @Override
    public void update(T entity) throws Exception {
        this.getSession().update(entity);
    }

    @Override
    public void delete(ID id) throws Exception {
        T entity = this.get(id);

        if (entity != null) {
            this.getEm().remove(entity);
        }

    }

    @Override
    public void delete(T entity) throws Exception {
        if (entity != null) {
            this.getEm().remove(entity);
        }

    }

    @Override
    public List<T> list() {
        return this.list(null, true, null, null, null, null, false,
                false, null, null, null,
                null, null, null, null, null, false);
    }

    @Override
    public List<T> list(Integer primerResultado, Integer cantResultado) {
        return this.list(null, false, primerResultado, cantResultado, null,
                null, false,
                false, null, null, null,
                null, null, null, null, null, false);
    }

    @Override
    public List<T> list(T ejemplo) {
        return this.list(ejemplo, true);
    }

    @Override
    public List<T> list(T ejemplo, boolean like) {
        return this.list(ejemplo, true, null, null, null, null, like, false,
                null, null, null,
                null, null, null, null,
                null, false);
    }
    
    @Override
    public List<T> list(T ejemplo, String orderByAttrList, String orderByDirList) {
        return this.list(ejemplo, true, null, null,
                new String[]{orderByAttrList},
                new String[]{orderByDirList}, false,
                false, null, null, null,
                null, null, null, null, null, false);
    }

    @Override
    public List<T> list(T ejemplo, boolean all, String campoComparacion, List<Object> valoresComparacion, String tipoFiltro) {
        return this.list(ejemplo, all, null, null,
                null,
                null, false,
                false, campoComparacion, valoresComparacion, tipoFiltro,
                null, null, null, null, null, false);
    }

    @Override
    public List<T> list(T ejemplo, String orderByAttrList,
            String orderByDirList, boolean like) {
        return this.list(ejemplo, true, null, null,
                new String[]{orderByAttrList},
                new String[]{orderByDirList}, like,
                false, null, null, null,
                null, null, null, null, null, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> list(T ejemplo, boolean all, Integer primerResultado,
            Integer cantResultados, String[] orderByAttrList,
            String[] orderByDirList, boolean like, boolean caseSensitive, String campoComparacion, List<Object> valoresComparacion, String tipoFiltro,
            List<String> atrMayIgual, List<Object> objMayIgual, List<String> atrMenIgual, List<Object> objMenIgual,
            String camposDistintos, boolean distintos) {

        JPASearchProcessor jpaSP = new JPASearchProcessor(
                HibernateMetadataUtil.getInstanceForSessionFactory(this
                        .getSessionFactory()));
        
        Search searchConfig = this.getSearchConfig(jpaSP, ejemplo, null, all,
                primerResultado, cantResultados, orderByAttrList,
                orderByDirList, like, caseSensitive, null, null, campoComparacion, valoresComparacion, tipoFiltro,
                atrMayIgual, objMayIgual, atrMenIgual, objMenIgual, null, null, null, null, camposDistintos, distintos);
        
        return jpaSP.search(em, searchConfig);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<T> list(T ejemplo, boolean all, Integer primerResultado,
            Integer cantResultados, String[] orderByAttrList,
            String[] orderByDirList, boolean like, boolean caseSensitive, String campoComparacion, List<Object> valoresComparacion, String tipoFiltro,
            List<String> atrMayIgual, List<Object> objMayIgual, List<String> atrMenIgual, List<Object> objMenIgual,
            String camposDistintos, boolean distintos, String[] camposNotNull, String tipoFiltroNull) {

        JPASearchProcessor jpaSP = new JPASearchProcessor(
                HibernateMetadataUtil.getInstanceForSessionFactory(this
                        .getSessionFactory()));

        Search searchConfig = this.getSearchConfig(jpaSP, ejemplo, null, all,
                primerResultado, cantResultados, orderByAttrList,
                orderByDirList, like, caseSensitive, null, null, campoComparacion, valoresComparacion, tipoFiltro,
                atrMayIgual, objMayIgual, atrMenIgual, objMenIgual, camposDistintos, distintos, camposNotNull, tipoFiltroNull);

        return jpaSP.search(em, searchConfig);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<T> list(T ejemplo, boolean all, Integer primerResultado,
            Integer cantResultados, String[] orderByAttrList,
            String[] orderByDirList, boolean like, boolean caseSensitive, String campoComparacion, List<Object> valoresComparacion, String tipoFiltro,
            List<String> atrMayIgual, List<Object> objMayIgual, List<String> atrMenIgual, List<Object> objMenIgual,
            String camposDistintos, boolean distintos, String[] camposNotNull, String[] tipoFiltroNull) {

        JPASearchProcessor jpaSP = new JPASearchProcessor(
                HibernateMetadataUtil.getInstanceForSessionFactory(this
                        .getSessionFactory()));

        Search searchConfig = this.getSearchConfig(jpaSP, ejemplo, null, all,
                primerResultado, cantResultados, orderByAttrList,
                orderByDirList, like, caseSensitive, null, null, campoComparacion, valoresComparacion, tipoFiltro,
                atrMayIgual, objMayIgual, atrMenIgual, objMenIgual, camposDistintos, distintos, camposNotNull, tipoFiltroNull);

        return jpaSP.search(em, searchConfig);
    }

    @Override
    public List<Map<String, Object>> listAtributos(T ejemplo, String[] atributos) {
        return this.listAtributos(ejemplo, atributos, true);
    }

    @Override
    public List<Map<String, Object>> listAtributos(T ejemplo,
            String[] atributos, boolean like) {
        return this.listAtributos(ejemplo, atributos, true, 0, null,
                null, null, like, true, null, null, null, null, null, null, null, null, null, null, false);
    }

    public List<Map<String, Object>> listAtributos(T ejemplo, String[] atributos,
            boolean all, Integer primerResultado, Integer cantResultados,
            String[] orderByAttrList, String[] orderByDirList, boolean like, boolean caseSensitive,
            List<String> atrMayIgual, List<Object> objMayIgual, List<String> atrMenIgual, List<Object> objMenIgual) {

        return this.listAtributos(ejemplo, atributos, all, primerResultado, cantResultados,
                orderByAttrList, orderByDirList, like, caseSensitive,
                null, null, null, null, null,
                atrMayIgual, objMayIgual, atrMenIgual, objMenIgual, null, false);

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> listAtributos(T ejemplo,
            String[] atributos, boolean all, Integer primerResultado,
            Integer cantResultados, String[] orderByAttrList,
            String[] orderByDirList, boolean like, boolean caseSensitive,
            String propiedadFiltroComunes, String comun, String campoComparacion, List<Object> valoresComparacion, String tipoFiltro,
            List<String> atrMayIgual, List<Object> objMayIgual, List<String> atrMenIgual, List<Object> objMenIgual,
            String camposDistintos, boolean distintos) {
        if (atributos == null || atributos.length == 0) {
            throw new RuntimeException(
                    "La lista de propiedades no puede ser nula o vacía");
        }

        JPASearchProcessor jpaSP = new JPASearchProcessor(
                HibernateMetadataUtil.getInstanceForSessionFactory(this
                        .getSessionFactory()));
        Search searchConfig = this.getSearchConfig(jpaSP, ejemplo, atributos,
                all, primerResultado, cantResultados, orderByAttrList,
                orderByDirList, like, caseSensitive,
                propiedadFiltroComunes, comun, campoComparacion, valoresComparacion, tipoFiltro,
                atrMayIgual, objMayIgual, atrMenIgual, objMenIgual, null, null, null, null, camposDistintos, distintos);
        
        return jpaSP.search(em, searchConfig);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> listAtributos(T ejemplo,
            String[] atributos, boolean all, Integer primerResultado,
            Integer cantResultados, String[] orderByAttrList,
            String[] orderByDirList, boolean like, boolean caseSensitive,
            String propiedadFiltroComunes, String comun, String campoComparacion, List<Object> valoresComparacion, String tipoFiltro,
            List<String> atrMayIgual, List<Object> objMayIgual, List<String> atrMenIgual, List<Object> objMenIgual,
            String camposDistintos, boolean distintos, String[] camposNotNull, String tipoFiltroNull) {
        if (atributos == null || atributos.length == 0) {
            throw new RuntimeException(
                    "La lista de propiedades no puede ser nula o vacía");
        }

        JPASearchProcessor jpaSP = new JPASearchProcessor(
                HibernateMetadataUtil.getInstanceForSessionFactory(this
                        .getSessionFactory()));
        
        Search searchConfig = this.getSearchConfig(jpaSP, ejemplo, atributos, all,
                primerResultado, cantResultados, orderByAttrList,
                orderByDirList, like, caseSensitive, propiedadFiltroComunes, comun, campoComparacion, valoresComparacion, tipoFiltro,
                atrMayIgual, objMayIgual, atrMenIgual, objMenIgual, camposDistintos, distintos, camposNotNull, tipoFiltroNull);
        
        
        return jpaSP.search(em, searchConfig);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> listAtributos(T ejemplo,
            String[] atributos, boolean all, Integer primerResultado,
            Integer cantResultados, String[] orderByAttrList,
            String[] orderByDirList, boolean like, boolean caseSensitive,
            String propiedadFiltroComunes, String comun, 
            String campoComparacion, List<Object> valoresComparacion, String tipoFiltro,
            String campoComparacion2, List<Object> valoresComparacion2, String tipoFiltro2,
            List<String> atrMayIgual, List<Object> objMayIgual, List<String> atrMenIgual, List<Object> objMenIgual,
            String camposDistintos, boolean distintos, String[] camposNotNull, String tipoFiltroNull) {
        if (atributos == null || atributos.length == 0) {
            throw new RuntimeException(
                    "La lista de propiedades no puede ser nula o vacía");
        }

        JPASearchProcessor jpaSP = new JPASearchProcessor(
                HibernateMetadataUtil.getInstanceForSessionFactory(this
                        .getSessionFactory()));
        
        Search searchConfig = this.getSearchConfig(jpaSP, ejemplo, atributos, all,
                primerResultado, cantResultados, orderByAttrList,
                orderByDirList, like, caseSensitive, propiedadFiltroComunes, comun, 
                campoComparacion, valoresComparacion, tipoFiltro,
                campoComparacion2, valoresComparacion2, tipoFiltro2,
                atrMayIgual, objMayIgual, atrMenIgual, objMenIgual, camposDistintos, distintos, camposNotNull, tipoFiltroNull);
        
        
        return jpaSP.search(em, searchConfig);
    }

    private Search getSearchConfig(JPASearchProcessor jpaSP, T ejemplo,
            String[] atributos, boolean all, Integer primerResultado,
            Integer cantResultados, String[] orderByAttrList,
            String[] orderByDirList, boolean like, boolean caseSensitive,
            String propiedadFiltroComunes, String valorComun,
            String campoComparacion, List<Object> valoresComparacion, String tipoFiltro,
            List<String> atrMayIgual, List<Object> objMayIgual,
            List<String> atrMenIgual, List<Object> objMenIgual,
            List<String> atrMay, List<Object> objMay,
            List<String> atrMen, List<Object> objMen,
            String camposDistintos, boolean distintos) {

        Search searchConfig = new Search(this.getEntityBeanType());

        if (ejemplo != null) {
            ExampleOptions exampleOptions = new ExampleOptions();
            exampleOptions.setExcludeNulls(true);

            if (like) {
                exampleOptions.setLikeMode(ExampleOptions.ANYWHERE);
            }

            exampleOptions.setIgnoreCase(caseSensitive);

            searchConfig.addFilter(jpaSP.getFilterFromExample(ejemplo,
                    exampleOptions));
        }

        if (atrMayIgual != null && objMayIgual != null && atrMayIgual.size() == objMayIgual.size()) {
            int index = 0;
            for (String atr : atrMayIgual) {
                if (objMayIgual.get(index) != null) {
                    Filter[] filtros = new Filter[2];
                    filtros[0] = Filter.isNull(atr);
                    filtros[1] = Filter.greaterOrEqual(atr, objMayIgual.get(index));
                    searchConfig.addFilterOr(filtros);
                }
                index++;
            }
        }

        if (atrMenIgual != null && objMenIgual != null && atrMenIgual.size() == objMenIgual.size()) {
            int index = 0;

            for (String atr : atrMenIgual) {
                if (objMenIgual.get(index) != null) {
                    Filter[] filtros = new Filter[2];
                    filtros[0] = Filter.isNull(atr);
                    filtros[1] = Filter.lessOrEqual(atr, objMenIgual.get(index));
                    searchConfig.addFilterOr(filtros);
                }
                index++;
            }
        }
        
        if (atrMay != null && objMay != null && atrMay.size() == objMay.size()) {
            int index = 0;
            for (String atr : atrMay) {
                if (objMay.get(index) != null) {
                    Filter[] filtros = new Filter[2];
                    filtros[0] = Filter.isNull(atr);
                    filtros[1] = Filter.greaterThan(atr, objMay.get(index));
                    searchConfig.addFilterOr(filtros);
                }
                index++;
            }
        }

        if (atrMen != null && objMen != null && atrMen.size() == objMen.size()) {
            int index = 0;

            for (String atr : atrMen) {
                if (objMen.get(index) != null) {
                    Filter[] filtros = new Filter[2];
                    filtros[0] = Filter.isNull(atr);
                    filtros[1] = Filter.lessThan(atr, objMen.get(index));
                    searchConfig.addFilterOr(filtros);
                }
                index++;
            }
        }

        if (valoresComparacion != null && campoComparacion != null && tipoFiltro != null) {
            int tam = valoresComparacion.size() / 1000;
            tam++;

            int inicio = 0;

            Filter[] filtros = new Filter[tam];
            for (int i = 0; i < tam; i++) {
                if (tipoFiltro.compareTo("OP_IN") == 0) {
                    filtros[i] = Filter.in(campoComparacion, valoresComparacion.subList(inicio, Math.min(valoresComparacion.size(), inicio + 1000)));
                } else {
                    filtros[i] = Filter.notIn(campoComparacion, valoresComparacion.subList(inicio, Math.min(valoresComparacion.size(), inicio + 1000)));
                }
                inicio += 1000;
            }
            if (tipoFiltro.compareTo("OP_IN") == 0) {
                searchConfig.addFilterOr(filtros);
            } else {
                searchConfig.addFilterAnd(filtros);
            }

        }

        if (valorComun != null && propiedadFiltroComunes != null) {

            String[] lista = propiedadFiltroComunes.split(",");
            String[] valores = valorComun.split(" ");

            for (int j = 0; j < valores.length; j++) {
                int index = 0;
                Filter[] filtros = new Filter[lista.length];

                for (int i = 0; i < lista.length; i++) {
                    Filter f = new Filter();
                    f.setProperty(lista[i]);
                    f.setValue("%" + valores[j] + "%");
                    f.setOperator(Filter.OP_ILIKE);
                    filtros[index] = f;
                    index++;
                }
                searchConfig.addFilterOr(filtros);
            }

        }

        if (!all && primerResultado != null && cantResultados != null) {
            searchConfig.setFirstResult(primerResultado);
            searchConfig.setMaxResults(cantResultados);
        }

        if (atributos != null && atributos.length > 0) {
            for (String a : atributos) {
                searchConfig.addField(a);
            }
            searchConfig.setResultMode(Search.RESULT_MAP);
        }

        if (distintos && camposDistintos != null) {
            String[] lista = camposDistintos.split(",");
            searchConfig.setDistinct(distintos);
            for (String atributo : lista) {
                searchConfig.addField(atributo);
            }

        } else {

            if (orderByAttrList != null && orderByDirList != null
                    && orderByAttrList.length == orderByDirList.length) {
                for (int i = 0; i < orderByAttrList.length; i++) {
                    if (orderByDirList[i].equalsIgnoreCase("desc")) {
                        searchConfig.addSortDesc(orderByAttrList[i]);
                    } else {
                        searchConfig.addSortAsc(orderByAttrList[i]);
                    }
                }
            } else if ((orderByAttrList != null && orderByDirList == null)
                    || (orderByAttrList == null && orderByDirList != null)) {
                throw new RuntimeException("No puede proporcionarse una lista de "
                        + "atributos para ordenamiento sin la correspondiente "
                        + "lista de direcciones de ordenamiento, o viceversa");
            } else if (orderByAttrList != null && orderByDirList != null
                    && orderByAttrList.length != orderByDirList.length) {
                throw new RuntimeException("No puede proporcionarse una lista de "
                        + "atributos para ordenamiento de tamaño dieferente a la "
                        + "lista de direcciones de ordenamiento");
            }

        }
        return searchConfig;
    }
    
    private Search getSearchConfig(JPASearchProcessor jpaSP, T ejemplo,
            String[] atributos, boolean all, Integer primerResultado,
            Integer cantResultados, String[] orderByAttrList,
            String[] orderByDirList, boolean like, boolean caseSensitive,
            String propiedadFiltroComunes, String valorComun,
            String campoComparacion, List<Object> valoresComparacion, String tipoFiltro,
            List<String> atrMayIgual, List<Object> objMayIgual,
            List<String> atrMenIgual, List<Object> objMenIgual,
            String camposDistintos, boolean distintos, String[] camposNotNull, String tipoFiltroNull) {

        Search searchConfig = new Search(this.getEntityBeanType());

        if (ejemplo != null) {
            ExampleOptions exampleOptions = new ExampleOptions();
            exampleOptions.setExcludeNulls(true);

            if (like) {
                exampleOptions.setLikeMode(ExampleOptions.ANYWHERE);
            }

            exampleOptions.setIgnoreCase(caseSensitive);

            searchConfig.addFilter(jpaSP.getFilterFromExample(ejemplo,
                    exampleOptions));
        }

        if (atrMayIgual != null && objMayIgual != null && atrMayIgual.size() == objMayIgual.size()) {
            int index = 0;
            for (String atr : atrMayIgual) {
                if (objMayIgual.get(index) != null) {
                    Filter[] filtros = new Filter[2];
                    filtros[0] = Filter.isNull(atr);
                    filtros[1] = Filter.greaterOrEqual(atr, objMayIgual.get(index));
                    searchConfig.addFilterOr(filtros);
                }
                index++;
            }
        }

        if (atrMenIgual != null && objMenIgual != null && atrMenIgual.size() == objMenIgual.size()) {
            int index = 0;

            for (String atr : atrMenIgual) {
                if (objMenIgual.get(index) != null) {
                    Filter[] filtros = new Filter[2];
                    filtros[0] = Filter.isNull(atr);
                    filtros[1] = Filter.lessOrEqual(atr, objMenIgual.get(index));
                    searchConfig.addFilterOr(filtros);
                }
                index++;
            }
        }

        if (valoresComparacion != null && campoComparacion != null && tipoFiltro != null) {
            int tam = valoresComparacion.size() / 1000;
            tam++;

            int inicio = 0;

            Filter[] filtros = new Filter[tam];
            for (int i = 0; i < tam; i++) {
                if (tipoFiltro.compareTo("OP_IN") == 0) {
                    filtros[i] = Filter.in(campoComparacion, valoresComparacion.subList(inicio, Math.min(valoresComparacion.size(), inicio + 1000)));
                } else {
                    filtros[i] = Filter.notIn(campoComparacion, valoresComparacion.subList(inicio, Math.min(valoresComparacion.size(), inicio + 1000)));
                }
                inicio += 1000;
            }
            if (tipoFiltro.compareTo("OP_IN") == 0) {
                searchConfig.addFilterOr(filtros);
            } else {
                searchConfig.addFilterAnd(filtros);
            }
        }

        if (tipoFiltroNull != null && camposNotNull != null) {

            Filter[] filtros = new Filter[camposNotNull.length];

            for (int j = 0; j < camposNotNull.length; j++) {
                if (tipoFiltroNull.compareTo("NOT_NULL") == 0) {
                    filtros[j] = Filter.isNotNull(camposNotNull[j]);
                } else {
                    filtros[j] = Filter.isNull(camposNotNull[j]);
                }
            }

            searchConfig.addFilterAnd(filtros);
        }

        if (valorComun != null && propiedadFiltroComunes != null) {

            String[] lista = propiedadFiltroComunes.split(",");
            String[] valores = valorComun.split(" ");

            for (int j = 0; j < valores.length; j++) {
                int index = 0;
                Filter[] filtros = new Filter[lista.length];

                for (int i = 0; i < lista.length; i++) {
                    Filter f = new Filter();
                    f.setProperty(lista[i]);
                    
                    
                        f.setValue("%" + valores[j] + "%");
                        f.setOperator(Filter.OP_ILIKE);
                    
                    
                    filtros[index] = f;
                    index++;
                }
                searchConfig.addFilterOr(filtros);
            }

        }

        if (!all && primerResultado != null && cantResultados != null) {
            searchConfig.setFirstResult(primerResultado);
            searchConfig.setMaxResults(cantResultados);
        }

        if (atributos != null && atributos.length > 0) {
            for (String a : atributos) {
                searchConfig.addField(a);
            }
            searchConfig.setResultMode(Search.RESULT_MAP);
        }

        if (distintos && camposDistintos != null) {
            String[] lista = camposDistintos.split(",");
            searchConfig.setDistinct(distintos);
            for (String atributo : lista) {
                searchConfig.addField(atributo);
            }

        } else {

            if (orderByAttrList != null && orderByDirList != null
                    && orderByAttrList.length == orderByDirList.length) {
                for (int i = 0; i < orderByAttrList.length; i++) {
                    if (orderByDirList[i].equalsIgnoreCase("desc")) {
                        searchConfig.addSortDesc(orderByAttrList[i]);
                    } else {
                        searchConfig.addSortAsc(orderByAttrList[i]);
                    }
                }
            } else if ((orderByAttrList != null && orderByDirList == null)
                    || (orderByAttrList == null && orderByDirList != null)) {
                throw new RuntimeException("No puede proporcionarse una lista de "
                        + "atributos para ordenamiento sin la correspondiente "
                        + "lista de direcciones de ordenamiento, o viceversa");
            } else if (orderByAttrList != null && orderByDirList != null
                    && orderByAttrList.length != orderByDirList.length) {
                throw new RuntimeException("No puede proporcionarse una lista de "
                        + "atributos para ordenamiento de tamaño dieferente a la "
                        + "lista de direcciones de ordenamiento");
            }

        }
        return searchConfig;
    }
    
    
    private Search getSearchConfig(JPASearchProcessor jpaSP, T ejemplo,
            String[] atributos, boolean all, Integer primerResultado,
            Integer cantResultados, String[] orderByAttrList,
            String[] orderByDirList, boolean like, boolean caseSensitive,
            String propiedadFiltroComunes, String valorComun,
            String campoComparacion, List<Object> valoresComparacion, String tipoFiltro,
            String campoComparacion2, List<Object> valoresComparacion2, String tipoFiltro2,
            List<String> atrMayIgual, List<Object> objMayIgual,
            List<String> atrMenIgual, List<Object> objMenIgual,
            String camposDistintos, boolean distintos, String[] camposNotNull, String tipoFiltroNull) {

        Search searchConfig = new Search(this.getEntityBeanType());

        if (ejemplo != null) {
            ExampleOptions exampleOptions = new ExampleOptions();
            exampleOptions.setExcludeNulls(true);

            if (like) {
                exampleOptions.setLikeMode(ExampleOptions.ANYWHERE);
            }

            exampleOptions.setIgnoreCase(caseSensitive);

            searchConfig.addFilter(jpaSP.getFilterFromExample(ejemplo,
                    exampleOptions));
        }

        if (atrMayIgual != null && objMayIgual != null && atrMayIgual.size() == objMayIgual.size()) {
            int index = 0;
            for (String atr : atrMayIgual) {
                if (objMayIgual.get(index) != null) {
                    Filter[] filtros = new Filter[2];
                    filtros[0] = Filter.isNull(atr);
                    filtros[1] = Filter.greaterOrEqual(atr, objMayIgual.get(index));
                    searchConfig.addFilterOr(filtros);
                }
                index++;
            }
        }

        if (atrMenIgual != null && objMenIgual != null && atrMenIgual.size() == objMenIgual.size()) {
            int index = 0;

            for (String atr : atrMenIgual) {
                if (objMenIgual.get(index) != null) {
                    Filter[] filtros = new Filter[2];
                    filtros[0] = Filter.isNull(atr);
                    filtros[1] = Filter.lessOrEqual(atr, objMenIgual.get(index));
                    searchConfig.addFilterOr(filtros);
                }
                index++;
            }
        }

        if (valoresComparacion != null && campoComparacion != null && tipoFiltro != null) {
            int tam = valoresComparacion.size() / 1000;
            tam++;

            int inicio = 0;

            Filter[] filtros = new Filter[tam];
            for (int i = 0; i < tam; i++) {
                if (tipoFiltro.compareTo("OP_IN") == 0) {
                    filtros[i] = Filter.in(campoComparacion, valoresComparacion.subList(inicio, Math.min(valoresComparacion.size(), inicio + 1000)));
                } else {
                    filtros[i] = Filter.notIn(campoComparacion, valoresComparacion.subList(inicio, Math.min(valoresComparacion.size(), inicio + 1000)));
                }
                inicio += 1000;
            }
            if (tipoFiltro.compareTo("OP_IN") == 0) {
                searchConfig.addFilterOr(filtros);
            } else {
                searchConfig.addFilterAnd(filtros);
            }
        }
        
        if (valoresComparacion2 != null && campoComparacion2 != null && tipoFiltro2 != null) {
            int tam = valoresComparacion2.size() / 1000;
            tam++;

            int inicio = 0;

            Filter[] filtros = new Filter[tam];
            for (int i = 0; i < tam; i++) {
                if (tipoFiltro2.compareTo("OP_IN") == 0) {
                    filtros[i] = Filter.in(campoComparacion2, valoresComparacion2.subList(inicio, Math.min(valoresComparacion2.size(), inicio + 1000)));
                } else {
                    filtros[i] = Filter.notIn(campoComparacion2, valoresComparacion2.subList(inicio, Math.min(valoresComparacion2.size(), inicio + 1000)));
                }
                inicio += 1000;
            }
            if (tipoFiltro2.compareTo("OP_IN") == 0) {
                searchConfig.addFilterOr(filtros);
            } else {
                searchConfig.addFilterAnd(filtros);
            }
        }

        if (tipoFiltroNull != null && camposNotNull != null) {

            Filter[] filtros = new Filter[camposNotNull.length];

            for (int j = 0; j < camposNotNull.length; j++) {
                if (tipoFiltroNull.compareTo("NOT_NULL") == 0) {
                    filtros[j] = Filter.isNotNull(camposNotNull[j]);
                } else {
                    filtros[j] = Filter.isNull(camposNotNull[j]);
                }
            }

            searchConfig.addFilterAnd(filtros);
        }

        if (valorComun != null && propiedadFiltroComunes != null) {

            String[] lista = propiedadFiltroComunes.split(",");
            String[] valores = valorComun.split(" ");

            for (int j = 0; j < valores.length; j++) {
                int index = 0;
                Filter[] filtros = new Filter[lista.length];

                for (int i = 0; i < lista.length; i++) {
                    Filter f = new Filter();
                    f.setProperty(lista[i]);
                    
                    
                        f.setValue("%" + valores[j] + "%");
                        f.setOperator(Filter.OP_ILIKE);
                    
                    
                    filtros[index] = f;
                    index++;
                }
                searchConfig.addFilterOr(filtros);
            }

        }

        if (!all && primerResultado != null && cantResultados != null) {
            searchConfig.setFirstResult(primerResultado);
            searchConfig.setMaxResults(cantResultados);
        }

        if (atributos != null && atributos.length > 0) {
            for (String a : atributos) {
                searchConfig.addField(a);
            }
            searchConfig.setResultMode(Search.RESULT_MAP);
        }

        if (distintos && camposDistintos != null) {
            String[] lista = camposDistintos.split(",");
            searchConfig.setDistinct(distintos);
            for (String atributo : lista) {
                searchConfig.addField(atributo);
            }

        } else {

            if (orderByAttrList != null && orderByDirList != null
                    && orderByAttrList.length == orderByDirList.length) {
                for (int i = 0; i < orderByAttrList.length; i++) {
                    if (orderByDirList[i].equalsIgnoreCase("desc")) {
                        searchConfig.addSortDesc(orderByAttrList[i]);
                    } else {
                        searchConfig.addSortAsc(orderByAttrList[i]);
                    }
                }
            } else if ((orderByAttrList != null && orderByDirList == null)
                    || (orderByAttrList == null && orderByDirList != null)) {
                throw new RuntimeException("No puede proporcionarse una lista de "
                        + "atributos para ordenamiento sin la correspondiente "
                        + "lista de direcciones de ordenamiento, o viceversa");
            } else if (orderByAttrList != null && orderByDirList != null
                    && orderByAttrList.length != orderByDirList.length) {
                throw new RuntimeException("No puede proporcionarse una lista de "
                        + "atributos para ordenamiento de tamaño dieferente a la "
                        + "lista de direcciones de ordenamiento");
            }

        }
        return searchConfig;
    }
    
    private Search getSearchConfig(JPASearchProcessor jpaSP, T ejemplo,
            String[] atributos, boolean all, Integer primerResultado,
            Integer cantResultados, String[] orderByAttrList,
            String[] orderByDirList, boolean like, boolean caseSensitive,
            String propiedadFiltroComunes, String valorComun,
            String campoComparacion, List<Object> valoresComparacion, String tipoFiltro,
            List<String> atrMayIgual, List<Object> objMayIgual,
            List<String> atrMenIgual, List<Object> objMenIgual,
            String camposDistintos, boolean distintos, String[] camposNotNull, String[] tipoFiltroNull) {

        Search searchConfig = new Search(this.getEntityBeanType());

        if (ejemplo != null) {
            ExampleOptions exampleOptions = new ExampleOptions();
            exampleOptions.setExcludeNulls(true);

            if (like) {
                exampleOptions.setLikeMode(ExampleOptions.ANYWHERE);
            }

            exampleOptions.setIgnoreCase(caseSensitive);

            searchConfig.addFilter(jpaSP.getFilterFromExample(ejemplo,
                    exampleOptions));
        }

        if (atrMayIgual != null && objMayIgual != null && atrMayIgual.size() == objMayIgual.size()) {
            int index = 0;
            for (String atr : atrMayIgual) {
                if (objMayIgual.get(index) != null) {
                    Filter[] filtros = new Filter[2];
                    filtros[0] = Filter.isNull(atr);
                    filtros[1] = Filter.greaterOrEqual(atr, objMayIgual.get(index));
                    searchConfig.addFilterOr(filtros);
                }
                index++;
            }
        }

        if (atrMenIgual != null && objMenIgual != null && atrMenIgual.size() == objMenIgual.size()) {
            int index = 0;

            for (String atr : atrMenIgual) {
                if (objMenIgual.get(index) != null) {
                    Filter[] filtros = new Filter[2];
                    filtros[0] = Filter.isNull(atr);
                    filtros[1] = Filter.lessOrEqual(atr, objMenIgual.get(index));
                    searchConfig.addFilterOr(filtros);
                }
                index++;
            }
        }

        if (valoresComparacion != null && campoComparacion != null && tipoFiltro != null) {
            int tam = valoresComparacion.size() / 1000;
            tam++;

            int inicio = 0;

            Filter[] filtros = new Filter[tam];
            for (int i = 0; i < tam; i++) {
                if (tipoFiltro.compareTo("OP_IN") == 0) {
                    filtros[i] = Filter.in(campoComparacion, valoresComparacion.subList(inicio, Math.min(valoresComparacion.size(), inicio + 1000)));
                } else {
                    filtros[i] = Filter.notIn(campoComparacion, valoresComparacion.subList(inicio, Math.min(valoresComparacion.size(), inicio + 1000)));
                }
                inicio += 1000;
            }
            if (tipoFiltro.compareTo("OP_IN") == 0) {
                searchConfig.addFilterOr(filtros);
            } else {
                searchConfig.addFilterAnd(filtros);
            }
        }

        if (tipoFiltroNull != null && camposNotNull != null) {

            Filter[] filtros = new Filter[camposNotNull.length];

            for (int j = 0; j < camposNotNull.length; j++) {
                if (tipoFiltroNull[j].compareTo("NOT_NULL") == 0) {
                    filtros[j] = Filter.isNotNull(camposNotNull[j]);
                } else {
                    filtros[j] = Filter.isNull(camposNotNull[j]);
                }
            }

            searchConfig.addFilterAnd(filtros);
        }

        if (valorComun != null && propiedadFiltroComunes != null) {

            String[] lista = propiedadFiltroComunes.split(",");
            String[] valores = valorComun.split(" ");

            for (int j = 0; j < valores.length; j++) {
                int index = 0;
                Filter[] filtros = new Filter[lista.length];

                for (int i = 0; i < lista.length; i++) {
                    Filter f = new Filter();
                    f.setProperty(lista[i]);
                    
                    
                        f.setValue("%" + valores[j] + "%");
                        f.setOperator(Filter.OP_ILIKE);
                    
                    
                    filtros[index] = f;
                    index++;
                }
                searchConfig.addFilterOr(filtros);
            }

        }

        if (!all && primerResultado != null && cantResultados != null) {
            searchConfig.setFirstResult(primerResultado);
            searchConfig.setMaxResults(cantResultados);
        }

        if (atributos != null && atributos.length > 0) {
            for (String a : atributos) {
                searchConfig.addField(a);
            }
            searchConfig.setResultMode(Search.RESULT_MAP);
        }

        if (distintos && camposDistintos != null) {
            String[] lista = camposDistintos.split(",");
            searchConfig.setDistinct(distintos);
            for (String atributo : lista) {
                searchConfig.addField(atributo);
            }

        } else {

            if (orderByAttrList != null && orderByDirList != null
                    && orderByAttrList.length == orderByDirList.length) {
                for (int i = 0; i < orderByAttrList.length; i++) {
                    if (orderByDirList[i].equalsIgnoreCase("desc")) {
                        searchConfig.addSortDesc(orderByAttrList[i]);
                    } else {
                        searchConfig.addSortAsc(orderByAttrList[i]);
                    }
                }
            } else if ((orderByAttrList != null && orderByDirList == null)
                    || (orderByAttrList == null && orderByDirList != null)) {
                throw new RuntimeException("No puede proporcionarse una lista de "
                        + "atributos para ordenamiento sin la correspondiente "
                        + "lista de direcciones de ordenamiento, o viceversa");
            } else if (orderByAttrList != null && orderByDirList != null
                    && orderByAttrList.length != orderByDirList.length) {
                throw new RuntimeException("No puede proporcionarse una lista de "
                        + "atributos para ordenamiento de tamaño dieferente a la "
                        + "lista de direcciones de ordenamiento");
            }

        }
        return searchConfig;
    }

//    private String getQuery(final List<CountAll> params) {
//
//        final StringBuffer queryString = new StringBuffer(
//                "SELECT count(*) from ");
//        queryString.append(getEntityBeanType().getSimpleName()).append(" o ");
//
//        boolean and = false;
//        for (CountAll rpm : params) {
//
//            if (rpm.getType().compareToIgnoreCase("Int") == 0
//                    && rpm.getValue() != null
//                    && rpm.getValue().compareToIgnoreCase("") != 0) {
//                if (!and) {
//                    and = true;
//                    queryString.append(" where ");
//                    queryString.append(" o.");
//                    queryString.append(rpm.getKey());
//                    queryString.append(" = ");
//                    queryString.append(rpm.getValue());
//                } else {
//                    queryString.append(" and o.");
//                    queryString.append(rpm.getKey());
//                    queryString.append(" = ");
//                    queryString.append(rpm.getValue());
//                }
//            } else if (rpm.getType().compareToIgnoreCase("String") == 0) {
//                if (!and) {
//                    and = true;
//                    queryString.append(" where ");
//                    queryString.append(" o.");
//                    queryString.append(rpm.getKey());
//                    queryString.append(" = '");
//                    queryString.append(rpm.getValue());
//                    queryString.append("'");
//                } else {
//                    queryString.append(" and o.");
//                    queryString.append(rpm.getKey());
//                    queryString.append(" = '");
//                    queryString.append(rpm.getValue());
//                    queryString.append("'");
//                }
//            }  else if (rpm.getType().compareToIgnoreCase("OP_IN") == 0) {
//                if (!and) {
//                    and = true;
//                    queryString.append(" where ");
//                    queryString.append(" o.");
//                    queryString.append(rpm.getKey());
//                    queryString.append(" IN (");
//                    
//                    ListIterator<Long> valueIterator = rpm.getIds().listIterator();
//                    
//                    while (valueIterator.hasNext()) {
//                        queryString.append(valueIterator.next().toString());
//                        if (valueIterator.hasNext()) {
//                            queryString.append(",");
//                        }
//                    }
//                    queryString.append(") ");
//                } else {
//                    queryString.append(" and ");
//                    queryString.append(" o.");
//                    queryString.append(rpm.getKey());
//                    queryString.append(" IN (");
//                   
//                    ListIterator<Long> valueIterator = rpm.getIds().listIterator();
//                    
//                    while (valueIterator.hasNext()) {
//                        queryString.append(valueIterator.next().toString());
//                        if (valueIterator.hasNext()) {
//                            queryString.append(",");
//                        }
//                    }
//                    
//                    queryString.append(") ");
//                }
//
//            } else if (rpm.getType().compareToIgnoreCase("NOT_IN") == 0) {
//
//                if (!and) {
//                    and = true;
//                    queryString.append(" where ");
//                    queryString.append(" o.");
//                    queryString.append(rpm.getKey());
//                    queryString.append(" NOT IN ( ");
//                    
//                    ListIterator<Long> valueIterator = rpm.getIds().listIterator();
//                    
//                    while (valueIterator.hasNext()) {
//                        queryString.append(valueIterator.next().toString());
//                        if (valueIterator.hasNext()) {
//                            queryString.append(",");
//                        }
//                    }
//                    
//                    queryString.append(") ");
//                } else {
//                    queryString.append(" and ");
//                    queryString.append(" o.");
//                    queryString.append(rpm.getKey());
//                    queryString.append(" NOT IN (");
//                    
//                    ListIterator<Long> valueIterator = rpm.getIds().listIterator();
//                    
//                    while (valueIterator.hasNext()) {
//                        queryString.append(valueIterator.next().toString());
//                        if (valueIterator.hasNext()) {
//                            queryString.append(",");
//                        }
//                    }
//                    queryString.append(") ");
//                }
//            }else if (rpm.getType().compareToIgnoreCase("DATES") == 0
//                    && rpm.getFechaDesde() != null
//                    && rpm.getFechaDesde().compareToIgnoreCase("") != 0) {
//                if (!and) {
//                    and = true;
//                    queryString.append(" where o.");
//                    queryString.append(rpm.getKey());
//                    queryString.append(" BETWEEN TO_DATE('");
//                    queryString.append(rpm.getFechaDesde());
//                    queryString.append("','dd/MM/yyyy HH24:MI') ");
//                    queryString.append(" and TO_DATE('");
//                    queryString.append(rpm.getFechaHasta());
//                    queryString.append("','dd/MM/yyyy HH24:MI') ");
//                } else {
//                    queryString.append(" and o.");
//                    queryString.append(rpm.getKey());
//                    queryString.append(" BETWEEN TO_DATE('");
//                    queryString.append(rpm.getFechaDesde());
//                    queryString.append("','dd/MM/yyyy HH24:MI') ");
//                    queryString.append(" and TO_DATE('");
//                    queryString.append(rpm.getFechaHasta());
//                    queryString.append("','dd/MM/yyyy HH24:MI') ");
//                }
//            }
//
//        }
//
//        return queryString.toString();
//
//    }
}

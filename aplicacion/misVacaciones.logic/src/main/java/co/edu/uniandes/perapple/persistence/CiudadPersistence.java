package co.edu.uniandes.perapple.persistence;

import co.edu.uniandes.perapple.entities.CiudadEntity;
import co.edu.uniandes.perapple.entities.SitioEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CiudadPersistence {

    private static final Logger LOGGER = Logger.getLogger(CiudadPersistence.class.getName());

    @PersistenceContext(unitName = "MisVacacionesPU")
    protected EntityManager em;

    public CiudadEntity create(CiudadEntity entity) {
        LOGGER.info("Creando una ciudad nueva");
        em.persist(entity);
        LOGGER.info("Creando una ciudad nueva");
        return entity;
    }

    public CiudadEntity update(CiudadEntity entity) {
        LOGGER.log(Level.INFO, "Actualizando ciudad con id={0}", entity.getId());
        return em.merge(entity);
    }

    public void delete(int id) {
        LOGGER.log(Level.INFO, "Borrando ciudad con id={0}", id);
        CiudadEntity entity = em.find(CiudadEntity.class, id);
        em.remove(entity);
    }

    public CiudadEntity find(int id) {
        LOGGER.log(Level.INFO, "Consultando ciudad con id={0}", id);
        return em.find(CiudadEntity.class, id);
    }

    public List<CiudadEntity> findAll() {
        LOGGER.info("Consultando todas las ciudades");
        Query q = em.createQuery("select u from CiudadEntity u");
        return q.getResultList();
    }

    public SitioEntity updateSitio(SitioEntity entity){
        LOGGER.log(Level.INFO, "Actualizando sitio con id={0}", entity.getId());
        return em.merge(entity);
    }
}

package co.edu.uniandes.perapple.ejbs;

import co.edu.uniandes.perapple.entities.ViajeroEntity;
import co.edu.uniandes.perapple.exceptions.BusinessLogicException;
import co.edu.uniandes.perapple.persistence.ViajeroPersistence;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import co.edu.uniandes.perapple.api.IViajeroLogic;

@Stateless
public class ViajeroLogic implements IViajeroLogic {

    private static final Logger LOGGER = Logger.getLogger(ViajeroLogic.class.getName());

    @Inject
    private ViajeroPersistence persistence;

    //Posibilidad de inyectar mas instancias de las otras persistencias de ser necesario
    @Override
    public List<ViajeroEntity> getViajeros() {
        LOGGER.info("Inicia proceso de consultar todas los viajeros.");
        List<ViajeroEntity> viajeros = persistence.findAll();
        LOGGER.info("Termina proceso de consultar todos los libros");

        return viajeros;
    }

    @Override
    public ViajeroEntity getViajero(int id) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Inicia proceso de consultar viajero con id={0}", id);
        ViajeroEntity viajero = persistence.find(id);
        if (viajero == null) {
            LOGGER.log(Level.SEVERE, "El viajero con el id {0} no existe", id);
            throw new BusinessLogicException("El viajero solicitado no existe");
        }
        LOGGER.log(Level.INFO, "Termina proceso de consultar El viajero con id={0}", id);
        return viajero;
    }

    @Override
    public ViajeroEntity createViajero(ViajeroEntity entity) throws BusinessLogicException {
        //Validaciones:
        //1. El viajero a crear no existe.
        //2. El nuevo ID a asignar al viajero está disponible.
        LOGGER.info("Inicia proceso de creación de un viajero");
        if (validateViajeroExiste(entity.getId())) {
            throw new BusinessLogicException("Ya existe un viajero con ese id. No se puede crear. ");
        }
        persistence.create(entity);
        LOGGER.info("Termina proceso de creación del viajero");
        return entity;
    }

    @Override
    public ViajeroEntity updateViajero(ViajeroEntity entity) throws BusinessLogicException {
        //Validaciones
        //1. El viajero ya existe.
        //2. No se actualiza el ID.
        LOGGER.log(Level.INFO, "Inicia proceso de actualizar viajero con id={0}", entity.getId());
        if (!validateViajeroExiste(entity.getId())) {
            throw new BusinessLogicException("el viajero que se quiere actualizar no existe.");
        }
        ViajeroEntity newEntity = persistence.update(entity);
        LOGGER.log(Level.INFO, "Termina proceso de actualizar viajero con id={0}", entity.getId());
        return newEntity;
    }

    @Override
    public void deleteViajero(int id) throws BusinessLogicException {
        //Validaciones
        //1. El viajero ya existe.
        LOGGER.log(Level.INFO, "Inicia proceso de borrar viajero con id={0}", id);
        if (!validateViajeroExiste(id)) {
            throw new BusinessLogicException("el viajero que se quiere eliminar no existe.");
        }
        persistence.delete(id);
        LOGGER.log(Level.INFO, "Termina proceso de borrar viajero con id={0}", id);
    }

    private boolean validateViajeroExiste(int viajeroId) {
        return persistence.find(viajeroId) != null;
    }
}

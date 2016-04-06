/*
 * CityResource.java
 * Clase que representa el recurso "/cities"
 * Implementa varios métodos para manipular las ciudades
 */
package co.edu.uniandes.misVacaciones.rest.resources;


import co.edu.uniandes.misVacaciones.rest.converters.EventoItinerarioConverter;
import co.edu.uniandes.misVacaciones.rest.converters.SitioItinerarioConverter;
import co.edu.uniandes.misVacaciones.rest.converters.CiudadItinerarioConverter;
import co.edu.uniandes.misVacaciones.rest.converters.CiudadConverter;
import co.edu.uniandes.misVacaciones.rest.converters.ItinerarioConverter;
import co.edu.uniandes.misVacaciones.rest.dtos.CiudadDTO;
import co.edu.uniandes.misVacaciones.rest.dtos.CiudadItinerarioDTO;
import co.edu.uniandes.misVacaciones.rest.dtos.EventoDTO;
import co.edu.uniandes.misVacaciones.rest.dtos.EventoItinerarioDTO;
import co.edu.uniandes.misVacaciones.rest.dtos.ItinerarioDTO;
import co.edu.uniandes.misVacaciones.rest.dtos.SitioDTO;
import co.edu.uniandes.misVacaciones.rest.dtos.SitioItinerarioDTO;
import co.edu.uniandes.misVacaciones.rest.exceptions.CiudadLogicException;
import co.edu.uniandes.misVacaciones.rest.exceptions.EventoLogicException;
import co.edu.uniandes.misVacaciones.rest.exceptions.ItinerarioLogicException;
import co.edu.uniandes.misVacaciones.rest.exceptions.SitioLogicException;
import co.edu.uniandes.perapple.api.IItinerarioLogic;
import co.edu.uniandes.perapple.entities.CiudadEntity;
import co.edu.uniandes.perapple.entities.CiudadItinerarioEntity;
import co.edu.uniandes.perapple.entities.EventoItinerarioEntity;
import co.edu.uniandes.perapple.entities.ItinerarioEntity;
import co.edu.uniandes.perapple.entities.SitioItinerarioEntity;
import co.edu.uniandes.perapple.exceptions.BusinessLogicException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;

import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Clase que implementa el recurso REST correspondiente a "itinerarios".
 *
 * Note que la aplicación (definida en RestConfig.java) define la ruta
 * "/api" y este recurso tiene la ruta "itinerarios".
 * Al ejecutar la aplicación, el recurse será accesibe a través de la
 * ruta "/api/cities"
 *
 * @author Perapple
 */
@Path("itinerarios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ItinerarioResource {
        private static final Logger logger = Logger.getLogger(CiudadResource.class.getName());
    
	@Inject
	IItinerarioLogic itinerarioLogic;

      /**
       * El itinerario que actualmente se este manejando
       * @return el itinerario que actualmente se esta manejando
       * @throws ItinerarioLogicException
       */

    @GET
    @Path("current")
    public ItinerarioDTO getCurrentItinerario(){
        return ItinerarioConverter.fullEntity2DTO(itinerarioLogic.getCurrentItinerario());
    }

    @PUT
    @Path("current")
    public ItinerarioDTO setCurrentItinerario(ItinerarioDTO nuevoCurrent){
        ItinerarioEntity entity = ItinerarioConverter.fullDTO2Entity(nuevoCurrent);
        return ItinerarioConverter.fullEntity2DTO(itinerarioLogic.setCurrentItinerario(entity));
    }

	/**
	 * Obtiene el listado de Itinerarios.
	 * @return lista de itinerarios
	 * @throws ItinerarioLogicException excepción retornada por la lógica
	 */
    @GET
    public List<ItinerarioDTO> getItinerarios() {
        return ItinerarioConverter.listEntity2DTO(itinerarioLogic.getItinerarios());
    }

    /**
     * Obtiene un itinerario
     * @param id identificador del itinerario
     * @return itinerario encontrado
     * @throws ItinerarioLogicException cuando el itineraio no existe
     */
    @GET
    @Path("{id: \\d+}")
    public ItinerarioDTO getItinerario(@PathParam("id") Long id) {
        try {
            return ItinerarioConverter.fullEntity2DTO(itinerarioLogic.getItinerario(id));
        } catch (BusinessLogicException ex){
            logger.log(Level.SEVERE, "La ciudad no existe", ex);
            throw new WebApplicationException(ex.getLocalizedMessage(), ex, Response.Status.NOT_FOUND);
        }
    }

    /**
     * Agrega un itinerario
     * @param itinerario itinerario a agregar
     * @return datos del itinerario a agregar
     * @throws ItinerarioLogicException cuando ya existe una ciudad con el id suministrado
     */
    @POST
    public ItinerarioDTO createItinerario(ItinerarioDTO itinerario) {
        ItinerarioEntity entity = ItinerarioConverter.fullDTO2Entity(itinerario);
        return ItinerarioConverter.fullEntity2DTO(itinerarioLogic.createItinerario(entity));
    }

    /**
     * Actualiza los datos de un itineario
     * @param id identificador del itinerario a modificar
     * @param itinerario itinerario a modificar
     * @return datos del itinerario a modificada
     * @throws ItinerarioLogicException cuando no existe un itinerario con el id suministrado
     */
    @PUT
    @Path("{id: \\d+}")
    public ItinerarioDTO updateItinerario(@PathParam("id") Long id, ItinerarioDTO itinerario) {
        ItinerarioEntity entity = ItinerarioConverter.fullDTO2Entity(itinerario);
        entity.setId(id);
        try {
            ItinerarioEntity oldEntity = itinerarioLogic.getItinerario(id);
            // TODO
            //entity.setCiudades(oldEntity.getCiudades());
        } catch (BusinessLogicException ex) {
            logger.log(Level.SEVERE, "El itinerario no existe", ex);
            throw new WebApplicationException(ex.getLocalizedMessage(), ex, Response.Status.NOT_FOUND);
        }
        return ItinerarioConverter.fullEntity2DTO(itinerarioLogic.updateItinerario(entity));
    }

    /**
     * Elimina los datos de un itinerario
     * @param id identificador del itinerario a eliminar
     * @throws ItinerarioLogicException cuando no existe un itinerario con el id suministrado
     */
    @DELETE
    @Path("{id: \\d+}")
    public void deleteItinerario(@PathParam("id") Long id) {
    	itinerarioLogic.deleteItinerario(id);
    }

    /**
     * Crea ciudad en el itinerario con id dado
     * @param id identificador del itinerario a agregar ciudad
     * @param ciudad dto de ciudad a agregar
     */
    @POST
    @Path("{id: \\d+}/ciudades")
    public CiudadItinerarioDTO createCiudad(@PathParam("id")Long id, CiudadItinerarioDTO ciudad) {
        CiudadItinerarioEntity entity = CiudadItinerarioConverter.fullDTO2Entity(ciudad);
        return CiudadItinerarioConverter.fullEntity2DTO(itinerarioLogic.addCiudad(entity, id));
    }
    /**
     * Actualiza los datos de una ciudad de un itineario
     * @param id identificador del itinerario a modificar
     * @param ciudad datos modificados de la ciudad
     * @param idciudad identificadot de la ciudad
     * @throws ItinerarioLogicException cuando no existe un itinerario con el id suministrado
     */
    @PUT
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}")
    public CiudadItinerarioDTO updateCiudades(@PathParam("id") Long id, CiudadItinerarioDTO ciudad, @PathParam("idciudad") Long idciudad) {
        CiudadItinerarioEntity entity = CiudadItinerarioConverter.fullDTO2Entity(ciudad);
        entity.setId(idciudad);
        CiudadItinerarioEntity oldEntity = itinerarioLogic.getCiudad(id, idciudad);
        // TODO agregar atributos de oldEntity en entity
        return CiudadItinerarioConverter.fullEntity2DTO(itinerarioLogic.updateCiudad(entity, idciudad));
    }

    /**
     * Elimina los datos de un itinerario
     * @param id identificador del itinerario a eliminar
     * @param idciudad
     */
    @DELETE
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}")
    public void deleteCiudad(@PathParam("id") Long id,@PathParam("idciudad") Long idciudad) {
    	itinerarioLogic.deleteCiudad(id, idciudad);
    }

    /**
     * Obtiene las ciudades del itinerario
     * @param id identificador del itinerario
     * @return lista de ciudades del itinerario
     * @throws ItinerarioLogicException cuando el itineraio no existe
     */
    @GET
    @Path("{id: \\d+}/ciudades")
    public List<CiudadItinerarioDTO> getCiudades(@PathParam("id") Long id) {
        return CiudadItinerarioConverter.listEntity2DTO(itinerarioLogic.getCiudades(id));
    }

    /**
     * Obtiene la ciudad con el idciudad dado, del itinerario con id dado
     * @param id identificador del itinerario
     * @param idciudad identificadot de la ciudad
     * @return ciudad buscada
     * @throws ItinerarioLogicException si no existe itinerario
     * @throws CiudadLogicException  si no existe ciudad
     */
    @GET
    @Path("{id: \\d+}/ciudades/{idciudad:\\d+}")
    public CiudadItinerarioDTO getCiudad(@PathParam("id") Long id, @PathParam("idciudad") Long idciudad) {
        return CiudadItinerarioConverter.fullEntity2DTO(itinerarioLogic.getCiudad(id, idciudad));
    }


    /**
     * Agrega un sitio de interes en una ciudad con el id dado del itinerario con id dado
     * @param id identificador del itinerario
     * @param idciudad identificador de la ciudad
     * @param sitio el sitio a agregar
     * @return el sitio que agregó
     */
    @POST
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}/sitios")
    public SitioItinerarioDTO createSitioInteres(@PathParam("id")Long id, @PathParam("idciudad") Long idciudad, SitioItinerarioDTO sitio) {
        SitioItinerarioEntity entity = SitioItinerarioConverter.fullDTO2Entity(sitio);
        return SitioItinerarioConverter.fullEntity2DTO(itinerarioLogic.createSitio(id, idciudad, entity));
    }

     /**
     * Elimina los datos de un sitio de interes en una ciudad del itinerario
     * @param id identificador del itinerario
     * @param idciudad identificador de la ciudad en el itinerario
     * @param idsitio identificador del sitio a eliminar
     */
    @DELETE
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}/sitios/{idsitio: \\d+}")
    public void deleteSitioDeInteres(@PathParam("id") Long id,@PathParam("idciudad") Long idciudad, @PathParam("idsitio") Long idsitio) {
    	itinerarioLogic.deleteSitio(id, idciudad, idsitio);
    }

     /**
     * Obtiene el listado de sitios de una ciudad en un itinerario
     * @param id identificador del itinerario
     * @param idciudad identificador de la ciudad
     * @return
     * @throws ItinerarioLogicException cuando no existe -----
     * @throws co.edu.uniandes.misVacaciones.rest.exceptions.CiudadLogicException
     */
    @GET
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}/sitios")
    public List<SitioItinerarioDTO> fetchSitiosInteres(@PathParam("id")Long id, @PathParam("idciudad") Long idciudad) {
        return SitioItinerarioConverter.listEntity2DTO(itinerarioLogic.getSitios(id, idciudad));
    }

         /**
     * Obtiene los datos de un sitio de interes en una ciudad del itinerario
     * @param id identificador del itinerario
     * @param idciudad identificador de la ciudad en el itinerario
     * @param idsitio identificador del sitio a buscar
     * @return
     * @throws ItinerarioLogicException cuando no existe un itinerario con el id suministrado
     * @throws CiudadLogicException cuando no existe una ciudad con el id suministrado
     * @throws SitioLogicException cuando no existe un sitio con el id sumunistrado
     */
    @GET
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}/sitios/{idsitio: \\d+}")
    public SitioItinerarioDTO fetchSitioDeInteres(@PathParam("id") Long id,@PathParam("idciudad") Long idciudad, @PathParam("idsitio") Long idsitio) {
    	return SitioItinerarioConverter.fullEntity2DTO(itinerarioLogic.getSitio(id, idciudad, idsitio));
    }

     /**
     * Agrega un evento en una ciudad con el id dado del itinerario con id dado
     * @param id identificador del itinerario
     * @param idciudad identificador de la ciudad
     * @param evento el evento a agregar
     * @return el evento que agregó
     * @throws ItinerarioLogicException cuando no existe -----
     * @throws co.edu.uniandes.misVacaciones.rest.exceptions.CiudadLogicException
     */
    @POST
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}/eventos")
    public EventoItinerarioDTO createEvento(@PathParam("id")Long id, @PathParam("idciudad") Long idciudad, EventoItinerarioDTO evento){
        EventoItinerarioEntity entity = EventoItinerarioConverter.fullDTO2Entity(evento);
        return EventoItinerarioConverter.fullEntity2DTO(itinerarioLogic.createEvento(id, idciudad, entity));
    }

     /**
     * Elimina los datos de un evento en una ciudad del itinerario
     * @param id identificador del itinerario
     * @param idciudad identificador de la ciudad en el itinerario
     * @param idevento identificador del evento a eliminar
     * @throws ItinerarioLogicException cuando no existe un itinerario con el id suministrado
     * @throws CiudadLogicException cuando no existe una ciudad con el id suministrado
     * @throws EventoLogicException cuando no existe un evento con el id sumunistrado
     */
    @DELETE
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}/eventos/{idevento: \\d+}")
    public void deleteEvento(@PathParam("id") Long id,@PathParam("idciudad") Long idciudad, @PathParam("idevento") Long idevento) {
    	itinerarioLogic.deleteEvento(id, idciudad, idevento);
    }

     /**
     * Obtiene el listado de eventos de una ciudad en un itinerario
     * @param id identificador del itinerario
     * @param idciudad identificador de la ciudad
     * @return lista de eventos de la ciudad con el id dado del itinerario con el id dado
     * @throws ItinerarioLogicException cuando no existe -----
     * @throws co.edu.uniandes.misVacaciones.rest.exceptions.CiudadLogicException
     */
    @GET
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}/eventos")
    public List<EventoItinerarioDTO> fetchEventos(@PathParam("id")Long id, @PathParam("idciudad") Long idciudad) {
        return EventoItinerarioConverter.listEntity2DTO(itinerarioLogic.getEventos(id, idciudad));
    }

    /**
     * Obtiene los datos de un evento en una ciudad del itinerario
     * @param id identificador del itinerario
     * @param idciudad identificador de la ciudad en el itinerario
     * @param idevento identificador del evento a buscar
     * @return evento buscado
     * @throws ItinerarioLogicException cuando no existe un itinerario con el id suministrado
     * @throws CiudadLogicException cuando no existe una ciudad con el id suministrado
     * @throws EventoLogicException cuando no existe un sitio con el id sumunistrado
     */
    @GET
    @Path("{id: \\d+}/ciudades/{idciudad: \\d+}/eventos/{idevento: \\d+}")
    public EventoItinerarioDTO fetchEvento(@PathParam("id") Long id,@PathParam("idciudad") Long idciudad, @PathParam("idevento") Long idevento) {
    	return EventoItinerarioConverter.fullEntity2DTO(itinerarioLogic.getEvento(id, idciudad, idevento));
    }

    /**
     * Retorna la lista de itinerarios asociados a un viajero particular
     * @param id del viajero del que se quieren obtener sus itienrarios
     * @return lista de itinerarios
     */
    @GET
    @Path("viajero/{idviajero: }")
    //Id como String o Long?
    public List<ItinerarioDTO> getItinerariosViajero(@PathParam("idviajero") Long id){
    	return ItinerarioConverter.listEntity2DTO(itinerarioLogic.getItinerariosViajero(id));
    }
}
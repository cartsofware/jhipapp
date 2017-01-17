package eu.cartsc.jhipapp.service;

import eu.cartsc.jhipapp.domain.Auto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Auto.
 */
public interface AutoService {

    /**
     * Save a auto.
     *
     * @param auto the entity to save
     * @return the persisted entity
     */
    Auto save(Auto auto);

    /**
     *  Get all the autos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Auto> findAll(Pageable pageable);

    /**
     *  Get the "id" auto.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Auto findOne(Long id);

    /**
     *  Delete the "id" auto.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}

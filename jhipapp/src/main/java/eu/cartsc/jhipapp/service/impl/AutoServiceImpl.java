package eu.cartsc.jhipapp.service.impl;

import eu.cartsc.jhipapp.service.AutoService;
import eu.cartsc.jhipapp.domain.Auto;
import eu.cartsc.jhipapp.repository.AutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Auto.
 */
@Service
@Transactional
public class AutoServiceImpl implements AutoService{

    private final Logger log = LoggerFactory.getLogger(AutoServiceImpl.class);
    
    @Inject
    private AutoRepository autoRepository;

    /**
     * Save a auto.
     *
     * @param auto the entity to save
     * @return the persisted entity
     */
    public Auto save(Auto auto) {
        log.debug("Request to save Auto : {}", auto);
        Auto result = autoRepository.save(auto);
        return result;
    }

    /**
     *  Get all the autos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Auto> findAll(Pageable pageable) {
        log.debug("Request to get all Autos");
        Page<Auto> result = autoRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one auto by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Auto findOne(Long id) {
        log.debug("Request to get Auto : {}", id);
        Auto auto = autoRepository.findOne(id);
        return auto;
    }

    /**
     *  Delete the  auto by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Auto : {}", id);
        autoRepository.delete(id);
    }
}

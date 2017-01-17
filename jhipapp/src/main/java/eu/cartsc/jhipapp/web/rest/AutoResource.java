package eu.cartsc.jhipapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.cartsc.jhipapp.domain.Auto;
import eu.cartsc.jhipapp.service.AutoService;
import eu.cartsc.jhipapp.web.rest.util.HeaderUtil;
import eu.cartsc.jhipapp.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Auto.
 */
@RestController
@RequestMapping("/api")
public class AutoResource {

    private final Logger log = LoggerFactory.getLogger(AutoResource.class);
        
    @Inject
    private AutoService autoService;

    /**
     * POST  /autos : Create a new auto.
     *
     * @param auto the auto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new auto, or with status 400 (Bad Request) if the auto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/autos")
    @Timed
    public ResponseEntity<Auto> createAuto(@Valid @RequestBody Auto auto) throws URISyntaxException {
        log.debug("REST request to save Auto : {}", auto);
        if (auto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("auto", "idexists", "A new auto cannot already have an ID")).body(null);
        }
        Auto result = autoService.save(auto);
        return ResponseEntity.created(new URI("/api/autos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("auto", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /autos : Updates an existing auto.
     *
     * @param auto the auto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated auto,
     * or with status 400 (Bad Request) if the auto is not valid,
     * or with status 500 (Internal Server Error) if the auto couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/autos")
    @Timed
    public ResponseEntity<Auto> updateAuto(@Valid @RequestBody Auto auto) throws URISyntaxException {
        log.debug("REST request to update Auto : {}", auto);
        if (auto.getId() == null) {
            return createAuto(auto);
        }
        Auto result = autoService.save(auto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("auto", auto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /autos : get all the autos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of autos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/autos")
    @Timed
    public ResponseEntity<List<Auto>> getAllAutos(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Autos");
        Page<Auto> page = autoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/autos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /autos/:id : get the "id" auto.
     *
     * @param id the id of the auto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the auto, or with status 404 (Not Found)
     */
    @GetMapping("/autos/{id}")
    @Timed
    public ResponseEntity<Auto> getAuto(@PathVariable Long id) {
        log.debug("REST request to get Auto : {}", id);
        Auto auto = autoService.findOne(id);
        return Optional.ofNullable(auto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /autos/:id : delete the "id" auto.
     *
     * @param id the id of the auto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/autos/{id}")
    @Timed
    public ResponseEntity<Void> deleteAuto(@PathVariable Long id) {
        log.debug("REST request to delete Auto : {}", id);
        autoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("auto", id.toString())).build();
    }

}

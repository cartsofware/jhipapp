package eu.cartsc.jhipapp.repository;

import eu.cartsc.jhipapp.domain.Auto;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Auto entity.
 */
@SuppressWarnings("unused")
public interface AutoRepository extends JpaRepository<Auto,Long> {

}

package org.jhipster.petclinic.service;

import java.util.Optional;
import org.jhipster.petclinic.service.dto.PetDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.jhipster.petclinic.domain.Pet}.
 */
public interface PetService {
    /**
     * Save a pet.
     *
     * @param petDTO the entity to save.
     * @return the persisted entity.
     */
    PetDTO save(PetDTO petDTO);

    /**
     * Updates a pet.
     *
     * @param petDTO the entity to update.
     * @return the persisted entity.
     */
    PetDTO update(PetDTO petDTO);

    /**
     * Partially updates a pet.
     *
     * @param petDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PetDTO> partialUpdate(PetDTO petDTO);

    /**
     * Get all the pets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PetDTO> findAll(Pageable pageable);

    /**
     * Get all the pets with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PetDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" pet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PetDTO> findOne(Long id);

    /**
     * Delete the "id" pet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

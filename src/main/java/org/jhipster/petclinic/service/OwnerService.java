package org.jhipster.petclinic.service;

import java.util.Optional;
import org.jhipster.petclinic.service.dto.OwnerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.jhipster.petclinic.domain.Owner}.
 */
public interface OwnerService {
    /**
     * Save a owner.
     *
     * @param ownerDTO the entity to save.
     * @return the persisted entity.
     */
    OwnerDTO save(OwnerDTO ownerDTO);

    /**
     * Updates a owner.
     *
     * @param ownerDTO the entity to update.
     * @return the persisted entity.
     */
    OwnerDTO update(OwnerDTO ownerDTO);

    /**
     * Partially updates a owner.
     *
     * @param ownerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OwnerDTO> partialUpdate(OwnerDTO ownerDTO);

    /**
     * Get all the owners.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OwnerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" owner.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OwnerDTO> findOne(Long id);

    /**
     * Delete the "id" owner.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

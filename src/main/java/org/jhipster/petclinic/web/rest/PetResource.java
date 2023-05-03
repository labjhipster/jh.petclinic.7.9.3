package org.jhipster.petclinic.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.jhipster.petclinic.repository.PetRepository;
import org.jhipster.petclinic.service.PetQueryService;
import org.jhipster.petclinic.service.PetService;
import org.jhipster.petclinic.service.criteria.PetCriteria;
import org.jhipster.petclinic.service.dto.PetDTO;
import org.jhipster.petclinic.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.jhipster.petclinic.domain.Pet}.
 */
@RestController
@RequestMapping("/api")
public class PetResource {

    private final Logger log = LoggerFactory.getLogger(PetResource.class);

    private static final String ENTITY_NAME = "pet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PetService petService;

    private final PetRepository petRepository;

    private final PetQueryService petQueryService;

    public PetResource(PetService petService, PetRepository petRepository, PetQueryService petQueryService) {
        this.petService = petService;
        this.petRepository = petRepository;
        this.petQueryService = petQueryService;
    }

    /**
     * {@code POST  /pets} : Create a new pet.
     *
     * @param petDTO the petDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new petDTO, or with status {@code 400 (Bad Request)} if the pet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pets")
    public ResponseEntity<PetDTO> createPet(@Valid @RequestBody PetDTO petDTO) throws URISyntaxException {
        log.debug("REST request to save Pet : {}", petDTO);
        if (petDTO.getId() != null) {
            throw new BadRequestAlertException("A new pet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PetDTO result = petService.save(petDTO);
        return ResponseEntity
            .created(new URI("/api/pets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pets/:id} : Updates an existing pet.
     *
     * @param id the id of the petDTO to save.
     * @param petDTO the petDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated petDTO,
     * or with status {@code 400 (Bad Request)} if the petDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the petDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pets/{id}")
    public ResponseEntity<PetDTO> updatePet(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody PetDTO petDTO)
        throws URISyntaxException {
        log.debug("REST request to update Pet : {}, {}", id, petDTO);
        if (petDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, petDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!petRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PetDTO result = petService.update(petDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, petDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pets/:id} : Partial updates given fields of an existing pet, field will ignore if it is null
     *
     * @param id the id of the petDTO to save.
     * @param petDTO the petDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated petDTO,
     * or with status {@code 400 (Bad Request)} if the petDTO is not valid,
     * or with status {@code 404 (Not Found)} if the petDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the petDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PetDTO> partialUpdatePet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PetDTO petDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pet partially : {}, {}", id, petDTO);
        if (petDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, petDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!petRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PetDTO> result = petService.partialUpdate(petDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, petDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pets} : get all the pets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pets in body.
     */
    @GetMapping("/pets")
    public ResponseEntity<List<PetDTO>> getAllPets(PetCriteria criteria, @org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get Pets by criteria: {}", criteria);
        Page<PetDTO> page = petQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pets/count} : count all the pets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pets/count")
    public ResponseEntity<Long> countPets(PetCriteria criteria) {
        log.debug("REST request to count Pets by criteria: {}", criteria);
        return ResponseEntity.ok().body(petQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pets/:id} : get the "id" pet.
     *
     * @param id the id of the petDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the petDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pets/{id}")
    public ResponseEntity<PetDTO> getPet(@PathVariable Long id) {
        log.debug("REST request to get Pet : {}", id);
        Optional<PetDTO> petDTO = petService.findOne(id);
        return ResponseUtil.wrapOrNotFound(petDTO);
    }

    /**
     * {@code DELETE  /pets/:id} : delete the "id" pet.
     *
     * @param id the id of the petDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pets/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        log.debug("REST request to delete Pet : {}", id);
        petService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
